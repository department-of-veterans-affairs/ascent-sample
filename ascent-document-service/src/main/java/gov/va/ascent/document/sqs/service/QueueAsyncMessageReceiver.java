package gov.va.ascent.document.sqs.service;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.va.ascent.document.service.api.DocumentService;
import gov.va.ascent.document.sqs.MessageAttributes;
import gov.va.ascent.starter.aws.s3.dto.CopyFileRequest;
import gov.va.ascent.starter.aws.s3.dto.MoveMessageRequest;
import gov.va.ascent.starter.aws.s3.services.S3Service;
import gov.va.ascent.starter.aws.sqs.config.SqsProperties;
import gov.va.ascent.starter.aws.sqs.services.SqsService;

@Service
public class QueueAsyncMessageReceiver {

	private final Logger logger = LoggerFactory.getLogger(QueueAsyncMessageReceiver.class);

	private static final String MESSAGE_TIME_ELAPSED = "Message time elapsed: ";
	private SQSConnection connection;

	@Value("${ascent.s3.bucket}")
	private String bucketName;

	@Value("${ascent.s3.target.bucket}")
	private String targetBucketName;

	@Value("${ascent.s3.dlq.bucket}")
	private String dlqBucketName;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	@Qualifier("IMPL")
	DocumentService documentService;

	@Autowired
	ConnectionFactory connectionFactory;

	@Autowired
	S3Service s3Services;

	@Autowired
	SqsService sqsServices;

	private SqsProperties sqsProperties;

	@Autowired
	public void setApp(final SqsProperties sqsProperties) {
		this.sqsProperties = sqsProperties;
	}

	@PostConstruct
	public void init() {
		startJmsConnection();
		logger.info("init() called. Started JMS Connection");
	}

	@PreDestroy
	public void cleanUp() throws JMSException {
		if (connection != null) {
			connection.close();
			logger.info("JMS connection closed");
		}
	}

	/**
	 * Creates a SQS Connection and listeners to the main and dead letter queues.
	 */
	private void startJmsConnection() {
		try {
			connection = (SQSConnection) connectionFactory.createConnection();

			// Create the session
			final Session session = connection.createSession(false, SQSSession.UNORDERED_ACKNOWLEDGE);

			// Create the Main Queue
			final MessageConsumer consumer = session.createConsumer(session.createQueue(sqsProperties.getQueueName()));
			final ReceiverCallback callback = new ReceiverCallback();
			consumer.setMessageListener(callback);

			// Create the Dead Letter Queue
			final MessageConsumer dlqconsumer = session.createConsumer(session.createQueue(sqsProperties.getDLQQueueName()));
			final DLQReceiverCallback dlqcallback = new DLQReceiverCallback();
			dlqconsumer.setMessageListener(dlqcallback);

			// No messages are processed until this is called
			connection.start();

		} catch (final JMSException e) {
			logger.error("Error occurred while starting JMS connection and listeners. Error: {}", e);
		}
	}

	/**
	 *
	 * @author rajuthota
	 *         Listener for the Main Queue
	 */
	private class ReceiverCallback implements MessageListener {
		@Override
		public void onMessage(final Message message) {
			try {
				logger.info("Consumer message processing started for Normal Queue. JMS Message ID: " + message.getJMSMessageID());
				if (message instanceof TextMessage) {
					final TextMessage messageText = (TextMessage) message;
					final MessageAttributes messageAttributes = documentService
							.getMessageAttributesFromJson(messageText.getText());
					findJMSElapsedTime(messageAttributes.getCreateTimestamp());
					final String docName = messageAttributes.getMessage();
					if (docName.contains("donotprocess")) {
						logger.error("Message is not processed. JMS Message ID: " + message.getJMSMessageID());
						return;
					}

					// throws to Exception catch block
					CopyFileRequest copyFileRequest = new CopyFileRequest();
					copyFileRequest.setKey(docName);
					copyFileRequest.setSourceBucketName(bucketName);
					copyFileRequest.setTargetBucketName(targetBucketName);
					s3Services.copyFileFromSourceToTargetBucket(copyFileRequest);
					message.acknowledge();
				}
				logger.info("Acknowledged message. JMS Message ID: " + message.getJMSMessageID());
			} catch (final JMSException e) {
				logger.error("Error occurred while processing message. Error: {}", e);
			} catch (final Exception e) {
				logger.error("Error occurred while copying the object. Error: {}", e);
				return;
			}
		}

		/**
		 * @param message
		 */
		private long findJMSElapsedTime(final long createTimeStamp) {
			final long currentTime = System.currentTimeMillis();
			final long differenceTime = currentTime - createTimeStamp;
			logger.info(MESSAGE_TIME_ELAPSED + differenceTime + " ms");
			logger.info(MESSAGE_TIME_ELAPSED + TimeUnit.MILLISECONDS.toSeconds(differenceTime) + " secs");
			logger.info(MESSAGE_TIME_ELAPSED + TimeUnit.MILLISECONDS.toMinutes(differenceTime) + " mins");
			logger.info(MESSAGE_TIME_ELAPSED + TimeUnit.MILLISECONDS.toHours(differenceTime) + " hrs");
			return differenceTime;
		}
	}

	/**
	 *
	 * @author rajuthota
	 *         Listener for the Dead Letter Queue. The message is psuhed back into main queue.
	 *         After three attempts, the message is deleted.
	 */
	private class DLQReceiverCallback implements MessageListener {
		@Override
		public void onMessage(final Message message) {
			try {
				logger.info(
						"Consumer message processing started for DLQ. JMS Message ID: " + message.getJMSMessageID());
				if (message instanceof TextMessage) {
					final TextMessage messageText = (TextMessage) message;
					final MessageAttributes messageAttributes = documentService
							.getMessageAttributesFromJson(messageText.getText());

					if (messageAttributes == null) {
						return;
					}
					if (messageAttributes.getNumberOfRetries() >= sqsProperties.getRetries()) {
						moveMessageToDlq(messageAttributes);
						logger.info("Deleting the message from DLQ after {} attempts. JMS Message ID: {}",
								sqsProperties.getRetries(), message.getJMSMessageID());
					} else {
						final TextMessage txtMessage = moveMessageToQueue(messageAttributes);
						sqsServices.sendMessage(txtMessage);
					}
					message.acknowledge();
				}
				logger.info("Acknowledged message from DLQ. JMS Message ID: " + message.getJMSMessageID());

			} catch (final JMSException e) {
				logger.error("Error occurred while processing message. Error: {}", e);
			}
		}
	}

	// sonar complains about the try/catch if moved inside DLQReceiverCallback()
	private TextMessage moveMessageToQueue(final MessageAttributes messageAttributes) { // NOSONAR
		// move the message to normal queue for processing
		messageAttributes.setNumberOfRetries(messageAttributes.getNumberOfRetries() + 1);
		TextMessage txtMessage = null;
		try {
			txtMessage = sqsServices.createTextMessage(mapper.writeValueAsString(messageAttributes));
		} catch (final JsonProcessingException e) {
			logger.error("Error occurred while creating text message. Error: {}", e);
		}
		return txtMessage;
	}

	// sonar complains about the try/catch if moved inside DLQReceiverCallback()
	private void moveMessageToDlq(final MessageAttributes messageAttributes) { // NOSONAR
		// move the message to s3 dlq bucket
		MoveMessageRequest moveMessageRequest = new MoveMessageRequest();
		moveMessageRequest.setDlqBucketName(dlqBucketName);
		moveMessageRequest.setKey(messageAttributes.getDocumentID());
		s3Services.moveMessageToS3(moveMessageRequest);
	}
}

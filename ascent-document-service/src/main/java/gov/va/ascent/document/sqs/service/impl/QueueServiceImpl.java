package gov.va.ascent.document.sqs.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.ProducerCallback;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.stereotype.Service;

import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.va.ascent.document.service.api.DocumentService;
import gov.va.ascent.document.sqs.MessageAttributes;
import gov.va.ascent.document.sqs.service.QueueService;
import gov.va.ascent.starter.aws.autoconfigure.s3.services.S3Services;
import gov.va.ascent.starter.aws.autoconfigure.sqs.config.SqsProperties;

@Service
public class QueueServiceImpl implements QueueService {

	private Logger logger = LoggerFactory.getLogger(QueueServiceImpl.class);
	private SQSConnection connection;

    @Autowired
    ObjectMapper mapper;
    
	@Autowired
	@Qualifier("IMPL")
	DocumentService documentService;

	@Resource
	JmsOperations jmsOperations;

	@Autowired
	ConnectionFactory connectionFactory;

	@Autowired
	S3Services s3Services;

	private SqsProperties sqsProperties;

	@Autowired
	public void setApp(SqsProperties sqsProperties) {
		this.sqsProperties = sqsProperties;
	}

	@PostConstruct
	public void init() {
		startJmsConnection();
		logger.info("init called. Connection opened");
	}

	@PreDestroy
	public void cleanUp() throws Exception {
		connection.close();
		logger.info("Connection closed");
	}

	/**
	 * Sends the message to the main queue.
	 */
	@Override
	@ManagedOperation
	public ResponseEntity<String> sendMessage(String request) {
		logger.info("Handling request: '{}'", request);

		final String messageId = jmsOperations.execute(new ProducerCallback<String>() {
			@Override
			public String doInJms(Session session, MessageProducer producer) throws JMSException {
				final TextMessage message = session.createTextMessage(request);
				producer.send(message);
				logger.info("Sent JMS message with payload='{}', id: '{}'", request, message.getJMSMessageID());
				return message.getJMSMessageID();
			}
		});

		return new ResponseEntity<>(messageId, HttpStatus.OK);
	}

	/**
	 * Creates a SQS Connection and listeners to the main and dead letter queues.
	 */
	private void startJmsConnection() {
		try {
			connection = (SQSConnection) connectionFactory.createConnection();

			// Create the session
			Session session = connection.createSession(false, SQSSession.UNORDERED_ACKNOWLEDGE);
			
			//Create the Main Queue
			MessageConsumer consumer = session.createConsumer(session.createQueue(sqsProperties.getQueueName()));
			ReceiverCallback callback = new ReceiverCallback();
			consumer.setMessageListener(callback);

			//Create the Dead Letter Queue
			MessageConsumer dlqconsumer = session.createConsumer(session.createQueue(sqsProperties.getDLQQueueName()));
			DLQReceiverCallback dlqcallback = new DLQReceiverCallback();
			dlqconsumer.setMessageListener(dlqcallback);
			
			// No messages are processed until this is called
			connection.start();

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @author rajuthota
	 * Listener for the Main Queue
	 */
	private class ReceiverCallback implements MessageListener {
		@Override
		public void onMessage(Message message) {
			try {
			    logger.info("Consumer message processing started for Normal Queue. JMS Message ID: " + message.getJMSMessageID());
				if (message instanceof TextMessage) {
					TextMessage messageText = (TextMessage) message;
					MessageAttributes messageAttributes = documentService
							.getMessageAttributesFromJson(messageText.getText());
					String docName = messageAttributes.getMessage();
					if (docName.contains("donotprocess")) {
						logger.error("Message is not processed. JMS Message ID: " + message.getJMSMessageID());
						return;
					}
					try {
						s3Services.copyFileFromSourceToTargetBucket(docName);
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					message.acknowledge();
				} 
				logger.info("Acknowledged message. JMS Message ID: " + message.getJMSMessageID());

			} catch (JMSException e) {
				logger.error("Error occurred while processing message. Error: " + e.getStackTrace());
			}
		}
	}
	
	/**
	 * 
	 * @author rajuthota
	 * Listener for the Dead Letter Queue. The message is psuhed back into main queue.
	 * After three attempts, the message is deleted.
	 */
	private class DLQReceiverCallback implements MessageListener {
		@Override
		public void onMessage(Message message) {
			try {
			  logger.info("Consumer message processing started for DLQ. JMS Message ID: " + message.getJMSMessageID());
				if (message instanceof TextMessage) {
					TextMessage messageText = (TextMessage) message;
					MessageAttributes messageAttributes = documentService
							.getMessageAttributesFromJson(messageText.getText());
				if (messageAttributes.getNumberOfRetries() >= sqsProperties.getDlqRetriesCount()) {
						try {
							s3Services.moveMessageToS3(messageAttributes.getDocumentID(), mapper.writeValueAsString(messageAttributes));
						} catch (JsonProcessingException e) {
							logger.error("Error occurred while moving DLQ message to S3. Error: " + e.getStackTrace());
						}
						logger.info("Deleting the message from DLQ after {} attempts. JMS Message ID: {}", 
						    sqsProperties.getDlqRetriesCount(), message.getJMSMessageID());
					} else {
						messageAttributes.setNumberOfRetries(messageAttributes.getNumberOfRetries() + 1);
						try {
							sendMessage(mapper.writeValueAsString(messageAttributes));
						} catch (JsonProcessingException e) {
							logger.error("Error occurred while processing Json. Error: " + e.getStackTrace());
						}
					}
					message.acknowledge();
				}
				logger.info("Acknowledged message from DLQ. JMS Message ID: " + message.getJMSMessageID());

			} catch (JMSException e) {
				logger.error("Error occurred while processing message. Error: " + e.getStackTrace());
			}
		}
	}
}

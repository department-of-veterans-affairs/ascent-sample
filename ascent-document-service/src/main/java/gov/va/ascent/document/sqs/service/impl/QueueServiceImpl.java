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
	}

	@PreDestroy
	public void cleanUp() throws Exception {
		connection.close();
		System.out.println("Connection closed");
	}

	@Override
	@ManagedOperation
	public ResponseEntity<String> sendMessage(String request) {
		logger.info("Handling request: '{}'", request);

		final String messageId = jmsOperations.execute(new ProducerCallback<String>() {
			@Override
			public String doInJms(Session session, MessageProducer producer) throws JMSException {
				final TextMessage message = session.createTextMessage(request);
				producer.send(message);
				logger.debug("Sent JMS message with payload='{}', id: '{}'", request, message.getJMSMessageID());
				return message.getJMSMessageID();
			}
		});

		return new ResponseEntity<>(messageId, HttpStatus.OK);
	}

	public void startJmsConnection() {
		try {
			connection = (SQSConnection) connectionFactory.createConnection();

			// Create the session
			Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			MessageConsumer consumer = session.createConsumer(session.createQueue(sqsProperties.getQueueName()));
			ReceiverCallback callback = new ReceiverCallback();
			consumer.setMessageListener(callback);

			// No messages are processed until this is called
			connection.start();

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private class ReceiverCallback implements MessageListener {
		@Override
		public void onMessage(Message message) {
			try {
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
}

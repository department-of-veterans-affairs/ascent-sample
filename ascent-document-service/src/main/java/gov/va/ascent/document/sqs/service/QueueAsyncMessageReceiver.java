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
import gov.va.ascent.starter.aws.s3.services.S3Service;
import gov.va.ascent.starter.aws.sqs.config.SqsProperties;
import gov.va.ascent.starter.aws.sqs.services.SqsService;

@Service
public class QueueAsyncMessageReceiver {

  private Logger logger = LoggerFactory.getLogger(QueueAsyncMessageReceiver.class);

  private static String MESSAGE_TIME_ELAPSED = "Message time elapsed: ";
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
  public void setApp(SqsProperties sqsProperties) {
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
      logger.error("Error occurred while starting JMS connection and listeners. Error: {}", e);
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
          findJMSElapsedTime(messageAttributes.getCreateTimestamp());
          String docName = messageAttributes.getMessage();
          if (docName.contains("donotprocess")) {
            logger.error("Message is not processed. JMS Message ID: " + message.getJMSMessageID());
            return;
          }
          try {
            s3Services.copyFileFromSourceToTargetBucket(bucketName, targetBucketName, docName);
          } catch (Exception e) {
            logger.error("Error occurred while copying the object. Error: {}", e);
            return;
          }
          message.acknowledge();
        } 
        logger.info("Acknowledged message. JMS Message ID: " + message.getJMSMessageID());
      } catch (JMSException e) {
        logger.error("Error occurred while processing message. Error: {}", e);
      }
    }
    
    /**
     * @param message
     * @throws JMSException
     */
    private long findJMSElapsedTime(long createTimeStamp) throws JMSException {
      long currentTime=System.currentTimeMillis();
      long differenceTime = currentTime - createTimeStamp;
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
   * Listener for the Dead Letter Queue. The message is psuhed back into main queue.
   * After three attempts, the message is deleted.
   */
  private class DLQReceiverCallback implements MessageListener {
    @Override
    public void onMessage(Message message) {
      try {
        logger.info(
            "Consumer message processing started for DLQ. JMS Message ID: " + message.getJMSMessageID());
        if (message instanceof TextMessage) {
          TextMessage messageText = (TextMessage) message;
          MessageAttributes messageAttributes = documentService
              .getMessageAttributesFromJson(messageText.getText());

          if (messageAttributes == null)
            return;
          if (messageAttributes.getNumberOfRetries() >= sqsProperties.getDlqRetriesCount()) {
            try {
              // move the message to s3 dlq bucket
              s3Services.moveMessageToS3(dlqBucketName, messageAttributes.getDocumentID(),
                  mapper.writeValueAsString(messageAttributes));
            } catch (JsonProcessingException e) {
              logger.error("Error occurred while moving DLQ message to S3. Error: " + e.getStackTrace());
            }
            logger.info("Deleting the message from DLQ after {} attempts. JMS Message ID: {}",
                sqsProperties.getDlqRetriesCount(), message.getJMSMessageID());
            /*
             * } else if (TimeUnit.MILLISECONDS.toHours(elapsedTime) < 12 ) { // keep the
             * message in flight return;
             */
          } else {
            // move the message to normal queue for processing
            messageAttributes.setNumberOfRetries(messageAttributes.getNumberOfRetries() + 1);
            TextMessage txtMessage = null;
            try {
              txtMessage = sqsServices.createTextMessage(mapper.writeValueAsString(messageAttributes));
            } catch (JsonProcessingException e) {
              logger.error("Error occurred while creating text message. Error: {}", e);
            }
            sqsServices.sendMessage(txtMessage);
          }
          message.acknowledge();
        }
        logger.info("Acknowledged message from DLQ. JMS Message ID: " + message.getJMSMessageID());

      } catch (JMSException e) {
        logger.error("Error occurred while processing message. Error: {}", e);
      }
    }
  }
}

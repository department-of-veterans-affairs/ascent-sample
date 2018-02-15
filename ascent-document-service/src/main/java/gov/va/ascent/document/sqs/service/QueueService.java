package gov.va.ascent.document.sqs.service;

import org.springframework.http.ResponseEntity;

public interface QueueService {
	/**
     * Send a message to SQS
     * @param request String message
     * @return ResponseEntity<String> JMS Message ID
     */
    public ResponseEntity<String> sendMessage(final String request);
}

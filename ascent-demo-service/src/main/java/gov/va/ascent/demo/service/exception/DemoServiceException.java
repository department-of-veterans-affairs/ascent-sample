package gov.va.ascent.demo.service.exception;

import gov.va.ascent.framework.messages.MessageSeverity;

public class DemoServiceException extends RuntimeException {		
	 	/** The Constant serialVersionUID. */		
	 	private static final long serialVersionUID = 8436650114143466441L;		
	 		
	 	private MessageSeverity severity;		
	 	private String key;		
	 	private String message;		
	 		
	 	public MessageSeverity getSeverity() {		
	 		return severity;		
	 	}		
	 		
	 	public String getKey() {		
	 		return key;		
	 	}		

	 	public String getMessage() {		
	 		return message;		
	 	}		

	 	public DemoServiceException(MessageSeverity severity, String key, String message) {		
	 		this.severity = severity;		
	 		this.key = key;		
	 		this.message = message;		
	 	}		
	 		
	 	public DemoServiceException(MessageSeverity severity, String message) {		
	 		this.severity = severity;		
	 		this.message = message;		
	 	}		
	 	
	 	public DemoServiceException(String message) {		
	 		this.message = message;		
	 	}		
	 			
	 }
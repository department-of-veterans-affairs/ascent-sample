package gov.va.ascent.demo.service.exception;

import gov.va.ascent.framework.messages.MessageSeverity;

public class DemoServiceException extends RuntimeException {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8436650114143466441L;

	private MessageSeverity severity; // NOSONAR cannot be final
	private String key; // NOSONAR cannot be final
	private final String message; // NOSONAR cannot be final

	public DemoServiceException(final MessageSeverity severity, final String key, final String message) {
		this.severity = severity;
		this.key = key;
		this.message = message;
	}

	public DemoServiceException(final MessageSeverity severity, final String message) {
		this.severity = severity;
		this.message = message;
	}

	public DemoServiceException(final String message) {
		this.message = message;
	}

	public MessageSeverity getSeverity() {
		return severity;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
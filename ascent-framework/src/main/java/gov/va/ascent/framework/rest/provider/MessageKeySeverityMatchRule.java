package gov.va.ascent.framework.rest.provider;

import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.http.HttpStatus;

import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.util.Defense;

/**
 * The Class MessageKeySeverityMatchRule is a rule to match a Message based on key and severity.
 *
 * @author jshrader
 */
public final class MessageKeySeverityMatchRule implements MessagesToHttpStatusRule {

	/** The message to match. */
	private final Message messageToMatch;

	/** The http status. */
	private final HttpStatus httpStatus;

	/**
	 * Instantiates a new message key severity match rule.
	 *
	 * @param messageToMatch the message to match
	 * @param httpStatus the http status
	 */
	public MessageKeySeverityMatchRule(final Message messageToMatch, final HttpStatus httpStatus) {
		super();
		Defense.notNull(messageToMatch, "messageToMatch cannot be null!");
		this.messageToMatch = messageToMatch;
		this.httpStatus = httpStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.va.ascent.framework.rest.provider.MessagesToHttpStatusRule#eval(java.util.Set)
	 */
	@Override
	public HttpStatus eval(final Set<Message> messagesToEval) {
		if (messagesToEval.contains(messageToMatch)) {
			return httpStatus;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

}

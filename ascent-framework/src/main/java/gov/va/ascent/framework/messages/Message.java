package gov.va.ascent.framework.messages;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import gov.va.ascent.framework.transfer.AbstractTransferObject;

/**
 * Message is a generic abstraction of a "message" or "notification" which is layer agnostic and can be used to
 * communicate status or other sorts of information during method calls between components/layers. This is serializable
 * and can be used in SOAP or REST calls.
 * 
 * @author jshrader
 */
public class Message extends AbstractTransferObject {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1711431368372127555L;
	
	/** The text is excluded from equals and hash as the key+severity are to jointly indicate
	 * a unique message.  The text is supplemental information. */
	private static final String[] EQUALS_HASH_EXCLUDE_FIELDS = new String[]{"text"};

	/** The key. */
	@XmlElement(required = true)
    @NotNull
	private String key;

	/** The message. */
	private String text;

	/** The message severity. */
	@XmlElement(required = true)
    @NotNull
	private MessageSeverity severity;

	/**
	 * Instantiates a new message.
	 */
	public Message() {
		super();
	}

	/**
	 * Instantiates a new message.
	 *
	 * @param severity the severity
	 * @param key the key
	 */
	public Message(final MessageSeverity severity, final String key) {
		super();
		this.severity = severity;
		this.key = key;
	}

	/**
	 * Instantiates a new message.
	 *
	 * @param severity the severity
	 * @param key the key
	 * @param text the text
	 */
	public Message(final MessageSeverity severity, final String key, final String text) {
		super();
		this.severity = severity;
		this.key = key;
		this.text = text;
	}

	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	public final String getKey() {
		return this.key;
	}

	/**
	 * Sets the key.
	 * 
	 * @param key the new key
	 */
	public final void setKey(final String key) {
		this.key = key;
	}


	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public final String getText() {
		return this.text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public final void setText(final String text) {
		this.text = text;
	}

	/**
	 * Gets the message severity.
	 * 
	 * @return the message severity
	 */
	public final MessageSeverity getSeverity() {
		return this.severity;
	}

	/**
	 * Sets the message severity.
	 * 
	 * @param severity the new message severity
	 */
	public final void setSeverity(final MessageSeverity severity) {
		this.severity = severity;
	}
	
	/* (non-Javadoc)
	 * @see gov.va.ascent.framework.transfer.AbstractTransferObject#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, EQUALS_HASH_EXCLUDE_FIELDS);
	}

	/* (non-Javadoc)
	 * @see gov.va.ascent.framework.transfer.AbstractTransferObject#hashCode()
	 */
	@Override
	public final int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, EQUALS_HASH_EXCLUDE_FIELDS);
	}
}

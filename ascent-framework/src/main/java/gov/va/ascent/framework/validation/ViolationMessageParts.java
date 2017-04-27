package gov.va.ascent.framework.validation;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * The contains the parts of the validation violation message.
 * 
 * @author jshrader
 */
public class ViolationMessageParts implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7035899352647533160L;
	
	/** The original key. */
	private String originalKey;
	
	/** The new key. */
	private String newKey;
	
	/** The text. */
	private String text;
	
	/**
	 * Gets the original key.
	 *
	 * @return the original key
	 */
	public final String getOriginalKey() {
		return originalKey;
	}
	
	/**
	 * Sets the original key.
	 *
	 * @param originalKey the new original key
	 */
	public final void setOriginalKey(final String originalKey) {
		this.originalKey = originalKey;
	}
	
	/**
	 * Gets the new key.
	 *
	 * @return the new key
	 */
	public final String getNewKey() {
		return newKey;
	}
	
	/**
	 * Sets the new key.
	 *
	 * @param newKey the new new key
	 */
	public final void setNewKey(final String newKey) {
		this.newKey = newKey;
	}
	
	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public final String getText() {
		return text;
	}
	
	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public final void setText(final String text) {
		this.text = text;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
	
	
}

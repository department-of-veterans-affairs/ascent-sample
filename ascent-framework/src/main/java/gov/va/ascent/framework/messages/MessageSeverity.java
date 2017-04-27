package gov.va.ascent.framework.messages;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * MessageSeverity is a type safe enum to represent the severity of a message.
 * 
 * @author jshrader
 */
@XmlType(name = "messageSeverity")
@XmlEnum
public enum MessageSeverity {

	/** The info  severity. */
	INFO,

	/** The warn  severity. */
	WARN, 

	/** The error severity. */
	ERROR, 
	
	/** The fatal severity. */
	FATAL;
	
	
	/**
	 * Value.
	 * 
	 * @return the string
	 */
	public String value() {
		return name();
	}

	/**
	 * From value.
	 *
	 * @param value the value
	 * @return the message severity
	 */
	public static MessageSeverity fromValue(final String value) {
		return valueOf(value);
	}
}

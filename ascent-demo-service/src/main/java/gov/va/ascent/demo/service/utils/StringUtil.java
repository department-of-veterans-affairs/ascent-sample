package gov.va.ascent.demo.service.utils;

import java.io.StringWriter;

import javax.xml.bind.JAXB;

/**
 * The Class StringUtil.
 *
 * @author BartholE
 */
public final class StringUtil {

	/** The Constant DEFAULT_MASK. */
	private static final char DEFAULT_MASK = 'x';

	/** The Constant DEFAULT_MASK_COUNT. */
	private static final int DEFAULT_MASK_COUNT = 4;

	/**
	 * hide constructor.
	 */
	private StringUtil() {
	}

	/**
	 * Returns mask value for a text string. First set of characters are masked.
	 *
	 * @param text The text.
	 * @param charMask The character mask.
	 * @param numViewableChars Number of viewable characters.
	 * @return The masked text.
	 */
	public static String getMask(final String text, final char charMask, final int numViewableChars) {
		if (text == null || text.isEmpty() || text.length() - numViewableChars <= 0) {
			return text;
		}

		final int length = text.length();
		final int pos = length - numViewableChars;
		final StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length; ++i) {
			if (i >= pos) {
				builder.append(text.charAt(i));
			} else {
				builder.append(charMask);
			}
		}
		return builder.toString();
	}

	/**
	 * Returns mask value where only the last 4 chars are viewable. All other characters are masked out.
	 *
	 * @param text The text.
	 * @return The masked text.
	 */
	public static String getMask4(final String text) {
		return getMask(text, DEFAULT_MASK, DEFAULT_MASK_COUNT);
	}

	/**
	 * True if the string is not null and not empty.
	 *
	 * @param testString the string to be tested.
	 * @return True if the string is not null and not empty.
	 */
	public static boolean isNotNullAndNotEmpty(String testString) {
		return testString != null && !testString.isEmpty();
	}

	/**
	 * Object to xml string.
	 *
	 * @param object the object
	 * @return the string
	 */
	public static String objectToXMLString(final Object object) {
		final StringWriter strWriter = new StringWriter();
		JAXB.marshal(object, strWriter);
		return strWriter.toString();
	}
}

package gov.va.ascent.framework.transfer.jaxb.adapters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class DateAdapter converts from Java date to/from JAXB XML
 */
public final class DateAdapter {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DateAdapter.class);

	/** The Constant DATE_TIME_FORMAT. */
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	/**
	 * utility classes should not have a public or default constructor
	 */
	private DateAdapter() {
	}

	/**
	 * Parses the date.
	 *
	 * @param dateString the date string
	 * @return the date
	 */
	public static Date parseDate(final String dateString) {
		return DatatypeConverter.parseDate(dateString).getTime();
	}

	/**
	 * Prints the date.
	 *
	 * @param date the date
	 * @return the string
	 */
	public static String printDate(final Date date) {
		final Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return DatatypeConverter.printDate(cal);
	}

	/**
	 * Parses the date time.
	 *
	 * @param dateTime the date time
	 * @return the date
	 */
	public static Date parseDateTime(final String dateTime) {
		final DateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
		try {
			return formatter.parse(dateTime);
		} catch (final ParseException parseException) {
			LOGGER.error("Error parsing date, returning null:" + dateTime, parseException);
			return null;
		}
	}

	/**
	 * Prints the date time.
	 * 
	 * crazy hack because the 'Z' formatter produces an output incompatible with the xsd:dateTime
	 *
	 * @param dateTime the date time
	 * @return the string
	 */
	public static String printDateTime(final Date dateTime) {
		final DateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
		final DateFormat tzFormatter = new SimpleDateFormat("Z", Locale.getDefault());
		final String timezone = tzFormatter.format(dateTime);
		return formatter.format(dateTime) + timezone.substring(0, 3) + ":" + timezone.substring(3);
	}
}

package gov.va.ascent.framework.exception;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains utility ops for logging and handling exceptions consistently. Primarily for usage in interceptors which
 * implement ThrowsAdvice and handle exceptions to ensure these all log then consistently.
 * 
 * @author jshrader
 */
public final class ExceptionHandlingUtils {

	/** The Constant LOC_EXCEPTION_PREFIX. */
	private static final String LOC_EXCEPTION_PREFIX =
			" caught exception, handling it as configured.  Here are details [";

	/** The Constant LOG_EXCEPTION_MID. */
	private static final String LOG_EXCEPTION_MID = "] args [";

	/** The Constant LOG_EXCEPTION_POSTFIX. */
	private static final String LOG_EXCEPTION_POSTFIX = "].";

	/** The Constant LOG_EXCEPTION_UNDERSCORE. */
	private static final String LOG_EXCEPTION_UNDERSCORE = "_";

	/** The Constant LOG_EXCEPTION_DOT. */
	private static final String LOG_EXCEPTION_DOT = ".";

	/**
	 * private constructor for utility class
	 */
	private ExceptionHandlingUtils() {
	}

	/**
	 * Log exception.
	 * 
	 * @param catcher the catcher - some descriptive name for whomever caught this exception and wants it logged
	 * @param method the method
	 * @param args the args
	 * @param throwable the throwable
	 */
	public static void logException(final String catcher, final Method method, final Object[] args,
			final Throwable throwable) {
		final Logger errorLogger =
				LoggerFactory.getLogger(method.getDeclaringClass().getName() + LOG_EXCEPTION_DOT + method.getName()
						+ LOG_EXCEPTION_UNDERSCORE + throwable.getClass().getName());
		final String errorMessage =
				throwable.getClass().getName() + " thrown by " + method.getDeclaringClass().getName()
						+ LOG_EXCEPTION_DOT + method.getName();
		if (errorLogger.isWarnEnabled()) {
			errorLogger.warn(catcher + LOC_EXCEPTION_PREFIX + errorMessage + LOG_EXCEPTION_MID + Arrays.toString(args)
					+ LOG_EXCEPTION_POSTFIX, throwable);
		} else {
			// if we disable warn logging (all the details and including stack trace) we only show minimal
			// evidence of the error in the logs
			errorLogger.error(catcher + LOC_EXCEPTION_PREFIX + errorMessage + LOG_EXCEPTION_MID + Arrays.toString(args)
					+ LOG_EXCEPTION_POSTFIX);
		}
	}

}

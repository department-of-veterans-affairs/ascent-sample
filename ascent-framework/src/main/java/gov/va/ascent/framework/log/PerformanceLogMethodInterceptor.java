package gov.va.ascent.framework.log;

import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a custom performance logging interceptor which simply wraps a method (any method) and calculates elapsed time.
 * 
 * This interceptor will create a org.apache.commons.logging.Log for the actual method intercepted and the execution time of the method
 * is logged at the info level if info level logging is enabled for that actual class.
 * 
 * This method takes a configurable 'warningThreshhold', the number of milliseconds until performance is considered a "warning."
 * If/when the 'warningThreshhold' is exceeded the performance will be logged as at a warning level.
 * 
 * @see org.aopalliance.intercept.MethodInterceptor
 * 
 * @author Jon Shrader
 */
public class PerformanceLogMethodInterceptor implements MethodInterceptor {

	/** number of milliseconds in a second */
	private static final int NUMBER_OF_MILLIS_N_A_SECOND = 1000;

	/** the default warning threshhold */
	public static final Integer DEFAULT_WARNING_THRESHHOLD = Integer.valueOf(1500);

	/** The custom set warning threshhold. */
	private Integer warningThreshhold;

	/** The custom warning threshold that is based on the class+method being run */
	private Map<String, Integer> classAndMethodSpecificWarningThreshold;

	/** The Constant IN_ELAPSED_TIME. */
	private static final String IN_ELAPSED_TIME = "] in elapsed time [";

	/** The Constant ENTER. */
	private static final String ENTER = "enter ";

	/** The Constant EXIT. */
	private static final String EXIT = "exit ";

	/** The Constant PERFORMANCE_WARNING_RESPONSE_FOR. */
	private static final String PERFORMANCE_WARNING_RESPONSE_FOR = "PERFORMANCE WARNING response for ";

	/** The Constant MILLIS. */
	private static final String MILLIS = " millis ";

	/** The Constant SECS. */
	private static final String SECS = " secs";

	/** The Constant OPEN_BRACKET. */
	private static final String OPEN_BRACKET = "[";

	/** The Constant CLOSE_BRACKET. */
	private static final String CLOSE_BRACKET = "]";

	/** The Constant DOT. */
	private static final String DOT = ".";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@Override
	// JSHRADER throws throwable part of the interface, unavoidable
	// CHECKSTYLE:OFF
			public final
			Object invoke(final MethodInvocation methodInvocation) throws Throwable {
		// CHECKSTYLE:ON
		final Logger methodLog = LoggerFactory.getLogger(methodInvocation.getMethod().getDeclaringClass());

		// only log entry at the debug level
		if (methodLog.isDebugEnabled()) {
			methodLog.debug(ENTER + OPEN_BRACKET + methodInvocation.getMethod().getDeclaringClass().getSimpleName() + DOT
					+ methodInvocation.getMethod().getName() + CLOSE_BRACKET);
		}

		final long startTime = System.currentTimeMillis();
		final Object retVal = methodInvocation.proceed();
		final long elapsedTime = System.currentTimeMillis() - startTime;

		final String callingClassAndMethod =
				methodInvocation.getMethod().getDeclaringClass().getSimpleName() + DOT + methodInvocation.getMethod().getName();
		final long warningThreshold = getWarningThreshold(callingClassAndMethod);
		// log exit performance timing at the warning, info or debug level
		if (elapsedTime > warningThreshold) {
			methodLog.warn(PERFORMANCE_WARNING_RESPONSE_FOR + OPEN_BRACKET + callingClassAndMethod + IN_ELAPSED_TIME + elapsedTime
					/ NUMBER_OF_MILLIS_N_A_SECOND + DOT + elapsedTime % NUMBER_OF_MILLIS_N_A_SECOND + SECS + CLOSE_BRACKET
					+ " is slower than configured threshold of [" + warningThreshold + CLOSE_BRACKET + MILLIS);
		} else if (methodLog.isDebugEnabled()) {
			methodLog.debug(EXIT + OPEN_BRACKET + callingClassAndMethod + IN_ELAPSED_TIME + elapsedTime / NUMBER_OF_MILLIS_N_A_SECOND
					+ DOT + elapsedTime % NUMBER_OF_MILLIS_N_A_SECOND + SECS + CLOSE_BRACKET);
		} else if (methodLog.isInfoEnabled()) {
			methodLog.info(EXIT + OPEN_BRACKET + callingClassAndMethod + IN_ELAPSED_TIME + elapsedTime / NUMBER_OF_MILLIS_N_A_SECOND
					+ DOT + elapsedTime % NUMBER_OF_MILLIS_N_A_SECOND + SECS + CLOSE_BRACKET);
		}

		return retVal;
	}

	/**
	 * Get the warning threshold
	 * 
	 * @return warning threshhold
	 */
	public final Integer getWarningThreshhold() {
		return getWarningThreshold(null);
	}

	/**
	 * Gets the warning threshold. Priority is 1. <code>methoToWarningThreshold</code> 2. <code>warningThreshhold</code> 3.
	 * <code>DEFAULT_WARNING_THRESHHOLD</code>
	 * 
	 * @param callingMethod - the method the interceptor is running on
	 * @return the warning threshold
	 */
	public final Integer getWarningThreshold(final String callingMethod) {
		Integer threshold = null;

		// check to see if the method has it's own threshold
		if (classAndMethodSpecificWarningThreshold != null) {
			threshold = classAndMethodSpecificWarningThreshold.get(callingMethod);
		}

		// there wasn't a custom threshold for the method - use either the class level warningThreshold or the default one
		if (threshold == null) {
			if (warningThreshhold == null) {
				threshold = DEFAULT_WARNING_THRESHHOLD;
			} else {
				threshold = warningThreshhold;
			}
		}

		return threshold;

	}

	/**
	 * Set a threshold, in millisecods, for which we log warnings if method responses come to us slower than the threshold.
	 * 
	 * @param warningThreshhold the new warning threshhold
	 */
	public final void setWarningThreshhold(final Integer warningThreshhold) {
		this.warningThreshhold = warningThreshhold;
	}

	/**
	 * Sets a threshold for specific methods in milliseconds. We log warnings if the method responses are slower than the threshold.
	 * 
	 * @param classAndMethodSpecificWarningThreshold the methodToWarningThreshold to set
	 */
	public final void setClassAndMethodSpecificWarningThreshold(final Map<String, Integer> classAndMethodSpecificWarningThreshold) {
		this.classAndMethodSpecificWarningThreshold = classAndMethodSpecificWarningThreshold;
	}

}

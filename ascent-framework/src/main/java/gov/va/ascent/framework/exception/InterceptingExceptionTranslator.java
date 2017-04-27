package gov.va.ascent.framework.exception;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ThrowsAdvice;

/**
 * This is a configurable interceptor which will catch and translate one exception type into another. This is useful if
 * you wish to &quot;wrap&quot; 3rd party framework exceptions for a &quot;suite&quot; of beans, and/or if you wish to
 * make sure every exception coming from a tier in your application is of a specific exception type.
 * 
 * In general, exceptions will be converted to some application specific equivalent.
 * 
 * 
 * @see org.aopalliance.intercept.MethodInterceptor
 * 
 * @author Jon Shrader
 */
public class InterceptingExceptionTranslator implements ThrowsAdvice {

	/** logger for this class. */
	private static final Logger LOGGER = LoggerFactory.getLogger(InterceptingExceptionTranslator.class);

	/**
	 * A map to tell us what keys to handle and convert into more specific exception types.
	 */
	private Map<String, Class<? extends RuntimeException>> exceptionMap;

	/**
	 * A set of packages and/or exception class names we should exclude during translation.
	 */
	private Set<String> exclusionSet;

	/**
	 * The default exception to raise in the event its not a success and also not a mapped exception type.
	 */
	private Class<? extends RuntimeException> defaultExceptionType;

	/**
	 * Log the exception, and rethrow a some sort of application exception.
	 * 
	 * @param method the method
	 * @param args the args
	 * @param target the target
	 * @param throwable the throwable
	 */
	public final void afterThrowing(final Method method, final Object[] args, final Object target,
			final Throwable throwable) {

		try {
			if (exclusionSet != null
					&& (exclusionSet.contains(throwable.getClass().getPackage().getName()) || exclusionSet
							.contains(throwable.getClass().getName()))) {
				if (LOGGER.isDebugEnabled()) {
					InterceptingExceptionTranslator.LOGGER.debug("Exception translator caught exception ["
							+ throwable.getClass() + "] however per configuration not translating this exception.");
				}
				return;
			} else {
				ExceptionHandlingUtils.logException("InterceptingExceptionTranslator", method, args, throwable);
			}

			final RuntimeException resolvedRuntimeException = resolveRuntimeException(throwable);

			// set the original message as the cause
			if (resolvedRuntimeException != null) {
				resolvedRuntimeException.initCause(throwable);
				throw resolvedRuntimeException;
			}

		} catch (final InstantiationException e) {
			InterceptingExceptionTranslator.LOGGER.error(
					"InstantiationException likely configuration error, review log/configuration to troubleshoot", e);

		} catch (final IllegalAccessException e) {
			InterceptingExceptionTranslator.LOGGER.error(
					"IllegalAccessException likely configuration error, review log/configuration to troubleshoot", e);
		}
	}

	/**
	 * Resolve the runtime exception for the throwable
	 * 
	 * @param throwable the throwable
	 * @return the runtime exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	private RuntimeException resolveRuntimeException(final Throwable throwable) throws InstantiationException,
			IllegalAccessException {
		// custom exception type to represent the error
		RuntimeException resolvedRuntimeException = null;

		// if the message is in the map as a key, raise the correct type of
		// exception
		if (exceptionMap != null && exceptionMap.containsKey(throwable.getClass().getName())) {
			resolvedRuntimeException = exceptionMap.get(throwable.getClass().getName()).newInstance();
		} else if (defaultExceptionType != null) {
			// otherwise raise the default type of exception
			resolvedRuntimeException = defaultExceptionType.newInstance();
		}
		return resolvedRuntimeException;
	}

	/**
	 * Sets the exception map.
	 * 
	 * @param exceptionMap the exceptionMap to set
	 */
	public final void setExceptionMap(final Map<String, Class<? extends RuntimeException>> exceptionMap) {
		this.exceptionMap = exceptionMap;
	}

	/**
	 * Sets the default exception type.
	 * 
	 * @param defaultExceptionType the defaultExceptionType to set
	 */
	public final void setDefaultExceptionType(final Class<? extends RuntimeException> defaultExceptionType) {
		this.defaultExceptionType = defaultExceptionType;
	}

	/**
	 * Sets the exclusion set.
	 * 
	 * @param exclusionSet the exclusionSet to set
	 */
	public final void setExclusionSet(final Set<String> exclusionSet) {
		this.exclusionSet = exclusionSet;
	}

}
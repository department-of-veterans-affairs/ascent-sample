package gov.va.ascent.framework.exception;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ExceptionToExceptionTranslationHandler is an exception handler that does a translation to a 
 * new exception type before rethrowing.  This is useful to quickly log and wrap an exception from a 3rd party 
 * component.  The wrapped exception type should be of a internal exception type that carries forth not only
 * the original exception but also additional diagnostic information that will help us trace down root causes later.
 * 
 * @author jshrader
 */
public class ExceptionToExceptionTranslationHandler {
	
	/** logger for this class. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionToExceptionTranslationHandler.class);
	
	/**
	 * A map to tell us what keys to handle and convert into more desired exception types.
	 */
	private Map<Class<? extends Throwable>, Class<? extends RuntimeException>> exceptionMap;

	/**
	 * A set of packages and/or exception class names we should exclude during translation.
	 */
	private Set<Class<? extends Throwable>> exclusionSet;

	/**
	 * The default exception to raise in the event its not a success and also not a mapped exception type.
	 */
	private Class<? extends RuntimeException> defaultExceptionType;
	
	/**
	 * Instantiates a new exception to exception translation handler.
	 */
	public ExceptionToExceptionTranslationHandler(){
		this(null, null, null);
	}
	
	/**
	 * Instantiates a new exception to exception translation handler.
	 *
	 * @param exceptionMap the exception map
	 * @param exclusionSet the exclusion set
	 * @param defaultExceptionType the default exception type
	 */
	public ExceptionToExceptionTranslationHandler(Map<Class<? extends Throwable>, Class<? extends RuntimeException>> exceptionMap, 
			Set<Class<? extends Throwable>> exclusionSet,
			Class<? extends RuntimeException> defaultExceptionType) {
		super();
		
		//create reasonable default exception type as our top most runtime exception if none was specified
		if(defaultExceptionType == null){
			this.defaultExceptionType = WssRuntimeException.class;
		} else {
			this.defaultExceptionType = defaultExceptionType;
		}
		
		//create reasonable default exclusion map if none specified
		if(exclusionSet == null){
			this.exclusionSet = new HashSet<Class<? extends Throwable>>();
		} else {
			this.exclusionSet = exclusionSet;
		}
		//ensure the default type is in the exclusion set to ensure we never "double wrap" exceptions
		this.exclusionSet.add(this.defaultExceptionType);
		
		//no reason to create a default exception map
		this.exceptionMap = exceptionMap;
	}

	/**
	 * Log the exception, and rethrow a some sort of application exception we translate into.
	 * 
	 * @param method the method
	 * @param args the args
	 * @param throwable the throwable
	 */
	public final void handleViaTranslation(final Method method, final Object[] args, final Throwable throwable) throws Throwable {

		if(method == null || throwable == null){
			LOGGER.error(
					"Invalid invocation of this method, method and/or throwable is null so doing nothing here!", method, throwable);
			return;
		}
		
		try {
			// 1st check if the throwable is already one we shouldn't translate.  
			// If so we don't want to log the exception again or wrap it in another exception
			if (shouldSkipTranslation(throwable)) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("ExceptionToExceptionTranslator inspecting exception ["
							+ throwable.getClass() + "] however per configuration not translating this exception.");
				}
				throw throwable;
			
			// else see if we have something to translate this to, and translate accordingly
			} else {
				ExceptionHandlingUtils.logException(this.getClass().getName(), method, args, throwable);
				
				RuntimeException resolvedRuntimeException = null;

				// check if our map contains a translation mapping for this data
				if (exceptionMap != null && exceptionMap.containsKey(throwable.getClass())) {
					resolvedRuntimeException = exceptionMap.get(throwable.getClass().getName()).newInstance();
				} else if (defaultExceptionType != null) {
					// otherwise raise the default type of exception
					resolvedRuntimeException = defaultExceptionType.newInstance();
				}
				
				// set the original message as the cause
				if (resolvedRuntimeException != null) {
					resolvedRuntimeException.initCause(throwable);
					throw resolvedRuntimeException;
				}

			}

			
		} catch (final InstantiationException e) {
			LOGGER.error(
					"InstantiationException likely configuration error, review log/configuration to troubleshoot", e);

		} catch (final IllegalAccessException e) {
			LOGGER.error(
					"IllegalAccessException likely configuration error, review log/configuration to troubleshoot", e);
		}
	}
	
	private boolean shouldSkipTranslation(Throwable throwable){
		boolean returnVal = false;
		if (exclusionSet.contains(throwable.getClass())) {
			returnVal = true;
		} else {
			for(Class<? extends Throwable> clazz: exclusionSet){
				if(clazz.isAssignableFrom(throwable.getClass())){
					returnVal = true;
					break;
				}
			}
		}
		return returnVal;
	}

}

package gov.va.ascent.framework.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceLoggingAspect {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(PerformanceLoggingAspect.class);
	
	/** number of milliseconds in a second */
	private static final int NUMBER_OF_MILLIS_N_A_SECOND = 1000;

	/** The Constant IN_ELAPSED_TIME. */
	private static final String IN_ELAPSED_TIME = "] in elapsed time [";

	/** The Constant ENTER. */
	private static final String ENTER = "enter ";

	/** The Constant EXIT. */
	private static final String EXIT = "exit ";

	/** The Constant SECS. */
	private static final String SECS = " secs";

	/** The Constant OPEN_BRACKET. */
	private static final String OPEN_BRACKET = "[";

	/** The Constant CLOSE_BRACKET. */
	private static final String CLOSE_BRACKET = "]";

	/** The Constant DOT. */
	private static final String DOT = ".";
	
	public static final Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {	
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("PerformanceLoggingAspect executing around method:" + joinPoint.toLongString());			
		}
		
        Method method = ((MethodSignature) joinPoint.getStaticPart().getSignature()).getMethod();
		final Logger methodLog = LoggerFactory.getLogger(method.getDeclaringClass());
		final long startTime = System.currentTimeMillis();
		
		// only log entry at the debug level
		if (methodLog.isDebugEnabled()) {
			methodLog.debug(ENTER + OPEN_BRACKET + method.getDeclaringClass().getSimpleName() + DOT
					+ method.getName() + CLOSE_BRACKET);
		}
		
		Object returnObject = null;
        try {
            returnObject = joinPoint.proceed();
        } catch (Throwable throwable) {
        	throw throwable;
        }
        finally {
        	LOGGER.debug("PerformanceLoggingAspect after method was called.");
        	final long elapsedTime = System.currentTimeMillis() - startTime;
        	final String callingClassAndMethod =
        			method.getDeclaringClass().getSimpleName() + DOT + method.getName();
    		if (methodLog.isInfoEnabled()) {
    			methodLog.info(EXIT + OPEN_BRACKET + callingClassAndMethod + IN_ELAPSED_TIME + elapsedTime / NUMBER_OF_MILLIS_N_A_SECOND
    					+ DOT + elapsedTime % NUMBER_OF_MILLIS_N_A_SECOND + SECS + CLOSE_BRACKET);
    		}

        }
        return returnObject;
		
    }

}

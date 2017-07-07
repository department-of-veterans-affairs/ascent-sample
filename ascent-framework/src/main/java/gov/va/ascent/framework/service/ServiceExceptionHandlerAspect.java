package gov.va.ascent.framework.service;

import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import gov.va.ascent.framework.exception.ExceptionToExceptionTranslationHandler;
import gov.va.ascent.framework.exception.WssRuntimeException;
import gov.va.ascent.framework.util.Defense;

/**
 * The Class ServiceExceptionHandlerAspect is a exception handling aspect for the service tier.  The current implementation
 * leverages ExceptionToExceptionTranslator to basically translate and log errors.  So if exceptions are raised by a
 * web service client, a DAO, etc. and are of the type "HibernateException" or some 3rd party exception type, the exception
 * will pass through the translator which will ensure it gets logged and wrapped into a exception of our own type with
 * associated diagnostic details that we can leverage to help trace through exceptions later.
 * 
 * @author jshrader
 */
@Aspect
@Component
public class ServiceExceptionHandlerAspect extends BaseServiceAspect {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionHandlerAspect.class);

	private ExceptionToExceptionTranslationHandler exceptionToExceptionTranslator; 
	
    /**
     * Instantiates a new service exception handler aspect using a default ExceptionToExceptionTranslator.
     * Explicitly declare this bean and use the other constructor(s) to customize the translator.
     */
    public ServiceExceptionHandlerAspect() {
    	//this to prevent double wrapping of WssRuntimeException
    	final Set<Class<? extends Throwable>> exclusionSet = new HashSet<Class<? extends Throwable>>();
    	exclusionSet.add(WssRuntimeException.class);
    	exclusionSet.add(IllegalArgumentException.class);
    	this.exceptionToExceptionTranslator = new ExceptionToExceptionTranslationHandler(
    			null, exclusionSet, ServiceException.class);
    	
    	//this will allow double wrapping of WssRuntimeExceptions
//    	this(new ExceptionToExceptionTranslationHandler(null, null, ServiceException.class));
    }

	/**
	 * Instantiates a new service exception handler aspect using the specified exception translator.
	 *
	 * @param exceptionToExceptionTranslator the exception to exception translator
	 */
	public ServiceExceptionHandlerAspect(ExceptionToExceptionTranslationHandler exceptionToExceptionTranslator) {
		super();
		Defense.notNull(exceptionToExceptionTranslator, "exceptionToExceptionTranslator cannot be null");
		this.exceptionToExceptionTranslator = exceptionToExceptionTranslator;
	}

	@AfterThrowing(pointcut = "publicStandardServiceMethod()", throwing = "throwable")
	public void afterThrowing(JoinPoint joinPoint, Throwable throwable) throws Throwable {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("ServiceExceptionHandlerAspect executing, handling a throwable:" + joinPoint.toLongString());			
		}
        exceptionToExceptionTranslator.handleViaTranslation(((MethodSignature) joinPoint.getStaticPart().getSignature()).getMethod(), 
        		joinPoint.getArgs(), throwable);
    }
	
}

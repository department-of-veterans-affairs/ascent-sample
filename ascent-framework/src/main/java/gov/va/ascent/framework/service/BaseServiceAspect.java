package gov.va.ascent.framework.service;

import org.aspectj.lang.annotation.Pointcut;

/**
 * This is the base class for service aspects.
 * 
 * @author jshrader
 */
public class BaseServiceAspect {
	
	/**
	 * This pointcut reflects a public standard service method.
	 * 
	 * These are methods which are
	 * (1) public
	 * (2) return a ServiceResponse
	 * 
	 * @See gov.va.ascent.framework.service.ServiceResponse
	 */
	@Pointcut("execution(public gov.va.ascent.framework.service.ServiceResponse+ *(..))")
	protected final static void publicStandardServiceMethod() {}

}

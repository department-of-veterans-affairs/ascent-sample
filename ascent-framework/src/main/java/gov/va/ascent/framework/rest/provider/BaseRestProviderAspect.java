package gov.va.ascent.framework.rest.provider;

import org.aspectj.lang.annotation.Pointcut;

/**
 * This is the base class for rest provider aspects.
 * 
 * @author jshrader
 */
public class BaseRestProviderAspect {
	
	/**
	 * This aspect defines the pointcut of standard REST controller.  Those are controllers that...
	 * 
	 * (1) are annotated with org.springframework.web.bind.annotation.RestController
	 * 
	 * Ensure you follow that pattern to make use of this standard pointcut.
	 */
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
	protected final static void restController() {}
	
	/**
	 * This aspect defines the pointcut of standard REST endpoints.  Those are endpoints that...
	 * 
	 * (1) are rest controllers (see that pointcut)
	 * (2) the method is public
	 * (3) the method returns org.springframework.http.ResponseEntity<gov.va.ascent.framework.service.ServiceResponse+>
	 * 
	 * Ensure you follow that pattern to make use of this standard pointcut.
	 */
	@Pointcut("execution(public org.springframework.http.ResponseEntity<gov.va.ascent.framework.service.ServiceResponse+> *(..))")
	protected final static void publicServiceResponseRestMethod() {}

}

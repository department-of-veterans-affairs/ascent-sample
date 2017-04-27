package gov.va.ascent.framework.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import gov.va.ascent.framework.aspect.PerformanceLoggingAspect;

@Aspect
@Component
@Order(-9999)
public class ServiceTimerAspect extends BaseServiceAspect {
	
	@Around("publicStandardServiceMethod()")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {	
		return PerformanceLoggingAspect.aroundAdvice(joinPoint);
    }
	
}

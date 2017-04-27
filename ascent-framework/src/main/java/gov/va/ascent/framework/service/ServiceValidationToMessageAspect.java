package gov.va.ascent.framework.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.validation.Validatable;
import gov.va.ascent.framework.validation.ViolationMessageParts;

/**
 * The Class ServiceValidationToMessageAspect is an aspect that performs validation (i.e. JSR 303) on the 
 * standard service methods which are validatable, converting validation errors into Message objects in a consistent way.
 * 
 * Standard service operations which are validatable are those which are...
 * (1) public
 * (2) return a ServiceResponse and
 * (3) have a single input that is of the type Validatable.
 * 
 * @See gov.va.ascent.framework.service.ServiceResponse
 * @see gov.va.ascent.framework.validation.Validatable
 * 
 * @author jshrader
 */
@Aspect
@Component
@Order(-9998)
public class ServiceValidationToMessageAspect extends BaseServiceAspect {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ServiceValidationToMessageAspect.class);
	
	@Around("publicStandardServiceMethod() && args(request)")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint, Validatable request) throws Throwable {	
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("ServiceValidationToMessageAspect executing around method:" + joinPoint.toLongString());			
		}
		
		// fetch the request
		Validatable serviceRequest = null;
		if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0] != null) {
			serviceRequest = (Validatable) joinPoint.getArgs()[0];
		}

		// start creating the response
		ServiceResponse serviceResponse = null;
		final Map<String, List<ViolationMessageParts>> messages = new LinkedHashMap<String, List<ViolationMessageParts>>();
		if (joinPoint.getArgs().length > 0) {
			MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
			if (serviceRequest == null) {
				serviceResponse = (ServiceResponse) methodSignature.getMethod().getReturnType().newInstance();
				serviceResponse.addMessage(MessageSeverity.ERROR, "Request.NotNull", methodSignature.getMethod()
						.getParameterTypes()[0].getSimpleName() + ".NotNull");
			} else {
				serviceRequest.validate(messages);
				if (!messages.isEmpty()) {
					serviceResponse = (ServiceResponse) methodSignature.getMethod().getReturnType().newInstance();
					convertMapToMessages(serviceResponse, messages);
				}
			}
		}

		try {
        	if (serviceResponse == null) {
    			serviceResponse = (ServiceResponse) joinPoint.proceed();
    		} else {
    			LOGGER.debug("ServiceValidationToMessageAspect encountered validation errors, not proceeding with call.");
    		}
        } catch (Throwable throwable) {
        	throw throwable;
        }
        finally {
        	LOGGER.debug("ServiceValidationToMessageAspect after method was called.");
        }

		return serviceResponse;
		
    }
	
	/**
	 * Convert map to messages.  This is exposed so services can call directly if they desire.
	 * 
	 * @param serviceResponse the service response
	 * @param messages the messages
	 */
	public static void convertMapToMessages(final ServiceResponse serviceResponse, final Map<String, List<ViolationMessageParts>> messages) {
		for (Entry<String, List<ViolationMessageParts>> entry : messages.entrySet()) {
			for (ViolationMessageParts fieldError : entry.getValue()) {
				serviceResponse.addMessage(MessageSeverity.ERROR, fieldError.getNewKey(), fieldError.getText());
			}
		}
		Collections.sort(serviceResponse.getMessages(), new Comparator<Message>() {
			@Override
			public int compare(Message message1, Message message2) {
				return message1.getKey().compareTo(message2.getKey());
			}
		});
	}
	
}

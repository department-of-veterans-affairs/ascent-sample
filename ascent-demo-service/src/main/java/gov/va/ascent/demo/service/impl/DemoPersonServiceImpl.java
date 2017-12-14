package gov.va.ascent.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import gov.va.ascent.demo.partner.person.ws.client.PersonWsClient;
import gov.va.ascent.demo.partner.person.ws.client.transfer.PersonInfo;
import gov.va.ascent.demo.partner.person.ws.client.transfer.PersonInfoRequest;
import gov.va.ascent.demo.partner.person.ws.client.transfer.PersonInfoResponse;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonBySSN;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonBySSNResponse;
import gov.va.ascent.demo.partner.person.ws.transfer.ObjectFactory;
import gov.va.ascent.demo.partner.person.ws.transfer.PersonDTO;
import gov.va.ascent.demo.service.api.DemoPersonService;
import gov.va.ascent.demo.service.exception.DemoServiceException;
import gov.va.ascent.demo.service.utils.HystrixCommandConstants;
import gov.va.ascent.demo.service.utils.StringUtil;
import gov.va.ascent.framework.exception.AscentRuntimeException;
import gov.va.ascent.framework.messages.Message;
import gov.va.ascent.framework.messages.MessageSeverity;
import gov.va.ascent.framework.util.Defense;

@Service(value = DemoPersonServiceImpl.BEAN_NAME)
@Component
@Qualifier("IMPL")
@RefreshScope
@DefaultProperties(groupKey = HystrixCommandConstants.ASCENT_PERSON_DEMO_SERVICE_GROUP_KEY)

/**
 * Implementation class for the Demo Person Service. 
 * The class demonstrates the implementation of hystrix circuit breaker 
 * pattern for read operations. When there is a failure the fallback method is invoked and the response is
 * returned from the cache 
 * @author 
 *
 */
public class DemoPersonServiceImpl implements DemoPersonService {
	final static Logger LOGGER = LoggerFactory.getLogger(DemoPersonServiceImpl.class);
	
	/** Bean name constant */
	public static final String BEAN_NAME = "personServiceImpl";

	/** The person web service client reference. */
	@Autowired
	private PersonWsClient personWsClient;
	
	@Autowired
	private CacheManager cacheManager;
	
	/** The length of a valid SSN */
	private static final int SSN_LENGTH = 9;
	
	/** String Constant NO_PERSON_FOUND_FOR_SSN */
	private static final String NO_PERSON_FOUND_FOR_SSN = "No person found for SSN ";

	/** String Constant NOPERSONFORSSN */
	private static final String NOPERSONFORSSN = "NOPERSONFORSSN";
	
	/** String Constant NOPERSONFORPTCTID */
	private static final String NOPERSONFORPTCTID = "NOPERSONFORPTCTID";
	
	/** String Constant NO_PERSON_FOUND_FOR_PARTICIPANT_ID */
	private static final String NO_PERSON_FOUND_FOR_PARTICIPANT_ID = "No person found for participantID ";
	
	/** The Constant PERSON_OBJECT_FACTORY. */
	protected static final ObjectFactory PERSON_OBJECT_FACTORY = new ObjectFactory();

	/* (non-Javadoc)
     * @see gov.va.ascent.demo.service.api.DemoPersonService#getPersonInfo
     *         (gov.va.ascent.demo.partner.person.ws.client.transfer.PersonInfoRequest)
     * 
     * @CachePut Annotation In contrast to the {@link Cacheable @Cacheable} annotation, this annotation does not 
     * cause the advised method to be skipped. Rather, it always causes the method to be invoked and its result to be stored in the 
     * associated cache
     * 
     */
    @Override
    @CachePut(value="demoPersonService", key="#personInfoRequest", 
          unless="#result == null || #result.personInfo == null || #result.hasErrors() || #result.hasFatals()")
    @HystrixCommand(
            fallbackMethod = "getPersonInfoFallBack", 
            commandKey = "GetPersonInfoBySSNCommand", 
            ignoreExceptions = {IllegalArgumentException.class})
    public PersonInfoResponse getPersonInfo(PersonInfoRequest personInfoRequest) {
        // Check for valid input arguments and WS Client reference.
        Defense.notNull(personWsClient, "Unable to proceed with Person Service request. The personWsClient must not be null.");
        Defense.notNull(personInfoRequest, "Invalid argument, personInfoRequest must not be null.");
        Defense.notNull(personInfoRequest.getSsn(), "Invalid personInfoRequest. SSN must not be null.");
        Defense.isTrue(personInfoRequest.getSsn().length() == SSN_LENGTH, "Invalid personInfoRequest SSN. Length must be "
                + SSN_LENGTH);
        if (cacheManager.getCache("demoPersonService") != null && cacheManager.getCache("demoPersonService").get(personInfoRequest) != null) {
          return cacheManager.getCache("demoPersonService").get(personInfoRequest, PersonInfoResponse.class);
        } else {
            // Prepare the WS request
            final JAXBElement<FindPersonBySSN> findPersonBySSNRequestElement = createFindPersonBySSNRequest(personInfoRequest);
            
            LOGGER.debug("FindPersonBySSN JAXBElement: {}", 
                    (findPersonBySSNRequestElement != null ? ReflectionToStringBuilder.toString(findPersonBySSNRequestElement): null));
    
            // Invoke the Person Web Service via the WS Client
            final JAXBElement<FindPersonBySSNResponse> findPersonBySSNResponseElement = personWsClient.getPersonInfo(findPersonBySSNRequestElement);
            
            LOGGER.debug("FindPersonBySSNResponse JAXBElement: {}", 
                    (findPersonBySSNResponseElement != null ? ReflectionToStringBuilder.toString(findPersonBySSNResponseElement): null));
    
            // Prepare the service response
            final PersonInfoResponse personInfoResponse =
                    createPersonInfoResponse(findPersonBySSNResponseElement, personInfoRequest.getSsn());
            LOGGER.debug("PersonInfoResponse: {}", 
                    ReflectionToStringBuilder.toString(personInfoResponse));
            return personInfoResponse;
        }
    }
    
    /* (non-Javadoc)
     * @see gov.va.ascent.demo.service.api.DemoPersonService#findPersonByParticipantID
     *                 (gov.va.ascent.demo.partner.person.ws.client.transfer.PersonInfoRequest)
     * @Cacheable Annotation indicating that the result of invoking a method (or all methods in a class) can be cached.
     */
    @Override
    @CachePut(value="demoPersonService", key="#personInfoRequest", 
      unless="#result == null || #result.personInfo == null || #result.hasErrors() || #result.hasFatals()")
    @HystrixCommand(
                fallbackMethod = "findPersonByParticipantIDFallBack", 
                commandKey = "GetPersonInfoByPIDCommand",
                ignoreExceptions = {IllegalArgumentException.class})
    public PersonInfoResponse findPersonByParticipantID(final PersonInfoRequest personInfoRequest) {
        
        // Check for valid input arguments and WS Client reference.
        Defense.notNull(personWsClient, "Unable to proceed with Person Service request. The personWsClient must not be null.");
        Defense.notNull(personInfoRequest.getParticipantID(), "Invalid argument, pid must not be null.");

        if (cacheManager.getCache("demoPersonService") != null && cacheManager.getCache("demoPersonService").get(personInfoRequest) != null) {
          return cacheManager.getCache("demoPersonService").get(personInfoRequest, PersonInfoResponse.class);
        } else {
            // Prepare the WS request
            final JAXBElement<FindPersonByPtcpntId> findPersonByPtcpntIdRequestElement = createFindPersonByPidRequest(personInfoRequest);
    
            // Invoke the Person Web Service via the WS Client
            final JAXBElement<FindPersonByPtcpntIdResponse> findPersonByPtcpntIdResponseElement =
                    personWsClient.getPersonInfoByPtcpntId(findPersonByPtcpntIdRequestElement);
    
            // Prepare the service response
            final PersonInfoResponse personInfoResponse =
                    createPersonInfoResponse(findPersonByPtcpntIdResponseElement, personInfoRequest.getParticipantID());
            return personInfoResponse;
        }
    }
	
	/**
	 * Hystrix Fallback Method Which is Triggered When there Is An Unexpected Exception
	 * in getPersonInfo
	 * @param personInfoRequest The request from the Java Service.
	 * @return A JAXB element for the WS request
	 */
	@HystrixCommand(commandKey = "GetPersonInfoFallBackCommand")
	public PersonInfoResponse getPersonInfoFallBack(PersonInfoRequest personInfoRequest, Throwable throwable) {
	  final PersonInfoResponse response = new PersonInfoResponse();
	    if (throwable != null) {
	      LOGGER.error("Exception occurred in getPersonInfoFallBack {}", throwable);
          DemoServiceException exc = new DemoServiceException("Error: " + throwable.toString());
          throw exc;
	    } 
	    else {
	      return response;  
	    }
	}
	
	/**
     * Hystrix Fallback Method Which is Triggered When there Is An Unexpected Exception
     * in findPersonByParticipantID method
     * @param personInfoRequest The request from the Java Service.
     * @return A JAXB element for the WS request
     */
    @HystrixCommand(commandKey = "FindPersonByParticipantIDFallBackCommand")
    public PersonInfoResponse findPersonByParticipantIDFallBack(PersonInfoRequest personInfoRequest, Throwable throwable) {
        final PersonInfoResponse response = new PersonInfoResponse();
        if (throwable != null) {
          final String msg = throwable.getMessage();
          final List<Message> messages = new ArrayList<Message>();
          messages.add(newMessage(MessageSeverity.FATAL, "FATAL", msg));
          response.setMessages(messages);
          if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(msg);
          }
          return response;
        } else {
            LOGGER.error("findPersonByParticipantIDFallBack No Throwable Exception and No Cached Data. Just Raise Runtime Exception {}", personInfoRequest);
            throw new AscentRuntimeException("There was a problem processing your request.");
        }
    }
    
    /**
     * Helper method to create a Message object.
     *
     * @param severity the severity
     * @param key the key
     * @param text the text
     * @return the message
     */
    private final Message newMessage(final MessageSeverity severity, final String key, final String text) {
        final Message msg = new Message();
        msg.setSeverity(severity);
        msg.setKey(key);
        msg.setText(text);
        return msg;
    }

	
	/**
	 * @param personInfoRequest The request from the Java Service.
	 * @return A JAXB element for the WS request
	 */
	private JAXBElement<FindPersonByPtcpntId> createFindPersonByPidRequest(final PersonInfoRequest personInfoRequest) {

		final FindPersonByPtcpntId findPersonByPtcpntId = new FindPersonByPtcpntId();
		findPersonByPtcpntId.setPtcpntId(personInfoRequest.getParticipantID());
		final JAXBElement<FindPersonByPtcpntId> findPersonByPtcptIdRequestElement =
				PERSON_OBJECT_FACTORY.createFindPersonByPtcpntId(findPersonByPtcpntId);
		return findPersonByPtcptIdRequestElement;
	}
	
	/**
	 * @param personInfoRequest The request from the Java Service.
	 * @return A JAXB element for the WS request
	 */
	private JAXBElement<FindPersonBySSN> createFindPersonBySSNRequest(final PersonInfoRequest personInfoRequest) {

		final FindPersonBySSN findPersonBySSN = new FindPersonBySSN();
		findPersonBySSN.setSsn(personInfoRequest.getSsn());
		final JAXBElement<FindPersonBySSN> findPersonBySSNRequestElement =
				PERSON_OBJECT_FACTORY.createFindPersonBySSN(findPersonBySSN);
		return findPersonBySSNRequestElement;
	}
	
	/**
	 * @param findPersonBySSNResponseElement The response JAXB element
	 * @param ssn The person's SSN
	 * @return the person info response
	 */
	private PersonInfoResponse createPersonInfoResponse(
			final JAXBElement<FindPersonBySSNResponse> findPersonBySSNResponseElement, final String ssn) {

		final PersonInfoResponse personInfoResponse = new PersonInfoResponse();
		final String maskedInfo = StringUtil.getMask4(ssn);
		// Check for null xml element
		if (findPersonBySSNResponseElement == null) {

			personInfoResponse.addMessage(MessageSeverity.ERROR, NOPERSONFORSSN, NO_PERSON_FOUND_FOR_SSN + maskedInfo);
			
		} else {
			final FindPersonBySSNResponse findPersonBySSNResponse = findPersonBySSNResponseElement.getValue();
			// Check for null response object
			if (findPersonBySSNResponse == null) {

				personInfoResponse.addMessage(MessageSeverity.ERROR, NOPERSONFORSSN, NO_PERSON_FOUND_FOR_SSN + maskedInfo);
				
			} else {
				final PersonDTO personDto = findPersonBySSNResponse.getPersonDTO();
				// If no DTO was returned, the SSN did not match a person.
				if (personDto == null) {

					personInfoResponse.addMessage(MessageSeverity.ERROR, NOPERSONFORSSN, NO_PERSON_FOUND_FOR_SSN + maskedInfo);
					
					
				} else {
					// Copy the data of interest to the person info and add it to the service response.
					final PersonInfo personInfo = createPersonInfo(personDto);
					personInfoResponse.setPersonInfo(personInfo);
				}
			}
		}
		return personInfoResponse;
	}
	
	/**
	 * @param findPersonByPtcpntIdResponseElement The response JAXB element
	 * @param participantID The person's participantID
	 * @return the person info response
	 */
	private PersonInfoResponse createPersonInfoResponse(
			final JAXBElement<FindPersonByPtcpntIdResponse> findPersonByPtcpntIdResponseElement, final Long participantID) {

		final PersonInfoResponse personInfoResponse = new PersonInfoResponse();
		final String maskedInfo = StringUtil.getMask4(participantID.toString());
		// Check for null xml element
		if (findPersonByPtcpntIdResponseElement == null) {

			personInfoResponse.addMessage(MessageSeverity.ERROR, NOPERSONFORPTCTID, 
					NO_PERSON_FOUND_FOR_PARTICIPANT_ID + maskedInfo);
			
		} else {
			final FindPersonByPtcpntIdResponse findPersonByPtcpntIdResponse = findPersonByPtcpntIdResponseElement.getValue();
			// Check for null response object
			if (findPersonByPtcpntIdResponse == null) {

				personInfoResponse.addMessage(MessageSeverity.ERROR, NOPERSONFORPTCTID, 
						NO_PERSON_FOUND_FOR_PARTICIPANT_ID + maskedInfo);
				
			} else {
				final PersonDTO personDto = findPersonByPtcpntIdResponse.getPersonDTO();
				// If no DTO was returned, the PID did not match a person.
				if (personDto == null) {

					personInfoResponse.addMessage(MessageSeverity.ERROR, NOPERSONFORPTCTID, 
							NO_PERSON_FOUND_FOR_PARTICIPANT_ID + maskedInfo);
					
				} else {
					// Copy the data of interest to the person info and add it to the service response.
					final PersonInfo personInfo = createPersonInfo(personDto);
					personInfoResponse.setPersonInfo(personInfo);
				}
			}
		}
		return personInfoResponse;
	}
	
	/**
	 * @param personDto The person DTO
	 * @return A PersonInfo object containing data from the DTO
	 */
	private PersonInfo createPersonInfo(final PersonDTO personDto) {
		final PersonInfo personInfo = new PersonInfo();
		personInfo.setFileNumber(personDto.getFileNbr());
		personInfo.setFirstName(personDto.getFirstNm());
		personInfo.setMiddleName(personDto.getMiddleNm());
		personInfo.setLastName(personDto.getLastNm());
		personInfo.setParticipantId(personDto.getPtcpntId());
		personInfo.setSocSecNo(personDto.getSsnNbr());
		return personInfo;
	}

}
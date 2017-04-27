package gov.va.ascent.demo.partner.person.ws.client;

import gov.va.ascent.framework.config.AscentCommonSpringProfiles;
import gov.va.ascent.framework.util.Defense;
import gov.va.ascent.framework.ws.client.BaseWsClientSimulator;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonBySSN;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonBySSNResponse;

import java.io.IOException;
import java.io.StringReader;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

/**
 * This class implements a Simulator for the Person WS Client.
 * 
 * @author StuartT
 *
 */
@Component(PersonWsClientSimulator.BEAN_NAME)
@Profile({ AscentCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS,
	PersonWsClient.PROFILE_PERSONWSCLIENT_REMOTE_CLIENT_SIM })
public class PersonWsClientSimulator extends BaseWsClientSimulator implements PersonWsClient {
	

	/** The Constant BEAN_NAME. */
	public static final String BEAN_NAME = "personWsClientSimulator";
	
	/** The JAXB marshaller for the Person WS Client data */
	@Autowired
	@Qualifier("personWsClient")
	private Jaxb2Marshaller personMarshaller;
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PersonWsClientSimulator.class);
	
	/** The Constant SOAP_TEMPLATE_RESOURCE_DIRECTORY. */
	private static final String SOAP_TEMPLATE_RESOURCE_DIRECTORY = "mockResponses/";

	/**
	 * Post construct.
	 */
	@PostConstruct
	public final void postConstruct() {
		LOGGER.warn("Using SIMULATOR as implementation for PersonWsClient Interface.");
	}
	
	/* (non-Javadoc)
	 * @see gov.va.wss.common.services.ws.client.person.PersonWsClient#getPersonInfo(javax.xml.bind.JAXBElement)
	 */
	@Override
	public final JAXBElement<FindPersonBySSNResponse> 
			getPersonInfo(final JAXBElement<FindPersonBySSN> findPersonBySSNRequest) {

		Defense.notNull(findPersonBySSNRequest, 
				"Invalid web service simulator invocation. findPersonBySSNRequest must not be null.");
		Defense.notNull(findPersonBySSNRequest.getValue(), 
				"Invalid request payload. findPersonBySSNRequest.value must not be null.");

		return getSimulatedResponse();
	}

	/**
	 * @return The FindPersonBySSNResponse element
	 */
	@SuppressWarnings("unchecked")
	private JAXBElement<FindPersonBySSNResponse> getSimulatedResponse() {
		
		// Read the xml response from the simulator file.
		String personWSResponseString = null;
		final String personWSResponseFile = SOAP_TEMPLATE_RESOURCE_DIRECTORY + "person.xml";
		LOGGER.info("Reading file for Person WS simulator: " + personWSResponseFile);
		try {
			personWSResponseString = getSimulatorResponseByFileName(personWSResponseFile);
		} catch (IOException iox) {
			throw new PersonWsClientException("Unable to read simulator response file " + personWSResponseFile, iox);
		}

		final JAXBElement<FindPersonBySSNResponse> responseElement = (JAXBElement<FindPersonBySSNResponse>) 
				personMarshaller.unmarshal(new StreamSource(new StringReader(personWSResponseString)));
			
		return responseElement;
	}

	@Override
	public final JAXBElement<FindPersonByPtcpntIdResponse> getPersonInfoByPtcpntId(
			final JAXBElement<FindPersonByPtcpntId> findPersonByPtcpntIdRequest) {
		
		Defense.notNull(findPersonByPtcpntIdRequest, 
				"Invalid web service simulator invocation. findPersonByPtcpntIdRequest must not be null.");
		Defense.notNull(findPersonByPtcpntIdRequest.getValue(), 
				"Invalid request payload. findPersonByPtcpntIdRequest.value must not be null.");

		return getSimulatedResponseByPtcpntId();
		
	}
	
	/**
	 * @return The FindPersonBySSNResponse element
	 */
	@SuppressWarnings("unchecked")
	private JAXBElement<FindPersonByPtcpntIdResponse> getSimulatedResponseByPtcpntId() {
		
		// Read the xml response from the simulator file.
		String personWSResponseString = null;
		final String personWSResponseFile = SOAP_TEMPLATE_RESOURCE_DIRECTORY + "personbypid.xml";
		LOGGER.info("Reading file for Person WS simulator: " + personWSResponseFile);
		try {
			personWSResponseString = getSimulatorResponseByFileName(personWSResponseFile);
		} catch (IOException iox) {
			throw new PersonWsClientException("Unable to read simulator response file " + personWSResponseFile, iox);
		}

		final JAXBElement<FindPersonByPtcpntIdResponse> responseElement = (JAXBElement<FindPersonByPtcpntIdResponse>) 
				personMarshaller.unmarshal(new StreamSource(new StringReader(personWSResponseString)));
			
		return responseElement;
	}
}

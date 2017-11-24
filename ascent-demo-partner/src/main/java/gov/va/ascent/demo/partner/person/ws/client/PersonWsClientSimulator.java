package gov.va.ascent.demo.partner.person.ws.client;

import java.io.IOException;
import java.io.StringReader;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import gov.va.ascent.framework.audit.AuditEvents;
import gov.va.ascent.framework.audit.Auditable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonBySSN;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonBySSNResponse;
import gov.va.ascent.framework.config.AscentCommonSpringProfiles;
import gov.va.ascent.framework.util.Defense;
import gov.va.ascent.framework.ws.client.BaseWsClientSimulator;

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
	@Auditable(event = AuditEvents.REQUEST_RESPONSE, activity = "simPersonInfo")
	public JAXBElement<FindPersonBySSNResponse>
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
		final String personWSResponseFile = SOAP_TEMPLATE_RESOURCE_DIRECTORY + "person.xml";
		LOGGER.info("Reading file for Person WS simulator: " + personWSResponseFile);

		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance(FindPersonBySSNResponse.class);
		} catch (JAXBException e) {
			LOGGER.error("Exception occurred while creating JAXBInstance of FindPersonBySSNResponse");
		}

		Unmarshaller unmarshaller = null;
		JAXBElement<FindPersonBySSNResponse> responseElement = null;
		try {
			if (jc != null) {
				unmarshaller = jc.createUnmarshaller();
				responseElement = (JAXBElement<FindPersonBySSNResponse>) unmarshaller.unmarshal(getXMLStreamReader(personWSResponseFile),
						FindPersonBySSNResponse.class);
			}
		} catch (JAXBException e) {
			LOGGER.error("Exception occurred while unmarshalling FindPersonBySSNResponse");
		}

		return responseElement;
	}

	/**
	 * 
	 * @param personWSResponseFile
	 * @return XMLStreamReader object
	 */
	private XMLStreamReader getXMLStreamReader(String personWSResponseFile) {
		XMLInputFactory xif = XMLInputFactory.newFactory();
		xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
		xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
		XMLStreamReader xsr = null;
		try {
			xsr = xif.createXMLStreamReader(
					new StreamSource(getSimulatorResponseStreamByFileName(personWSResponseFile)));
		} catch (XMLStreamException e) {
			LOGGER.error("Exception occurred while reading from person xml file");
		}
		return xsr;
	}
	@Override
	@Auditable(event = AuditEvents.REQUEST_RESPONSE, activity = "simPersonInfoByPtcpntId")
	public JAXBElement<FindPersonByPtcpntIdResponse> getPersonInfoByPtcpntId(
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
		final String personWSResponseFile = SOAP_TEMPLATE_RESOURCE_DIRECTORY + "personbypid.xml";
		LOGGER.info("Reading file for Person WS simulator: " + personWSResponseFile);
		
		JAXBContext jc = null;
		try {
			jc = JAXBContext.newInstance(FindPersonByPtcpntIdResponse.class);
		} catch (JAXBException e) {
			LOGGER.error("Exception occurred while creating JAXBInstance of FindPersonBySSNResponse");
		}

		Unmarshaller unmarshaller = null;
		JAXBElement<FindPersonByPtcpntIdResponse> responseElement = null;
		try {
			if (jc != null) {
				unmarshaller = jc.createUnmarshaller();
				responseElement = (JAXBElement<FindPersonByPtcpntIdResponse>) unmarshaller.unmarshal(getXMLStreamReader(personWSResponseFile),
						FindPersonByPtcpntIdResponse.class);
			}
		} catch (JAXBException e) {
			LOGGER.error("Exception occurred while unmarshalling FindPersonBySSNResponse");
		}

		return responseElement;	
	}
}

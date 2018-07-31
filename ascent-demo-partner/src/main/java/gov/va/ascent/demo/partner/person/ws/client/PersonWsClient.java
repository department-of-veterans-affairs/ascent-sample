package gov.va.ascent.demo.partner.person.ws.client;

import javax.xml.bind.JAXBElement;

import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonBySSN;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonBySSNResponse;

/**
 * The interface for the PersonWsClient Web Service Client.
 *
 */
public interface PersonWsClient { // NOSONAR constants will remain here

	/**
	 * Spring profile for personservicewsclient remote client implementation.
	 */
	String PROFILE_PERSONWSCLIENT_REMOTE_CLIENT_IMPL = "personservicewsclient_remote_client_impl";

	/**
	 * Spring profile for personservicewsclient remote client simulator.
	 */
	String PROFILE_PERSONWSCLIENT_REMOTE_CLIENT_SIM = "personservicewsclient_remote_client_sim";

	/**
	 * @param findPersonBySSNRequest The Person Web Service request entity
	 * @return FindPersonBySSNResponse The Person Web Service response entity
	 */
	JAXBElement<FindPersonBySSNResponse> getPersonInfo(JAXBElement<FindPersonBySSN> findPersonBySSNRequest);

	/**
	 * @param findPersonByPtcpntIdRequest The Person Web Service request entity
	 * @return findPersonByPtcpntIdResponse The Person Web Service response entity
	 */
	JAXBElement<FindPersonByPtcpntIdResponse> getPersonInfoByPtcpntId(JAXBElement<FindPersonByPtcpntId> findPersonByPtcpntIdRequest);

}

package gov.va.ascent.demo.partner.person.ws.client;

import static org.junit.Assert.assertNotNull;

import javax.xml.bind.JAXBElement;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.va.ascent.demo.partner.mock.framework.PartnerMockFrameworkTestConfig;
import gov.va.ascent.demo.partner.person.ws.client.transfer.PersonInfoResponse;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonBySSN;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonBySSNResponse;
import gov.va.ascent.demo.partner.person.ws.transfer.ObjectFactory;
import gov.va.ascent.demo.partner.person.ws.transfer.PersonDTO;
import gov.va.ascent.framework.config.AscentCommonSpringProfiles;
import gov.va.ascent.framework.messages.MessageSeverity;

/**
 * Unit test of PersonWsClientImpl.
 */
// ignored for now as its integration test and requires SOAP UI to be running
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({ AscentCommonSpringProfiles.PROFILE_ENV_LOCAL_INT, AscentCommonSpringProfiles.PROFILE_REMOTE_CLIENT_IMPLS })
@ContextConfiguration(inheritLocations = false, classes = { PersonWsClientConfig.class, PartnerMockFrameworkTestConfig.class })
public class PersonWsClientImpl_UnitTest{
	
	//private Validator validator;

	/** The address validate ws client. */
	@Autowired
	PersonWsClient personWsClient;

	/** The Constant PERSON_OBJECT_FACTORY. */
	protected static final ObjectFactory PERSON_OBJECT_FACTORY = new ObjectFactory();

	private static final String TEST_SSN = "796079018";
	
	@Before
	public void before() {
		assertNotNull(personWsClient);
		//ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        //this.validator = vf.getValidator();
	}
	
	/**
	 * Test address validate.
	 */
	@Test
	public void testGetPersonInfo() {

		final FindPersonBySSN findPersonBySSN = new FindPersonBySSN();
		findPersonBySSN.setSsn(TEST_SSN);
		final JAXBElement<FindPersonBySSN> findPersonBySSNRequestElement =
				PERSON_OBJECT_FACTORY.createFindPersonBySSN(findPersonBySSN);
		final JAXBElement<FindPersonBySSNResponse> findPersonBySSNResponseElement =
				personWsClient.getPersonInfo(findPersonBySSNRequestElement);

		// Check the response for valid data.
		Assert.assertNotNull("Invalid Person Web Service response element. Response must not be null.", findPersonBySSNResponseElement);

		final FindPersonBySSNResponse findPersonBySSNResponse = findPersonBySSNResponseElement.getValue();
		Assert.assertNotNull("Invalid Person Web Service response element. Response value must not be null.", findPersonBySSNResponse);
		final PersonDTO personDto = findPersonBySSNResponse.getPersonDTO();
		final PersonInfoResponse personInfoResponse = new PersonInfoResponse();
		Assert.assertTrue("person DTO should not be null", personDto != null);
		Assert.assertNotNull("No data returned from Person Web Service for SSN " + findPersonBySSN.getSsn(), personDto);
		personInfoResponse.addMessage(MessageSeverity.ERROR, "NOPERSONFORSSN", "No person found for SSN " + TEST_SSN);
	}
	
	/**
	 * Test address validate.
	 */
	@Test
	public void testGetPersonInfoByPtcpntId() {
		
		final FindPersonByPtcpntId findPersonByPtcpntId = new FindPersonByPtcpntId();
		findPersonByPtcpntId.setPtcpntId(new Long(00000));
		
		final JAXBElement<FindPersonByPtcpntId> findPersonByPtcpntIdRequestElement=
				PERSON_OBJECT_FACTORY.createFindPersonByPtcpntId(findPersonByPtcpntId);

		final JAXBElement<FindPersonByPtcpntIdResponse> findPersonByPtcpntIdResponseElement =
				personWsClient.getPersonInfoByPtcpntId(findPersonByPtcpntIdRequestElement);
		
		// Check the response for valid data.
		Assert.assertNotNull("Invalid Person Web Service response element. Response must not be null.", findPersonByPtcpntIdResponseElement);

		final FindPersonByPtcpntIdResponse findPersonPtcpntIdResponse = findPersonByPtcpntIdResponseElement.getValue();
		Assert.assertNotNull("Invalid Person Web Service response element. Response value must not be null.", findPersonPtcpntIdResponse);
		final PersonDTO personDto = findPersonPtcpntIdResponse.getPersonDTO();
		Assert.assertTrue("person DTO should not be null", personDto != null);
		}
	
}
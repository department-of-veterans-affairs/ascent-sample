/*
 * 
 */
package gov.va.ascent.demo.partner.person.ws.client;

import javax.xml.bind.JAXBElement;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.va.ascent.demo.partner.mock.framework.PartnerMockFrameworkTestConfig;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonByPtcpntId;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonByPtcpntIdResponse;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonBySSN;
import gov.va.ascent.demo.partner.person.ws.transfer.FindPersonBySSNResponse;
import gov.va.ascent.demo.partner.person.ws.transfer.ObjectFactory;
import gov.va.ascent.framework.config.AscentCommonSpringProfiles;
import junit.framework.TestCase;

/**
 * The Class PersonWsClientSimulator_UnitTest.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({ AscentCommonSpringProfiles.PROFILE_ENV_LOCAL_INT, AscentCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS })
@ContextConfiguration(inheritLocations = false, classes = { PersonWsClientConfig.class, PartnerMockFrameworkTestConfig.class })
public class PersonWsClientSimulator_UnitTest extends TestCase {

	/** The Person WS client. */
	@Autowired
	PersonWsClient personWsClient;

	/** The Constant PERSON_OBJECT_FACTORY. */
	protected static final ObjectFactory PERSON_OBJECT_FACTORY = new ObjectFactory();

	private static final String TEST_SSN = "796079018";

	@Before
	public void before() throws Exception {
		assertNotNull(personWsClient);
	}

	/**
	 * Test get icn.
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
		Assert.assertNotNull("Invalid Person Web Service response element. Response must not be null.",
				findPersonBySSNResponseElement);
		Assert.assertNotNull("Invalid Person Web Service response element. Person object must not be null.",
				findPersonBySSNResponseElement.getValue().getPersonDTO());
	}

	/**
	 * Test get icn.
	 */
	@Test
	public void testGetPersonInfoByPtcpntId() {

		final FindPersonByPtcpntId findPersonByPtcpntId = new FindPersonByPtcpntId();
		findPersonByPtcpntId.setPtcpntId(new Long(00000));

		final JAXBElement<FindPersonByPtcpntId> findPersonByPtcpntIdRequestElement =
				PERSON_OBJECT_FACTORY.createFindPersonByPtcpntId(findPersonByPtcpntId);

		final JAXBElement<FindPersonByPtcpntIdResponse> findPersonByPtcpntIdResponseElement =
				personWsClient.getPersonInfoByPtcpntId(findPersonByPtcpntIdRequestElement);

		// Check the response for valid data.
		Assert.assertNotNull("Invalid Person Web Service response element. Response must not be null.",
				findPersonByPtcpntIdResponseElement);
		Assert.assertNotNull("Invalid Person Web Service response element. Person object must not be null.",
				findPersonByPtcpntIdResponseElement.getValue().getPersonDTO());
	}

}

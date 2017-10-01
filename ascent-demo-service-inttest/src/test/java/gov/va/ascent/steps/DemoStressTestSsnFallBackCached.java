package gov.va.ascent.steps;

import java.util.Map;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gov.va.ascent.util.BaseStepDef;

public class DemoStressTestSsnFallBackCached extends BaseStepDef {

	@Before({ "@stresstestssnfallbackcached" })
	public void setUpREST() {
		initREST();
	}

	// Steps for SSN Fall Back Cached//
	@Given("^I pass the header information for ssn stresstest$")
	public void passHeaderInformationForSSNFallBackCached(Map<String, String> tblHeader) throws Throwable {
		passHeaderInformation(tblHeader);
	}

	@When("^client request POST \"([^\"]*)\" with json data \"([^\"]*)\" with loadsize (\\d+)$")
	public void clientRequestPOSTWithJsondataSSNFallBackCached(String strURL, String requestFile, int loadsize)
			throws Throwable {
		resUtil.setUpRequest(requestFile, headerMap);
		for (int i = 0; i < loadsize; i++) {
			strResponse = resUtil.POSTResponse(strURL);
			ValidateStatusCode(200);
			Thread.sleep(1000);
		}
		// log.info("Actual Response=" + strResponse);
	}

	@Then("^view the result for load testing$")
	public void viewthereport() throws Throwable {

	}

	@After({ "@ssnfallbackcached, @ssnfallbacknotcached" })
	public void cleanUp(Scenario scenario) {
		postProcess(scenario);

	}
}

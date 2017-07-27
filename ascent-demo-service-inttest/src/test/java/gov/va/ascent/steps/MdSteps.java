package gov.va.ascent.steps;

import java.util.Map;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gov.va.ascent.util.BaseStepDef;

public class MdSteps extends BaseStepDef {
	@Before({ "@md" })
	public void setUpREST() {
		initREST();
	}

	@Given("^I pass the header information for MD5 API$")
	public void passHeaderInformationForMd(Map<String, String> tblHeader) throws Throwable {
		passHeaderInformation(tblHeader);
	}

	@When("^I invoke an API \"([^\"]*)\" using MD$")
	public void makearestcalltoMDAPIserviceusingGET(String strURL) throws Throwable {
		invokeAPIUsingGet(strURL, "md5.baseURL");
	}

	@Then("^the service respose for MD API status code should be (\\d+)$")
	public void serviceresposestatuscodemustbe(int intStatusCode) throws Throwable {
		ValidateStatusCode(intStatusCode);
	}

	@And("^result should be same as valid Transactions response for MD \"(.*?)\"$")
	public void resultshouldbesameasvalidTransactionsresponse(String strResFile) throws Throwable {
		checkResponseContainsValue(strResFile);
	}

	@After({ "@md" })
	public void cleanUp(Scenario scenario) {
		postProcess(scenario);
	}
}

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

public class EchoJsonSteps extends BaseStepDef {

	@Before({ "@echojson" })
	public void setUpREST() {
		initREST();
	}

	@Given("^I pass the header information for echo API$")
	public void passHeaderInformationForEchoAPI(Map<String, String> tblHeader) throws Throwable {
		passHeaderInformation(tblHeader);
	}

	@When("^I invoke echo API \"([^\"]*)\" using GET method$")
	public void makerestcalltoEchoAPIserviceusingGET(String strURL) throws Throwable {
		invokeAPIUsingGet(strURL, "echojson.baseURL");
	}

	@Then("^the echo service respose for API status code should be (\\d+)$")
	public void serviceresposestatuscodemustbe(int intStatusCode) throws Throwable {
		ValidateStatusCode(intStatusCode);
	}

	@And("^echo service result should be same as valid Transactions response \"(.*?)\"$")
	public void resultshouldbesameasvalidTransactionsresponse(String strResFile) throws Throwable {
		checkResponseContainsValue(strResFile);
	}

	@After({ "@echojson" })
	public void cleanUp(Scenario scenario) {
		postProcess(scenario);
	}
}

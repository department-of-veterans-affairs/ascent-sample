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
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class SearchSSNSteps  extends BaseStepDef {

	@Before({ "@searchssn" })
	public void setUpREST() {
		initREST();
	}
	@Given("^I pass the header information for search ssn$")
	public void passHeaderInformationForVeteran(
			Map<String, String> tblHeader) throws Throwable {
		passHeaderInformation(tblHeader);
	}

	@When("^request POST \"([^\"]*)\" with json data \"([^\"]*)\"$")
	public void clientrequestPOSTwithjsondata(
			String strURL, String requestFile) throws Throwable {
		resUtil.setUpRequest(requestFile, headerMap);
		strResponse = resUtil.POSTResponse(strURL);
		log.info("Actual Response=" + strResponse);
	}
	 
	
	@Then("^the response code must be (\\d+)$")
	public void serviceresposestatuscodemustbe(int intStatusCode)
			throws Throwable {
		ValidateStatusCode(intStatusCode);
	}


	@And("^the search SSN result should be same as valid response \"(.*?)\"$")
	public void resultshouldbesameasvalidTransactionsresponse(
			String strResFile) throws Throwable {
		checkResponseContainsValue(strResFile);
	}

	@After({ "@searchssn" })
	public void cleanUp(Scenario scenario) {
		postProcess(scenario);
	}
	
}

package gov.va.ascent.steps;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gov.va.ascent.util.BaseStepDef;
import gov.va.ascent.util.RESTUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SsnFallBackCachedSteps  extends BaseStepDef {

	@Before({ "@ssnfallbackcached" })
	public void setUpREST() {
		initREST();
	}
	
	@Given("^I pass the header information for ssn$")
	public void passHeaderInformationForEchoAPI(
			Map<String, String> tblHeader) throws Throwable {
		passHeaderInformation(tblHeader);
	}
	
	@When("^client request POST \"([^\"]*)\" with json data \"([^\"]*)\"$")
	public void client_request_POST_with_json_data(
			String strURL, String requestFile) throws Throwable {
		resUtil.setUpRequest(requestFile, headerMap);
		strResponse = resUtil.POSTResponse(strURL);
		log.info("Actual Response=" + strResponse);
	}
	 
		@Then("^the response code should be (\\d+)$")
		public void serviceresposestatuscodemustbe(int intStatusCode)
				throws Throwable {
			ValidateStatusCode(intStatusCode);
		}

		@And("^the SSNcached result should be same as valid transaction response \"(.*?)\"$")
		public void resultshouldbesameasvalidTransactionsresponse(
				String strResFile) throws Throwable {
			checkResponseContainsValue(strResFile);
		}
	
		@After({"@ssnfallbackcached" })
		public void cleanUp(Scenario scenario) {
			postProcess(scenario);
		
}
}


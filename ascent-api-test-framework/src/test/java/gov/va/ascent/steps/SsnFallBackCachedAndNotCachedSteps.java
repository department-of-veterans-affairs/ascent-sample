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
public class SsnFallBackCachedAndNotCachedSteps extends BaseStepDef {

	@Before({ "@ssnfallbackcached, @ssnfallbacknotcached" })
	public void setUpREST() {
		initREST();
	}

	              // Steps for SSN Fall Back Cached//
	@Given("^I pass the header information for ssn$")
	public void passHeaderInformationForSSNFallBackCached(Map<String, String> tblHeader) throws Throwable {
		passHeaderInformation(tblHeader);
	}

	@When("^client request POST \"([^\"]*)\" with json data \"([^\"]*)\"$")
	public void clientRequestPOSTWithJsondataSSNFallBackCached(String strURL, String requestFile) throws Throwable {
		resUtil.setUpRequest(requestFile, headerMap);
		strResponse = resUtil.POSTResponse(strURL);
		log.info("Actual Response=" + strResponse);
	}

	@Then("^the response code should be (\\d+)$")
	public void FallBackCachedserviceresposestatuscodemustbe(int intStatusCode) throws Throwable {
		ValidateStatusCode(intStatusCode);
	}

	@And("^the SSNcached result should be same as valid transaction response \"(.*?)\"$")
	public void FallBackCachedresultshouldbesameasvalidTransactionsresponse(String strResFile) throws Throwable {
		checkResponseContainsValue(strResFile);
	}
	// Steps for SSN Fall Back NotCached//

	@Given("^I pass the header information for ssn not cached$")
	public void passHeaderInformationForSSNNotCached(Map<String, String> tblHeader) throws Throwable {
		passHeaderInformation(tblHeader);
	}

	@When("^client request POST url \"([^\"]*)\" with data \"([^\"]*)\"$")
	public void ClientRequestPOSTWithJsondataSSNFallBackNotCached(String strURL, String requestFile) throws Throwable {
		resUtil.setUpRequest(requestFile, headerMap);
		strResponse = resUtil.POSTResponse(strURL);
		log.info("Actual Response=" + strResponse);
	}

	@Then("^the response code be (\\d+)$")
	public void SSNFallBackNotCachedServiceResposeStatusCodeMustBe(int intStatusCode) throws Throwable {
		ValidateStatusCode(intStatusCode);
	}

	@And("^the SSNnotcached result should be same as valid transaction response \"(.*?)\"$")
	public void SSNFallBackNotCachedResultShouldBeSameAsValidTransactionsResponse(String strResFile) throws Throwable {
		checkResponseContainsValue(strResFile);
	}

	@After({ "@ssnfallbackcached, @ssnfallbacknotcached" })
	public void cleanUp(Scenario scenario) {
		postProcess(scenario);

	}
}

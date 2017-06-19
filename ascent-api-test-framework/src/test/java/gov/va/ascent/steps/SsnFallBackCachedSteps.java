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
import gov.va.ascent.util.RESTUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SsnFallBackCachedSteps {
	protected RESTUtil resUtil = null;
	protected HashMap<String, String> headerMap = null;
	String strResponse = null;
	String strRequest =null;

	@Before({ "@ssnfallbackcached" })
	public void setUpREST() {
		try {
			resUtil = new RESTUtil();
			headerMap = new HashMap();
		} catch (Exception ex) {
			log.info("Failed:Setup of REST util failed");
			ex.printStackTrace();
		}
	}
	
	@Given("^I pass the header information for ssn$")
	public void i_pass_header_information_for_ssn(
			Map<String, String> tblHeader) throws Throwable {

		headerMap = new HashMap<String, String>(tblHeader);
		System.out.println(headerMap);
	}

	
	@When("^client request POST \"([^\"]*)\" with json data \"([^\"]*)\"$")
	public void client_request_POST_with_json_data(
			String strURL, String requestFile) throws Throwable {
		resUtil.setUpRequest(requestFile, headerMap);
		strResponse = resUtil.POSTResponse(strURL);
		log.info("Actual Response=" + strResponse);
	}
		 
		@Then("^the response code should be (\\d+)$")
		public void the_response_code_should_be(int intStatusCode)
				throws Throwable {
		resUtil.ValidateStatusCode(intStatusCode);
		}
		

		@And("^the SSNcached result should be same as valid transaction response \"(.*?)\"$")
		public void SSNcached_result_should_be_same_as_valid_Transactions_response
		 (String strResFile) throws Throwable {

			String strExpectedResponse = resUtil.readExpectedResponse(strResFile);
			assertThat(strResponse).contains(strExpectedResponse);
			log.info("Actual Response matched the expected response");

		}
		@After({ "@ssnfallbackcached" })
		public void cleanUp(Scenario scenario) {
			String strResponseFile = null;
			try {
				strResponseFile = "target/TestResults/Response/"
						+ scenario.getName() + ".Response";
				FileUtils.writeStringToFile(new File(strResponseFile), strResponse);
			} catch (Exception ex) {
			log.info("Failed:Unable to write response to a file");
				ex.printStackTrace();
			}
			scenario.write(scenario.getStatus());
	}
}
package gov.va.ascent.steps;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;

import com.jayway.restassured.path.json.JsonPath;

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
public class OpenCompletedAndCancellationClaimSteps extends BaseStepDef {

	private Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T.*"); 

	@Before({ "@openclaim" })
	public void setUpREST() {
		initREST();
	}

	@Given("^the veteran has a open claim$")
	public void passHeaderInformationForVeteran(
			Map<String, String> tblHeader) throws Throwable {
		passHeaderInformation(tblHeader);
	}

	@When("the claim service is called \"([^\"]*)\"$")
	public void makerestcalltoClaimserviceusingGET(
			String strURL) throws Throwable {
		
		
		invokeAPIUsingGet(strURL, "claims.baseURL");
	}

	@Then("^the claim has a recieved date and does not have closed date and complete is false")
	public void serviceresposestatusopenclaim()
			throws Throwable {
		Matcher matcher;
		List<Map<String, Object>> claims = JsonPath.with(strResponse).get("claims.findAll { claim -> claim.claimStatus == 'PEND' }");
		System.out.println(claims.size());
		for(Map<String, Object> claim : claims) {
			String receivedDate = claim.get("receivedDate").toString();
			Object closedDate = claim.get("closedDate");
			Boolean complete = (Boolean) claim.get("complete");
			Assert.assertTrue(receivedDate != null && 
					pattern.matcher(receivedDate).matches() &&
					closedDate == null  &&
					!complete
					);
		}
	}
	
	@After({ "@openclaim" })
	public void cleanUp(Scenario scenario) {
		postProcess(scenario);
	}
}

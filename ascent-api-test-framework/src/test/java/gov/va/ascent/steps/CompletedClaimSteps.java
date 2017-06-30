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
public class CompletedClaimSteps extends BaseStepDef {

	private Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T.*"); 

	@Before({ "@completedclaim" })
	public void setUpREST() {
		initREST();
	}

	@Given("^the veteran has a closed claim$")
	public void passHeaderInformationForVeteran(
			Map<String, String> tblHeader) throws Throwable {
		passHeaderInformation(tblHeader);
	}

	@When("the closed claim service is called \"([^\"]*)\"$")
	public void makerestcalltoClaimserviceusingGET(
			String strURL) throws Throwable {
		invokeAPIUsingGet(strURL, "claims.baseURL");
	}

	@Then("^the claim status is CLR and claims has a recived date and claims has a closed update and completed date and the claim phase status is complete and true")
	  public void serviceresposestatuscompletedclaim()
	      throws Throwable {
	    Matcher matcher;
	    List<Map<String, Object>> claims = JsonPath.with(strResponse).get("claims.findAll { claim -> claim.claimStatus == 'CLR' }");
	    System.out.println(claims.size());
	    for(Map<String, Object> claim : claims) {
	      String receivedDate = claim.get("receivedDate").toString();
	      String closedDate = claim.get("closedDate").toString();
	      String updatedDate =claim.get("updatedDate").toString();
	      String completeDate = claim.get("completeDate").toString();
	      String phaseStatus = claim.get("phaseStatus").toString();
	      Boolean complete = (Boolean) claim.get("complete");
	      
	      Assert.assertTrue(receivedDate != null && 
	          pattern.matcher(receivedDate).matches() &&
	          closedDate != null  && pattern.matcher( closedDate).matches() &&
	          updatedDate != null  && pattern.matcher( updatedDate).matches() &&
	          completeDate != null  && pattern.matcher( completeDate).matches() &&
	        		phaseStatus.equals("Complete")
	          && complete
	          );
	    }

	  }
	
	@After({ "@completedclaim" })
	public void cleanUp(Scenario scenario) {
		postProcess(scenario);
	}
}

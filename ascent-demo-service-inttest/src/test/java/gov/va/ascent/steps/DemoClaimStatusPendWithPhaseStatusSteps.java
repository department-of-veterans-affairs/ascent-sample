package gov.va.ascent.steps;

import java.util.List;
import java.util.Map;
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

public class DemoClaimStatusPendWithPhaseStatusSteps extends BaseStepDef {

	private Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}T.*");

	@Before({ "@gatheringofevidence, @claimreceived" })
	public void setUpREST() {
		initREST();
	}

	// Steps for Gathering Of Evidence//
	@Given("^the veteran has pending fivetwentysix claim$")
	public void passHeaderInformationForVeteran(Map<String, String> tblHeader) throws Throwable {
		passHeaderInformation(tblHeader);
	}

	@When("the pend gathering of claim service is called \"([^\"]*)\"$")
	public void makerestcalltoClaimserviceusingGET(String strURL) throws Throwable {
		invokeAPIUsingGet(strURL, "claims.baseURL");
	}

	@Then("^the claim has a claimstaus of PEND and claim type of compensation$")
	public void pendclaimtypeofcompensation() throws Throwable {
		List<Map<String, Object>> claims = JsonPath.with(strResponse)
				.get("claims.findAll { claim -> claim.claimStatus == 'PEND' }");
		System.out.println(claims.size());
		int claimTypeCount = 0;
		for (Map<String, Object> claim : claims) {
			String claimType = claim.get("claimType").toString();
			if (claimType.equals("Compensation")) {
				claimTypeCount++;
			}
		}
		Assert.assertTrue(claimTypeCount > 0);
	}

	@And("^the claim phasestaus is Gathering of Evidence$")
	public void phasestatusgatheringofevidence() throws Throwable {
		List<Map<String, Object>> claims = JsonPath.with(strResponse)
				.get("claims.findAll { claim -> claim.claimStatus == 'PEND' }");
		System.out.println(claims.size());
		int phaseStatusCount = 0;
		for (Map<String, Object> claim : claims) {
			String claimType = claim.get("claimType").toString();
			String phaseStatus = claim.get("phaseStatus").toString();
			if (claimType.equals("Compensation") && phaseStatus.equals("Gathering of Evidence")) {
				phaseStatusCount++;
			}
		}
		Assert.assertTrue(phaseStatusCount > 0);
	}

	@And("^the claim has a valid recived date$")
	public void reciveddate() throws Throwable {
		List<Map<String, Object>> claims = JsonPath.with(strResponse)
				.get("claims.findAll { claim -> claim.claimStatus == 'PEND' }");
		System.out.println(claims.size());
		for (Map<String, Object> claim : claims) {
			String receivedDate = claim.get("receivedDate").toString();
			Assert.assertTrue(receivedDate != null && pattern.matcher(receivedDate).matches());
		}
	}

	@And("^the claim phasestaus is Gathering of Evidence and claim status is false$")
	public void phasestatusgatheringofevidenceclaimcomplete() throws Throwable {
		List<Map<String, Object>> claims = JsonPath.with(strResponse)
				.get("claims.findAll { claim -> claim.claimStatus == 'PEND' }");
		System.out.println(claims.size());
		for (Map<String, Object> claim : claims) {
			String claimType = claim.get("claimType").toString();
			String phaseStatus = claim.get("phaseStatus").toString();
			Boolean complete = (Boolean) claim.get("complete");
			if (claimType.equals("Compensation") && phaseStatus.equals("Gathering of Evidence")) {
				Assert.assertTrue(!complete);
			}

		}
	}

	// Steps for Claims Received//
	@Given("^the veteran has pending fivetwentysix claim for claims recieved$")
	public void passHeaderInformationForVeteranClaimsReceived(Map<String, String> tblHeader) throws Throwable {
		passHeaderInformation(tblHeader);
	}

	@When("the claim recieved service is called \"([^\"]*)\"$")
	public void makerestcalltoReceivedClaimserviceusingGET(String strURL) throws Throwable {
		invokeAPIUsingGet(strURL, "claims.baseURL");
	}

	@Then("^the claim recieved has a claimstaus of PEND and claim type of compensation$")
	public void pendReceivedclaimtypeofcompensation() throws Throwable {
		List<Map<String, Object>> claims = JsonPath.with(strResponse)
				.get("claims.findAll { claim -> claim.claimStatus == 'PEND' }");
		System.out.println(claims.size());
		int claimTypeCount = 0;
		for (Map<String, Object> claim : claims) {
			String claimType = claim.get("claimType").toString();
			if (claimType.equals("Compensation")) {
				claimTypeCount++;
			}
		}
		Assert.assertTrue(claimTypeCount > 0);
	}

	@And("^the claim phasestaus is claim recived$")
	public void phasestatusclaimsrecived() throws Throwable {
		List<Map<String, Object>> claims = JsonPath.with(strResponse)
				.get("claims.findAll { claim -> claim.claimStatus == 'PEND' }");
		System.out.println(claims.size());
		int phaseStatusCount = 0;
		for (Map<String, Object> claim : claims) {
			String claimType = claim.get("claimType").toString();
			String phaseStatus = claim.get("phaseStatus").toString();
			if (claimType.equals("Compensation") && phaseStatus.equals("Claim Received")) {
				phaseStatusCount++;
			}
		}
		Assert.assertTrue(phaseStatusCount > 0);
	}

	@And("^the claim recived has a valid recived date$")
	public void claimreciveddate() throws Throwable {
		List<Map<String, Object>> claims = JsonPath.with(strResponse)
				.get("claims.findAll { claim -> claim.claimStatus == 'PEND' }");
		System.out.println(claims.size());
		for (Map<String, Object> claim : claims) {
			String receivedDate = claim.get("receivedDate").toString();
			Assert.assertTrue(receivedDate != null && pattern.matcher(receivedDate).matches());
		}
	}

	@And("^the claim complete is false$")
	public void phasestatusclaimrecivedclaimcomplete() throws Throwable {
		List<Map<String, Object>> claims = JsonPath.with(strResponse)
				.get("claims.findAll { claim -> claim.claimStatus == 'PEND' }");
		System.out.println(claims.size());
		for (Map<String, Object> claim : claims) {
			String claimType = claim.get("claimType").toString();
			String phaseStatus = claim.get("phaseStatus").toString();
			Boolean complete = (Boolean) claim.get("complete");
			if (claimType.equals("Compensation") && phaseStatus.equals("Claim Received")) {
				Assert.assertTrue(!complete);
			}

		}
	}

	@After({ "@gatheringofevidence,  @claimreceived" })
	public void cleanUp(Scenario scenario) {
		postProcess(scenario);
	}
}

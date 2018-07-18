package gov.va.ascent.demo.service.steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import cucumber.api.java.en.When;
import gov.va.ascent.test.framework.restassured.BaseStepDefHandler;

public class DemoSsnFallBackCachedAndNotCachedSteps {

	final static Logger LOGGER = LoggerFactory.getLogger(DemoSsnFallBackCachedAndNotCachedSteps.class);
	private BaseStepDefHandler handler = null;

	public DemoSsnFallBackCachedAndNotCachedSteps(BaseStepDefHandler handler) {
		this.handler = handler;
	}

	@Before({})
	public void setUpREST() {
		handler.initREST();
	}

	// Steps for SSN Fall Back Cached//

	@When("^client request SSNcached \"([^\"]*)\" with SSNcached data \"([^\"]*)\"$")
	public void clientRequestPOSTWithJsondataSSNFallBackCached(String strURL, String requestFile) throws Throwable {
		String baseUrl = handler.getRestConfig().getProperty("baseURL", true);
		handler.getRestUtil().setUpRequest(requestFile, handler.getHeaderMap());
		handler.invokeAPIUsingPost(baseUrl + strURL);
	}

	// Steps for SSN Fall Back NotCached//

	@When("^client request SSNnotcached \"([^\"]*)\" with SSNnotcached data \"([^\"]*)\"$")
	public void ClientRequestPOSTWithJsondataSSNFallBackNotCached(String strURL, String requestFile) throws Throwable {
		String baseUrl = handler.getRestConfig().getProperty("baseURL", true);
		handler.invokeAPIUsingPost(baseUrl + strURL);
	}

	@After({})
	public void cleanUp(Scenario scenario) {
		handler.postProcess(scenario);

	}
}

package gov.va.ascent.demo.service.steps;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import cucumber.api.java.en.When;

import gov.va.ascent.test.framework.restassured.BaseStepDefHandler;


public class DemoSearchSSNSteps {

	final static Logger LOGGER = LoggerFactory.getLogger(DemoSearchSSNSteps.class);
	private BaseStepDefHandler handler = null;

	public DemoSearchSSNSteps(BaseStepDefHandler handler) {
		this.handler = handler;
	}

	@Before({  })
	public void setUpREST() {
		handler.initREST();
	}

	@When("^request search ssn \"([^\"]*)\" with ssn data \"([^\"]*)\"$")
	public void clientrequestPOSTwithjsondata(String strURL, String requestFile) throws Throwable {
        String baseUrl = handler.getRestConfig().getProperty("baseURL", true);
		handler.invokeAPIUsingPost(baseUrl + strURL);
	}

	@After({  })
	public void cleanUp(Scenario scenario) {
		handler.postProcess(scenario);
	}

}

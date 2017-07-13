package gov.va.ascent.util;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

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
public class BaseStepDef {
	protected RESTUtil resUtil = null;
	protected HashMap<String, String> headerMap = null;
	protected String strResponse = null;
	private RESTConfig restConfig = null;
	
	public void initREST() {
		try {
			resUtil = new RESTUtil();
			restConfig = new RESTConfig();
			String environment = System.getProperty("ascent-env");
			String url = "";
			if (StringUtils.isNotBlank(environment)) {
				url = "config/restconfig-"+environment+".properties";
			} else {
				url = "config/restconfig.properties";
			}
			URL urlConfigFile = RESTUtil.class.getClassLoader().getResource(
					url);

			File strFile = new File(urlConfigFile.toURI());
			restConfig.openPropertyFile(strFile);
		} catch (Exception ex) {
			log.info("Failed:Setup of REST util failed");
			ex.printStackTrace();
		}
	}
	
	public void passHeaderInformation(
			Map<String, String> tblHeader) throws Throwable {

		headerMap = new HashMap<String, String>(tblHeader);
		System.out.println(headerMap);
	}

	public void invokeAPIUsingGet(
			String strURL) throws Throwable {
		resUtil.setUpRequest(headerMap);
		strResponse = resUtil.GETResponse(strURL);
		log.info("Actual Response=" + strResponse);
	}

	public void invokeAPIUsingGet(
			String strURL, String baseUrlProperty) throws Throwable {
		String baseUrl = restConfig.getPropertyName(baseUrlProperty);
		invokeAPIUsingGet(baseUrl + strURL);
		log.info("Actual Response=" + strResponse);
	}
	
	public void invokeAPIUsingPost(
			String strURL) throws Throwable {
		resUtil.setUpRequest(headerMap);
		strResponse = resUtil.POSTResponse(strURL);
		log.info("Actual Response=" + strResponse);
	}

	public void invokeAPIUsingPost(
			String strURL, String baseUrlProperty) throws Throwable {
		String baseUrl = restConfig.getPropertyName(baseUrlProperty);
		invokeAPIUsingPost(baseUrl + strURL);
		log.info("Actual Response=" + strResponse);
	}
	
	public void ValidateStatusCode(int intStatusCode)
			throws Throwable {
		resUtil.ValidateStatusCode(intStatusCode);
	}


	public void checkResponseContainsValue(
			String strResFile) throws Throwable {

		String strExpectedResponse = resUtil.readExpectedResponse(strResFile);
		assertThat(strResponse).contains(strExpectedResponse);
		log.info("Actual Response matched the expected response");

	}

	public void postProcess(Scenario scenario) {
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

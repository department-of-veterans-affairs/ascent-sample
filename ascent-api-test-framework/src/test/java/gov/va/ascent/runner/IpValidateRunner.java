package gov.va.ascent.runner;

import org.testng.annotations.BeforeSuite;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(format = {
		"html:target/TestResults/html/ipvalidate/cucumber-html-report",
		"json:target/TestResults/json/ipvalidate-reports.json" }, features = { "src/test/resources/gov/va/ascent/feature/ipvalidate.feature" }, tags = {"@ipvalidate"}, glue = { "gov.va.ascent.steps" })
public class IpValidateRunner extends AbstractTestNGCucumberTests {
	
	@BeforeSuite(alwaysRun = true)
	public void setUp() throws Exception {
		System.out.println("Before run------------------------");
	}

}

package gov.va.ascent.runner;

import org.testng.annotations.BeforeSuite;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(format = {
		"html:target/TestResults/html/ssnfallbackcached/cucumber-html-report",
		"json:target/TestResults/json/ssnfallbackcached-reports.json" }, features = { "src/test/resources/gov/va/ascent/feature/ssnfallbackcached.feature" }, tags = {"@ssnfallbackcached"}, glue = { "gov.va.ascent.steps" })
public class SsnFallBackCachedRunner extends AbstractTestNGCucumberTests {
	
	@BeforeSuite(alwaysRun = true)
	public void setUp() throws Exception {
		System.out.println("Before run------------------------");
	}

}

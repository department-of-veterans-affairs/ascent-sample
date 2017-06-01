package gov.va.ascent.runner;

import org.testng.annotations.BeforeSuite;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(format = {
		"html:target/TestResults/html/echojson/cucumber-html-report",
		"json:target/TestResults/json/echojson-reports.json" }, features = { "src/test/resources/gov/va/ascent/feature/echojson.feature" }, tags = {"@echojson"}, glue = { "gov.va.ascent.steps" })
public class EchoJsonRunner extends AbstractTestNGCucumberTests {
	
	@BeforeSuite(alwaysRun = true)
	public void setUp() throws Exception {
		System.out.println("Before run------------------------");
	}

}

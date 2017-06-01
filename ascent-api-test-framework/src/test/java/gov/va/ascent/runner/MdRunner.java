package gov.va.ascent.runner;

import org.testng.annotations.BeforeSuite;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

@CucumberOptions(format = {
		"html:target/TestResults/html/md/cucumber-html-report",
		"json:target/TestResults/json/md-reports.json" }, features = { "src/test/resources/gov/va/ascent/feature/md.feature" }, tags = {"@md"}, glue = { "gov.va.ascent.steps" })
public class MdRunner extends AbstractTestNGCucumberTests {
	
	@BeforeSuite(alwaysRun = true)
	public void setUp() throws Exception {
		System.out.println("Before run------------------------");
	}

}

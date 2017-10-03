
## What is this repository for? ##

This is a suite of projects to demo various patterns required to deploy and run application services on to the new Ascent Platform.  

## Platform Services Overview ##

Platform services are built and deployed to support application services in the distributed enviroment. If you are interested to understand these components in depth, then follow the listed 2 links in this section

* [Ascent GitHub Repository](https://github.com/department-of-veterans-affairs/ascent.git)
* [Ascent Wiki Documentation](https://github.com/department-of-veterans-affairs/ascent/wiki)

## Project Breakdown ##

1. ascent-sample-parentpom This project is the parent pom for ascent-sample. This extends ascent-platform-parent and supplies common Maven configuration specific to the ascent-sample suite of projects.

1. ascent-demo-partner: Partner services for Demo, showing BGS for sample mock data

2. ascent-demo-service-inttest: Contains the integration tests using RestAssured, Cucumber libraries. Includes Test cases against the end points for ascent demo and claims demo. 

3. ascent-demo-service-perftest: Contains the performance JMX tests scripts for Apache JMeter

4. ascent-demo-service: Demo for the core service.  It has REST endpoints and demo's various patterns for producing REST endpoints, swagger for the application, registering the application with Eureka, calling REST endpoints through Zuul, Hystrix Circuit Breaker etc.  

5. ascent-document-service: Second demo service to show FEIGN client service to service call implementation

## How do I get set up? ##

Follow the instructions on the Quick Start Guide Wiki Page [Set Up Guide](https://github.com/department-of-veterans-affairs/ascent-sample/wiki/Ascent-Sample-Quick-Start-Guide)
  
## Service Patterns ##

Read through some of the documentation [Ascent Patterns](https://github.com/department-of-veterans-affairs/ascent-platform/wiki#patterns)

## Contribution guidelines ## 
* If you or your team wants to contribute to this repository, then fork the repository and follow the steps to create a PR for our upstream repo to review and commit the changes
* [Creating a pull request from a fork](https://help.github.com/articles/creating-a-pull-request-from-a-fork/)
	

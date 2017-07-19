# README #

This page documents all the steps that are necessary to get this demo application up and running and possibly contribute towards it.

## What is this repository for? ##

This is a suite of small projects to demo/learn capabilities of the potential next generation architecture.  

## Core Services Overview ##

Platform team will provide all the core services to support application services in the distributed platform.

Ascent Platform [GitHub Repository](https://github.com/department-of-veterans-affairs/ascent.git)

Ascent Platform [Wiki Documentation](https://github.com/department-of-veterans-affairs/ascent/wiki)

## Support Services Overview ##

**ascent-demo-service: Ascent Demo Service**

This is a demo for the core service.  It has REST endpoints and demo's various patterns for producing REST endpoints, swagger for the application, registering the application with Eureka, calling REST endpoints through Zuul, Hystrix Circuit Breaker etc.  This service has the most custom code obviously as it demo's a service tier.

**Service Application Ports**
* Demo Service - 8080

## How do I get set up? ##

* Ensure you have Maven, JDK8, GIT and Optionally Docker (To run ascent sample demo service in the container) installed.  
* Clone the GIT repo.  (Due to 2FA enabled for repo, you will have to set up SSH Key Passphrase)
* From the project root, If Docker is RUNNING, run 'mvn clean install' from the reactor pom to build the project which will create the 
  docker images. If Docker is OFFLINE, run 'mvn clean install -DskipDockerBuild' from the reactor pom to build the project with no docker 
  images. The build should succeed with no test failures.
* The recommended IDE is Spring Tool Suite as it plays most nicely with Spring Boot (obviously).  However you are free to choose.
* Deployment instructions...

  See below for information on the various techniques.  Currently there are 3 documented ways to deploy locally...

  (1) From your IDE you can deploy simply the ascent-demo-service in "stand alone mode" to code only in that app
  
  (2) From your IDE you can deploy an "*integrated environment*" that includes all components
  
  (3) You can leverage docker to automatically stand up the integrated environment for demo purposes
  
## Deployment Details ##

### (1) IDE Deploy only ascent-demo-service ###
* Assuming you are using Spring Tool Suite as suggested
* Ensure you've imported the projects in the IDE
* In the "Boot Dashboard" within Spring Tool Suite, highlight ascent-demo-service project and click the "*(Re)start*" button
* URLs for testing/using this deployment approach
   
  [Demo Service Swagger](http://localhost:8080/swagger-ui.html)
   
  *Note there are other URLs, such as all the actuator URLs.  Listed here are the basic minimum URLs.*
  
### (2) IDE Deploy of "*integrated environment*" ###
* Assuming you are using Spring Tool Suite as suggested
* Ensure you've imported the projects in the IDE

**Configure Projects**
* In the "Boot Dashboard" within Spring Tool Suite, highlight ascent-demo-service project and click the "*open config*" button
* On the "*Spring Boot*" tab key in "*local-int*" as the profile and "*Apply/Close*" the window

**Start Projects**
* For Ascent Workspace "Boot Dashboard" within Spring Tool Suite, ensure that the following projects are STARTED UP
* ascent-discovery
* ascent-config
* ascent-gateway
* In the "Boot Dashboard" within Spring Tool Suite, highlight ascent-demo-service project and click the "*(Re)start*" button
* URLs for testing/using this deployment approach
   
  [Demo Service Swagger (direct)](http://localhost:8080/swagger-ui.html)

  [Demo Service Swagger (thru Gateway)](http://localhost:8762/api/ascent-demo-service/swagger-ui.html)   
   
   
### (3) Local Docker Deployment ###
* Run 'mvn clean install' from the reactor pom to build the project which will create the docker images.

* Start the containers by running the script "**./start-all.sh**"

* URLs for testing/using this deployment approach
   
  [Demo Service Swagger (thru Zuul)](http://localhost:8762/api/ascent-demo-service/swagger-ui.html)
   
  *Note there are other URLs, such as all the actuator URLs.  Listed here are the basic minimum URLs.*
    
  **Testing Scaling**
  
  Docker makes it easier to test scaling of services.  That is, run multiple instances of the services and ensure that each registers itself with Discovery: Eureka, finds it's way into Gateway: Zuul and all of the load balancing, etc.  To do this...
    
    * "*docker-compose scale ascent-demo-service=2*"
    
    * "*docker-compose up --force-recreate*"
  
  * Note go into Discovery:Eureka and see the multiple services registered.  Go into swagger and call the echo operation multiple times and observe multiple responses.*
   
  * When done, recommend running "**./stop-all.sh**" to stop and remove the containers, networks, images and volumes so you don't always run at scale.*
  
## Service Patterns ##

* [Ascent Hystrix Pattern: Circuit Breaker](https://github.com/department-of-veterans-affairs/ascent/wiki/Ascent-Hystrix-Pattern)

## Contribution guidelines ## 
* If you or your team wants to contribute to this repository, then fork the repository and follow the steps to create a PR for our upstream repo to review and commit the changes
* [Creating a pull request from a fork](https://help.github.com/articles/creating-a-pull-request-from-a-fork/)
	
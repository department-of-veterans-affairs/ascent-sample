This document provides the details of **Ascent Demo Service Functional Testing**.

## Functional test for ascent Demo Service  ##
Functional test are created to make sure the end points in demo service are working as expected.

Project uses Java - Maven platform, the REST-Assured jars for core API validations.

## Project Structure ##

src/inttest/gov/va/ascent/features - This is where you will create the cucumber feature files that contain the Feature
and Scenarios for the demo service you are testing.

src/inttest/java/gov/va/ascent/demo/service/steps- The implementation steps related to the feature
and scenarios mentioned in the cucumber file for the API needs to be created in this location. 

src/inttest/java/gov/va/ascent/demo/service/runner - Cucumber runner class that contains all feature file entries that needs to be executed at runtime.
The annotations provided in the cucumber runner class will assist in bridging the features to step definitions.

src/inttest/resources/Request/ci – This folder contains CI request files if the body of your API call is static and needs to be sent as a XML/JSON file.

src/inttest/resources/Request/va – This folder contains VA request files if the body of your API call is static and needs to be sent as a XML/JSON file.

src/inttest/resources/Response – This folder contains response files that you may need to compare output, you can store the Response files in this folder. 
EMPTY for this project.

src/test/resources/users/ci: All the property files for CI users should go under this folder.

src/test/resources/users/va: All the property files for VA users should go under this folder.

src/inttest/resources/logback-test.xml - Logback Console Appender pattern and loggers defined for this project

src/inttest/resources/config/vetservices-ci.properties – CI configuration properties such as URL are specified here.

src/inttest/resources/config/vetservices-va.properties – STAGE configuration properties such as URL are specified here.

## Execution ##
**Command Line:** Use this command(s) to execute the demo service Functional test. 

Default Local: mvn verify -Pinttest -Ddockerfile.skip=true -Dcucumber.options="--tags @CI"

CI: mvn verify -Pinttest -Dtest.env=ci -Ddockerfile.skip=true -Dcucumber.options="--tags @CI"

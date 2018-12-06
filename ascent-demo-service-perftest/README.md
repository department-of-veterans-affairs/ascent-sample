# Ascent Demo Service Performance Test 

Performance testing configurations (jmx files) go in this directory.

This module was created to facilitate the performance testing of ascent demo Service and has no other functionality.

This test will execute requests for the rest end points available in ascent demo service module.

-Demo person service API search based on SSN - /api/ascent-demo-service/demo/v1/person/ssn

Additionally it will also hit the /token end point to generate JWT token for the users.

## Performance Test Configuration

## Ascent sample Perftest is executed with the deafult values in the pipeline.
The test suite can be configure to execute each test a different number of time and with different number of threads.
To override any of the below properties please pass them as -D arguments (ex. -Ddomain=dev.internal.vetservices.gov)

Below is the typical configuration values, But ascent sample is using the default values in JMX file.

|Property|Description|Default Value|Perf Env Test Values|
|-|-|-|-|
|domain| Ascent Demo service Base Url|localhost| |
|port|Ascent Demo Service Port|8762|443 |
|protocol|Ascent Demo Service Protocol|http|https |
|DemoSearchSSNFallBackCached.threadGroup.threads|Number of threads for SSN Fall back cached|5|150|
|DemoSearchSSNFallBackCached.threadGroup.rampUp|Thead ramp up|2|150|
|DemoSearchSSNFallBackCached.threadGroup.loopCount|Number of executions|10|-1|
|DemoSearchSSNFallBackCached.threadGroup.duration|Scheduler Duration in seconds|200|230|
|DemoSearchSSNFallBackCached.threadGroup.startUpDelay|Delay to Start|5|30|
|DemoSearchSSNFallBackNotCached.threadGroup.threads|Number of threads for SSN Fall back not cached|5|150|
|DemoSearchSSNFallBackNotCached.threadGroup.rampUp|Thead ramp up|2|150|
|DemoSearchSSNFallBackNotCached.threadGroup.loopCount|Number of executions|10|-1|
|DemoSearchSSNFallBackNotCached.threadGroup.duration|Scheduler Duration in seconds|200|230|
|DemoSearchSSNFallBackNotCached.threadGroup.startUpDelay|Delay to Start|2|30|
|DemoSearchEmptySSN.threadGroup.threads|Number of threads for Empty SSN|5|150|
|DemoSearchEmptySSN.threadGroup.rampUp|Thead ramp up|2|150|
|DemoSearchEmptySSN.threadGroup.loopCount|Number of executions |10|-1|
|DemoSearchEmptySSN.threadGroup.duration|Scheduler Duration in seconds|200|230|
|DemoSearchEmptySSN.threadGroup.startUpDelay|Delay to Start|2|30|
|DemoSearchSSNInvalid.threadGroup.threads|Number of threads for Invalis SSN|5|150|
|DemoSearchSSNInvalid.threadGroup.rampUp|Thead ramp up|2|150|
|DemoSearchSSNInvalid.threadGroup.loopCount|Number of executions |10|-1|
|DemoSearchSSNInvalid.threadGroup.duration|Scheduler Duration in seconds|200|230|
|DemoSearchSSNInvalid.threadGroup.startUpDelay|Delay to Start|2|30|
|DemoSearchNoSSN.threadGroup.threads|Number of threads for no SSN|5|150|
|DemoSearchNoSSN.threadGroup.rampUp|Thead ramp up|2|150|
|DemoSearchNoSSN.threadGroup.loopCount|Number of executions |10|-1|
|DemoSearchNoSSN.threadGroup.duration|Scheduler Duration in seconds|200|230|
|DemoSearchNoSSN.threadGroup.startUpDelay|Delay to Start|2|30|
|BearerTokenCreate.threadGroup.threads|Number of threads for Bearer Token Create/Generate|5|150|
|BearerTokenCreate.threadGroup.rampUp|Thead ramp up|1|50|
|BearerTokenCreate.threadGroup.loopCount|Number of executions |1|1|

## Performance Test Execution

To execute test locally simply run **mvn clean verify -Pperftest**. If you need to override any of the properties please use -D arguments with the arguments mention above.

Use below sample commands to execute for different environment:

DEV: **mvn clean verify -Pperftest -Dprotocol=https -Ddomain=dev.internal.vetservices.gov -Dport=443  -Djavax.net.ssl.keyStore="path to keystore" -Djavax.net.ssl.keyStorePassword="keystore password"**

Stage: **mvn clean verify -Pperftest -Dprotocol=https -Ddomain=staging.internal.vetservices.gov -Dport=443 -Djavax.net.ssl.keyStore="path to keystore" -Djavax.net.ssl.keyStorePassword="keystore password"**

Note: To execute the test against dev and stagging JKS file has to be stored locally.

Sample Command for executing the test in performance test environment: 

**mvn clean verify -Pperftest -Dprotocol=<> -Ddomain=<> -Dport=<> -DBearerTokenCreate.threadGroup.threads=150 
-Djavax.net.ssl.keyStore="path to keystore" -Djavax.net.ssl.keyStorePassword="keystore password" -DBearerTokenCreate.threadGroup.rampUp=50 -DBearerTokenCreate.threadGroup.loopCount=1 -DDemoSearchSSNFallBackCached.threadGroup.threads=150 -DDemoSearchSSNFallBackCached.threadGroup.rampUp=150 -DDemoSearchSSNFallBackCached.threadGroup.loopCount=-1 -DDemoSearchSSNFallBackCached.threadGroup.duration=230 -DDemoSearchSSNFallBackCached.threadGroup.startUpDelay=30 -DDemoSearchSSNFallBackNotCached.threadGroup.threads=150 -DDemoSearchSSNFallBackNotCached.threadGroup.rampUp=150 -DDemoSearchSSNFallBackNotCached.threadGroup.loopCount=-1 -DDemoSearchSSNFallBackNotCached.threadGroup.duration=230 -DDemoSearchSSNFallBackNotCached.threadGroup.startUpDelay=30 -DDemoSearchEmptySSN.threadGroup.threads=150 -DDemoSearchEmptySSN.threadGroup.rampUp=150 -DDemoSearchEmptySSN.threadGroup.loopCount=-1 -DDemoSearchEmptySSN.threadGroup.duration=230 -DDemoSearchEmptySSN.threadGroup.startUpDelay=30 -DDemoSearchSSNInvalid.threadGroup.threads=150 -DDemoSearchSSNInvalid.threadGroup.rampUp=150 -DDemoSearchSSNInvalid.threadGroup.loopCount=-1 -DDemoSearchSSNInvalid.threadGroup.duration=230 -DDemoSearchSSNInvalid.threadGroup.startUpDelay=30 -DDemoSearchNoSSN.threadGroup.threads=150 -DDemoSearchNoSSN.threadGroup.rampUp=150 -DDemoSearchNoSSN.threadGroup.loopCount=-1 -DDemoSearchNoSSN.threadGroup.duration=230 -DDemoSearchNoSSN.threadGroup.startUpDelay=30**

For developing the performance test, use the GUI mode. For the actual test execution, use of NON-GUI mode is recommended.

### Ascent Demo Service performance test

In ascent-demo-service-perftest we have JMX files for load performance test, stress performance test and spike performance test. Please refer to the below URL for detailed test plan.

[Ascent Demo Service Performance TestPlan](https://github.com/department-of-veterans-affairs/ascent-sample/wiki/QA-:-Performance-Test-Plan-using-JMeter) 
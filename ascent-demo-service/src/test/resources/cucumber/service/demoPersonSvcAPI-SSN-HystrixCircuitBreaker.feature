###########################################################################################################
# Test Script:
# Use Case ID:
# Objective: To provide service fault tolerance using hystrix circuit breaker pattern
#
# Hystrix Fallback is enabled on DemoPersonServiceImpl#getPersonInfo() which has a fallback method configured
# to getPersonInfoFallBack(). A fallback method is defined in the same class. Also a fallback method has the 
# same signature to a method which was invoked as hystrix command. Also, fallback method won't be triggered 
# for IllegalArgumentException as seen below. For demo purpose, the fallback method uses REDIS bucket to 
# store and retrieve the cached data. If no cached data is found, 
#
# {code} snippet for annotation on DemoPersonServiceImpl#getPersonInfo()
# 
# @CachePut(value="getPersonInfo", key="#personInfoRequest")
# @HystrixCommand(
#	fallbackMethod = "getPersonInfoFallBack", 
#	commandKey = "GetPersonInfoBySSNCommand", 
#	ignoreExceptions = {IllegalArgumentException.class})
# 
# MOCK SOAP UI : 
#	File: ascent-demo-partner-soapui-project.xml
#   Mock Person Service Name: MockPersonWebService
#
###########################################################################################################
Feature: Demo Person Service API - SSN - Hystrix Fallback

  As a User
  I want to retrieve person information based on SSN that triggers Hystrix Fallback
  
  Scenario: Search based on SSN with Hystrix Fallback Cached
  	Given the user is authorized
  	Given the person partner response is cached for ssn 796079018
  	Given the person partner is mocked to failure
	When client request POST /demo/v1/person/ssn with json data:
		"""
        {"participantID": 0,"ssn": "796079018"}
        """
	Then the response code should be 200
    Then the result json should be:
    """
    {"messages": [],"personInfo": {"fileNumber": "796079018","firstName": "RUSSELL-SSN","middleName": "BILL","lastName": "WATSON","participantId": 13364995,"ssn": "796079018"}
    """
   
  Scenario: Search based on SSN with Hystrix Fallback Not Cached
  	Given the user is authorized
  	Given the person partner response is not cached for ssn 796079018
  	Given the person partner is mocked to failure
	When client request POST /demo/v1/person/ssn with json data:
		"""
        {"participantID": 0,"ssn": "796079018"}
        """
	Then the response code should be 500
    Then the result json should be:
    """
    {"messages": [],"personInfo": null}
    """
    
  Scenario: Search based on Invalid SSN
  	Given the user is authorized
  	Given the person partner is mocked to failure
	When client request POST /demo/v1/person/ssn with json data:
		"""
        {"participantID": 0,"ssn": "7960"}
        """
	Then the response code should be 500
    Then the result json should be:
    """
    {"messages": [{"key": "UNEXPECTED_ERROR","text": "Unique ID: [1493839869589] java.lang.IllegalArgumentException: Invalid personInfoRequest SSN. Length must be 9","severity": "FATAL"}]}
    """
  
  Scenario: Search based on Empty SSN
  	Given the user is authorized
  	Given the person partner is mocked to failure
	When client request POST /demo/v1/person/ssn with json data:
		"""
        {"participantID": 0,"ssn": ""}
        """
	Then the response code should be 500
    Then the result json should be:
    """
    {"messages": [{"key": "UNEXPECTED_ERROR","text": "Unique ID: [1493839869589] java.lang.IllegalArgumentException: Invalid personInfoRequest SSN. Length must be 9","severity": "FATAL"}]}
    """
	
  Scenario: Search based on No SSN
  	Given the user is authorized
  	Given the person partner is mocked to failure
	When client request POST /demo/v1/person/ssn with json data:
		"""
        {"participantID": 0}
        """
	Then the response code should be 500
    Then the result json should be:
    """
    {"messages": [{"key": "UNEXPECTED_ERROR","text": "Unique ID: [1493839869589] java.lang.IllegalArgumentException: Invalid personInfoRequest. SSN must not be null.","severity": "FATAL"}]}
    """
	

  	
  

    
    


###########################################################################################################
# Test Script:
# Use Case ID:
# Objective: To provide service fault tolerance criteria and expected behavior
#
#
###########################################################################################################
Feature: Demo Person Service API - SSN

  As a User
  I want to retrieve person information based on SSN
  
  Scenario: Search based on SSN
  	Given the user is authorized
  	Given the person partner is mocked to success
	When client request POST /demo/v1/person/ssn with json data:
		"""
        {"participantID": 0,"ssn": "796079018"}
        """
	Then the response code should be 200
    Then the result json should be:
    """
    {"messages": [],"personInfo": {"fileNumber": "796079018","firstName": "RUSSELL-SSN","middleName": "BILL","lastName": "WATSON","participantId": 13364995,"ssn": "796079018"}
    """
    
  Scenario: Search based on Invalid SSN
  	Given the user is authorized
  	Given the person partner is mocked to success
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
  	Given the person partner is mocked to success
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
  	Given the person partner is mocked to success
	When client request POST /demo/v1/person/ssn with json data:
		"""
        {"participantID": 0}
        """
	Then the response code should be 500
    Then the result json should be:
    """
    {"messages": [{"key": "UNEXPECTED_ERROR","text": "Unique ID: [1493839869589] java.lang.IllegalArgumentException: Invalid personInfoRequest. SSN must not be null.","severity": "FATAL"}]}
    """
	

  	
  

    
    


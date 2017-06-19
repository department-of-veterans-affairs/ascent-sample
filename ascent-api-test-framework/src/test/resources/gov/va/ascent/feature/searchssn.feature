Feature: Demo Person Service API SSN Search based on SSN

  @searchssn
  Scenario Outline: Search based on SSN
    Given I pass the header information for search ssn
      | Accept       | application/json;v=3 |
      | Content-Type | application/json;v=3 |
    When request POST "<ServiceURL>" with json data "<RequestFile>"
    Then the response code must be 400
    And the search SSN result should be same as valid response "<ResponseFile>"
	
    Examples: 
      | ServiceURL                               | RequestFile        | ResponseFile        |
      | http://localhost:8080/demo/v1/person/ssn | invalidssn.Request | invalidssn.Response |
      | http://localhost:8080/demo/v1/person/ssn | emptyssn.Request   | emptyssn.Response   |
      | http://localhost:8080/demo/v1/person/ssn | nossn.Request      | nossn.Response      |

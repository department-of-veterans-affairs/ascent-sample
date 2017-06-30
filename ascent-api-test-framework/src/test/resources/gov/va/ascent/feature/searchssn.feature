Feature: Demo person service API SSN search based on SSN

  @searchssn
  Scenario Outline: Search based on SSN
    Given I pass the header information for search ssn
      | Accept       | application/json;v=3 |
      | Content-Type | application/json;v=3 |
    When request POST "<ServiceURL>" with json data "<RequestFile>"
    Then the response code must be 400
    And the search SSN result should be same as valid response "<ResponseFile>"

    Examples: 
      | ServiceURL          | RequestFile        | ResponseFile        |
      | /demo/v1/person/ssn | invalidssn.Request | invalidssn.Response |
      | /demo/v1/person/ssn | emptyssn.Request   | emptyssn.Response   |
      | /demo/v1/person/ssn | nossn.Request      | nossn.Response      |

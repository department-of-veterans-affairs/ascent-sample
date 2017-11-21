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
      | ServiceURL          | RequestFile            | ResponseFile            |
      | /api/ascent-demo-service/demo/v1/person/ssn | demoinvalidssn.Request | demoinvalidssn.Response |
      | /api/ascent-demo-service/demo/v1/person/ssn | demoemptyssn.Request   | demoemptyssn.Response   |
      | /api/ascent-demo-service/demo/v1/person/ssn | demonossn.Request      | demonossn.Response      |

Feature: Validate json response

  @echojson
  Scenario Outline: Returns a customized JSON object
    Given I pass the header information for echo API
      | Accept       | application/json;v=3 |
      | Content-Type | application/json;v=3 |
    When I invoke echo API "<ServiceURL>" using GET method
    Then the echo service respose for API status code should be 200
    And echo service result should be same as valid Transactions response "<ResponseFile>"

    Examples: 
      | ServiceURL                                 | ResponseFile      |
      | /key/value/one/two | echojson.Response |

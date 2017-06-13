Feature: Returns the MD5 hash code of a given string.

  @md
  Scenario Outline: Returns the MD5 hash code of a given string
    Given I pass the header information for MD5 API
      | Content-Type | application/json; |
    When I invoke an API "<ServiceURL>" using MD
    Then the service respose for MD API status code should be 200
    And result should be same as valid Transactions response for MD "<ResponseFile>"

    Examples: 
      | ServiceURL                          | ResponseFile |
      | http://md5.jsontest.com/?text=[text | md.Response  |

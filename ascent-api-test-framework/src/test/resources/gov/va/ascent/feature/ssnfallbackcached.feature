Feature: 
  Demo Person Service API - SSN - Hystrix Fallback

  @ssnfallbackcached
  Scenario Outline: Search based on SSN with Hystrix Fallback Cached
    Given I pass the header information for ssn
      | Content-Type | application/json |
    When client request POST "<ServiceURL>" with json data "<Requestfile>"
    Then the response code should be 200
    And the SSNcached result should be same as valid transaction response "<ResponseFile>"

    Examples: 
      | ServiceURL                               | RequestFile               | ResponseFile               |
      | http://localhost:8080/demo/v1/person/ssn | ssnfallbackcached.Request | ssnfallbackcached.Response |


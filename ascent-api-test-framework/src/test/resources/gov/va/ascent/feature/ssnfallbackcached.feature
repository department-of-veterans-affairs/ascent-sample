Feature: Demo Person Service SSN Hystrix Fallback cached and not cached

  @ssnfallbackcached
  Scenario Outline: Search based on SSN with Hystrix Fallback Cached
    Given I pass the header information for ssn
      | Content-Type | application/json |
    When client request POST "<ServiceURL>" with json data "<RequestFile>"
    Then the response code should be 200
    And the SSNcached result should be same as valid transaction response "<ResponseFile>"

    Examples: 
      | ServiceURL                               | RequestFile                  | ResponseFile                  |
      | http://localhost:8080/demo/v1/person/ssn | ssnfallbackcachedone.Request | ssnfallbackcachedone.Response |
      | http://localhost:8080/demo/v1/person/ssn | ssnfallbackcachedtwo.Request | ssnfallbackcachedtwo.Response |

  @ssnfallbacknotcached
  Scenario Outline: Search based on SSN with Hystrix Fallback not Cached
    Given I pass the header information for ssn not cached
      | Content-Type | application/json |
    When client request POST url "<ServiceURL>" with data "<RequestFile>"
    Then the response code be 400
    And the SSNnotcached result should be same as valid transaction response "<ResponseFile>"

    Examples: 
      | ServiceURL                               | RequestFile                  | ResponseFile                  |
      | http://localhost:8080/demo/v1/person/ssn | ssnfallbacknotcached.Request | ssnfallbacknotcached.Response |

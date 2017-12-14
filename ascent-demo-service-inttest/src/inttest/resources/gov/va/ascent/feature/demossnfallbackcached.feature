Feature: Demo person service SSN hystrix fallback cached and not cached

   @ssnfallbacknotcached
  Scenario Outline: Search based on SSN with Hystrix Fallback not Cached
    Given I pass the header information for ssn not cached
      | Content-Type | application/json |
    When client request POST url "<ServiceURL>" with data "<RequestFile>"
    Then the response code be 400
    And the SSNnotcached result should be same as valid transaction response "<ResponseFile>"

    Examples: 
      | ServiceURL          | RequestFile                      | ResponseFile                      |
      | /api/ascent-demo-service/demo/v1/person/ssn | demossnfallbacknotcached.Request | demossnfallbacknotcached.Response |
 
 
 @ssnfallbackcached
  Scenario Outline: Search based on SSN with Hystrix Fallback Cached
    Given I pass the header information for ssn
      | Content-Type | application/json |
    When client request POST "<ServiceURL>" with json data "<RequestFile>"
    Then the response code should be 200
    And the SSNcached result should be same as valid transaction response "<ResponseFile>"

    Examples: 
      | ServiceURL          | RequestFile                      | ResponseFile                      |
      | /api/ascent-demo-service/demo/v1/person/ssn | demossnfallbackcachedone.Request | demossnfallbackcachedone.Response |
      



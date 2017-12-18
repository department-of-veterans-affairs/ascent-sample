Feature: Demo person service SSN hystrix fallback cached and not cached
  @ssnfallbacknotcached
  Scenario Outline: Search based on SSN with Hystrix Fallback not Cached
    Given I pass the header information for SSNNotcached
      | Content-Type | application/json |
    When client request SSNnotcached "<ServiceURL>" with SSNnotcached data "<RequestFile>"
    Then the response code for SSNnotcached should be 400
    And the SSNnotcached result should be same as valid transaction response "<ResponseFile>"

    Examples: 
      | ServiceURL          | RequestFile                      | ResponseFile                      |
      | /api/ascent-demo-service/demo/v1/person/ssn | demossnfallbacknotcached.Request | demossnfallbacknotcached.Response |
 
 
 @ssnfallbackcached
  Scenario Outline: Search based on SSN with Hystrix Fallback Cached
    Given I pass the header information for ssn cached
      | Content-Type | application/json |
    When client request SSNcached "<ServiceURL>" with SSNcached data "<RequestFile>"
    Then the response code for SSNcached should be 200 
    And the SSNcached result should be same as valid transaction response "<ResponseFile>"

    Examples: 
      | ServiceURL          | RequestFile                      | ResponseFile                      |
      | /api/ascent-demo-service/demo/v1/person/ssn | demossnfallbackcachedone.Request | demossnfallbackcachedone.Response |
      



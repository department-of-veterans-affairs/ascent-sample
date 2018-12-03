Feature: Demo person service SSN hystrix fallback cached and not cached

  @ssnfallbacknotcached
  Scenario Outline: Search based on SSN with Hystrix Fallback not Cached
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    When client request SSNnotcached "<ServiceURL>" with SSNnotcached data "<RequestFile>"
    Then the service returns status code = 400
    And the response should be same as "<ResponseFile>"

    @CI
    Examples: 
      | Veteran     | tokenrequestfile  | ServiceURL                                  | RequestFile                          | ResponseFile                      |
      | dev-janedoe | dev/token.Request | /api/ascent-demo-service/demo/v1/person/ssn | dev/demossnfallbacknotcached.Request | demossnfallbacknotcached.Response |

    @VA
    Examples: 
      | Veteran    | tokenrequestfile | ServiceURL                                  | RequestFile                         | ResponseFile                      |
      | va-janedoe | va/token.Request | /api/ascent-demo-service/demo/v1/person/ssn | va/demossnfallbacknotcached.Request | demossnfallbacknotcached.Response |

  @ssnfallbackcached
  Scenario Outline: Search based on SSN with Hystrix Fallback Cached
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    When client request SSNcached "<ServiceURL>" with SSNcached data "<RequestFile>"
    Then the service returns status code = 200
    And the response should be same as "<ResponseFile>"

    @DEV
    Examples: 
      | Veteran     | tokenrequestfile  | ServiceURL                                  | RequestFile                          | ResponseFile                      |
      | dev-janedoe | dev/token.Request | /api/ascent-demo-service/demo/v1/person/ssn | dev/demossnfallbackcachedone.Request | demossnfallbackcachedone.Response |

    @VA
    Examples: 
      | Veteran    | tokenrequestfile | ServiceURL                                  | RequestFile                         | ResponseFile                      |
      | va-janedoe | va/token.Request | /api/ascent-demo-service/demo/v1/person/ssn | va/demossnfallbackcachedone.Request | demossnfallbackcachedone.Response |

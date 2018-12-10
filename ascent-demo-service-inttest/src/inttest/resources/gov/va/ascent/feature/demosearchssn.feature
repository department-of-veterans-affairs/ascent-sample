Feature: Demo person service API search based on SSN

  @searchssn
  Scenario Outline: Search based on SSN
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    When request search ssn "<ServiceURL>" with ssn data "<RequestFile>"
    Then the service returns status code = 400
    And the response should be same as "<ResponseFile>"

    @DEV
    Examples: 
      | Veteran    | tokenrequestfile | ServiceURL                                  | RequestFile               | ResponseFile            |
      | dev-janedoe | dev/token.Request | /api/ascent-demo-service/demo/v1/person/ssn | dev/demoinvalidssn.Request | demoinvalidssn.Response |
      | dev-janedoe | dev/token.Request | /api/ascent-demo-service/demo/v1/person/ssn | dev/demoemptyssn.Request   | demoemptyssn.Response   |
      | dev-janedoe | dev/token.Request | /api/ascent-demo-service/demo/v1/person/ssn | dev/demonossn.Request      | demonossn.Response      |

    @VA
    Examples: 
      | Veteran    | tokenrequestfile | ServiceURL                                  | RequestFile               | ResponseFile            |
      | va-janedoe | va/token.Request | /api/ascent-demo-service/demo/v1/person/ssn | va/demoinvalidssn.Request | demoinvalidssn.Response |
      | va-janedoe | va/token.Request | /api/ascent-demo-service/demo/v1/person/ssn | va/demoemptyssn.Request   | demoemptyssn.Response   |
      | va-janedoe | va/token.Request | /api/ascent-demo-service/demo/v1/person/ssn | va/demonossn.Request      | demonossn.Response      |

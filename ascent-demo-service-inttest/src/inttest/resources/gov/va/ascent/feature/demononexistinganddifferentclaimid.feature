Feature: Claims for validating non existing claim id and different veteran claim id

  @nonexistingcalimid
  Scenario Outline: Non Existing claim ID
    Given Veteran has a claim Id that doesn't exist
      | va_eauth_pnid                 |                                                                                                                             123456789 |
      | va_eauth_issueinstant         | 2015-04-17T14:52:48Z                                                                                                                  |
      | va_eauth_dodedipnid           |                                                                                                                            1105051936 |
      | va_eauth_lastName             | Doe                                                                                                                                   |
      | va_eauth_firstName            | Jane                                                                                                                                  |
      | va_eauth_csid                 | DSLogon                                                                                                                               |
      | va_eauth_authenticationmethod | DSLogon                                                                                                                               |
      | va_eauth_assurancelevel       |                                                                                                                                     2 |
      | va_eauth_pid                  |                                                                                                                             123456789 |
      | va_eauth_pnidtype             | SSN                                                                                                                                   |
      | va_eauth_authorization        | {"authorizationResponse":{"id":"123456789","idType":"SSN","edi":"1105051934","firstName":"JANE","lastName":"DOE","status":"VETERAN"}} |
    When claim service is called with claim Id "<ServiceURL>" with "<claimId>"
    Then the claim sercice returns message.severity of error
    And the claim service returns message.key of claims.claimsnotfound
    And claims service returns message.text of error retrieving with given claimid

    Examples: 
      | ServiceURL                                                          | claimId   |
      | /wss-claims-services-web/rest/claims/v2/{claimId}/evidentiaryWaiver | 600099000 |

  @differentclaimid
  Scenario Outline: Different claim ID
    Given Veteran has a claim Id that belongs to a different veteran
      | va_eauth_issueinstant         | 2015-04-17T14:52:48Z                                                                                                                  |
      | va_eauth_dodedipnid           |                                                                                                                            1105051936 |
      | va_eauth_lastName             | Doe                                                                                                                                   |
      | a_eauth_firstName             | Jane                                                                                                                                  |
      | va_eauth_csid                 | DSLogon                                                                                                                               |
      | va_eauth_authenticationmethod | DSLogon                                                                                                                               |
      | va_eauth_assurancelevel       |                                                                                                                                     2 |
      | va_eauth_pid                  |                                                                                                                             600087101 |
      | va_eauth_pnidtype             | SSN                                                                                                                                   |
      | va_eauth_authorization        | {"authorizationResponse":{"id":"123456780","idType":"SSN","edi":"1105051934","firstName":"JANE","lastName":"DOE","status":"VETERAN"}} |
    When the claim service is called with different claimid "<ServiceURL>" with "<claimId>"
    Then the claim service returns different claimid message.severity of error
    And the claim service returns different claimid message.key of claims.claimsnotfound
    And claims service returns different claimid message.text of error reteriving with given claimid

    Examples: 
      | ServiceURL                                                          | claimId |
      | /wss-claims-services-web/rest/claims/v2/{claimId}/evidentiaryWaiver |  123456 |

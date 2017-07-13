Feature: Feature file to test open, completed and cancelled claim

  @openclaim
  Scenario Outline: open claim
    Given the veteran has a open claim
      | va_eauth_issueinstant         | 2015-04-17T14:52:48Z                                                                                                                  |
      | va_eauth_dodedipnid           |                                                                                                                            1105051936 |
      | va_eauth_lastName             | Doe                                                                                                                                   |
      | va_eauth_csid                 | DSLogon                                                                                                                               |
      | va_eauth_firstName            | Jane                                                                                                                                  |
      | va_eauth_authenticationmethod | DSLogon                                                                                                                               |
      | va_eauth_assurancelevel       |                                                                                                                                     2 |
      | va_eauth_pid                  |                                                                                                                             123456789 |
      | va_eauth_authorization        | {"authorizationResponse":{"id":"123456789","idType":"SSN","edi":"1105051934","firstName":"JANE","lastName":"DOE","status":"VETERAN"}} |
      | va_eauth_middleName           |                                                                                                                                       |
      | va_eauth_pnidtype             | SSN                                                                                                                                   |
    When the claim service is called "<ServiceURL>"
    Then the claim has a recieved date and does not have closed date and complete is false

    Examples: 
      | ServiceURL                              |
      | /wss-claims-services-web/rest/claims/v2 |

  @completedclaim
  Scenario Outline: Closed claim
    Given the veteran has a closed claim
      | va_eauth_issueinstant         | 2015-04-17T14:52:48Z                                                                                                                  |
      | va_eauth_dodedipnid           |                                                                                                                            1105051936 |
      | va_eauth_lastName             | Doe                                                                                                                                   |
      | va_eauth_csid                 | DSLogon                                                                                                                               |
      | va_eauth_firstName            | Jane                                                                                                                                  |
      | va_eauth_authenticationmethod | DSLogon                                                                                                                               |
      | va_eauth_assurancelevel       |                                                                                                                                     2 |
      | va_eauth_pid                  |                                                                                                                             123456789 |
      | va_eauth_authorization        | {"authorizationResponse":{"id":"123456789","idType":"SSN","edi":"1105051934","firstName":"JANE","lastName":"DOE","status":"VETERAN"}} |
      | va_eauth_middleName           |                                                                                                                                       |
      | va_eauth_pnidtype             | SSN                                                                                                                                   |
    When the closed claim service is called "<ServiceURL>"
    Then the claim status is CLR and claims has a recived date and claims has a closed update and completed date and the claim phase status is complete and true

    Examples: 
      | ServiceURL                              |
      | /wss-claims-services-web/rest/claims/v2 |

  @cancelledclaim
  Scenario Outline: Cancelled claim
    Given the veteran has a cancelled claim
      | va_eauth_issueinstant         | 2015-04-17T14:52:48Z                                                                                                                  |
      | va_eauth_dodedipnid           |                                                                                                                            1105051936 |
      | va_eauth_lastName             | Doe                                                                                                                                   |
      | va_eauth_csid                 | DSLogon                                                                                                                               |
      | va_eauth_firstName            | Jane                                                                                                                                  |
      | va_eauth_authenticationmethod | DSLogon                                                                                                                               |
      | va_eauth_assurancelevel       |                                                                                                                                     2 |
      | va_eauth_pid                  |                                                                                                                             123456789 |
      | va_eauth_authorization        | {"authorizationResponse":{"id":"123456789","idType":"SSN","edi":"1105051934","firstName":"JANE","lastName":"DOE","status":"VETERAN"}} |
      | va_eauth_middleName           |                                                                                                                                       |
      | va_eauth_pnidtype             | SSN                                                                                                                                   |
    When the cancelled claim service is called "<ServiceURL>"
    Then the claim status is CAN and claims has a recived date and claims has a closed update and completed date and the claim phase status is complete and true

    Examples: 
      | ServiceURL                              |
      | /wss-claims-services-web/rest/claims/v2 |

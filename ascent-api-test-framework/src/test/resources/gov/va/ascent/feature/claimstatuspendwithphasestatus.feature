Feature: Claim the status pend with phase status

  @gatheringofevidence
  Scenario Outline: claim status pend with phase status Gathering of evidence
    Given the veteran has pending fivetwentysix claim
      | va_eauth_issueinstant         | 2015-04-17T14:52:48Z                                                                                                                  |
      | va_eauth_dodedipnid           |                                                                                                                            1105051936 |
      | va_eauth_lastName             | Brooks                                                                                                                                |
      | va_eauth_csid                 | DSLogon                                                                                                                               |
      | va_eauth_firstName            | Jerry                                                                                                                                 |
      | va_eauth_authenticationmethod | DSLogon                                                                                                                               |
      | va_eauth_assurancelevel       |                                                                                                                                     2 |
      | va_eauth_pid                  |                                                                                                                             600036156 |
      | va_eauth_authorization        | {"authorizationResponse":{"id":"123456780","idType":"SSN","edi":"1105051934","firstName":"JANE","lastName":"DOE","status":"VETERAN"}} |
      | va_eauth_middleName           |                                                                                                                                       |
      | va_eauth_pnidtype             | SSN                                                                                                                                   |
    When the pend gathering of claim service is called "<ServiceURL>"
    Then the claim has a claimstaus of PEND and claim type of compensation
    And the claim phasestaus is Gathering of Evidence
    And the claim has a valid recived date
    And the claim phasestaus is Gathering of Evidence and claim status is false

    Examples: 
      | ServiceURL                              |
      | /wss-claims-services-web/rest/claims/v2 |

  @claimreceived
  Scenario Outline: claim status pend with phase status claim received
    Given the veteran has pending fivetwentysix claim for claims recieved
      | va_eauth_issueinstant         | 2015-04-17T14:52:48Z                                                                                                                  |
      | va_eauth_dodedipnid           |                                                                                                                            1105051936 |
      | va_eauth_lastName             | Brooks                                                                                                                                |
      | va_eauth_csid                 | DSLogon                                                                                                                               |
      | va_eauth_firstName            | Jerry                                                                                                                                 |
      | va_eauth_authenticationmethod | DSLogon                                                                                                                               |
      | va_eauth_assurancelevel       |                                                                                                                                     2 |
      | va_eauth_pid                  |                                                                                                                             600036156 |
      | va_eauth_authorization        | {"authorizationResponse":{"id":"123456780","idType":"SSN","edi":"1105051934","firstName":"JANE","lastName":"DOE","status":"VETERAN"}} |
      | va_eauth_middleName           |                                                                                                                                       |
      | va_eauth_pnidtype             | SSN                                                                                                                                   |
    When the claim recieved service is called "<ServiceURL>"
    Then the claim recieved has a claimstaus of PEND and claim type of compensation
    And the claim phasestaus is claim recived
    And the claim recived has a valid recived date
    And the claim complete is false

    Examples: 
      | ServiceURL                              |
      | /wss-claims-services-web/rest/claims/v2 |
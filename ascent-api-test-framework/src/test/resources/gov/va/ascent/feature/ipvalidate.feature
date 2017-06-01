Feature: Validate ip address
@ipvalidate
Scenario Outline: Validate ip address
Given I pass the header information for API
	| Accept       | application/json;v=3 |
	When I invoke API "<ServiceURL>" using GET
Then the service respose for API status code should be 200 
And result should be same as valid Transactions response "<ResponseFile>"

Examples:
| ServiceURL                 | ResponseFile                    |
| http://ip.jsontest.com/    | ipvalidate.Response                |                  
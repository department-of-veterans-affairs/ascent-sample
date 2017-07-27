@availablity
Feature: Ascent Availability Test Application

  Scenario Outline: Availability for ascent application to test if a zone fails to see the other zone handles the request.
    Given when there are two availability zones
    And when one zone fails with health check
    And health check response is 404 from all instances of that zone
    When I submit a get request URL with <Service URL>
    Then the request should redirect to available zone
    Then the response code should be 200

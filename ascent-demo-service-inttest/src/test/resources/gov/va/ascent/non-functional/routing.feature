@Routing
Feature: Ascent Routing Test Application

  Scenario Outline: Routing for Ascent application to test the request routed based on the availability of service
    Given when zone is down
    And health check response is 404 from all instances in the region
    When I submit a get request URL with <Service URL>
    Then the router should redirect to another zone
    Then the response code should be 200

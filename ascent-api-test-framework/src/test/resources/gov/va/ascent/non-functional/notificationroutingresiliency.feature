@notification
Feature: Ascent Test Application for notification, routing and resilliency

  Scenario Outline: Notification for Ascent application to test the memory manager and disk utilization
    Given all the server are up and running
    And create a topic
    And name the notification
    And subscribe to the notification
    When the server is 90% utilized
    Then the subscribers should get an email notification
    And the notification should display the reason.

    Examples: 
      | ServiceURL         | ResponseFile      |
      | http://asccent.com | response.response |

  @routing, @resiliency
  Scenario Outline: Routing for Ascent application to test server gets routed to another instance if a zone is down.
    Given when zone is down
    And health check response is 404 from all instances in the region
    When I submit a get request URL with <Service URL>
    Then the router should redirect to other zone
    Then the response code should be 200

    Examples: 
      | ServiceURL         | ResponseFile      |
      | http://asccent.com | response.response |

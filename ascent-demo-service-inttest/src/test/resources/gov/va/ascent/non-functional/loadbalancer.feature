@loadbalancer
Feature: Ascent Load Balancing Test Application

  Scenario Outline: Load balance the Ascent application when all instances are running
    Given all the ascent micro services are up and running in all instances
    And health check response is 200 from all instances
    When I submit get Request to Load balancer URL with <ServiceURL>
    Then the response should match response file <ResponseFile>

    Examples: 
      | ServiceURL         | ResponseFile      |
      | http://asccent.com | response.response |

  Scenario Outline: Load balance the Ascent application when half of the instances are down
    Given all the ascent micro services are up and running in some instances
    And health check response is 200 from some instances
    When I submit get Request to Load balancer URL with <ServiceURL>
    Then the response should match response file <ResponseFile>

    Examples: 
      | ServiceURL         | ResponseFile      |
      | http://asccent.com | response.response |

  Scenario Outline: Load balance the Ascent application when just 1 instance is running
    Given all the ascent micro services are up and running in 1 instance
    And health check response is 200 from 1 instance
    When I submit get Request to Load balancer URL with <ServiceURL>
    Then the response should match response file <ResponseFile>

    Examples: 
      | ServiceURL         | ResponseFile      |
      | http://asccent.com | response.response |

  Scenario Outline: Load balance the Ascent application when all instances are down
    Given all instances are down
    And health check response is 404 from all instances
    When I submit get Request to Load balancer URL with <ServiceURL>
    Then the response code should be 404

    Examples: 
      | ServiceURL         | ResponseFile      |
      | http://asccent.com | response.response |

  Scenario Outline: Load balancer test the ascent application with round robin technique
    Given there are two servers
    And the health check response is 200 from both instances
    When I submit get request to load balancer URL with <ServiceURL>
    Then the request sent will be distributed based on the cluster of servers
    And the response should match response file <ResponseFile>

    Examples: 
      | ServiceURL         | ResponseFile      |
      | http://asccent.com | response.response |

@performancetest
Feature: Ascent performance Test Application

  Scenario Outline: Load testing for Ascent application to test the application behavior on certain load
    Given all the ascent services are up and running
    And add test plan element
    And add thread group element
    When I submit get request to load test URL with <Service URl>
    Then the response should match response file<ResponseFile>
    And view the report for response time during the load duration for the specific API under test.

    Examples: 
      | ServiceURL         | ResponseFile      |
      | http://asccent.com | response.response |

  Scenario Outline: Stress testing for Ascent application to test the capacity of the system and see if the current loads go above the maximum
    Given all the ascent services are up and running
    And add the test configuration
    When I submit get request to stress test URL with <Service URl>
    Then the response should match response file<ResponseFile>
    And view the report for maximum load capacity.

    Examples: 
      | ServiceURL         | ResponseFile      |
      | http://asccent.com | response.response |

  Scenario Outline: Spike testing for Ascent application to test the system will sustain the workload
    Given all the ascent services are up and running
    And add the test configuration
    And increase the no of users by large amount
    When I submit get request to stress test URL with <Service URl>
    Then the response should match response file<ResponseFile>
    And view the report for the workload.

    Examples: 
      | ServiceURL         | ResponseFile      |
      | http://asccent.com | response.response |

  Scenario Outline: Endurance testing for Ascent application to test the system with an expected amount of load over a long period of time to find the behavior of a system.
    Given all the ascent services are up and running
    And add the test configuration
    And increase the time load for large amount
    When I submit get request to stress test URL with <Service URl>
    Then the response should match response file<ResponseFile>
    And view the report to find the behavior of the system.

    Examples: 
      | ServiceURL         | ResponseFile      |
      | http://asccent.com | response.response |

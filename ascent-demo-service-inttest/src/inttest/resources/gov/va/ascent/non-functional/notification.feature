@notification
Feature: Ascent Notification Test Application

  Scenario Outline: Notification for Ascent application to test the memory manager
    Given all the server are up and running
    And create a topic
    And name the notification
    And subscribe to the notification
    When the server is 90% utilized
    Then the subscribers should get an email notification
    And the notification should display the reason

  Scenario Outline: Notification for Ascent application to test the disk utilization
    Given all the server are up and running
    And create a topic
    And name the notification
    And subscribe to the notification
    When the disk is 90% utilized
    Then the subscribers should get an email notification
    And the notification should display the reason.

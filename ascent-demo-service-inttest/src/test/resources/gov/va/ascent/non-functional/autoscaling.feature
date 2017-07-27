@autoscaling
Feature: Scale up and down services

  Scenario Outline: Scale up Service
    Given 1 task of any ascent service running
    When load increases
    Then scale up service task by 1
    And make sure we have 2 task running.

  Scenario Outline: Scale down service
    Given 5 tasks of ascent service running
    When the load is less than 20 percentage
    Then scale down task
    And make sure the task is down by 1.

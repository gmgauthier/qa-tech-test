Feature: Partition Quiz
  Scroll to the bottom, calculate the partition value of three arrays, enter and submit.

  Background:
    Given The user is at the home page
    Then The render challenge button should be visible

  Scenario: Quiz value submissions
    Given The user has navigated to the entry fields
     When The user has submitted the correct entry field values
     Then The success message is displayed

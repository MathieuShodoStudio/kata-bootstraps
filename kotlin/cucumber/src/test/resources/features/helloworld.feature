Feature: Greeting from the world

  Scenario: World says hello
    Given the World was created
    When I ask for a greeting
    Then the greeting should be "Hello World"

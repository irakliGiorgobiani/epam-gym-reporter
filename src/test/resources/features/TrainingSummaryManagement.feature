Feature: Training Summary Management
  Scenario: Creating a new training summary
    Given a new TrainingDto instance with the username "john_doe"
    When saving to the database
    Then a new training summary is added to the database with the username "john_doe"

  Scenario: Updating a training summary
    Given a new TrainingDto instance with the username "john_doe"
    And existing training summary instance with the username "john_doe"
    When saving to the database
    Then the training summary with the username "john_doe" is updated
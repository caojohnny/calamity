Feature: Calamity Buffer Implementation

  Scenario: Add a byte to a regular buffer
    Given a regular buffer
    When byte value 1 is added to the end
    Then the buffer size should be 1
    And byte at the end should be 1

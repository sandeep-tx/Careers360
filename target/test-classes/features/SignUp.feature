@SignUp @RegistrationRevamp
  Feature: SignUp

  @SignUp @RR001 @Web
  Scenario: Verify error message for name text field while entering single character
    Given Launch "Careers360" website
    And click on "Login"
    And enter signup details for "RR001" account
    And clicked on "SIGN UP" button
    And validate error message associated to "Name" field for "RR001"

    @SignUp @RR002 @Web
    Scenario: Verify error message for name text field while entering more than 60 characters
      Given Launch "Careers360" website
      And click on "Login"
      And enter signup details for "RR002" account
      And clicked on "SIGN UP" button
      And validate error message associated to "Name" field for "RR002"

$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("SignUp.feature");
formatter.feature({
  "line": 2,
  "name": "SignUp",
  "description": "",
  "id": "signup",
  "keyword": "Feature",
  "tags": [
    {
      "line": 1,
      "name": "@SignUp"
    },
    {
      "line": 1,
      "name": "@RegistrationRevamp"
    }
  ]
});
formatter.before({
  "duration": 1461262199,
  "status": "passed"
});
formatter.scenario({
  "line": 5,
  "name": "Verify error message for name text field while entering single character",
  "description": "",
  "id": "signup;verify-error-message-for-name-text-field-while-entering-single-character",
  "type": "scenario",
  "keyword": "Scenario",
  "tags": [
    {
      "line": 4,
      "name": "@SignUp"
    },
    {
      "line": 4,
      "name": "@RR001"
    },
    {
      "line": 4,
      "name": "@Web"
    }
  ]
});
formatter.step({
  "line": 6,
  "name": "Launch \"Careers360\" website",
  "keyword": "Given "
});
formatter.step({
  "line": 7,
  "name": "click on \"Login\"",
  "keyword": "And "
});
formatter.step({
  "line": 8,
  "name": "enter signup details for \"RR001\" account",
  "keyword": "And "
});
formatter.step({
  "line": 9,
  "name": "clicked on \"SIGN UP\" button",
  "keyword": "And "
});
formatter.step({
  "line": 10,
  "name": "validate error message associated to \"Name\" field for \"RR001\"",
  "keyword": "And "
});
formatter.match({
  "arguments": [
    {
      "val": "Careers360",
      "offset": 8
    }
  ],
  "location": "Login.navigateToURL(String)"
});
formatter.result({
  "duration": 840956899,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Login",
      "offset": 10
    }
  ],
  "location": "HomePage.clickOnLabels(String)"
});
formatter.result({
  "duration": 5069028300,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "RR001",
      "offset": 26
    }
  ],
  "location": "SignUp.enterSignupDetailsForAccount(String)"
});
formatter.result({
  "duration": 692382101,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "SIGN UP",
      "offset": 12
    }
  ],
  "location": "SignUp.clickedOnButtonLabel(String)"
});
formatter.result({
  "duration": 793665201,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "Name",
      "offset": 38
    },
    {
      "val": "RR001",
      "offset": 55
    }
  ],
  "location": "SignUp.validateErrorMessageAssociatedToFieldFor(String,String)"
});
formatter.result({
  "duration": 177034700,
  "error_message": "java.lang.AssertionError: expected [true] but found [false]\r\n\tat org.testng.Assert.fail(Assert.java:97)\r\n\tat org.testng.Assert.failNotEquals(Assert.java:969)\r\n\tat org.testng.Assert.assertTrue(Assert.java:43)\r\n\tat org.testng.Assert.assertTrue(Assert.java:53)\r\n\tat stepDefinitions.SignUp.SignUp.validateErrorMessageAssociatedToFieldFor(SignUp.java:29)\r\n\tat ✽.And validate error message associated to \"Name\" field for \"RR001\"(SignUp.feature:10)\r\n",
  "status": "failed"
});
formatter.after({
  "duration": 2385183200,
  "status": "passed"
});
});
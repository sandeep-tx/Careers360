package stepDefinitions.Login;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import Module.Login.LoginModule;
import utilities.ExcelDataUtil;
import utilities.KeywordUtil;

import java.util.HashMap;

public class Login {
    public static HashMap<String, String> dataMap = new HashMap<>();
    String className = this.getClass().getSimpleName();

    @Given("^Launch \"([^\"]*)\" website$")
    public void navigateToURL(String url) throws InterruptedException {
        dataMap = ExcelDataUtil.getTestDataWithTestCaseID(className, url);
        KeywordUtil.navigateToUrl(dataMap.get("URL"));
    }

    @And("^Login with \"([^\"]*)\" username and password$")
    public void loginWith(String TestData) {
        dataMap = ExcelDataUtil.getTestDataWithTestCaseID(className, TestData);
        LoginModule.EnterUsernameAndPassword(dataMap.get("UserName"), dataMap.get("Password"));
    }

    @Then("^Verify login is \"([^\"]*)\"$")
    public void verifyUserIsLoggedIn(String Flag) {
        LoginModule.VerifySign(Flag.equalsIgnoreCase("successful"));
    }

    @And("Reset password for \"([^\"]*)\" user")
    public void resetPasswordForUser(String TestData) {
        dataMap = ExcelDataUtil.getTestDataWithTestCaseID(className, TestData);
        LoginModule.ResetUser(dataMap.get("UserName"), dataMap.get("Email"));
    }

    @And("Set new password for \"([^\"]*)\" user")
    public void setNewPasswordForUser(String TestData) {
        dataMap = ExcelDataUtil.getTestDataWithTestCaseID(className, TestData);
        LoginModule.SetNewPassword(dataMap.get("NewPassword"));
    }

    @And("Logout")
    public void logout() {
        LoginModule.logOut();
    }

    @And("logout and close")
    public void logoutAndClose() {
        LoginModule.logOutAndClose();
    }
    @And("Open new window")
    public void logoutAndOpenNewTab() {
        LoginModule.openNewWindow();
    }

}

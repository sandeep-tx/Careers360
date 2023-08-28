package stepDefinitions.SignUp;

import Module.SignUp.SignUpModule;
import cucumber.api.java.en.And;
import org.python.antlr.ast.Str;
import org.testng.Assert;
import utilities.ExcelDataUtil;

import java.util.HashMap;

public class SignUp extends SignUpModule {
    public static HashMap<String, String> dataMap = new HashMap<>();
    String className = this.getClass().getSimpleName();

    @And("enter signup details for \"([^\"]*)\" account")
    public void enterSignupDetailsForAccount(String testData) throws InterruptedException {
        dataMap = ExcelDataUtil.getTestDataWithTestCaseID(className, testData);
        enterSignUpDetail(dataMap.get("Name"));
    }

    @And("clicked on \"([^\"]*)\" button")
    public void clickedOnButtonLabel(String label) throws InterruptedException {
        clickedOnButton(label);
    }

    @And("validate error message associated to \"([^\"]*)\" field for \"([^\"]*)\"")
    public void validateErrorMessageAssociatedToFieldFor(String field, String testData) throws InterruptedException {
        dataMap = ExcelDataUtil.getTestDataWithTestCaseID(className, testData);
        Assert.assertTrue(validateErrorMessage(field,dataMap.get("ExpectedError")));
    }
}

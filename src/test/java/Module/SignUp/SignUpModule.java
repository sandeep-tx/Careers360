package Module.SignUp;

import com.relevantcodes.extentreports.LogStatus;
import org.python.antlr.ast.Str;
import pages.HomePageObjects;
import pages.SignUpPageObjects;
import runner.MyTestRunner;
import utilities.HTMLReportUtil;
import utilities.KeywordUtil;

public class SignUpModule extends KeywordUtil {

    public static void enterSignUpDetail(String name) throws InterruptedException {
        getDriver().switchTo().frame(SignUpPageObjects.loginIFrame);
        inputText(SignUpPageObjects.commonSignUpTextFields(SignUpPageObjects.name),name,"Name entered: "+name);
        switchToDefaultContent();
    }
    public static void clickedOnButton(String label) throws InterruptedException {
        switch(label){
            case "SIGN UP":
                getDriver().switchTo().frame(SignUpPageObjects.loginIFrame);
                click(SignUpPageObjects.commonButtonByText(label),"Clicked on '"+label+"' button");
                switchToDefaultContent();
                break;
        default:
            break;
        }
    }

    public static boolean validateErrorMessage(String label, String expectedMessage) throws InterruptedException {
        getDriver().switchTo().frame(SignUpPageObjects.loginIFrame);
        String actualErrorMessageText = getElementText(SignUpPageObjects.commonErrorText(label));
        switchToDefaultContent();
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.infoStringGreenColor("Expected error: "+expectedMessage));
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.infoStringGreenColor("Actual error: "+actualErrorMessageText));
        if (expectedMessage.equalsIgnoreCase(actualErrorMessageText)){
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.infoStringGreenColor("Expected message is equal to error message"));
        }else {
            MyTestRunner.logger.log(LogStatus.FAIL, HTMLReportUtil.infoStringGreenColor("Expected message is not equal to error message"));
        }
        return actualErrorMessageText.equalsIgnoreCase(expectedMessage);
    }

}

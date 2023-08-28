package Module.Login;


import gherkin.lexer.Pa;
import org.openqa.selenium.JavascriptExecutor;
import pages.LoginPageObjects;
import utilities.KeywordUtil;


public class LoginModule extends KeywordUtil {
    public static String newPasswordText=null;
    /**
     * @Description: This method will enter username and password in Login screen.
     * @Parameter: String Username, String Password
     * @Created By: Sandeep Singh
     * @Date: May 4, 2023
     */
    public static void EnterUsernameAndPassword(String User, String Password) {
        try {
            getDriver().manage().deleteAllCookies();
            getDriver().navigate().refresh();
            if (newPasswordText!=null){
                Password=newPasswordText;
            }
            inputText(LoginPageObjects.UserName, User, "Username entered");
            inputText(LoginPageObjects.PassWord, Password, "Password entered");
            takeScreenshotAndAttachInReport();
            click(LoginPageObjects.SignInButton, "Clicked on Sign in button");
            newPasswordText=null;

        } catch (Exception e) {
            catchAssertError(e);
        }
    }

    /**
     * @Description: This method will verify whether is Sign in is "successful" or "unsuccessful".
     * @Parameter: Boolean Flag("successful" is true, "unsuccessful" is false)
     * @Created By: Sandeep Singh
     * @Date: May 4, 2023
     */
    public static void VerifySign(boolean Flag) {
        try {
            if (Flag) {
                isWebElementVisible(LoginPageObjects.LoginStatusPass, "Login successful");
            } else
                isWebElementVisible(LoginPageObjects.LoginStatusFail, "Login unsuccessful");
        }
        catch (Exception e) {
            catchAssertError(e);
        }
        takeScreenshotAndAttachInReport();
    }

    /**
     * @Description: This method will reset user's account
     * @Parameter: String UserName, String Email
     * @Created By: Sandeep Singh
     * @Date: May 10, 2023
     */
    public static void ResetUser(String UserName, String Email) {
        try {
            click(LoginPageObjects.ForgetPassword, "Clicked on Forget password");
            waitForVisible(LoginPageObjects.ForgetEmailID);
//            isWebElementVisible(LoginPageObjects.ForgetEmailID, "Page loaded, fields are visible");
            if (!Email.equalsIgnoreCase("#skip#")) {
                inputText(LoginPageObjects.ForgetEmailID, Email, "Email id entered");
            } else {
                inputText(LoginPageObjects.UserName, UserName, "Username entered");
            }
            click(LoginPageObjects.SendEmail, "Send Email button clicked");
            takeScreenshotAndAttachInReport();
        } catch (Exception e) {
            catchAssertError(e);
        }
    }

    /**
     * @Description: This method will set new password".
     * @Parameter: String NewPassword
     * @Created By: Sandeep Singh
     * @Date: May 11, 2023
     */
    public static void SetNewPassword(String NewPassword) {
        try {
            newPasswordText=NewPassword+System.currentTimeMillis();
            inputText(LoginPageObjects.NewPassword1, newPasswordText, "Entered new password: "+newPasswordText);
            inputText(LoginPageObjects.NewPassword2, newPasswordText, "Re-entered new password: "+newPasswordText);
            takeScreenshotAndAttachInReport();
            click(LoginPageObjects.ChangePasswordSubmitButton, "Clicked on Changed password button");
            takeScreenshotAndAttachInReport();
            click(LoginPageObjects.loginHere,"Clicked on Login here");
        } catch (Exception e) {
            catchAssertError(e);
        }
    }
    public static void logOut(){
        try {
            waitForVisible(LoginPageObjects.CommonAnchorElement("Sign Out"));
            click(LoginPageObjects.CommonAnchorElement("Sign Out"),"Clicked on Sign out");
            isWebElementVisible(LoginPageObjects.UserName,"User logged out successfully");
            getDriver().manage().deleteAllCookies();
        }catch (Exception e) {
            catchAssertError(e);
        }
    }

    public static void logOutAndClose(){
        try {
            click(LoginPageObjects.CommonAnchorElement("Sign Out"),"Clicked on Sign out");
            isWebElementVisible(LoginPageObjects.UserName,"User logged out successfully");
//            getDriver().close();
        }catch (Exception e) {
            catchAssertError(e);
        }
    }
    public static void openNewWindow(){
        try {
            ((JavascriptExecutor) getDriver()).executeScript("window.open('https://uatcms.webdev.skynewsarabia.com/publisher/login')");
            switchToWindow();
            getDriver().manage().deleteAllCookies();
        }catch (Exception e) {
            catchAssertError(e);
        }
    }
}

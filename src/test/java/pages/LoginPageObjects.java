package pages;

import org.openqa.selenium.By;

public class LoginPageObjects {

    public static By UserName = By.xpath("//input[@id='username']");
    public static By PassWord = By.xpath("//input[@id='password']");
    public static By SignInButton = By.xpath("//input[@id='login.submit']");
    public static By LoginStatusPass = By.xpath("//p[@id='loginstatus']");
    public static By LoginStatusFail = By.xpath("//div[@class='messages']/div[contains(text(),'Your login attempt was not successful')]");

    public static By ForgetPassword = By.xpath("//a[text()='Forgot Password?']");
    public static By ForgetEmailID = By.xpath("//input[@id='email']");
    public static By SendEmail = By.xpath("//input[@id='login.submit']");
    public static By NewPassword1 = By.xpath("//input[@id='newPassword1']");
    public static By NewPassword2 = By.xpath("//input[@id='newPassword2']");
    public static By ChangePasswordSubmitButton = By.xpath("//button[@type='submit']");
    public static By CommonAnchorElement(String Text) {
        return By.xpath("//a[text()='" + Text + "']");
    }
    public static By loginHere =  By.xpath("//a[text()='Login Here']");


}

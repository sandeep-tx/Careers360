package pages;

import org.openqa.selenium.By;

public class SignUpPageObjects {
    public static By commonSignUpTextFields(String label){
        return By.xpath("//label[contains(text(),'"+label+"')]/following-sibling::input");
    }
    public static By commonButtonByText(String label){
        return By.xpath("//button[contains(text(),'"+label+"')]");
    }

    public static By commonErrorText(String label){
        return By.xpath("//label[contains(text(),'"+label+"')]/parent::div/span");
    }
    public static String loginIFrame="someFrame";
    public static String name="Name";
    public static String emailId="Email ID";
    public static String mobileNumber="Mobile Number";

}

package pages;

import org.openqa.selenium.By;

public class HomePageObjects {
    public static By commonAnchor(String label){
        return By.xpath("//a[text()='"+label+"']");
    }
    public static By signUpLabel=By.xpath("//div[@class='titleHeader']");
    public static String loginIFrame="someFrame";


}

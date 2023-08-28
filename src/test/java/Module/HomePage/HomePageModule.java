package Module.HomePage;

import pages.HomePageObjects;
import utilities.KeywordUtil;

public class HomePageModule extends KeywordUtil {
    public static void clickOnLabel(String label) throws InterruptedException {
        switch (label){
            case "Login":
                waitForVisible(HomePageObjects.commonAnchor(label));
                click(HomePageObjects.commonAnchor(label),"Clicked on '"+label+"'");
                getDriver().switchTo().frame(HomePageObjects.loginIFrame);
                waitForVisibleUntilTime(HomePageObjects.signUpLabel,8);
                waitForVisible(HomePageObjects.signUpLabel);
                switchToDefaultContent();
                takeScreenshotAndAttachInReport();
                break;
            default:
                break;
        }
    }
}

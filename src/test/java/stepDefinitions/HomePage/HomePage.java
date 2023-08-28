package stepDefinitions.Login;

import Module.HomePage.HomePageModule;
import cucumber.api.java.en.And;

import java.util.HashMap;

public class HomePage extends HomePageModule {
    public static HashMap<String, String> dataMap = new HashMap<>();
    String className = this.getClass().getSimpleName();

    @And("click on \"([^\"]*)\"")
    public void clickOnLabels(String label) throws InterruptedException {
        clickOnLabel(label);
    }
}

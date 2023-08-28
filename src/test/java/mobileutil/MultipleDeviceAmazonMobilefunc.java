package mobileutil;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import groovyjarjarantlr.collections.List;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import runner.MyTestRunner;
import utilities.DriverUtil;
import utilities.GlobalUtil;


public class MultipleDeviceAmazonMobilefunc extends MyTestRunner {

    public static AndroidDriver<MobileElement> Mdriver;


    public static <AndroidElement> void skipSignIn(AndroidDriver<MobileElement> Mdriver) throws InterruptedException {
        Thread.sleep(5000);
        System.out.println("In SKip Signin");
        //Mdriver.findElementByXPath("//android.widget.Button[@text='Skip sign in']").click();

        AndroidElement searchElement = (AndroidElement) new WebDriverWait(Mdriver, 30).until(
                ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Search Wikipedia")));
        ((RemoteWebElement) searchElement).click();
        AndroidElement insertTextElement = (AndroidElement) new WebDriverWait(Mdriver, 30).until(
                ExpectedConditions.elementToBeClickable(MobileBy.id("org.wikipedia.alpha:id/search_src_text")));
        ((RemoteWebElement) insertTextElement).sendKeys("BrowserStack");
        Thread.sleep(5000);
        java.util.List<MobileElement> allProductsName = Mdriver.findElementsByClassName("android.widget.TextView");
        assert (allProductsName.size() > 0);

        /*
         * logger.log(LogStatus.PASS,"open Wikipedia"); MobileElement searchElement =
         * (MobileElement) new WebDriverWait(Mdriver, 30).until(
         * ExpectedConditions.elementToBeClickable(MobileBy.
         * AccessibilityId("Search Wikipedia"))); searchElement.click();
         *
         * logger.log(LogStatus.PASS,"click on search button");
         *
         * MobileElement insertTextElement = (MobileElement) new WebDriverWait(Mdriver,
         * 30).until( ExpectedConditions.elementToBeClickable(MobileBy.id(
         * "org.wikipedia.alpha:id/search_src_text")));
         * insertTextElement.sendKeys("BrowserStack");
         *
         * Thread.sleep(5000); java.util.List<MobileElement> allProductsName =
         * Mdriver.findElementsByClassName("android.widget.TextView");
         * assert(allProductsName.size() > 0);
         * logger.log(LogStatus.PASS,"disply all product list");
         */


    }

    public static void clickShopByCategory() {
        System.out.println("In clickShopByCategory");

    }


    public static void clickFirstCategoryExpandButton() throws InterruptedException {
        System.out.println("In clickFirstCategoryExpandButton");

    }

    public static void selectProduct() {
        //executionDelay(5000);
        System.out.println("In selectProduct");

    }

    public static void addProductToCart() throws IOException, InterruptedException {
        System.out.println("In addProductToCart");

    }

    public static void verifyItemAddedToCart() {
        System.out.println("In verifyItemAddedToCart");

    }


}

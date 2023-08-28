package mobileutil;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.sun.javafx.scene.traversal.Direction;
import io.appium.java_client.*;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import runner.MyTestRunner;
import utilities.GlobalUtil;
import utilities.HTMLReportUtil;
import utilities.KeywordUtil;
import utilities.LogUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MobileKeywords2 {
    public static Class<MobileKeywords2> thisClass = MobileKeywords2.class;
    public static Dimension size;
    public static int fail = 0;
    static WebElement webElement;
    public static String URL = "";
    public static boolean flag1 = false;
    public static String logging_step;
    static int row = 1;
    static Logger errorLogger;
    static Logger normalLogger;
    static FileAppender normalFileApp;
    static FileAppender errorFileApp;
    static ConsoleAppender conApp;
    public static boolean isInit = false;
    public static ExtentTest test = null;
    private static ExtentTest testHist = null;
    static PatternLayout patternLayout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p - %m%n");
    static PatternLayout consolePatternLayout = new PatternLayout("\tLOG-: [%m -  %d{yyyy-MM-dd HH:mm:ss a}] %n");

    public static AndroidDriver<MobileElement> MobileDriver;

    public static boolean isDisplayed;
    // public static AndroidDriver<MobileElement> Mdriver;

    /*
     * //public static Appiumdriver1<MobileElement> driver1=null; public static
     * Appiumdriver1<MobileElement> driver1 = (Appiumdriver1<MobileElement>)
     * GlobalUtil.getdriver1(); public static Appiumdriver1<MobileElement> driver1 =
     * (Appiumdriver1<MobileElement>) GlobalUtil.getMdriver1();
     */
    public static DesiredCapabilities capabilities = new DesiredCapabilities();

    public static String GetValue(String key) {
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/Config/config.properties");
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Properties prop = new Properties();

        try {
            prop.load(fileInput);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String strbaseURL = prop.getProperty(key);
        return strbaseURL;
    }

    public static int GetIntValue(String key) {
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/Config/config.properties");

        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Properties prop = new Properties();

        try {
            prop.load(fileInput);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String strbaseURL = prop.getProperty(key);
        return Integer.parseInt(strbaseURL);
    }
    /*
     * public static boolean navigate() {
     *
     * driver1.navigate().to(GetValue("BASE_URL")); return true; }
     */

    public static By locatortype(String type, String value) {

        By locName = null;
        if (type.equalsIgnoreCase("xpath")) {
            locName = By.xpath(value);
        } else if (type.equalsIgnoreCase("id")) {
            locName = By.id(value);
        } else if (type.equalsIgnoreCase("linkText")) {
            locName = By.linkText(value);
        } else if (type.equalsIgnoreCase("classname")) {
            locName = By.className(value);
        } else if (type.equalsIgnoreCase("name")) {
            locName = By.name(value);
        } else if (type.equalsIgnoreCase("accessibilityId")) {
            locName = MobileBy.AccessibilityId(value);
        } else if (type.equalsIgnoreCase("predicateString")) {
            locName = MobileBy.iOSNsPredicateString(value);
        } else if (type.equalsIgnoreCase("iOSClassChain")) {
            locName = MobileBy.iOSClassChain(value);
        } else if (type.equalsIgnoreCase("uiSelector")) {
            locName = MobileBy.AndroidUIAutomator(value);
        } else
            locName = By.partialLinkText(value);
        return locName;

    }


    public static boolean isWebElementPresent(String path, String type) {
        boolean flag = false;
        WebDriverWait wait = new WebDriverWait(GlobalUtil.mdriver, 60);
        try {
            String element = wait.until(ExpectedConditions.visibilityOfElementLocated(locatortype(type, path))).getText();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean isWebElementPresent(String path, String type, String logStep) {
        boolean flag = false;
        WebDriverWait wait = new WebDriverWait(GlobalUtil.mdriver, 60);
        try {
            String element = wait.until(ExpectedConditions.visibilityOfElementLocated(locatortype(type, path))).getText();
            if (logStep.length() > 0) {
                MyTestRunner.logger.log(LogStatus.PASS, "Validated that " + logStep + " is visible");
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isEditable(String path, String type) {
        boolean flag = false;
        WebElement element = null;
        String currentVal = null;
        WebDriverWait wait = new WebDriverWait(GlobalUtil.mdriver, 60);
        try {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(locatortype(type, path)));
            currentVal = element.getText().split(",")[0];
            element.clear();
            element.sendKeys("123");
            String newVal = element.getText().split(",")[0];
            if (newVal.equals("123"))
                return true;
            else
                return false;

        } catch (Exception e) {
            return false;
        } finally {
            element.sendKeys(currentVal);

        }
    }

    public static boolean isSuspectedWebElementPresent(String path, String type) {
        try {
            GlobalUtil.mdriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            return GlobalUtil.mdriver.findElement(locatortype(type, path)).isDisplayed();
        } catch (Exception e) {
            return false;
        } finally {
            GlobalUtil.mdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        }
    }

    public static boolean isLessSuspectedWebElementPresent(String path, String type) {
        try {
            GlobalUtil.mdriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            return GlobalUtil.mdriver.findElement(locatortype(type, path)).isDisplayed();
        } catch (Exception e) {
            return false;
        } finally {
            GlobalUtil.mdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        }
    }

    public static void clearFraudRulesForAndroid() {
        try {
            Process process = Runtime.getRuntime().exec("sh " + "src/test/resources/ClearFraud.sh");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (Throwable e) {
            KeywordUtil.catchAssertError(e);
        }
    }

    public static void bringFocusToNativeView() {
        TouchAction touchAction = new TouchAction(GlobalUtil.mdriver);
        touchAction.tap(PointOption.point(384, 2121)).perform();
    }

    public static boolean isWebElementVisible(String path, String type) {
        Boolean flag = false;
        if (GlobalUtil.getMDriver().findElements(locatortype(type, path)).size() > 0) {
            flag = true;
            // LogUtil.infoLog(thisClass, "Element Visible");
        }

        return flag;

    }

    public static void waitUntilWebElementVisible(String path, String type) {
        try {
            WebDriverWait wait = new WebDriverWait(GlobalUtil.getMDriver(), 60);
//			wait.until(ExpectedConditions.visibilityOfElementLocated(locatortype(type, path)));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locatortype(type, path)));
        } catch (Exception e) {
            LogUtil.infoLog(thisClass, "Element" + path + "with type " + type + " not found");
        }
    }

    public static void waitUntilWebElementInVisible(String path, String type) {
        try {
            WebDriverWait wait = new WebDriverWait(GlobalUtil.getMDriver(), 30);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locatortype(type, path)));
        } catch (Exception e) {
            LogUtil.infoLog(thisClass, "Element" + path + "with type " + type + " not found");
        }
    }

    public static boolean isWebElementNotPresent(String path, String type) {

        @SuppressWarnings("unchecked")
        List<AndroidElement> elements = (List<AndroidElement>) GlobalUtil.getMDriver()
                .findElements(locatortype(type, path));

        if (elements.size() > 0) {
            System.out.println("Element Present");
            LogUtil.infoLog(thisClass, "Element present");
            return false;
        } else {
            System.out.println("Element is not Present");
            LogUtil.infoLog(thisClass, "Element Not present");

            return true;
        }
    }

    public static boolean writeInInput(String path, String type, String data, String logstep) {
        // driver1=(Appiumdriver1<MobileElement>) GlobalUtil.getMdriver1();
        WebElement element = GlobalUtil.getMDriver().findElement(locatortype(type, path));

        // element.clear();
        element.sendKeys(data);

        System.out.println("Value Entered");
        MyTestRunner.logger.log(LogStatus.PASS, logstep);
        LogUtil.infoLog(thisClass, "Test Data entered successfully");
        return true;

    }

    public static String getValue(String path, String type, String logstep) {
        MobileElement element = (MobileElement) GlobalUtil.getMDriver().findElement(locatortype(type, path));
        System.out.println("Value got from element is :" + element.getText());
        if (logstep.length() > 0)
            MyTestRunner.logger.log(LogStatus.PASS, logstep);

        return element.getText();

    }



    public static Integer generateRandomNumber(Integer max, Integer min) {
        Random random = new Random();
        Integer val = random.nextInt(max - min + 1) + min;
        return val;
    }

    public static boolean setValue(String path, String type, String data, String logstep) {
        WebDriverWait wait = new WebDriverWait(GlobalUtil.mdriver, 30);
        MobileElement element = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(GlobalUtil.getMDriver().findElement(locatortype(type, path))));
        element.clear();
        element.sendKeys(data);
        System.out.println("Value Entered");
        if (logstep.length() > 0)
            MyTestRunner.logger.log(LogStatus.PASS, logstep);

        return true;

    }

    public static boolean setValueForIPhone(String path, String type, String data, String logstep) {
        WebDriverWait wait = new WebDriverWait(GlobalUtil.mdriver, 30);
        MobileElement element = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(GlobalUtil.getMDriver().findElement(locatortype(type, path))));
        element.clear();
        element.sendKeys(data);
        System.out.println("Value Entered");
        getElement(IPhoneConstants.PayRange.btnDoneFromAddFundsScreen, IPhoneConstants.Common.type_predicateString, "").click();
        if (logstep.length() > 0)
            MyTestRunner.logger.log(LogStatus.PASS, logstep);

        return true;

    }

    public static boolean setValueUsingKeyBoard(String path, String type, String data, String logstep) {
        WebDriverWait wait = new WebDriverWait(GlobalUtil.mdriver, 30);
        MobileElement element = (MobileElement) wait.until(ExpectedConditions.elementToBeClickable(GlobalUtil.getMDriver().findElement(locatortype(type, path))));
        element.clear();
        element.click();
        GlobalUtil.mdriver.getKeyboard().sendKeys(data);
        System.out.println("Value Entered");
        GlobalUtil.mdriver.hideKeyboard();
        MyTestRunner.logger.log(LogStatus.PASS, logstep);

        return true;

    }

    public static boolean enter() throws IOException {
        Runtime.getRuntime().exec("adb shell input keyevent 66");
        return true;

    }

    public static boolean clearAppData() throws IOException {
        Runtime.getRuntime().exec("adb shell pm clear com.cloudfmgroup.cloudFMApp");
        return true;

    }

    public static boolean swipeIOS(String path, String type, String data) {
        MobileElement element = (MobileElement) GlobalUtil.getMDriver().findElement(locatortype(type, path));
        element.setValue(data);

        System.out.println("Value Entered");
        return true;

    }

    public static boolean enterInput(String path, String type, String data, String logstep) {
        WebElement element = GlobalUtil.getMDriver().findElement(locatortype(type, path));

        ((JavascriptExecutor) GlobalUtil.getMDriver()).executeScript("arguments[0].value = arguments[1]", element,
                data);

        System.out.println("Value Entered");
        MyTestRunner.logger.log(LogStatus.PASS, logstep);

        return true;

    }

    public static WebElement explicitWaitForElement(String path, String type) {
        WebDriverWait wait = new WebDriverWait(GlobalUtil.getMDriver(), GetIntValue("explicit_timeout"));

        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locatortype(type, path)));

        return element;

    }

    public static WebElement explicitWaitForElementfor20(String path, String type) {
        WebDriverWait wait = new WebDriverWait(GlobalUtil.getMDriver(), 15);

        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locatortype(type, path)));

        return element;

    }

    /*
    Method used to clear login data for PayRange app
     */
    public static void clearIOSSession() {

        click(IPhoneConstants.PayRange.hamburgerMenu, IPhoneConstants.Common.type_accessibilityId, "");
        click(IPhoneConstants.PayRange.menuOptionSettings, IPhoneConstants.Common.type_name, "");
        click(IPhoneConstants.PayRange.lblClearKeyChain, IPhoneConstants.Common.type_predicateString, "");


    }

    /**
     * Performs swipe inside an element
     *
     * @param el  the element to swipe
     * @param dir the direction of swipe
     * @version java-client: 7.3.0
     **/
    public static void swipeElementAndroid(MobileElement el, Direction dir) {
        System.out.println("swipeElementAndroid(): dir: '" + dir + "'"); // always log your actions

        // Animation default time:
        //  - Android: 300 ms
        //  - iOS: 200 ms
        // final value depends on your app and could be greater
        final int ANIMATION_TIME = 300; // ms

        final int PRESS_TIME = 600; // ms

        int edgeBorder;
        PointOption pointOptionStart, pointOptionEnd;

        // init screen variables
        Rectangle rect = el.getRect();
        // sometimes it is needed to configure edgeBorders
        // you can also improve borders to have vertical/horizontal
        // or left/right/up/down border variables
        edgeBorder = 0;

        switch (dir) {
            case DOWN: // from up to down
                pointOptionStart = PointOption.point(rect.x + rect.width / 2,
                        rect.y + edgeBorder);
                pointOptionEnd = PointOption.point(rect.x + rect.width / 2,
                        rect.y + rect.height - edgeBorder);
                break;
            case UP: // from down to up
                pointOptionStart = PointOption.point(rect.x + rect.width / 2,
                        rect.y + rect.height - edgeBorder);
                pointOptionEnd = PointOption.point(rect.x + rect.width / 2,
                        rect.y + edgeBorder);
                break;
            case LEFT: // from right to left
                pointOptionStart = PointOption.point(rect.x + rect.width - edgeBorder,
                        rect.y + rect.height / 2);
                pointOptionEnd = PointOption.point(rect.x + edgeBorder,
                        rect.y + rect.height / 2);
                break;
            case RIGHT: // from left to right
                pointOptionStart = PointOption.point(rect.x + edgeBorder + 30,
                        rect.y + rect.height / 2);

                pointOptionEnd = PointOption.point(rect.x + rect.width - edgeBorder,
                        rect.y + rect.height / 2);
                break;
            default:
                throw new IllegalArgumentException("swipeElementAndroid(): dir: '" + dir + "' NOT supported");
        }

        // execute swipe using TouchAction
        try {
            new TouchAction(GlobalUtil.mdriver)
                    .press(pointOptionStart)
                    // a bit more reliable when we add small wait
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME)))
                    .moveTo(pointOptionEnd)
                    .release().perform();
        } catch (Exception e) {
            System.err.println("swipeElementAndroid(): TouchAction FAILED\n" + e.getMessage());
            return;
        }

        // always allow swipe action to complete
        try {
            Thread.sleep(ANIMATION_TIME);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    public static void swipeElementIOS(MobileElement el, Direction dir) {
        System.out.println("swipeElementIOS(): dir: '" + dir + "'"); // always log your actions

        // Animation default time:
        //  - Android: 300 ms
        //  - iOS: 200 ms
        // final value depends on your app and could be greater
        final int ANIMATION_TIME = 200; // ms

        final int PRESS_TIME = 500; // ms

        // init screen variables
        Dimension dims = GlobalUtil.mdriver.manage().window().getSize();
        Rectangle rect = el.getRect();

        // check element overlaps screen
        if (rect.x >= dims.width || rect.x + rect.width <= 0
                || rect.y >= dims.height || rect.y + rect.height <= 0) {
            throw new IllegalArgumentException("swipeElementIOS(): Element outside screen");
        }

        // init borders per your app screen
        // or make them configurable with function variables
        int leftBorder, rightBorder, upBorder, downBorder;
        leftBorder = 0;
        rightBorder = 0;
        upBorder = 0;
        downBorder = 0;

        // find rect that overlap screen
        if (rect.x < 0) {
            rect.width = rect.width + rect.x;
            rect.x = 0;
        }
        if (rect.y < 0) {
            rect.height = rect.height + rect.y;
            rect.y = 0;
        }
        if (rect.width > dims.width)
            rect.width = dims.width;
        if (rect.height > dims.height)
            rect.height = dims.height;

        PointOption pointOptionStart, pointOptionEnd;
        switch (dir) {
            case DOWN: // from up to down
                pointOptionStart = PointOption.point(rect.x + rect.width / 2,
                        rect.y + upBorder);
                pointOptionEnd = PointOption.point(rect.x + rect.width / 2,
                        rect.y + rect.height - downBorder);
                break;
            case UP: // from down to up
                pointOptionStart = PointOption.point(rect.x + rect.width / 2,
                        rect.y + rect.height - downBorder);
                pointOptionEnd = PointOption.point(rect.x + rect.width / 2,
                        rect.y + upBorder);
                break;
            case LEFT: // from right to left
                pointOptionStart = PointOption.point(rect.x + rect.width - rightBorder,
                        rect.y + rect.height / 2);
                pointOptionEnd = PointOption.point(rect.x + leftBorder,
                        rect.y + rect.height / 2);
                break;
            case RIGHT: // from left to right
                pointOptionStart = PointOption.point(rect.x + leftBorder,
                        rect.y + rect.height / 2);
                pointOptionEnd = PointOption.point(rect.x + rect.width - rightBorder,
                        rect.y + rect.height / 2);
                break;
            default:
                throw new IllegalArgumentException("swipeElementIOS(): dir: '" + dir + "' NOT supported");
        }

        // execute swipe using TouchAction
        try {
            new TouchAction(GlobalUtil.mdriver)
                    .press(pointOptionStart)
                    // a bit more reliable when we add small wait
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME)))
                    .moveTo(pointOptionEnd)
                    .release().perform();
        } catch (Exception e) {
            System.err.println("swipeElementIOS(): TouchAction FAILED\n" + e.getMessage());
            return;
        }

        // always allow swipe action to complete
        try {
            Thread.sleep(ANIMATION_TIME);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    public static void switchToWebView() {
        GlobalUtil.mdriver.context("WEBVIEW_com.payrange.payrange");
    }

    public static void switchToNativeView() {
        GlobalUtil.mdriver.context("NATIVE_APP");
    }

    public static boolean mobileClick(String path, String type, String logstep) {
        executionDelay(1000);
        MobileElement element = GlobalUtil.mdriver.findElement(locatortype(type, path));
        element.click(); //
        LogUtil.infoLog(thisClass, "Click on element " + path + " successfully");
        if (logstep.length() > 0)
            MyTestRunner.logger.log(LogStatus.PASS, logstep);
        return true;
    }

    public static void selectDropDownValue(String path, String type, String logstep) {

        GlobalUtil.mdriver.context("WEBVIEW_com.payrange.payrange");
        executionDelay(1000);
        GlobalUtil.mdriver.findElement(locatortype(type, path)).click();
        LogUtil.infoLog(thisClass, "Click on element " + path + " successfully");
        GlobalUtil.mdriver.context("NATIVE_APP");
        executionDelay(1000);
        if (logstep.length() > 0)
            MyTestRunner.logger.log(LogStatus.PASS, logstep);
    }

    public static boolean click(String path, String type, String logstep) {
        executionDelay(1000);
        // WebDriverWait wait = new  WebDriverWait(GlobalUtil.mdriver, 20);
        WebDriverWait wait = new WebDriverWait(GlobalUtil.getMDriver(), 20);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locatortype(type, path)));
        element.click(); //
        LogUtil.infoLog(thisClass, "Click on element " + path + " successfully");
        if (logstep.length() > 0)
            MyTestRunner.logger.log(LogStatus.PASS, logstep);
        return true;
    }


    public static boolean clickOnSuspectedElement(String path, String type, String logstep) {
        try {
            GlobalUtil.mdriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            WebElement element = GlobalUtil.mdriver.findElement(locatortype(type, path));
            element.click(); //
            LogUtil.infoLog(thisClass, "Click on element " + path + " successfully");
            if (logstep.length() > 0)
                MyTestRunner.logger.log(LogStatus.PASS, logstep);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            GlobalUtil.mdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        }
    }

    public static boolean clickOnLessSuspectedElement(String path, String type, String logstep, long duration) {
        try {
            GlobalUtil.mdriver.manage().timeouts().implicitlyWait(duration, TimeUnit.SECONDS);
            WebElement element = GlobalUtil.mdriver.findElement(locatortype(type, path));
            element.click(); //
            LogUtil.infoLog(thisClass, "Click on element " + path + " successfully");
            if (logstep.length() > 0)
                MyTestRunner.logger.log(LogStatus.PASS, logstep);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            GlobalUtil.mdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        }
    }

    public static void scrollDown(AppiumDriver driver, int i, float dimensionBottomHeightFactor, float dimensionTopHeightFactor, float dimensionWidthFactor) {
        Dimension dimension = driver.manage().window().getSize();
        for (int j = 0; j <= i; j++) {
            int start = (int) (dimension.getHeight() * dimensionBottomHeightFactor);
            int end = (int) (dimension.getHeight() * dimensionTopHeightFactor);
            int x = (int) (dimension.getWidth() * dimensionWidthFactor);
            new TouchAction<>(driver).longPress(PointOption.point(x, start)).moveTo(PointOption.point(x, end)).release().perform();
        }
    }

    public static AndroidElement getElementByUiSelectorContainsText(String text) {

        return (AndroidElement) GlobalUtil.getMDriver().findElement(MobileBy.AndroidUIAutomator(
                "new UiSelector().textContains(" + "\"" + text + "\")"));
    }

    public static AndroidElement getElementByUiSelectorContainsText(String text, String instance) {

        return (AndroidElement) GlobalUtil.getMDriver().findElement(MobileBy.AndroidUIAutomator(
                "new UiSelector().textContains(" + "\"" + text + "\").instance(" + instance + ")"));
    }

    public static AndroidElement getElementByUiSelectorByClass(String text, String instance) {

        return (AndroidElement) GlobalUtil.getMDriver().findElement(MobileBy.AndroidUIAutomator(
                "new UiSelector().className(" + "\"" + text + "\").instance(" + instance + ")"));
    }

    public static List<MobileElement> getElementsList(String path, String type, String logStep) {
        List<MobileElement> elements = (List<MobileElement>) GlobalUtil.getMDriver()
                .findElements(locatortype(type, path));
        if (logStep.length() > 0)
            MyTestRunner.logger.log(LogStatus.PASS, logStep);
        return elements;

    }

    public static List<MobileElement> getSuspectedElementsList(String path, String type, String logStep) {
        GlobalUtil.mdriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        List<MobileElement> elements = (List<MobileElement>) GlobalUtil.getMDriver()
                .findElements(locatortype(type, path));
        if (logStep.length() > 0)
            MyTestRunner.logger.log(LogStatus.PASS, logStep);
        GlobalUtil.mdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        return elements;

    }

    public static MobileElement getElement(String path, String type, String logStep) {
        WebDriverWait wait = new WebDriverWait(GlobalUtil.mdriver, 30);
        MobileElement element = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(locatortype(type, path)));
        if (logStep.length() > 0)
            MyTestRunner.logger.log(LogStatus.PASS, logStep);
        return element;


    }


    public static MobileElement getSuspectedElement(String path, String type, String logStep) {
        GlobalUtil.mdriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        MobileElement element = null;
        try {
            element = GlobalUtil.mdriver.findElement(locatortype(type, path));
            if (logStep.length() > 0)
                MyTestRunner.logger.log(LogStatus.PASS, logStep);
        } catch (Exception e) {

        } finally {
            GlobalUtil.mdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        }

        return element;


    }


    public static void clickByAcID(String accessibilityId, String logMsg) {
        new WebDriverWait(GlobalUtil.getMDriver(), 20)
                .until(ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId(accessibilityId))).click();
        MyTestRunner.logger.log(LogStatus.PASS, logMsg);
    }

    /*
     * public static boolean tap(String path,String type) { WebElement element =
     * (explicitWaitForElement(path, type)); TouchAction action = new
     * TouchAction((IOSdriver1)driver1); action.tap(element).perform(); return true;
     * }
     */
    /*
     * public static void init(Class clazz){ if(!isInit){
     *
     * normalLogger = Logger.getLogger(clazz+",NormalLogger");
     * normalLogger.setLevel(Level.INFO);
     *
     * normalFileApp= new FileAppender(); normalFileApp.setLayout(patternLayout);
     * normalFileApp.setFile(System.getProperty("user.dir")+GetValue(
     * "logInfoFilePath"));
     *
     * normalFileApp.setImmediateFlush(true);
     * normalLogger.addAppender(normalFileApp); normalFileApp.activateOptions();
     *
     * errorLogger = Logger.getLogger(clazz+",ErrorLogger");
     * errorLogger.setLevel(Level.ERROR); errorFileApp= new FileAppender();
     * errorFileApp.setLayout(patternLayout);
     * errorFileApp.setFile(System.getProperty("user.dir")+GetValue(
     * "logErrorFilePath"));
     *
     * errorFileApp.setImmediateFlush(true); errorLogger.addAppender(errorFileApp);
     * errorFileApp.activateOptions();
     *
     *
     * conApp = new ConsoleAppender(); conApp.setLayout(consolePatternLayout);
     * conApp.setTarget("System.out"); conApp.activateOptions();
     *
     * normalLogger.addAppender(conApp);
     *
     *
     * isInit=true; } }
     */
    /*
     * public static void infoLog(Class clazz,String message){ init(clazz);
     * normalLogger.info(message);
     *
     *
     *
     * }
     */
    public static boolean click(By locator, String logStep) {

        WebDriverWait wait = new WebDriverWait(GlobalUtil.getMDriver(), 20);
        WebElement elm = wait.until(ExpectedConditions.elementToBeClickable(locator));

        KeywordUtil.lastAction = "Click: " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        // WebElement elm = (locator);
        if (elm == null) {
            return false;
        } else {
            // ((JavascriptExecutor)
            // GlobalUtil.getDriver()).executeScript("arguments[0].scrollIntoView();", elm);
            elm.click();
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            return true;
        }
    }

    public static void stepInfo(String stepName) {
        test.log(LogStatus.PASS, stepName);
        testHist.log(LogStatus.PASS, stepName);

    }
    /*
     * public static void executeStep(Boolean check, Class className, String
     * logstep) {
     *
     * try {
     *
     * if (check) { infoLog(className, logstep + "-PASS "); stepInfo(logstep +
     * "-PASS"); } else { infoLog(className, logstep + "-FAIL "); stepInfo(logstep +
     * "-FAIL"); } }catch(Exception e) { throw e; } }
     */

    public static void executionDelay(long time) {

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public boolean verifyAllVauesOfDropDown(String path, String type, String data) {

        boolean flag = false;
        WebElement element = explicitWaitForElement(path, type);
        List<WebElement> options = element.findElements(By.tagName("option"));
        String temp = data;
        String allElements[] = temp.split(",");
        String actual;
        for (int i = 0; i < allElements.length; i++) {

            System.out.println("Actual : " + options.get(i).getText());

            System.out.println("Expected: " + allElements[i].trim());
            actual = options.get(i).getText().trim();
            if (actual.equals(allElements[i].trim())) {
                flag = true;
            } else {
                flag = false;
                break;
            }
        }

        return flag;

    }

    public boolean verifyCurrentDateInput(String path, String type) {
        boolean flag = false;
        WebElement element = explicitWaitForElement(path, type);
        String actual = element.getAttribute("value").trim();
        DateFormat DtFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        DtFormat.setTimeZone(TimeZone.getTimeZone("US/Central"));
        String expected = DtFormat.format(date).toString().trim();
        if (actual.trim().contains(expected)) {
            flag = true;

        }
        return flag;

    }

    public static Boolean validateNotesInput(String path, String type, String errorMessage) {
        Boolean flag = false;

        WebElement element = explicitWaitForElement(path, type);
        String pattern[] = {"<", ">", "(", ")", "'", "\\"};
        for (int i = 0; i < pattern.length; i++) {
            element.clear();
            element.sendKeys(pattern[i]);
            flag = isWebElementPresent(errorMessage, type);

            if (!flag) {
                break;
            }

        }

        return flag;

    }

    public boolean selectList(final String path, String type, String data, String logstep) {

        Boolean flag = false;

        WebElement select = explicitWaitForElement(path, type);

        List<WebElement> options = select.findElements(By.tagName("option"));
        String expected = data.trim();
        System.out.println("Expected: " + expected);
        for (WebElement option : options) {

            String actual = option.getText().trim();
            System.out.println("Actual: " + actual);
            if (actual.equals(expected)) {

                option.click();
                MyTestRunner.logger.log(LogStatus.PASS, logstep);

                flag = true;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return flag;
            }
        }

        return flag;
    }

    public boolean verifyDropdownSelectedValue(String path, String type, String data) {

        Boolean flag = false;
        WebElement select = explicitWaitForElement(path, type);

        Select sel = new Select(select);
        String defSelectedVal = sel.getFirstSelectedOption().getText();

        if (defSelectedVal.trim().equals(data.trim())) {
            flag = true;

            return flag;
        } else {
            return flag;
        }
    }

    public static boolean verifyElementSize(String path, String type, int size) {

        @SuppressWarnings("unchecked")
        List<AndroidElement> elements = (List<AndroidElement>) GlobalUtil.getMDriver()
                .findElements(locatortype(type, path));

        if (elements.size() == size) {
            System.out.println("Element is Present " + size + "times");
            return true;
        } else {
            System.out.println("Element is not Present with required size");

            return false;
        }
    }

    public static boolean uploadFilesUsingSendKeys(String path, String type, String data) throws InterruptedException {
        WebElement element = GlobalUtil.getMDriver().findElement(locatortype(type, path));
        element.clear();
        element.sendKeys(System.getProperty("user.dir") + "\\src\\test\\resources\\uploadFiles\\" + data);
        System.out.println("Value Entered");
        return true;

    }

    public static boolean writeInInputCharByChar(String path, String type, String data, String logstep)
            throws InterruptedException {
        WebElement element = GlobalUtil.getMDriver().findElement(locatortype(type, path));
        element.clear();
        String b[] = data.split("");

        for (int i = 0; i < b.length; i++) {

            element.sendKeys(b[i]);
            Thread.sleep(1000);

        }
        System.out.println("Value Entered");
        MyTestRunner.logger.log(LogStatus.PASS, logstep);

        return true;

    }

    public static boolean isRadioSelected(String path, String type) {

        WebElement element = (explicitWaitForElement(path, type));
        if (element.isSelected()) {
            return true;
        } else {

            return false;
        }
    }

    public static boolean isRadioNotSelected(String path, String type) {

        WebElement element = (explicitWaitForElement(path, type));
        if (element.isSelected()) {
            return false;
        } else {

            return true;
        }
    }

    public static void stepPass(String stepName) {
        String html = "<span style='color:green'><b>" + stepName + "-PASS</b></span>";
        test.log(LogStatus.PASS, html);
        testHist.log(LogStatus.PASS, html);

    }

    public static void stepFail(String stepName) {
        String html = "<span style='color:red'><b>" + stepName + "-FAIL</b></span>";
        test.log(LogStatus.INFO, html);
        testHist.log(LogStatus.INFO, html);

    }
    /*
     * public static void logResult(boolean status, String logStep ){ if (status) {
     * infoLog(MobileKeywords.class, logStep + "-PASS "); stepPass(logStep); } else
     * { infoLog(MobileKeywords.class, logStep + "-FAIL "); stepFail(logStep);
     *
     * }
     *
     *
     * }
     */

    /*
     * public static boolean selectByIndex(String path, String type, Integer index){
     *
     * boolean status = false; boolean elemPresent = isWebElementPresent(path,
     * type); if(elemPresent){ WebElement elem =
     * driver1.findElement(locatortype(type, path)); Select select = new
     * Select(elem); select.selectByIndex(index); status = true; logResult(status,
     * "Select action is performed !!!" ); return status; }else{ status = false;
     * logResult(status, "No Select action performed !!!" ); return status; }
     *
     * }
     */

    public static boolean clearInput(String path, String type) {
        WebElement element = GlobalUtil.getMDriver().findElement(locatortype(type, path));

        element.clear();

        System.out.println("input field cleared");
        return true;

    }

    public boolean delDirectory() {
        File delDestination = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\downloadFile");
        if (delDestination.exists()) {
            File[] files = delDestination.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    delDirectory();
                } else {
                    files[i].delete();
                }
            }
        }
        return (delDestination.delete());
    }

    public boolean verifyCssProperty(String path, String type, String data) {

        String property[] = data.split(":", 2);
        String exp_prop = property[0];
        String exp_value = property[1];
        boolean flag = false;
        String prop = (explicitWaitForElement(path, type)).getCssValue(exp_prop);
        System.out.println(prop);

        if (prop.trim().equals(exp_value.trim())) {
            flag = true;
            return flag;
        } else {
            return flag;

        }

    }

    public static boolean switchContext() {
        boolean colFlag1 = false;
        // driver1= GlobalUtil.getMDriver();
        Set<String> contextNames = GlobalUtil.getMDriver().getContextHandles();
        for (String contextName : contextNames) {
            if (contextName.contains("cloud")) {
                GlobalUtil.getMDriver().context(contextName);
                System.out.println(GlobalUtil.getMDriver().context(contextName));
                Set<String> contextNamesa = GlobalUtil.getMDriver().getContextHandles();
                System.out.println(contextNamesa);
                // System.out.println("switched to webview");
                colFlag1 = true;
                break;
            }

        }
        return colFlag1;

    }

    public static boolean switchContext1() {
        boolean colFlag1 = false;
        Set<String> contextNames = GlobalUtil.getMDriver().getContextHandles();
        for (String contextName : contextNames) {
            if (contextName.contains("NATIVE")) {
                GlobalUtil.getMDriver().context(contextName);
                // System.out.println("switched to native");
                colFlag1 = true;
                break;
            }

        }
        return colFlag1;

    }

    public static boolean changeContext(String data) {
        while (!flag1) {
            try {
                if (data.contains("WEBVIEW")) {
                    flag1 = switchContext();
                } else {
                    flag1 = switchContext1();
                }
            } catch (Exception e) {
                if (e.getMessage().contains("NoSuchContextException")) {
                    flag1 = false;
                }
            }

        }
        flag1 = false;
        return true;
    }

    public static String GetTextOfElement(By value) {

        WebElement element = GlobalUtil.getMDriver().findElement(value);

        return element.getText();
    }

    public static String verifyCurrentDate() {

        DateFormat DtFormat = new SimpleDateFormat("MMM dd yyyy");
        Date date = new Date();
        DtFormat.setTimeZone(TimeZone.getTimeZone("BST"));
        String expected = DtFormat.format(date).toString().trim();
        return expected;
    }

    public static String currentdateWithDay() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date1 = new Date();
        String expected = dateFormat.format(date1).toString().trim();

        String[] dateArray = expected.split("/");

        int year = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1]);
        int day = Integer.parseInt(dateArray[2]);

        String dateString = String.format("%d-%d-%d", year, month, day);
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);

        return dayOfWeek;
    }

    public static String TimesheetDayAspercloudfm(String actual) {
        String DayaccordingtoCloudFm;
        String[] date = actual.split(" ");

        if (date[2].contains("th") || date[2].contains("st") || date[2].contains("nd") || date[2].contains("rd")) {
            if (date[2].length() == 3) {
                String str = date[2];
                date[2] = 0 + str.substring(0, 1);
                // System.out.println(date[2]);
            }
            if (date[2].length() == 4) {
                String str = date[2];
                date[2] = str.substring(0, 2);
                // System.out.println(date[2]);
            }
            if (date[0].length() > 3) {
                String str = date[0];
                date[0] = str.substring(0, 3);
                // System.out.println(date[2]);
                date[3] = date[3].substring(0, 4);
            }
        }
        DayaccordingtoCloudFm = date[0] + " " + date[1] + " " + date[2] + " " + date[3];
        return DayaccordingtoCloudFm;
    }

    public static String TimesheetPage1() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println(expecteddate);
        return expecteddate;
    }

    public static String TimesheetPage2() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println(expecteddate);
        return expecteddate;
    }

    public static String TimesheetPage3() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println(expecteddate);
        return expecteddate;
    }

    public static String TimesheetPage4() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println(expecteddate);
        return expecteddate;
    }

    public static String TimesheetPage5() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println(expecteddate);
        return expecteddate;
    }

    public static String TimesheetPage6() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println(expecteddate);
        return expecteddate;
    }

    public static String TimesheetPage7() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        int dt = (Integer.parseInt(expecteddateArray[2])) + 01;
        expecteddateArray[2] = Integer.toString(dt);
        if (expecteddateArray[2].length() <= 1) {
            expecteddateArray[2] = "0" + expecteddateArray[2];
        }
        expecteddateArray[0] = "Sun";
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println(expecteddate);
        return expecteddate;
    }

    // ******************************************************************New
    // Implimention**********************************************************************
    public String GetCurrentDate(String path, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d YYYY");
        Date d = new Date();
        String Currentdate = sdf.format(d);
        return Currentdate;
    }

    public static String Gettext(String path, String type) throws InterruptedException {
        Thread.sleep(1500);
        WebElement element = GlobalUtil.getMDriver().findElement(locatortype(type, path));
        String s = element.getText();
        System.out.println("Text has copyed in clipboard");
        return s;
    }

    public static String GetAttribute(String path, String type) throws InterruptedException {
        Thread.sleep(1500);
        WebElement element = GlobalUtil.getMDriver().findElement(locatortype(type, path));
        String s = element.getAttribute("name");
        // System.out.println("Text has copyed in clipboard: "+s);
        return s;
    }

    public static String GetMondayOfCurrentWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println("Current Monday of the week: " + expecteddate);
        return expecteddate;
    }

    public static String GetTuesdayOfCurrentWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println("Current Tuesday of the week: " + expecteddate);
        return expecteddate;
    }

    public static String GetWednesdayOfCurrentWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println("Current Wednesday of the week: " + expecteddate);
        return expecteddate;
    }

    public static String GetThursdayOfCurrentWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println("Current Thrusday of the week: " + expecteddate);
        return expecteddate;
    }

    public static String GetFridayOfCurrentWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println("Current Friday date of the week: " + expecteddate);
        return expecteddate;
    }

    public static String GetSaturdayOfCurrentWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println("Current Saturday of the week: " + expecteddate);
        return expecteddate;
    }

    public static String GetSundayOfCurrentWeek() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        String expecteddate = c.getTime().toString().trim();
        String[] expecteddateArray = expecteddate.split(" ");
        expecteddate = expecteddateArray[0] + " " + expecteddateArray[1] + " " + expecteddateArray[2] + " "
                + expecteddateArray[5];
        System.out.println("Current Sunday of the week: " + expecteddate);
        return expecteddate;
    }

    public static void verifyPastDate(String pastdate) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        String currentDate = verifyCurrentDate();
        System.out.println(currentDate);
        // Date date = format.parse(currentDate);
        // c1.setTime(currentDate.);
        // c2.setTime(someOtherDate);

        c1.get(Calendar.YEAR);
        c2.get(Calendar.YEAR);
        c1.get(Calendar.MONTH);
        c2.get(Calendar.MONTH);
        c1.get(Calendar.DAY_OF_MONTH);
        c2.get(Calendar.DAY_OF_MONTH);
    }

    /*
     * public static void verticalSwipeDown() { executionDelay(900); Dimension dim =
     * GlobalUtil.getMDriver().manage().window().getSize(); int height =
     * dim.getHeight(); int width = dim.getWidth(); int x = width / 2; int starty =
     * (int) (height * 0.60); int endy = (int) (height * 0.20);
     * GlobalUtil.getMDriver().swipe(x, starty, x, endy, 750);
     *
     * }
     */


    public static void scrollDown() throws TimeoutException, InterruptedException {

        for (int i = 1; i <= 3; i++) {
            scrollscroll();
        }

        int retry = 0;
        while (retry <= 2) {
            try {

                WebElement we = GlobalUtil.mdriver.findElement(By.xpath("//*[@resource-id='add-to-cart-button']"));
                we.click();
                break;
            } catch (org.openqa.selenium.NoSuchElementException e) {
                scrollscroll();
                retry++;
            }
        }

        //Below Code is Working
        //for(int i=1; i<=5; i++)
        //scrollscroll();
        //WebElement we = GlobalUtil.mdriver.findElement(By.xpath("//*[@resource-id='add-to-cart-button']"));
        //we.click();


        //MyTestRunner.logger.log(LogStatus.PASS, "Selected Product Added to the cart");

      /*    for(int i=1; i<=4; i++)
           scrollscroll();
          // click(AndriodConstants.Amazon.add_to_cart_btn3, AndriodConstants.Common.type_xpath,"Click on Add to Cart button");
           GlobalUtil.mdriver.findElement(By.xpath("//*[@resource-id='add-to-cart-button']")).click();
               //boolean isElementDisplayed = false;
             //isElementDisplayed = GlobalUtil.mdriver.findElement(By.xpath("//*[@resource-id='add-to-cart-button']")).isDisplayed();
             /* int m = GlobalUtil.mdriver.findElements(By.xpath("//*[@resource-id='add-to-cart-button']")).size();

                if(m>0)
                {
                //GlobalUtil.mdriver.findElement(By.xpath("//*[@resource-id='add-to-cart-button']")).click();
                click(AndriodConstants.Amazon.add_to_cart_btn3, AndriodConstants.Common.type_xpath,"Click on Add to Cart button");
                //int m = GlobalUtil.mdriver.findElements(By.xpath("//*[@resource-id='add-to-cart-button']")).size();
                //if (m > 0)	{
                 }
                   else
                     {
                       //int k = GlobalUtil.mdriver.findElements(MobileBy.AccessibilityId("Add to Cart Add to Shopping Cart")).size();
                       Dimension dim1 = GlobalUtil.mdriver.manage().window().getSize();
                       int height1 =  dim1.getHeight();
                       int width1 = dim1.getWidth();
                       int x1 = width1/2;
                       int top_y1 = (int)(height1*0.80);
                       int bottom_y1 = (int)(height1*0.20);
                       //System.out.println("coordinates :" + x + "  "+ top_y + " "+ bottom_y);
                       TouchAction ts1 = new	  TouchAction(GlobalUtil.mdriver);
                       ts1.longPress(PointOption.point(x1,top_y1)).moveTo(PointOption.point(x1, bottom_y1)).release().perform();
                       GlobalUtil.mdriver.findElement(By.xpath("//*[@resource-id='add-to-cart-button']")).click();
                      // GlobalUtil.mdriver.findElement(MobileBy.AccessibilityId("Add to Cart Add to Shopping Cart")).click();
                       // break;
                     } */

    }


    @SuppressWarnings("rawtypes")
    public static void swipeHorizontal(double startPercentage, double finalPercentage, int duration) {
        Dimension size = GlobalUtil.mdriver.manage().window().getSize();
        int anchor = (int) (size.height * 0.5);
        int startPoint = (int) (size.width * startPercentage);
        int endPoint = (int) (size.width * finalPercentage);
        new TouchAction(GlobalUtil.mdriver).press(PointOption.point(startPoint, anchor))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
                .moveTo(PointOption.point(endPoint, anchor)).release().perform();
    }

    // Call these methods for the below purposes:

    // For right to left:
    // swipeHorizontal(0.9,0.01,0.5,3000);
    //
    // For left to right:
    // swipeHorizontal(0.01,0.9,0.5,3000);

    //
    // For scroll up: swipeVertical((0.9,0.1,0.5,3000);
    //
    // For scroll down: swipeVertical(0.1,0.9,0.5,3000);

    @SuppressWarnings("rawtypes")
    public static void swipeVertical(double startPercentage, double finalPercentage, int duration) {
        Dimension size = GlobalUtil.mdriver.manage().window().getSize();
        int anchor = (int) (size.width * 0.5);
        int startPoint = (int) (size.height * startPercentage);
        int endPoint = (int) (size.height * finalPercentage);
        new TouchAction(GlobalUtil.mdriver).press(PointOption.point(anchor, startPoint))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
                .moveTo(PointOption.point(anchor, endPoint)).release().perform();
    }

    public static void verticalSwipeUp() {
        int duration = 750;
        double startPercentage = 0.20;
        double finalPercentage = 0.60;
        swipeVertical(startPercentage, finalPercentage, duration);
    }

    public static void verticalSwipeDown() {
        int duration = 750;
        double startPercentage = 0.60;
        double finalPercentage = 0.20;
        swipeVertical(startPercentage, finalPercentage, duration);
    }

    /*
     * public static void swipeVertical(double startPercentage, double
     * finalPercentage) { try { Thread.sleep(2000); } catch (InterruptedException e)
     * { // TODO Auto-generated catch block e.printStackTrace(); } Dimension size =
     * GlobalUtil.getMDriver().manage().window().getSize(); int anchor = (int)
     * (size.width * 0.5); int startPoint = (int) (size.height * startPercentage);
     * int endPoint = (int) (size.height * finalPercentage); new TouchAction(
     * GlobalUtil.getMDriver()).press((WebElement) PointOption.point(anchor,
     * startPoint)).moveTo((WebElement) PointOption.point(anchor,
     * endPoint)).release().perform(); }
     */

    public static void scrollInMobile() throws IOException, InterruptedException {

        Dimension dim = GlobalUtil.getMDriver().manage().window().getSize();
        int height = dim.getHeight();
        int width = dim.getWidth();
        int x = width / 2;
        int top_y = (int) (height * 0.80);
        int bottom_y = (int) (height * 0.20);
        System.out.println("coordinates :" + x + "  " + top_y + " " + bottom_y);
        TouchAction ts = new TouchAction(GlobalUtil.mdriver);
        ts.longPress(PointOption.point(x, top_y)).moveTo(PointOption.point(x, bottom_y)).release().perform();

        /*
         * for(int i=1;i<=9;i++) {
         * Runtime.getRuntime().exec("adb shell input swipe 500 1000 500 50"); if
         * (GlobalUtil.mdriver.findElements(MobileBy.
         * AccessibilityId("Add to Cart Add to Shopping Cart")).size() > 0) {
         * //System.out.println("FOUND"); //GlobalUtil.mdriver.findElement(By.
         * xpath("//android.widget.Button[@content-desc=\"Add to Cart Add to Shopping Cart\"]"
         * )).click(); GlobalUtil.mdriver.findElement(MobileBy.
         * AccessibilityId("Add to Cart Add to Shopping Cart")).click(); break; } }

             MyTestRunner.logger.log(LogStatus.PASS, "Selected Product Added to the cart"); */
        Runtime.getRuntime().exec("adb shell input swipe 500 1000 500 50");
        Runtime.getRuntime().exec("adb shell input swipe 500 1000 500 50");
        Runtime.getRuntime().exec("adb shell input swipe 500 1000 500 50");
        Runtime.getRuntime().exec("adb shell input swipe 500 1000 500 50");
    }


    public static void scrollAndClick(String visibleText) {
        AndroidDriver driver = (AndroidDriver) GlobalUtil.getMDriver();
        driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"" + visibleText + "\").instance(0))").click();
    }


    public static void scrollTillMobileElementDisplayed(int maxNumberOfScrolls) throws TimeoutException, InterruptedException {
        boolean isElementDisplayed = false;
        try {
            //isElementDisplayed = GlobalUtil.mdriver.findElement(locator).isDisplayed();
            isElementDisplayed = GlobalUtil.mdriver.findElement(By.xpath("//*[@resource-id='add-to-cart-button']")).isDisplayed();
            //GlobalUtil.mdriver.findElement(By.xpath("//*[@resource-id='add-to-cart-button']"));
        } catch (NoSuchElementException e) {
        }

        int i = 5;
        while (!isElementDisplayed) {
            if (i == maxNumberOfScrolls)
                break;
            Thread.sleep(100);
            verticalSwipeDown();
            Thread.sleep(100);
            try {
                isElementDisplayed = GlobalUtil.mdriver.findElement(By.xpath("//*[@resource-id='add-to-cart-button']")).isDisplayed();
            } catch (NoSuchElementException e) {
            }
            i += 1;
        }
    }

    public static void scrollscroll() throws InterruptedException {
        Dimension dim = GlobalUtil.mdriver.manage().window().getSize();
        int height = dim.getHeight();
        int width = dim.getWidth();
        System.out.println("Width " + width);
        System.out.println("Height" + height);

        int x = width / 2;
        int top_y = (int) (height * 0.80);
        //int top_y = (int)(height*.9);
        int bottom_y = (int) (height * 0.20);
        //  int bottom_y = (int)(height*.2);
        //System.out.println("coordinates :" + x + "  "+ top_y + " "+ bottom_y);
        @SuppressWarnings("rawtypes")
        TouchAction ts = new TouchAction(GlobalUtil.mdriver);
        ts.longPress(PointOption.point(x, top_y)).moveTo(PointOption.point(x, bottom_y)).release().perform();

    }

    ////// ramesh //////

    public static void scrollToMobileElement(MobileElement element, int scrollCount) {
        try {
            for (int i = 0; i < scrollCount; i++) {
                if (isElementDisplayed(element)) {
                    break;
                } else {
                    swipeUp();
                }
            }
        } catch (Exception e) {
            System.out.println("Scroll to mobile element failed");
        }
    }

    public static void swipeUp() {
        Dimension dim = GlobalUtil.getMDriver().manage().window().getSize();
        int height = dim.getHeight();
        int width = dim.getWidth();
        int x = width / 2;
        int top_y = (int) (height * 0.80);
        int bottom_y = (int) (height * 0.20);
        System.out.println("coordinates :" + x + "  " + top_y + " " + bottom_y);
        TouchAction ts = new TouchAction(GlobalUtil.getMDriver());
        ts.press(PointOption.point(x, top_y)).moveTo(PointOption.point(x, bottom_y)).release().perform();
    }


    public static boolean isElementDisplayed(MobileElement locator) {
        try {
            if (locator.isDisplayed())
                System.out.println("Element present on screen ***********" + locator);
            return true;
        } catch (NoSuchElementException e) {
            System.out.println("Element not present on screen **************" + locator);
            return false;
        }
    }


    public static void scrollToId(AndroidDriver<MobileElement> driver, String id) {

        int pressX = driver.manage().window().getSize().width / 2;
        int bottomY = driver.manage().window().getSize().height * 4 / 5;
        int topY = driver.manage().window().getSize().height / 8;

        int i = 0;

        do {

            //boolean isPresent = GlobalUtil.mdriver.findElement(By.xpath("//*[@resource-id='add-to-cart-button']")).size()>0;
            int kk = GlobalUtil.mdriver.findElements(By.xpath("//*[@resource-id='add-to-cart-button']")).size();
            if (kk > 0) {
                WebElement we = GlobalUtil.mdriver.findElement(By.xpath("//*[@resource-id='add-to-cart-button']"));
                we.click();
                break;
            } else {
                scroll(pressX, bottomY, pressX, topY);
            }
            i++;

        } while (i <= 5);
    }

    private static void scroll(int fromX, int fromY, int toX, int toY) {
        TouchAction touchAction = new TouchAction(GlobalUtil.mdriver);
        // touchAction.longPress(fromX, fromY).moveTo(toX, toY).release().perform();
        touchAction.longPress(PointOption.point(fromX, fromY)).moveTo(PointOption.point(toX, toY)).release().perform();
    }

    public static void AmazonScroll() {
        try {

            Point value = null;
            value = GlobalUtil.mdriver.findElement(By.xpath("//*[@resource-id='add-to-cart-button']")).getLocation();
            int x = value.x;
            int y = value.y;
            int y1 = value.y + 450;
            Amazonswipe(x, y, x, y1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method used to scroll to IOS element with the
     * reference of text/label
     *
     * @param text label/text of the element
     */
    public static void scrollIOSElement(String text) {
        RemoteWebElement parent = (RemoteWebElement) GlobalUtil.mdriver.findElement(By.className("XCUIElementTypeTable")); //identifying the parent Table
        String parentID = parent.getId();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("element", parentID);

        // Use the predicate that provides the value of the label attribute

        scrollObject.put("predicateString", "label == '" + text + "'");
        GlobalUtil.mdriver.executeScript("mobile:scroll", scrollObject);  // scroll t
    }

    public static void scrollToIOSOption(String text) {

        RemoteWebElement element = (RemoteWebElement) GlobalUtil.mdriver.findElement(MobileBy.iOSNsPredicateString("type == 'XCUIElementTypeStaticText' AND value BEGINSWITH[c] '" + text + "'"));
        String elementID = element.getId();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("element", elementID); // Only for ‘scroll in element’
        scrollObject.put("toVisible", "not an empty string");
        GlobalUtil.mdriver.executeScript("mobile:scroll", scrollObject);

    }

    /**
     * direction can be “up”, “down”, “left”, “right”.
     *
     * @param direction
     * @param name      parent scrollable element
     */
    public static void scrollWithDirection(String name, String direction) {
        RemoteWebElement element = GlobalUtil.mdriver.findElement(By.name("banner"));
        String elementID = element.getId();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
//        scrollObject.put("element", elementID); // Only for ‘scroll in element’
        scrollObject.put("direction", direction);
        GlobalUtil.mdriver.executeScript("mobile:scroll", scrollObject);
    }

    public static void Amazonswipe(int fromX, int fromY, int toX, int toY) {

        TouchAction action = new TouchAction(GlobalUtil.mdriver);
        action.press(PointOption.point(fromX, fromY))
                .waitAction(new WaitOptions().withDuration(Duration.ofMillis(3000))) //you can change wait durations as per your requirement
                .moveTo(PointOption.point(toX, toY))
                .release()
                .perform();
    }

    public static boolean click(String path, String type) {
        executionDelay(1500);
        WebElement element = GlobalUtil.getMDriver().findElement(locatortype(type, path));
        element.click();
        return true;
    }

    /*public static Boolean swipeVerticalBottom() throws InterruptedException {
        Boolean flag = false;

        size = GlobalUtil.getMDriver().manage().window().getSize();
        int starty = (int) (size.height * 0.50);
        int endy = (int) (size.height * 0.10);
        int startx = size.width / 2;
         ((Object) GlobalUtil.getMDriver()).swipe(startx, starty, startx, endy, 3000);
        Thread.sleep(2000);
        flag = true;

        if (flag)
            return true;
        else
            return false;
    }*/
    public static void AmazonscrollNClick() {

    }

    public static void AAscrollDown() {
        Dimension dimension = GlobalUtil.mdriver.manage().window().getSize();
        int scrollStart = (int) (dimension.getHeight() * 0.5);
        int scrollEnd = (int) (dimension.getHeight() * 0.2);

        new TouchAction((PerformsTouchActions) GlobalUtil.mdriver)
                .press(PointOption.point(0, scrollStart))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(0, scrollEnd))
                .release().perform();
    }


    public static void scrollDropDown(String value) {
        String itemName = value;
        MobileElement element = (MobileElement) GlobalUtil.mdriver.findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().text(\"" + itemName + "\"))"));
    }

    public static void scrollToMobileElement(String value) {
        String itemName = value;
        MobileElement element = (MobileElement) GlobalUtil.mdriver.findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))" +
                        ".scrollIntoView(new UiSelector().textContains(\"" + itemName + "\"))"));
    }


}

/*
 * int pressX = GlobalUtil.getMDriver().manage().window().getSize().width / 2;
 * // int bottomY = GlobalUtil.getMDriver().manage().window().getSize().height *
 * // 4/5; int bottomY = (int)
 * (GlobalUtil.getMDriver().manage().window().getSize().height * 0.8); // int
 * topY = (int) (GlobalUtil.getMDriver().manage().window().getSize().height //
 * /8); int topY = (int)
 * (GlobalUtil.getMDriver().manage().window().getSize().height * 0.4);
 *
 * int i = 0;
 *
 * do {
 *
 * boolean isPresent; // String XpathElement =
 * AndriodConstants.Amazon.add_to_cart_btn; // isPresent = //
 * GlobalUtil.getMDriver().findElements(By.xpath(
 * "//*[@resource-id='add-to-cart-button']")).size()>0; isPresent =
 * GlobalUtil.getMDriver().findElements(By.xpath(ElementPassed)).size() > 0; if
 * (isPresent) { break; } else { TouchAction touchAction = new
 * TouchAction(GlobalUtil.getMDriver()); touchAction.press(pressX,
 * bottomY).waitAction(Duration.ofMillis(1000)).moveTo(pressX, topY)
 * .release().perform(); // touchAction.press(pressX, bottomY).moveTo(pressX,
 * topY).release().perform(); } i++;
 *
 * } while (i <= 4); }
 */

// MobileElement element = (MobileElement)
// GlobalUtil.getMDriver().findElement(By.xpath("//*[@resource-id='add-to-cart-button']"));
// MobileElement startElement = driver1.findElement(MobileBy.xpath(strtElem));
// MobileElement endElement = (MobileElement)
// GlobalUtil.getMDriver().findElement(MobileBy.xpath(endElem));
// WebElement startElement =
// GlobalUtil.getMDriver().findElementByXPath(strtElem);
// WebElement endElement = GlobalUtil.getMDriver().findElementByXPath(endElem);
/*
 * WebElement Felem = GlobalUtil.getMDriver().
 * findElementByXPath("//android.widget.Button[@text='Alexa See how it works']"
 * ); WebElement Selem = GlobalUtil.getMDriver().findElementByXPath(
 * "//*[@resource-id='add-to-cart-button']");
 *
 * int x = Felem.getLocation().getX(); int y = Felem.getLocation().getY(); int
 * x1 = Selem.getLocation().getX(); int y1 = Selem.getLocation().getY();
 * GlobalUtil.getMDriver().swipe(x1, y1, x, y, 1);
 */

// TouchAction acrtions = new TouchAction(GlobalUtil.getMDriver());
// actions.press(Felem).waitAction(Duration.ofSeconds(2)).moveTo(Selem).release().perform();

/*
 * Boolean isFoundElement; isFoundElement =
 * GlobalUtil.getMDriver().findElements(By.xpath(
 * "//*[@resource-id='add-to-cart-button']")).size()>0;
 *
 * while (isFoundElement == false){
 *
 * TouchAction scrollto = new TouchAction(GlobalUtil.getMDriver());
 * scrollto.press(Felem).waitAction(Duration.ofSeconds(2)).moveTo(Selem).release
 * ().perform();
 *
 * Dimension dim = GlobalUtil.getMDriver().manage().window().getSize(); int
 * height = dim.getHeight(); int width = dim.getWidth(); int x = height/2; int
 * starty1 = (int)(height * 0.80); int endy1 = (int)(height * 0.20);
 *
 * TouchAction scrollto = new TouchAction(GlobalUtil.getMDriver());
 *
 * scrollto.longPress() //GlobalUtil.getMDriver().longPress(x,starty1).moveTo(0,
 * endy1).release(); scrollto.perform(); //Thread.sleep(1000);
 *
 * }
 */
/*
 * Runtime.getRuntime().exec("adb shell input swipe 500 1000 500 50");
 * Runtime.getRuntime().exec("adb shell input swipe 500 1000 500 50");
 */
// TODO Auto-generated method stub

// MobileElement element = (MobileElement)
// GlobalUtil.getMDriver().findElement(By.xpath("//*[@resource-id='add-to-cart-button']"));

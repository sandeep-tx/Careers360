package utilities;

import com.google.common.base.Function;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import runner.MyTestRunner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static utilities.GlobalUtil.getDriver;

/**
 * @author TX
 */
public class KeywordUtil extends GlobalUtil {
    public static String cucumberTagName;
    private static final int DEFAULT_WAIT_SECONDS = 30;
    protected static final int FAIL = 0;
    static WebElement webElement;
    protected static String url = "";
    private static String userDir = "user.dir";
    private static String text = "";
    public static final String VALUE = "value";
    public static String lastAction = "";

    static String result_FolderName = System.getProperty("user.dir") + "\\\\ExecutionReports\\HTMLReports";


    public static void onExecutionFinish() {

        // TODO Auto-generated method stub
        // Send Mail functionality if
        LogUtil.infoLog("TestProcessEnd", "Test process has ended");

        if (GlobalUtil.getCommonSettings().getEmailOutput().equalsIgnoreCase("Y")) {
            LogUtil.infoLog("TestEmailProcessing",
                    "Email Flag Set To: " + GlobalUtil.getCommonSettings().getEmailOutput());
            try {
                sendMail.sendEmailToClient(
                        "Hi All, \n\nPlease find the attached Execution Report.\n\n\nThanks & Regards\nTesting Xperts",
                        true, false);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            LogUtil.infoLog("TestEmailProcessing",
                    "Email Flag Set To: " + GlobalUtil.getCommonSettings().getEmailOutput());
        }

        String htmlReportFile = System.getProperty("user.dir") + "/" + ConfigReader.getValue("HtmlReportFullPath");
        System.out.println("cucumber path is" + htmlReportFile);
        File f = new File(htmlReportFile);
        if (f.exists()) {

            try {
                File file = new File(htmlReportFile);
                Desktop.getDesktop().browse(file.toURI());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String htmlExtentReportFile = System.getProperty("user.dir") + "/" + ConfigReader.getValue("extentReportPath");
        // String htmlReportFile = System.getProperty("user.dir") + "\\" +
        // ConfigReader.getValue("HtmlReportFullPath");
        System.out.println("Extent Report File path is" + htmlExtentReportFile);
        File extentReport = new File(htmlExtentReportFile);
        if (extentReport.exists()) {
            try {
                File file = new File(htmlExtentReportFile);
                Desktop.getDesktop().browse(file.toURI());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] takeScreenshot(String screenshotFilePath) {
        try {
            byte[] screenshot = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
            FileOutputStream fileOuputStream = new FileOutputStream(screenshotFilePath);
            fileOuputStream.write(screenshot);
            fileOuputStream.close();
            return screenshot;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void catchAssertError(Throwable e) {
        GlobalUtil.e = e;
        GlobalUtil.errorMsg = e.getMessage();
        String[] msg = e.getMessage().split("expected");
        MyTestRunner.logger.log(LogStatus.FAIL, msg[0]);
        Assert.fail(e.getMessage());
    }

    public static String captureScreenForMobile(String screenShotName) throws IOException {

        if (System.getProperty("screen_shot") != null) {
            if (System.getProperty("screen_shot").equals("true")) {
                TakesScreenshot ts = GlobalUtil.getMDriver();
                File source = ts.getScreenshotAs(OutputType.FILE);
                String dest = ConfigReader.getValue("screenshotPath") + "\\" + screenShotName + ".png";
                File destination = new File(dest);
                FileUtils.copyFile(source, destination);
                InputStream is = new FileInputStream(destination);
                byte[] ssByte = IOUtils.toByteArray(is);
                String base64 = Base64.getEncoder().encodeToString(ssByte);
                return "data:image/png;base64," + base64;
            } else return null;
        } else return null;

    }


    public static boolean scrollingToElementofAPage(By locator, String logStep) throws InterruptedException {
        Thread.sleep(5000);
        WebElement element = getDriver().findElement(locator);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView();", element);
        // highLightElement(driver, element);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return true;
    }

    public static byte[] takeMobileScreenshot(String screenshotFilePath) {
        try {
            byte[] screenshot = ((TakesScreenshot) GlobalUtil.getMDriver()).getScreenshotAs(OutputType.BYTES);
            FileOutputStream fileOuputStream = new FileOutputStream(screenshotFilePath);
            fileOuputStream.write(screenshot);
            fileOuputStream.close();
            return screenshot;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentDateTime() {

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMddHHmmss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        System.out.println(strDate);
        return strDate;
    }

    /**
     * @param :locator
     * @return
     */
    public static void navigateToUrl(String url) {
        try {
            KeywordUtil.lastAction = "Navigate to: " + url;
            LogUtil.infoLog(KeywordUtil.class, "User is navigated to URL: " + url);
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor("User is navigated to URL: " + url));
            // getDriver().navigate().to(url);
            getDriver().get(url);
            String Pagetitle = getDriver().getTitle();
            System.out.println(Pagetitle);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static String getCurrentUrl() {
        return getDriver().getCurrentUrl();
    }

    public static WebElement waitForClickable(By locator) {
        WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
        wait.ignoring(ElementNotVisibleException.class);
        wait.ignoring(WebDriverException.class);

        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    //Added by Anand
    public static WebElement waitForClickableMobile(By locator) {
        WebDriverWait wait = new WebDriverWait(getMDriver(), DEFAULT_WAIT_SECONDS);
        wait.ignoring(ElementNotVisibleException.class);
        wait.ignoring(WebDriverException.class);

        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * @param locator
     * @return
     */
    public static WebElement waitForPresent(By locator) {
        WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
        wait.ignoring(ElementNotVisibleException.class);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * @param locator
     * @return
     */
    public static WebElement waitForVisible(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
            // wait.ignoring(ElementNotVisibleException.class);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            return null;
        }
    }

    //Added by Anand
    public static WebElement waitForVisibleMobile(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(getMDriver(), DEFAULT_WAIT_SECONDS);
            // wait.ignoring(ElementNotVisibleException.class);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean waitForInVisibile(By locator) {
        WebDriverWait wait = new WebDriverWait(getDriver(), 30);
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibleIgnoreStaleElement(By locator) {
        WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
        wait.ignoring(StaleElementReferenceException.class);
        wait.ignoring(ElementNotVisibleException.class);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * @param locator
     * @param seconds
     * @param poolingMil
     * @return
     * @throws Exception
     */
    public static WebElement findWithFluintWait(final By locator, int seconds, int poolingMil) throws Exception {
        // Because if implicit wait is set then fluint wait will not work

        getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        WebElement element = null;
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver()).withTimeout(seconds, TimeUnit.SECONDS)
                    .pollingEvery(poolingMil, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class).ignoring(ElementNotVisibleException.class)
                    .ignoring(WebDriverException.class);
            element = wait.until(new Function<WebDriver, WebElement>() {
                @Override
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(locator);
                }
            });

        } catch (Exception t) {
            throw new Exception("Timeout reached when searching for element! Time: " + seconds + " seconds " + "\n"
                    + t.getMessage());
        } finally {
            // getDriver().manage().timeouts().implicitlyWait(Utility.getIntValue("implicitlyWait"),
            // TimeUnit.SECONDS);
        }

        return element;
    }// End FindWithWait()

    /**
     * @param locator
     * @return
     * @throws Exception
     */
    public static WebElement findWithFluintWait(final By locator) throws Exception {
        getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        // Because if implict wait is set then fluint wait will not work
        KeywordUtil.lastAction = "Find Element: " + locator.toString();
        // getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        WebElement element = null;

        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(getDriver())
                    .withTimeout(DEFAULT_WAIT_SECONDS, TimeUnit.SECONDS).pollingEvery(200, TimeUnit.MILLISECONDS)
                    .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class)
                    .ignoring(ElementNotVisibleException.class);

            element = wait.until(new Function<WebDriver, WebElement>() {
                @Override
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(locator);
                }
            });

        } catch (Exception t) {
            throw new Exception("Timeout reached when searching for element! Time: " + DEFAULT_WAIT_SECONDS
                    + " seconds " + "\n" + t.getMessage());
        } finally {
            // getDriver().manage().timeouts().implicitlyWait(Utility.getIntValue("implicitlyWait"),
            // TimeUnit.SECONDS);
        }

        return element;
    }// End FindWithWait()

    public static WebElement getWebElement(By locator) throws Exception {
        KeywordUtil.lastAction = "Find Element: " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        return findWithFluintWait(locator);
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /*
     * Web driver common functions
     * ===========================================================
     */

    /**
     * @param locator
     * @return
     */
    public static boolean click(By locator, String logStep) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        WebDriverWait wait = new WebDriverWait(getDriver(), 20);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).isDisplayed();

        KeywordUtil.lastAction = "Click: " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement elm = waitForClickable(locator);
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

    //Added by Anand
    public static boolean clickMobile(By locator, String logStep) {
        WebDriverWait wait = new WebDriverWait(getMDriver(), 30);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).isDisplayed();
        wait.until(ExpectedConditions.elementToBeClickable(locator)).isEnabled();

        KeywordUtil.lastAction = "Click: " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement elm = waitForClickableMobile(locator);
        if (elm == null) {
            return false;
        } else {
            elm.click();
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            return true;
        }
    }

    // ............
    public static boolean clickCart(By locator, String logStep) {

        KeywordUtil.lastAction = "Click: " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement elm = waitForClickable(locator);
        if (elm == null) {
            return false;
        } else {

            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView();", elm);
            // ((JavascriptExecutor)
            // GlobalUtil.getDriver()).executeScript("window.scrollBy(0,1000)");
            elm.click();
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            return true;
        }
    }

    // ....

    public static boolean acceptAlert() {

        Alert alert = getDriver().switchTo().alert();
        alert.accept();
        return true;

    }

    // ......
    public static boolean switchToWindow() {

        ArrayList<String> tabs2 = new ArrayList<String>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs2.get(1));
        return true;

    }
    // ....

    /**
     * @param linkText
     * @return
     */
    public static boolean clickLink(String linkText, String logStep) {
        KeywordUtil.lastAction = "Click Link: " + linkText;
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement elm = waitForClickable(By.linkText(linkText));
        if (elm == null) {
            return false;
        } else {
            elm.click();
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            return true;
        }
    }

    /**
     * @param locator
     * @return
     */
    public static String getElementText(By locator) {
        KeywordUtil.lastAction = "Get Element text: " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement elm = waitForClickable(locator);
        return elm.getText().trim();
    }

    public static String getImageTitle(By locator) {
        WebElement elm = waitForVisible(locator);
        return elm.getAttribute("title");

    }

    /**
     * @param locator
     * @return
     */
    public static boolean isWebElementVisible(By locator, String logStep) {
        try {
            KeywordUtil.lastAction = "Check Element visible: " + locator.toString();
            LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
            WebElement elm = waitForVisible(locator);
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            return elm.isDisplayed();
        } catch (Exception e) {
            return false;
        }

    }
    public static boolean isWebElementVisible(By locator) {
        try {
            KeywordUtil.lastAction = "Check Element visible: " + locator.toString();
            LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
            WebElement elm = waitForVisible(locator);

            return elm.isDisplayed();
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isMobileElementVisible(By locator, String logStep) {
        try {
            KeywordUtil.lastAction = "Check Element visible: " + locator.toString();
            LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
            WebElement elm = waitForVisibleMobile(locator);
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            return elm.isDisplayed();
        } catch (Exception e) {
            return false;
        }

    }

    public static boolean isWebElementEnable(By locator, String logStep) {
        KeywordUtil.lastAction = "Check Element visible: " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement elm = waitForVisible(locator);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return elm.isEnabled();
    }

    /**
     * @param locator
     * @return
     */
    public static List<WebElement> getListElements(By locator, String logStep) {
        KeywordUtil.lastAction = "Get List of Elements: " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);

        try {
            findWithFluintWait(locator, 60, 300);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return getDriver().findElements(locator);

    }

    public static boolean isWebElementPresent(By locator, String logStep) {
        KeywordUtil.lastAction = "Check Element present: " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        List<WebElement> elements = getDriver().findElements(locator);
        if (elements.isEmpty()) {
            return false;
        }
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return true;
    }

    public static boolean hoverOnElement(By by) throws InterruptedException {
        Thread.sleep(1000);
        WebElement element = getDriver().findElement(by);
        Actions act = new Actions(getDriver());
        act.moveToElement(element).build().perform();

        Thread.sleep(3000);

        return true;

    }

    /**
     * @param locator
     * @return
     */
    public static boolean isWebElementNotPresent(By locator) {
        KeywordUtil.lastAction = "Check Element not present: " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        List<WebElement> elements = (new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));

        if (elements.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @param locator
     * @param data
     * @return
     */
    public static boolean inputText(By locator, String data, String logStep) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        KeywordUtil.lastAction = "Input Text: " + data + " - " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement elm = waitForVisible(locator);
        if (elm == null) {
            return false;
        } else {
            elm.clear();
            elm.sendKeys(data);
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
            return true;
        }
    }

    public static boolean inputTextMobile(By locator, String data, String logStep) {
        KeywordUtil.lastAction = "Input Text: " + data + " - " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement elm = waitForVisibleMobile(locator);
        if (elm == null) {
            return false;
        } else {
            elm.sendKeys(data);
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
            return true;
        }
    }

    public static void pressTabKey(By locator) {
        WebElement elm = waitForVisible(locator);
        elm.sendKeys(Keys.TAB);
    }

    public static void pressEnter(By locator) {
        WebElement elm = waitForVisible(locator);
        elm.sendKeys(Keys.ENTER);
    }

    public static void pressDownArrow(By locator) {
        WebElement elm = waitForVisible(locator);
        elm.sendKeys(Keys.ARROW_DOWN);
    }

    public static void pressRightArrow(By locator) {
        WebElement elm = waitForVisible(locator);
        elm.sendKeys(Keys.ARROW_RIGHT);
    }

    public static void pressControlA(By locator) {
        WebElement elm = waitForVisible(locator);
        elm.sendKeys(Keys.CONTROL+"a");
    }

    /**
     * @param locator
     * @param data
     * @return
     */
    public static boolean inputTextJS(By locator, String data, String logStep) {
        KeywordUtil.lastAction = "Input Text: " + data + " - " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement element = waitForVisible(locator);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].value = arguments[1]", element, data);
        if (element.getText().equalsIgnoreCase(data)) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            return true;
        } else
            return false;
    }

    /**
     * @param locator
     * @return
     */
    public static boolean isRadioSelected(By locator, String logStep) {
        KeywordUtil.lastAction = "Is Radio Selected: " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement element = waitForVisible(locator);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return element.isSelected();
    }

    /**
     * @param locator
     * @return
     */
    public static boolean isRadioNotSelected(By locator, String logStep) {
        KeywordUtil.lastAction = "Is Radio Not Selected: " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        boolean check = isRadioSelected(locator, logStep);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return (!check);
    }

    /**
     * @param locator
     * @return
     */
    public static boolean clearInput(By locator) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element = waitForVisible(locator);
        return element.getAttribute(VALUE).isEmpty();
    }

    /**
     * @param locator
     * @param data
     * @return
     */
    public static boolean verifyCssProperty(By locator, String data, String logStep) {
        KeywordUtil.lastAction = "Verify CSS : " + data + " - " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);

        String[] property = data.split(":", 2);
        String expProp = property[0];
        String expValue = property[1];
        boolean flag = false;
        String prop = (waitForPresent(locator)).getCssValue(expProp);
        if (prop.trim().equals(expValue.trim())) {
            flag = true;
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            return flag;
        } else {
            return flag;
        }
    }

    /**
     * @param locator
     * @param data
     * @return
     */
    public static boolean verifyInputText(By locator, String data, String logStep) {
        KeywordUtil.lastAction = "Verify Input Expected Text: " + data + " - " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement element = waitForVisible(locator);
        String actual = element.getAttribute(VALUE);
        LogUtil.infoLog(KeywordUtil.class, "Actual:" + actual);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return actual.equalsIgnoreCase(data);

    }

    /**
     * @param locator
     * @param data
     * @return
     */
    public static boolean verifyInputTextJS(By locator, String data, String logStep) {
        KeywordUtil.lastAction = "Verify Input Expected Text: " + data + " - " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement element = waitForVisible(locator);

        String message = String.format("Verified text expected \"%s\" actual \"%s\" ", data, element.getText());
        LogUtil.infoLog(KeywordUtil.class, message);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return data.equalsIgnoreCase(element.getText());
    }

    /**
     * <h1>Log results</h1>
     * <p>
     * This function will write results to the log file.
     * </p>
     *
     * @param locator
     * @param data
     * @return
     */
    /**
     * @param locator
     * @param data
     * @return
     */
    public static boolean verifyText(By locator, String data, String logStep) {
        KeywordUtil.lastAction = "Verify Expected Text: " + data + " - " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement element = waitForVisible(locator);
        String message = String.format("Verified text expected \"%s\" actual \"%s\" ", data, element.getText());
        LogUtil.infoLog(KeywordUtil.class, message);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return element.getText().equalsIgnoreCase(data);
    }

    //Added by Anand
    public static boolean verifyTextMobile(By locator, String data, String logStep) throws Exception {
        try {
            KeywordUtil.lastAction = "Verify Expected Text: " + data + " - " + locator.toString();
            LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
            WebElement element = waitForVisibleMobile(locator);
            String message = String.format("Verified text expected \"%s\" actual \"%s\" ", data, element.getText());
            LogUtil.infoLog(KeywordUtil.class, message);
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            return element.getText().equalsIgnoreCase(data);
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean verifyTextContains(By locator, String data, String logStep) {
        KeywordUtil.lastAction = "Verify Text Contains: " + data + " - " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement element = waitForVisible(locator);
        String message = new String(
                String.format("Verified text expected \"%s\" actual \"%s\" ", data, element.getText()));
        LogUtil.infoLog(KeywordUtil.class, message);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return element.getText().toUpperCase().contains(data.toUpperCase());

    }
    /**
     * Method used to zoom out the screen
     */
    public static void zoomOut()
    {
        try {
            Robot robot=new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            if(GlobalUtil.getCommonSettings().getBrowser().equalsIgnoreCase("chrome")){
            for(int i=0;i<4;i++) {

                robot.keyPress(KeyEvent.VK_SUBTRACT);
                robot.keyRelease(KeyEvent.VK_SUBTRACT);

            }} else if (GlobalUtil.getCommonSettings().getBrowser().equalsIgnoreCase("firefox")) {
                for(int i=0;i<3;i++) {

                    robot.keyPress(KeyEvent.VK_SUBTRACT);
                    robot.keyRelease(KeyEvent.VK_SUBTRACT);

                }
            }
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_SUBTRACT);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param locator
     * @return
     */
    public static boolean verifyDisplayAndEnable(By locator, String logStep) {
        KeywordUtil.lastAction = "Is Element Displayed and Enable : " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement element = waitForVisible(locator);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
        return element.isDisplayed() && element.isEnabled();
    }

    /**
     * @param :locator
     * @param :data
     * @return
     */
    public static boolean clickJS(By locator, String logStep) {
        KeywordUtil.lastAction = "Click : " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement element = waitForVisible(locator);
        Object obj = ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return obj == null;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /*
     * Handling selects ===========================================================
     */

    /**
     * @param locator
     * @param index
     * @return
     */
    public static boolean selectByIndex(By locator, int index, String logStep) {
        KeywordUtil.lastAction = "Select dropdown by index : " + index + " - " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        Select sel = new Select(getDriver().findElement(locator));
        sel.selectByIndex(index);

        // Check whether element is selected or not
        sel = new Select(getDriver().findElement(locator));
        if (sel.getFirstSelectedOption().isDisplayed()) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param locator
     * @param value
     * @return
     */
    public static boolean selectByValue(By locator, String value, String logStep) {
        KeywordUtil.lastAction = "Select dropdown by value : " + value + " - " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        Select sel = new Select(getDriver().findElement(locator));
        sel.selectByValue(value);

        // Check whether element is selected or not
        sel = new Select(getDriver().findElement(locator));
        if (sel.getFirstSelectedOption().isDisplayed()) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            return true;
        } else {
            return false;
        }
    }

    /**
     * @param locator
     * @param value
     * @return
     */
    public static boolean selectByVisibleText(By locator, String value, String logStep) {
        try {
            waitForClickable(locator);
            KeywordUtil.lastAction = "Select dropdown by text : " + value + " - " + locator.toString();
            LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
            Select sel = new Select(getDriver().findElement(locator));
            sel.selectByVisibleText(value);
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // Check whether element is selected or not
        // sel = new Select(getDriver().findElement(locator));
        // if (sel.getFirstSelectedOption().getText().equalsIgnoreCase(value)) {
        // return true;
        // } else {
        // return false;
        // }
    }

    /**
     * @param locator
     * @param data
     * @return
     * @throws Throwable
     */
    public static boolean verifyAllValuesOfDropDown(By locator, String data, String logStep) throws Throwable {
        KeywordUtil.lastAction = "Verify Dropdown all values: " + data + " - " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        boolean flag = false;
        WebElement element = findWithFluintWait(locator);
        List<WebElement> options = element.findElements(By.tagName("option"));
        String[] allElements = data.split(",");
        String actual;
        for (int i = 0; i < allElements.length; i++) {
            LogUtil.infoLog(KeywordUtil.class, options.get(i).getText());
            LogUtil.infoLog(KeywordUtil.class, allElements[i].trim());

            actual = options.get(i).getText().trim();
            if (actual.equalsIgnoreCase(allElements[i].trim())) {
                MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

                flag = true;
            } else {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * @param locator
     * @param data
     * @return
     */
    public static boolean verifyDropdownSelectedValue(By locator, String data, String logStep) {
        KeywordUtil.lastAction = "Verify Dropdown selected option: " + data + " - " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        Select sel = new Select(waitForVisible(locator));
        String defSelectedVal = sel.getFirstSelectedOption().getText();
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));
        return defSelectedVal.trim().equals(data.trim());
    }

    /**
     * @param locator
     * @param size
     * @return
     */
    public static boolean verifyElementSize(By locator, int size, String logStep) {
        KeywordUtil.lastAction = "Verify Element size: " + size + " - " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        List<WebElement> elements = getDriver().findElements(locator);
        if (elements.size() == size) {
            LogUtil.infoLog(KeywordUtil.class, "Element is Present " + size + "times");
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            return true;
        } else {
            LogUtil.infoLog(KeywordUtil.class, "Element is not Present with required size");
            LogUtil.infoLog(KeywordUtil.class, "Expected size:" + size + " but actual size: " + elements.size());
            return false;
        }
    }

    /**
     * @param locator
     * @param data
     * @return
     * @throws InterruptedException
     */
    public static boolean writeInInputCharByChar(By locator, String data, String logStep) throws InterruptedException {
        WebElement element = waitForVisible(locator);
        element.clear();
        String[] b = data.split("");
        for (int i = 0; i < b.length; i++) {
            element.sendKeys(b[i]);
            Thread.sleep(250);
        }
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return true;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    // /**
    // * @param check
    // * @param className
    // * @param logstep
    // * @throws Exception
    // */
    // public static void executeStep(Boolean check, String logstep) throws
    // Exception {
    // if (check) {
    // logStepPass(logstep);
    // } else {
    // logStepFail(logstep);
    // throw new Exception("Step failed - " + logstep);
    // }
    // }

    /**
     * @param :check
     * @param :className
     * @param :logstep
     * @throws Exception //
     */
    // public static void verifyStep(Boolean check, String logstep) throws
    // TestStepFailedException {
    // if (check) {
    // logStepPass(logstep);
    // } else {
    // logStepFail(logstep);
    // throw new TestStepFailedException("Varification failed-->" + logstep );
    // }
    // }

    // Get Tag name and locator value of Element
    public static String getElementInfo(By locator) throws Exception {
        return " Locator: " + locator.toString();
    }

    public static String getElementInfo(WebElement element) throws Exception {
        String webElementInfo = "";
        webElementInfo = webElementInfo + "Tag Name: " + element.getTagName() + ", Locator: ["
                + element.toString().substring(element.toString().indexOf("->") + 2);
        return webElementInfo;

    }

    /**
     * @param time
     * @throws InterruptedException
     */
    public static void delay(long time) throws InterruptedException {
        Thread.sleep(time);
    }

    /**
     * @param locator
     * @return
     */
    public boolean verifyCurrentDateInput(By locator, String logStep) {
        boolean flag = false;
        WebElement element = waitForVisible(locator);
        String actual = element.getAttribute(VALUE).trim();
        DateFormat dtFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        dtFormat.setTimeZone(TimeZone.getTimeZone("US/Central"));
        String expected = dtFormat.format(date).trim();
        if (actual.trim().contains(expected)) {
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            flag = true;

        }
        return flag;
    }

    /**
     * @param locator
     * @param data
     * @return
     * @throws InterruptedException
     */
    public static boolean uploadFilesUsingSendKeys(By locator, String data, String logStep)
            throws InterruptedException {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(System.getProperty(userDir) + "\\src\\test\\resources\\uploadFiles\\" + data);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return true;
    }

    // /**
    // * @param data
    // * @param page
    // * @param fileName
    // * @return
    // * @throws IOException
    // */
    // public static boolean verifyPDFData(String data, int page, String
    // fileName) throws IOException {
    // FileInputStream fis = null;
    // String dwnFile = null;
    // try {
    //
    // String dirName = System.getProperty(userDir) +
    // "\\src\\test\\resources\\downloadFile\\";
    // File dir = new File(dirName);
    // File[] path1 = dir.listFiles();
    //
    // for (int k = 0; k < path1.length; k++) {
    // dwnFile = path1[k].toString();
    // if (dwnFile.contains(fileName)) {
    // break;
    // }
    //
    // continue;
    // }
    // File file = new File(dwnFile);
    // fis = new FileInputStream(file.getAbsolutePath());
    // PdfReader text = new PdfReader(fis);
    // String expected = PdfTextExtractor.getTextFromPage(text, page);
    //
    // String[] b = data.split(",");
    // fis.close();
    // for (int i = 0; i < b.length; i++) {
    // if (expected.contains(b[i]))
    // continue;
    // }
    // } catch (Exception e) {
    // LogUtil.errorLog(KeywordUtil.class, e.getMessage(), e);
    // }
    // return true;
    // }

    /**
     * @return
     */
    public boolean delDirectory() {
        File delDestination = new File(System.getProperty(userDir) + "\\src\\test\\resources\\downloadFile");
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
        return delDestination.delete();
    }

    // public static void hoverElement(By locator) throws InterruptedException{
    // KeywordUtil.lastAction="Hover Element: "+locator.toString();
    // LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
    //
    // WebElement element = waitForClickable(locator);
    // Point p =element.getLocation();
    // Actions builder = new Actions(getDriver());
    // builder.moveToElement(element, p.getX(), p.getY()).build().perform();
    // pause(1000);
    //
    // }

    public static void openControlCenter() {
        new TouchAction(GlobalUtil.mdriver)
                .press(PointOption.point(354, 2))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(354, 240))
                .release().perform();
    }

    public static void closeControlCenter() {
        new TouchAction(GlobalUtil.mdriver)
                .press(PointOption.point(354, 240))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
                .moveTo(PointOption.point(354, 2))
                .release().perform();
    }

    public static void enableBluetoothIOS() {
        openControlCenter();
        GlobalUtil.mdriver.findElement(By.name("bluetooth-button")).click();
        closeControlCenter();

    }

    public static boolean doubleClick(By locator, String logStep) {
        boolean result = false;
        try {
            KeywordUtil.lastAction = "Double click: " + locator.toString();
            LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
            WebElement element = getDriver().findElement(locator);
            Actions action = new Actions(getDriver()).doubleClick(element);
            action.build().perform();
            MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

            result = true;

        } catch (StaleElementReferenceException e) {
            LogUtil.infoLog("DoubleClick",
                    locator.toString() + " - Element is not attached to the page document " + e.getStackTrace());
            result = false;
        } catch (NoSuchElementException e) {
            LogUtil.infoLog("DoubleClick",
                    locator.toString() + " - Element is not attached to the page document " + e.getStackTrace());
            result = false;
        } catch (Exception e) {
            LogUtil.infoLog("DoubleClick",
                    locator.toString() + " - Element is not attached to the page document " + e.getStackTrace());
            result = false;
        }
        return result;
    }

    public static boolean switchToFrame(String frameName) {

        try {
            getDriver().switchTo().frame(frameName);
            return true;

        } catch (Exception e) {
            LogUtil.infoLog("switchToFrame", frameName + " TO FRAME FAILED" + e.getStackTrace());
            return false;
        }
    }

    public static String createZipFile() throws IOException {
        result_FolderName = result_FolderName.replace("\\", "/");
        String outputFile = result_FolderName + ".zip";
        FileOutputStream fos = new FileOutputStream(outputFile);
        ZipOutputStream zos = new ZipOutputStream(fos);
        packCurrentDirectoryContents(result_FolderName, zos);
        zos.closeEntry();
        zos.close();
        fos.close();
        return outputFile;
    }

    public static void packCurrentDirectoryContents(String directoryPath, ZipOutputStream zos) throws IOException {
        for (String dirElement : new File(directoryPath).list()) {
            String dirElementPath = directoryPath + "/" + dirElement;
            if (new File(dirElementPath).isDirectory()) {
                packCurrentDirectoryContents(dirElementPath, zos);
            } else {
                ZipEntry ze = new ZipEntry(dirElementPath.replaceAll(result_FolderName + "/", ""));
                zos.putNextEntry(ze);
                FileInputStream fis = new FileInputStream(dirElementPath);
                byte[] bytesRead = new byte[512];
                int bytesNum;
                while ((bytesNum = fis.read(bytesRead)) > 0) {
                    zos.write(bytesRead, 0, bytesNum);
                }

                fis.close();
            }
        }
    }

    public static WebElement getElementByImg(String img) {
        return GlobalUtil.mdriver.findElement(MobileBy.image(DriverUtil.getImgRef(img)));
    }

    //Added by Anand
    public static String getAttributeValueMobile(By locator, String attName) {
        WebDriverWait wait = new WebDriverWait(getMDriver(), 30);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).isDisplayed();
        wait.until(ExpectedConditions.elementToBeClickable(locator)).isEnabled();

        KeywordUtil.lastAction = "Click: " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement elm = waitForClickableMobile(locator);
        if (elm == null) {
            return null;
        } else {
            return elm.getAttribute(attName);
        }
    }

    public static String getAttributeValue(By locator, String attName) {
        WebElement elm = waitForVisible(locator);

        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        return elm.getAttribute(attName);

    }

    public static void extentReportHeading(String reportName) throws ParserConfigurationException, IOException, SAXException, TransformerException, XPathExpressionException {


        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = f.newDocumentBuilder();
        Document doc = b.parse(new File(System.getProperty("user.dir") + "/src/main/resources/Config/extent-config.xml"));

        XPath xPath = XPathFactory.newInstance().newXPath();
        Node startDateNode = (Node) xPath.compile("/extentreports/configuration/reportName").evaluate(doc, XPathConstants.NODE);
        startDateNode.setTextContent(reportName);

        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty(OutputKeys.METHOD, "xml");
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource domSource = new DOMSource(doc);
        StreamResult sr = new StreamResult(new File(System.getProperty("user.dir") + "/src/main/resources/Config/extent-config.xml"));
        tf.transform(domSource, sr);

    }

    public static void takeScreenshotAndAttachInReport() {

        try {
            if (System.getProperty("screen_shot") != null && System.getProperty("screen_shot").equalsIgnoreCase("true")) {
                String imagePath, pathForLogger;
                String scFileName = "ScreenShot_" + System.currentTimeMillis();
                String screenshotFilePath = ConfigReader.getValue("screenshotsPath") + "\\" + scFileName + ".png";

                imagePath = HTMLReportUtil.testFailTakeScreenshot(screenshotFilePath);

                InputStream is = new FileInputStream(imagePath);
                byte[] imageBytes = org.apache.commons.compress.utils.IOUtils.toByteArray(is);
                Thread.sleep(2000);
                String base64 = Base64.getEncoder().encodeToString(imageBytes);
                pathForLogger = MyTestRunner.logger.addBase64ScreenShot("data:image/png;base64," + base64);
                MyTestRunner.logger.log(LogStatus.PASS,
                        HTMLReportUtil.infoStringGreenColor(pathForLogger));


            }
            else
            {
                if (ConfigReader.getValue("screenshotFlag").equalsIgnoreCase("true")) {
                    String imagePath, pathForLogger;
                    String scFileName = "ScreenShot_" + System.currentTimeMillis();
                    String screenshotFilePath = ConfigReader.getValue("screenshotsPath") + "\\" + scFileName + ".png";

                    imagePath = HTMLReportUtil.testFailTakeScreenshot(screenshotFilePath);

                    InputStream is = new FileInputStream(imagePath);
                    byte[] imageBytes = org.apache.commons.compress.utils.IOUtils.toByteArray(is);
                    Thread.sleep(2000);
                    String base64 = Base64.getEncoder().encodeToString(imageBytes);
                    pathForLogger = MyTestRunner.logger.addBase64ScreenShot("data:image/png;base64," + base64);
                    MyTestRunner.logger.log(LogStatus.PASS,
                            HTMLReportUtil.infoStringGreenColor(pathForLogger));

                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void DragAndDrop(By Source, By Destination) {
        Actions actions = new Actions(getDriver());
        WebElement sourceElem = getDriver().findElement(Source);
        WebElement targetElem = getDriver().findElement(Destination);
        actions.clickAndHold(sourceElem);
        actions.moveToElement(targetElem);
        actions.release().build().perform();
    }
    
    public static void DragAndDropFrame(By Source, By Destination, By Iframe, String Component) throws
            InterruptedException {
        WebDriver driver = GlobalUtil.getDriver();
        scrollToElementWithoutLog(Destination);
        WebElement dest = driver.findElement(Destination);
        WebElement source = driver.findElement(Source);
        Actions actions = new Actions(driver);
        videoLoop:
        for (int i = 0; i < 8; i++) {
            try {
                Action dragAndDrop = actions.clickAndHold(source)
                        .moveToElement(dest)
                        .release(dest)
                        .build();
                dragAndDrop.perform();

            } catch (Exception e) {
                driver.switchTo().frame("body_ifr");
                WebElement attachedVieoElement;

                if (Objects.equals(Component, "Poll Daddy")) {
                    attachedVieoElement = driver.findElement(By.xpath("//span[contains(text(),'poll')]"));
                } else {
                    attachedVieoElement = driver.findElement(By.xpath("//span[contains(text(),'" + Component.toLowerCase() + "')]"));
                }

                if (attachedVieoElement != null) {
                    System.out.println("element attached");
                    driver.switchTo().defaultContent();
                    break;
                } else
                    continue videoLoop;
            }
        }
}

    public static boolean uploadFilesUsingSendKeys(By locator, String data)
            throws InterruptedException {
        delay(1000);
        WebElement element = getDriver().findElement(locator);
        element.sendKeys(System.getProperty(userDir) + "\\src\\test\\resources\\uploadFiles\\" + data);
        return true;
    }

    public static void switchToFrame(By locator)
            throws InterruptedException {
        WebElement element = getDriver().findElement(locator);
        getDriver().switchTo().frame(element);
    }

    public static void switchToDefaultContent()
            throws InterruptedException {
        getDriver().switchTo().defaultContent();
    }
    public static boolean waitForURLContains(String url, String logStep) {
        WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor(logStep));

        return wait.until(ExpectedConditions.urlContains(url));
    }
    public static boolean clickWithoutLog(By locator) {
        try {
            delay(500);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        WebDriverWait wait = new WebDriverWait(getDriver(), 20);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).isDisplayed();
        WebElement elm = waitForClickable(locator);
        if (elm == null) {
            return false;
        } else {
            elm.click();
            return true;
        }
    }

    public static String getElementAttribute(By locator, String attribute) {
        WebElement elm = waitForVisible(locator);
        return elm.getAttribute(attribute);
    }
    public static void closeChildWindow() {


        GlobalUtil.getDriver().close();


    }
    public static boolean switchToParentWindow() {
        ArrayList<String> tabs2 = new ArrayList<String>(GlobalUtil.getDriver().getWindowHandles());
        GlobalUtil.getDriver().switchTo().window(tabs2.get(0));
        return true;
    }

    public static int numberOfWindowsPresent() {
        ArrayList<String> tabs = new ArrayList<String>(GlobalUtil.getDriver().getWindowHandles());
        return tabs.size();
    }

    public static String extractNumber(String str) {
        Pattern pattern = Pattern.compile("\\d+"); // Matches one or more digits
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static void DragAndDropByOffset(By Source, By Destination,int x,int y) {
        Actions actions = new Actions(getDriver());
        WebElement sourceElem = getDriver().findElement(Source);
        WebElement targetElem = getDriver().findElement(Destination);
        actions.clickAndHold(sourceElem);
        actions.moveToElement(targetElem);
        actions.moveByOffset(x,y);
        actions.release().build().perform();
    }
    public static boolean scrollToElementWithoutLog(By locator) throws InterruptedException {
        Thread.sleep(2000);
        WebElement element = getDriver().findElement(locator);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView();", element);
        return true;
    }
    public static boolean checkUncheckCheckbox(By locator, String status, String elementLabel) {
        boolean flag = false;
        if (status.equalsIgnoreCase("check") || status.equalsIgnoreCase("uncheck")) {
            String checkBoxStatus = getElementAttribute(locator, "checked");
            if (checkBoxStatus != null)
                flag = true;
            else
                flag = false;
            if (status.equalsIgnoreCase("check") && !flag)
                click(locator, "Checked '"+elementLabel+"' checkbox");
            else if (status.equalsIgnoreCase("check") && flag)
                MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor("'"+elementLabel+"' checkbox already checked."));
            else if (status.equalsIgnoreCase("uncheck") && flag)
                click(locator, "Unchecked '"+elementLabel+"' checkbox");
            else if (status.equalsIgnoreCase("uncheck") && !flag) {
                MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.passStringGreenColor("'"+elementLabel+"' checkbox already Unchecked."));
            }
            takeScreenshotAndAttachInReport();
        }
        return flag;
    }
    public static String getbrowserName() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        String browserName = (String) jsExecutor.executeScript("return navigator.userAgent");
        return browserName;
    }
    public static boolean clickJSWithoutLog(By locator) {
        KeywordUtil.lastAction = "Click : " + locator.toString();
        LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
        WebElement element = waitForVisible(locator);
        Object obj = ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element);
        return obj == null;
    }
    public static WebElement waitForVisibleUntilTime(By locator, int timeInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), timeInSeconds);
            // wait.ignoring(ElementNotVisibleException.class);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            return null;
        }
    }
    public static boolean isWebElementVisibleUntilTime(By locator,int timeInSeconds) {
        try {
            KeywordUtil.lastAction = "Check Element visible: " + locator.toString();
            LogUtil.infoLog(KeywordUtil.class, KeywordUtil.lastAction);
            WebElement elm = waitForVisibleUntilTime(locator,timeInSeconds);

            return elm.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public static void delayForFirefox(long timeInMiliSeconds) throws InterruptedException {
        if (GlobalUtil.getCommonSettings().getBrowser().equalsIgnoreCase("firefox"))
            delay(timeInMiliSeconds);
    }

    /**
     * Method used to zoom in the screen for web
     */
    public static void zoomIn() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            if (GlobalUtil.getCommonSettings().getBrowser().equalsIgnoreCase("chrome")) {
                for (int i = 0; i < 4; i++) {

                    robot.keyPress(KeyEvent.VK_ADD);
                    robot.keyRelease(KeyEvent.VK_ADD);

                }
            } else if (GlobalUtil.getCommonSettings().getBrowser().equalsIgnoreCase("firefox")) {
                for (int i = 0; i < 3; i++) {

                    robot.keyPress(KeyEvent.VK_ADD);
                    robot.keyRelease(KeyEvent.VK_ADD);

                }
            }
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_ADD);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    public static void waitTillPageIsCompletelyLoaded() {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), DEFAULT_WAIT_SECONDS);
            wait.until((ExpectedCondition<Boolean>) wd ->
                    ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {

        }

    }

}// End class

class TestStepFailedException extends Exception {
    TestStepFailedException(String s) {
        super(s);
    }

    public static void scrolldown(WebElement Element) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollBy(0,600);", Element);
    }
}



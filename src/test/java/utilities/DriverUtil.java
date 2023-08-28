package utilities;

import com.google.common.collect.ImmutableMap;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import mobileutil.MobileKeywords;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import runner.MyTestRunner;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * This DriverUtil class refer to browsers, os details, browser versions and
 * will close all browsers
 */

public class DriverUtil {

    public static int row;
    public static String NDevice_Name = null;
    public static String ODevice_Name;
    public static String username;

    public static final String IE = "IE";
    public static final String REMOTE = "BrowserStack";
    public static final String EDGE = "edge";
    public static final String CHROME = "Chrome";
    public static final String FIREFOX = "Firefox";
    private static Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();

    private static HashMap<String, String> checkLogin = new HashMap<String, String>();
    public static String appium_ip_address = mobileutil.MobileKeywords.GetValue("appium_ip_address");
    public static String appium_port = mobileutil.MobileKeywords.GetValue("appium_port");
    public static DesiredCapabilities capabilities = new DesiredCapabilities();

    public static XSSFWorkbook wb;
    public static XSSFSheet sheet1;

    public static final String USER_NAME = "txdemo_UIFksp";

    public static final String ACCESS_KEY = "E9A8uKH17peejwWKFWok";

    /**
     * will use this getting browser(chrome, ie, ff)NorfsbK5jasabqG4jqR5
     *
     * @param
     * @return
     */
    private DriverUtil() {

    }

    public static AppiumDriver<MobileElement> invokeLocalIPhoneMobileApp(String exeEnv, String deviceDetails) {
        String deviceName = deviceDetails.split("_")[0];
        String osVersion = deviceDetails.split("_")[1];
        System.out.println(deviceName);
        System.out.println(osVersion);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, osVersion);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        capabilities.setCapability("autoGrantPermissions", "true");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        capabilities.setCapability("noReset", "false");
        String udid = System.getProperty("udid");
        if (udid == null)
            capabilities.setCapability("udid", "00008101-001D152821E1001E");
        else
            capabilities.setCapability("udid", udid);
        capabilities.setCapability("automationName", "XCUITest");
//		capabilities.setCapability("fullReset", "true");
        capabilities.setCapability("autoAcceptAlerts", "false");
        capabilities.setCapability("bundleId", "com.payrange.payrange");
        try {
            GlobalUtil.mdriver = new IOSDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            System.err.println("");
            e.printStackTrace();
        }
        GlobalUtil.mdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        MyTestRunner.logger.log(LogStatus.INFO,
                "<font color=green>Execution Done By The Device with udid:" + udid + "</font>");
        return GlobalUtil.mdriver;
    }


    public static AppiumDriver<MobileElement> invokeBrowserStackMobileApp(String deviceDetails) {
        String deviceName = deviceDetails.split("_")[0];
        String osVersion = deviceDetails.split("_")[1];
        System.out.println(deviceName);
        System.out.println(osVersion);
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("device", deviceName);
        caps.setCapability("os_version", osVersion);
        caps.setCapability("autoDismissAlerts", true);
        caps.setCapability("name", "Bstack-[Java]-Mobile Amazon Test");
        caps.setCapability("app", "bs://2d814536cd2e82e0da6fc5a0838e546cb9474e03");
        System.out.println(caps.getCapability("app"));

        try {
            GlobalUtil.mdriver = new AndroidDriver<MobileElement>(
                    new URL("http://" + USER_NAME + ":" + ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub"), caps);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        GlobalUtil.mdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // click_on_SKIP_SIGN_IN_button();
        /*
         * AmazonAppFunctions.skipSignIn(ODevice_Name, NDevice_Name);
         * AmazonAppFunctions.clickShopByCategory(ODevice_Name , NDevice_Name);
         * AmazonAppFunctions.clickFirstCategoryExpandButton(ODevice_Name ,
         * NDevice_Name); AmazonAppFunctions.selectProduct(ODevice_Name , NDevice_Name);
         * AmazonAppFunctions.addProductToCart(ODevice_Name , NDevice_Name);
         * AmazonAppFunctions.verifyItemAddedToCart(ODevice_Name , NDevice_Name);
         */

        // Negative TestCase Condition
        // AmazonAppFunctions.loginToAmazonApp(username,ODevice_Name, NDevice_Name);
        // NDevice_Name = ODevice_Name;
        // GlobalUtil.mdriver.quit();
        return GlobalUtil.mdriver;
    }

    /**
     * @param browserName
     * @return
     */
    public static WebDriver getBrowser(String exeEnv, String browserName) {
        WebDriver browser = null;
        try {
            DesiredCapabilities caps;
            String URL = null;

            if (exeEnv.equals("REMOTE")) {

                if (browserName.equalsIgnoreCase(CHROME)) {
                    caps = DesiredCapabilities.chrome();
                    caps.setCapability("os", "WINDOWS");


                    if (GlobalUtil.getCommonSettings().getRemoteOS().toUpperCase().contains("WINDOWS")) {
                        caps.setCapability("os_version", GlobalUtil.getCommonSettings().getRemoteOS().split("_")[1]);
                        ;
                    }
                    if (GlobalUtil.getCommonSettings().getRemoteOS().toUpperCase().contains("MAC")) {

                        caps.setCapability("os", "OS X");
                        caps.setCapability("os_version", GlobalUtil.getCommonSettings().getRemoteOS().split("_")[1]);
                    }
                } else if (browserName.equalsIgnoreCase(IE)) {
                    caps = DesiredCapabilities.internetExplorer();
                    caps.setCapability("os", "WINDOWS");
                    if (GlobalUtil.getCommonSettings().getRemoteOS().toUpperCase().contains("WINDOWS")) {
                        caps.setCapability("os_version", GlobalUtil.getCommonSettings().getRemoteOS().split("_")[1]);
                        ;
                    }
                } else if (browserName.equalsIgnoreCase(EDGE)) {
                    caps = DesiredCapabilities.edge();
                    caps.setCapability("os", "WINDOWS");
                    caps.setCapability("version", "14.0");
                    caps.setCapability("os_version", "10");
                    ;
                } else if (browserName.equalsIgnoreCase("Safari")) {
                    caps = DesiredCapabilities.safari();
                    if (GlobalUtil.getCommonSettings().getRemoteOS().toUpperCase().contains("WINDOWS")) {
                        caps.setCapability("os", "OS X");
                        caps.setCapability("os_version", "SIERRA");
                    } else {
                        caps.setCapability("os", "OS X");
                        caps.setCapability("os_version", GlobalUtil.getCommonSettings().getRemoteOS().split("_")[1]);
                    }
                }

                // firefox
                else {
                    // else if (browserName.equalsIgnoreCase("Firefox")) {

                    caps = DesiredCapabilities.firefox();
                    caps.setCapability("os", "WINDOWS");
                    if (GlobalUtil.getCommonSettings().getRemoteOS().toUpperCase().contains("WINDOWS")) {
                        caps.setCapability("os_version", GlobalUtil.getCommonSettings().getRemoteOS().split("_")[1]);
                        ;
                    }
                    if (GlobalUtil.getCommonSettings().getRemoteOS().toUpperCase().contains("MAC")) {
                        caps.setCapability("os", "OS X");
                        caps.setCapability("os_version", GlobalUtil.getCommonSettings().getRemoteOS().split("_")[1]);
                    }
                }

                if (GlobalUtil.getCommonSettings().getCloudProvider().equalsIgnoreCase("BrowserStack")) {
                    caps.setCapability("browserstack.debug", "true");
                    caps.setCapability("build", GlobalUtil.getCommonSettings().getBuildNumber());
                    URL = "https://" + GlobalUtil.getCommonSettings().getHostName() + ":"
                            + GlobalUtil.getCommonSettings().getKey() + "@hub-cloud.browserstack.com/wd/hub";
                }
                try {
                    browser = new RemoteWebDriver(new URL(URL), caps);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                drivers.put(browserName, browser);

            } else {

                if (browserName.equalsIgnoreCase(CHROME)) {
                    // Write code for chrome
                    browser = drivers.get(browserName);
                    if (browser == null) {
                        // File chromeExecutable = new File(ConfigReader.getValue("ChromeDriverPath"));
                        // System.setProperty("webdriver.chrome.driver",
                        // chromeExecutable.getAbsolutePath());
                        System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "/src/test/resources/chromedriver.exe");
                        ChromeOptions options = new ChromeOptions();
                        options.addArguments("--ignore-ssl-errors=yes");
                        options.addArguments("--ignore-certificate-errors");
//                        options.setAcceptInsecureCerts(true);
                        options.addArguments("--disable-popup-blocking");
                        options.addArguments("--disable-notifications");
//                        options.addArguments("--headless");
                        options.addArguments("window-size=1920,1080");

                        WebDriverManager.chromedriver().setup();
                        browser = new ChromeDriver(options);
                        drivers.put("Chrome", browser);
                        exeEnv = REMOTE;
                    } // End if
                } else if (browserName.equalsIgnoreCase(IE)) {
                    // Write code for IE
                    browser = drivers.get(browserName);
                    if (browser == null) {
                        File ieExecutable = new File(ConfigReader.getValue("IEDriverPath"));
                        System.setProperty("webdriver.ie.driver", ieExecutable.getAbsolutePath());
                        DesiredCapabilities capabilitiesIE = DesiredCapabilities.internetExplorer();
                        capabilitiesIE.setCapability("ie.ensureCleanSession", true);
                        capabilitiesIE.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
                        browser = new InternetExplorerDriver(capabilitiesIE);
                        drivers.put("IE", browser);
                        checkLogin.put(browserName, "Y");
                    }
                } else if (browserName.equalsIgnoreCase("Firefox")) {
// Getting Firefox Browser
                    browser = drivers.get("Firefox");
// if (browser==null) {

                    /*
                     * File geckoExecutable = new File(ConfigReader.getValue("GeckoDriverPath"));
                     * System.out.println(geckoExecutable.getAbsolutePath());
                     * System.setProperty("webdriver.gecko.driver",geckoExecutable.getAbsolutePath()
                     * );
                     */
                    System.setProperty("webdriver.gecko.driver","C:\\Users\\Sandeep Singh\\Downloads\\geckodriver.exe");
                    FirefoxProfile ffprofile = new FirefoxProfile();
                    ffprofile.setPreference("dom.webnotifications.enabled", true);
                    ffprofile.setPreference("dom.push.enabled", true);
                    ffprofile.setPreference("browser.cache.disk.enable", false);
                    ffprofile.setPreference("browser.cache.memory.enable", false);
                    ffprofile.setPreference("browser.cache.offline.enable", false);
                    FirefoxOptions options = new FirefoxOptions();
                    options.addArguments("--ignore-ssl-errors=yes");
                    options.addArguments("--disable-popup-blocking");
                    options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    options.addArguments("-private");
//                    options.addArguments("--headless");
                    // options.addArguments("window-size=1440,784");
                    options.setProfile(ffprofile);
//                    WebDriverManager.firefoxdriver().setup();
                    browser = new FirefoxDriver(options);
                    drivers.put("Firefox", browser);
                    checkLogin.put(browserName, "Y");

                }
            }

            // browser.manage().timeouts().pageLoadTimeout(90, TimeUnit.SECONDS);
            // browser.manage().deleteAllCookies();
            browser.manage().window().maximize();
            LogUtil.infoLog("TestStarted",
                    GlobalUtil.getCommonSettings().getBrowser() + " : Browser Launched and Maximized.");
        } catch (Exception e) {
            LogUtil.errorLog(DriverUtil.class, "Browser not launched. Please check the configuration ", e);
            e.printStackTrace();
        }
        return browser;
    }

    /**
     * will get browser type and version
     *
     * @param browser
     * @param cap
     * @return
     */
    public static String getBrowserAndVersion(WebDriver browser, DesiredCapabilities cap) {
        String browserversion;
        String windows = "Windows";
        String browsername = cap.getBrowserName();
        // This block to find out IE Version number
        if ("IE".equalsIgnoreCase(browsername)) {
            String uAgent = (String) ((JavascriptExecutor) browser).executeScript("return navigator.userAgent;");
            LogUtil.infoLog(DriverUtil.class, uAgent);
            // uAgent return as "MSIE 8.0 Windows" for IE8
            if (uAgent.contains("MSIE") && uAgent.contains(windows)) {
                browserversion = uAgent.substring(uAgent.indexOf("MSIE") + 5, uAgent.indexOf(windows) - 2);
            } else if (uAgent.contains("Trident/7.0")) {
                browserversion = "11.0";
            } else {
                browserversion = "0.0";
            }
        } else {
            // Browser version for Firefox and Chrome
            browserversion = cap.getVersion();
        }
        String browserversion1 = browserversion.substring(0, browserversion.indexOf('.'));
        return browsername + " " + browserversion1;
    }

    /**
     * will get operating system information
     *
     * @return
     */

    /**
     * close all browsersw
     *
     * @return
     */
    public static void closeAllDriver() {

        drivers.entrySet().forEach(key -> {
            key.getValue().quit();
            key.setValue(null);
        });

        LogUtil.infoLog(DriverUtil.class, "Closing Browsers");
    }

    public static String getImgRef(String imgFile) {
        return new DriverUtil().getRefImage(imgFile);
    }

    private String getRefImage(String imgFile) {
        String openCVImgsFolder = "OpenCVImages/";
        URL refImgUrl = getClass().getClassLoader().getResource(openCVImgsFolder + imgFile + ".png");
        File refImgFile;
        try {
            refImgFile = Paths.get(refImgUrl.toURI()).toFile();
            System.out.println("File Found : " + refImgFile.exists());
            return Base64.getEncoder().encodeToString(Files.readAllBytes(refImgFile.toPath()));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void invokeChromeOnLocalMobile() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "13.0");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
        capabilities.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
        URL url = null;
        try {
            url = new URL("http://0.0.0.0:4723/wd/hub");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        GlobalUtil.mdriver = new AndroidDriver(url, capabilities);
    }

    public static AppiumDriver<MobileElement> invokeLocalMobileApp(String exeEnv, String deviceDetails) {
        String deviceName = deviceDetails.split("_")[0];
        String osVersion = deviceDetails.split("_")[1];
        System.out.println(deviceName);
        System.out.println(osVersion);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, osVersion);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        capabilities.setCapability("autoGrantPermissions", true);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobileKeywords.GetValue("platformName"));
        capabilities.setCapability("chromedriverExecutable", System.getProperty("user.dir") + "/src/test/resources/chromedriver114.exe");
        capabilities.setCapability("appium:chromeOptions", ImmutableMap.of("w3c", false));
        capabilities.setCapability("appPackage", "com.android.chrome");
        capabilities.setCapability("appActivity", "com.google.android.apps.chrome.Main");

        try {
            GlobalUtil.mdriver = new AndroidDriver<MobileElement>(new URL("http://0.0.0.0:4723/wd/hub"), capabilities);
        } catch (MalformedURLException e) {
            System.err.println("");
            e.printStackTrace();
        }
        GlobalUtil.mdriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        MyTestRunner.logger.log(LogStatus.PASS, HTMLReportUtil.infoStringGreenColor("execution done by the device : " + deviceDetails));

        return GlobalUtil.mdriver;
    }
}

// End class

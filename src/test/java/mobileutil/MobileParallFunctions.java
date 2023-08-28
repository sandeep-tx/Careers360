package mobileutil;

import java.net.MalformedURLException;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class MobileParallFunctions {

    @Parameters({"platformVersion", "deviceName", "port"})
    @BeforeTest
    public void setUp(String platformVersion, String deviceName, String port) throws MalformedURLException {
        AppiumDriverLocalService service = new AppiumServiceBuilder().usingPort(Integer.valueOf(port)).build();
        service.start();
        DesiredCapabilities dc = new DesiredCapabilities();
        dc.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        dc.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        dc.setCapability("appPackage", "in.amazon.mShop.android.shopping");
        dc.setCapability("appActivity", "com.amazon.mShop.home.HomeActivity");
    }


}

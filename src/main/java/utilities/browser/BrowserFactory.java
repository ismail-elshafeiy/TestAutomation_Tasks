package utilities.browser;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import utilities.actions.Helper;
import utilities.Logger;

import java.net.MalformedURLException;
import java.net.URL;

import static org.testng.Assert.fail;

public class BrowserFactory {
    static DesiredCapabilities capabilities = new DesiredCapabilities();
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final String propertiesFileName = "project.properties";
    private static final String browserTypeProperty = Helper.getProperty(propertiesFileName, "browser.type");
    private static final String executionTypeProperty = Helper.getProperty(propertiesFileName, "execution.type");
    private static final String operatingSystemTypeProperty = Helper.getProperty(propertiesFileName, "targetOperatingSystem");
    private static final String host = Helper.getProperty(propertiesFileName, "remote.execution.host");
    private static final String port = Helper.getProperty(propertiesFileName, "remote.execution.port");

    /**
     * Check the Browser, Execution and Operating System from properties file
     *
     * @return  ExecutionType, Operating System, BrowserType
     */
    public static WebDriver getBrowser() {
        return getBrowser(ExecutionType.FROM_PROPERTIES, OperatingSystemType.FROM_PROPERTIES, BrowserType.FROM_PROPERTIES);
    }

    // Check the Browser and Execution from property file
    @Step("Initializing a new Web GUI Browser!.....")
    public static WebDriver getBrowser(ExecutionType executionType, OperatingSystemType operatingSystemType, BrowserType browserType) {
        Logger.logStep("Initialize [" + browserType.getValue() + "] Browser and the Execution Type is [" + executionType.getValue() + "] " + "and the Operating System is [" + operatingSystemType.getValue() + "]");

        ///////////////////////////////////////////////////////////////////////////////////////////
        /////////////////      Remote execution    /////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////

        // Remote execution condition >>>>
        if (executionType == ExecutionType.REMOTE || (executionType == ExecutionType.FROM_PROPERTIES && executionTypeProperty.equalsIgnoreCase("remote"))) {
            // Check the browser typeProperty is Chrome >>>>
            if (browserType == BrowserType.GOOGLE_CHROME || (browserType == BrowserType.FROM_PROPERTIES && browserTypeProperty.equalsIgnoreCase("google chrome"))) {
                try {
                    driver.set(new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), getChromeOptions()));
                    BrowserActions.maximizeWindow(driver.get());
                    Helper.implicitWait(driver.get());
                } catch (MalformedURLException e) {
                    Logger.logMessage("Invalid grid URL" + e.getMessage());
                    e.printStackTrace();
                }
                // Check the operating system type is Linux >>>>
                if (operatingSystemType == OperatingSystemType.LINUX || (operatingSystemType == OperatingSystemType.FROM_PROPERTIES && operatingSystemTypeProperty.equalsIgnoreCase("Linux-64"))) {
                    try {
                        driver.set(new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), getChromeOptions()));
                        capabilities.setPlatform(Platform.LINUX);
                        BrowserActions.maximizeWindow(driver.get());
                        Helper.implicitWait(driver.get());
                    } catch (MalformedURLException e) {
                        Logger.logMessage("Invalid grid URL" + e.getMessage());
                        e.printStackTrace();
                    }
                    // Check the operating system type is window >>>>
                } else if (operatingSystemType == OperatingSystemType.WINDOWS || (operatingSystemType == OperatingSystemType.FROM_PROPERTIES && operatingSystemTypeProperty.equalsIgnoreCase("Windows-64"))) {
                    try {
                        driver.set(new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), getChromeOptions()));
                        capabilities.setPlatform(Platform.WINDOWS);
                        BrowserActions.maximizeWindow(driver.get());
                        Helper.implicitWait(driver.get());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else {
                    String warningMsg = "The driver is null! because the operatingSystemType  [" + operatingSystemType + "] is not valid, Please choose a valid operatingSystemType from  properties file ";
                    Logger.logMessage(warningMsg);
                    fail(warningMsg);
                }
                // Check the browser typeProperty is firefox  >>>>
            } else if (browserType == BrowserType.MOZILLA_FIREFOX
                    || (browserType == BrowserType.FROM_PROPERTIES && browserTypeProperty.equalsIgnoreCase("mozilla firefox"))) {
                try {
                    driver.set(new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), getFirefoxOptions()));
                    BrowserActions.maximizeWindow(driver.get());
                    Helper.implicitWait(driver.get());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                if (operatingSystemType == OperatingSystemType.LINUX
                        || (operatingSystemType == OperatingSystemType.FROM_PROPERTIES && operatingSystemTypeProperty.equalsIgnoreCase("Linux-64"))) {
                    try {
                        driver.set(new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), getFirefoxOptions()));
                        capabilities.setPlatform(Platform.LINUX);
                        BrowserActions.maximizeWindow(driver.get());
                        Helper.implicitWait(driver.get());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else if (operatingSystemType == OperatingSystemType.WINDOWS
                        || (operatingSystemType == OperatingSystemType.FROM_PROPERTIES && operatingSystemTypeProperty.equalsIgnoreCase("Windows-64"))) {
                    try {
                        driver.set(new RemoteWebDriver(new URL("http://" + host + ":" + port + "/wd/hub"), getFirefoxOptions()));
                        capabilities.setPlatform(Platform.WINDOWS);
                        BrowserActions.maximizeWindow(driver.get());
                        Helper.implicitWait(driver.get());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                } else {
                    String warningMsg = "The driver is null! because the operatingSystemType  [" + operatingSystemType + "] is not valid, Please choose a valid operatingSystemType from  properties file ";
                    Logger.logMessage(warningMsg);
                    fail(warningMsg);
                }
            } else {
                String warningMsg = "The driver is null! because the browser type [" + browserTypeProperty + "] is not valid, Please choose a valid browser type from properties file";
                Logger.logMessage(warningMsg);
                fail(warningMsg);

            }

        }

        ///////////////////////////////////////////////////////////////////////////////////////////
        /////////  Local execution condition   /////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////

        else if (executionType == ExecutionType.LOCAL || (executionType == ExecutionType.FROM_PROPERTIES && executionTypeProperty.equalsIgnoreCase("local"))) {
            if (browserType == BrowserType.GOOGLE_CHROME
                    || (browserType == BrowserType.FROM_PROPERTIES && browserTypeProperty.equalsIgnoreCase("google chrome"))) {
                WebDriverManager.chromedriver().setup();
                driver.set(new ChromeDriver());
                BrowserActions.maximizeWindow(driver.get());
                Helper.implicitWait(driver.get());
            } else if (browserType == BrowserType.MOZILLA_FIREFOX
                    || (browserType == BrowserType.FROM_PROPERTIES && browserTypeProperty.equalsIgnoreCase("mozilla firefox"))) {
                WebDriverManager.firefoxdriver().setup();
                driver.set(new FirefoxDriver());
                Helper.implicitWait(driver.get());

            } else {
                String warningMsg = "The driver is null! because the browser type [" + browserTypeProperty + "] is not valid/supported; Please choose a valid browser type from the given choices in the properties file";
                Logger.logMessage(warningMsg);
                fail(warningMsg);
            }

        }

        ///////////////////////////////////////////////////////////////////////////////////////////
        ///////////  Headless execution    /////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////

        else if (executionType == ExecutionType.LOCAL_HEADLESS
                || (executionType == ExecutionType.FROM_PROPERTIES && executionTypeProperty.equalsIgnoreCase("local headless"))) {
            if (browserType == BrowserType.GOOGLE_CHROME
                    || (browserType == BrowserType.FROM_PROPERTIES && browserTypeProperty.equalsIgnoreCase("google chrome"))) {
                WebDriverManager.chromedriver().setup();
                driver.set(new ChromeDriver(getChromeOptions()));
                BrowserActions.maximizeWindow(driver.get());
                Helper.implicitWait(driver.get());

            } else if (browserType == BrowserType.MOZILLA_FIREFOX
                    || (browserType == BrowserType.FROM_PROPERTIES && browserTypeProperty.equalsIgnoreCase("mozilla firefox"))) {
                WebDriverManager.firefoxdriver().setup();
                driver.set(new FirefoxDriver(getFirefoxOptions()));
                BrowserActions.maximizeWindow(driver.get());
                Helper.implicitWait(driver.get());

            } else {
                String warningMsg = "The driver is null! because the browser type [" + browserTypeProperty + "] is not valid/supported; Please choose a valid browser type from the given choices in the properties file";
                Logger.logMessage(warningMsg);
                fail(warningMsg);
            }

        } else {
            String warningMsg = "The driver is null! because the execution type [" + executionTypeProperty + "] is not valid/supported; Please choose a valid execution type from the given choices in the properties file";
            Logger.logMessage(warningMsg);
            fail(warningMsg);
        }
        return driver.get();
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public enum ExecutionType {
        LOCAL("Local"), REMOTE("Remote"), LOCAL_HEADLESS("Local Headless"), FROM_PROPERTIES(executionTypeProperty);
        private final String value;

        ExecutionType(String type) {
            this.value = type;
        }

        private String getValue() {
            return value;
        }
    }

    public enum OperatingSystemType {
        WINDOWS("Windows-64"), LINUX("Linux-64"), FROM_PROPERTIES(operatingSystemTypeProperty);
        private final String value;

        OperatingSystemType(String type) {
            this.value = type;
        }

        private String getValue() {
            return value;
        }
    }

    public enum BrowserType {
        MOZILLA_FIREFOX("Mozilla Firefox"), GOOGLE_CHROME("Google Chrome"), FROM_PROPERTIES(browserTypeProperty);
        private final String value;

        BrowserType(String type) {
            this.value = type;
        }

        private String getValue() {
            return value;
        }
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(true);
        chromeOptions.addArguments("--window-size=1920,1080");
        return chromeOptions;
    }

    private static FirefoxOptions getFirefoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setHeadless(true);
        firefoxOptions.addArguments("--window-size=1920,1080");
        return firefoxOptions;
    }
}



package utilities;

import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.WebDriver;
import org.testng.*;

public class Listener implements ISuiteListener, ITestListener, IInvokedMethodListener {
    ///////////////// ISuiteListener //////////////////
    @Override
    public void onStart(ISuite suite) {
        System.out.println("\n" + "=============================================================================================================================================");
        System.out.println("Starting Test Suite: By Ismail Elshafeiy ");
        System.out.println("=============================================================================================================================================" + "\n");
        ExtentReport.initReports();
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("\n" + "=============================================================================================================================================");
        System.out.println("Finished Test Suite: By Ismail Elshafeiy ");
        System.out.println("=============================================================================================================================================" + "\n");
        ExtentReport.flushReports();
    }

    ///////////////// ITestListener //////////////////

    @Override
    public void onStart(ITestContext context) {
        System.out.println("\n" + "############################################### " + "Test: ["
                + context.getName() + "] Started" + " ###############################################" + "\n");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("\n" + "############################################### " + "Test: ["
                + context.getName() + "] Finished" + " ###############################################" + "\n");
    }

    //////////////////   Test methods                 ////////////////////////

    @Override
    public void onTestStart(ITestResult result) {
	ExtentReport.createTest(result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReport.pass(MarkupHelper.createLabel(result.getMethod().getMethodName() + " Passed!", ExtentColor.GREEN));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ITestContext context = result.getTestContext();
        WebDriver driver = (WebDriver) context.getAttribute("driver");
        if (driver != null) {
            Logger.attachScreenshotToAllureReport(driver);
            ExtentReport.fail(Logger.attachScreenshotToExtentReport(driver));
        }
        ExtentReport.fail(MarkupHelper.createLabel(result.getMethod().getMethodName() + " Failed!", ExtentColor.RED));
        if (result.getThrowable() != null) {
            ExtentReport.fail(result.getThrowable());
        }
    }

    ///////////////// IInvokedMethodListener //////////////////

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        ITestNGMethod testMethod = method.getTestMethod();
        if (testMethod.getDescription() != null && !testMethod.getDescription().equals("")) {
            ExtentReport.createTest(testMethod.getDescription());
        } else {
            ExtentReport.createTest(testMethod.getMethodName());
        }
        System.out.println("\n" + "==============================================================================================================================================");

        if (method.isConfigurationMethod()) {
            System.out.println("Starting Configuration Method ( Setup ): [" + testResult.getName() + "]");
            if (testMethod.getDescription() != null && !testMethod.getDescription().equals("")) {
                ExtentReport.removeTest(testMethod.getDescription());
            } else {
                ExtentReport.removeTest(testResult.getName());
            }
        } else {
            System.out.println("Starting Test Case: [" + testResult.getName() + "]");
        }
        System.out.println("=============================================================================================================================================" + "\n");
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        System.out.println("\n" + "=============================================================================================================================================");
        if (method.isConfigurationMethod()) {
            System.out.println("Finished Configuration Method ( Setup ): [" + testResult.getName() + "]");
        } else {
            System.out.println("Finished Test Case: [" + testResult.getName() + "]");
        }
        System.out.println("============================================================================================================================================" + "\n");
    }

}

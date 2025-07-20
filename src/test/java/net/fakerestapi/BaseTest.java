package net.fakerestapi;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import net.fakerestapi.constants.GeneralConstants;
import net.fakerestapi.utils.Log;
import net.fakerestapi.utils.PropertiesFilesHandler;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class BaseTest {

    public static ExtentReports extent;
    public ExtentHtmlReporter htmlReporter;
    public ExtentTest test;
    public int success_tests = 0;
    public int fail_tests = 0;
    public int skip_tests = 0;
    PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
    Properties generalConfigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);
    public String url = generalConfigsProps.getProperty(GeneralConstants.BASE_URL) + generalConfigsProps.getProperty(GeneralConstants.API_VERSION);
    public int connectionTimeout = Integer.parseInt(generalConfigsProps.getProperty(GeneralConstants.CONNECTION_TIMEOUT));
    public int socketTimeout = Integer.parseInt(generalConfigsProps.getProperty(GeneralConstants.SOCKET_TIMEOUT));
    public int connectionManagerTimeout = Integer.parseInt(generalConfigsProps.getProperty(GeneralConstants.CONNECTION_MANAGER_TIMEOUT));
    String extentReportFilePath = generalConfigsProps.getProperty(GeneralConstants.EXTENT_REPORT_FILE_PATH);
    String dateTime = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
    String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

    @BeforeSuite(description = "Setting up Base requirements", alwaysRun = true)
    public void setup() {
        RestAssured.baseURI = url;
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.config =  RestAssuredConfig.config().httpClient(
                HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", connectionTimeout)
                        .setParam("http.socket.timeout", socketTimeout)
                        .setParam("http.connection-manager.timeout", (long) connectionManagerTimeout));

        Log.info("URL : " + url);
    }

    @BeforeSuite(description = "Setting up extent report", alwaysRun = true)
    @Parameters({"applicationName"})
    public void setExtent(String applicationName) {
        try {
            Log.info("Setting up extent report");

            // specify location of the report
            htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + extentReportFilePath + applicationName + GeneralConstants.STRING_DELIMITER + dateTime + ".html");


            htmlReporter.config().setDocumentTitle(generalConfigsProps.getProperty(GeneralConstants.EXTENT_REPORT_TITLE)); // Tile of report
            htmlReporter.config().setReportName(generalConfigsProps.getProperty(GeneralConstants.EXTENT_REPORT_NAME)); // Name of the report
            htmlReporter.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);

            // Passing General information
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Url", url);
            extent.setSystemInfo("Date", date);
        } catch (Exception e) {
            Log.error("Error occurred while setting up extent reports", e);
        }
    }

    @AfterMethod(description = "Logging test status to log file and extent report", alwaysRun = true)
    public void logTestStatusForReport(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                Log.info("logging Testcase FAILED " + result.getName() + " in Extent Report");
                test.log(Status.FAIL, "Test Case Name FAILED is " + result.getName()); // to add name in extent report
                test.log(Status.FAIL, "EXCEPTION Thrown is " + result.getThrowable()); // to add error/exception in extent report
                fail_tests++;
            } else if (result.getStatus() == ITestResult.SKIP) {
                Log.info("logging Testcase SKIPPED " + result.getName() + " in Extent Report");
                test.log(Status.SKIP, "Test Case SKIPPED is " + result.getName());
                skip_tests++;
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                Log.info("logging Testcase SUCCESS " + result.getName() + " in Extent Report");
                test.log(Status.PASS, "Test Case PASSED is " + result.getName());
                success_tests++;
            }
            // log that test case has ended
            Log.endTestCase(result.getName());
        } catch (Exception e) {
            Log.warn("Error occurred while logging testcase " + result.getName() + " result to extent report", e);
        }
    }

    @AfterSuite(description = "Closing extent report after running Suite", alwaysRun = true)
    public void endReport() {
        Log.info("Closing Extent report after Test");
        if (extent != null) extent.flush();

        Log.info("Update Extent report name with Test Cases Status");
        File extentReportNameBeforeAddTestCaseStatus = new File(htmlReporter.getFilePath());
        File extentReportNameAfterAddTestCaseStatus = new File(System.getProperty("user.dir") + extentReportFilePath + GeneralConstants.STRING_DELIMITER + dateTime + GeneralConstants.STRING_DELIMITER + "SuccessTCs" + success_tests + GeneralConstants.STRING_DELIMITER + "FailedTCs" + fail_tests + GeneralConstants.STRING_DELIMITER + "SkippedTCs" + skip_tests + ".html");
        if (extentReportNameBeforeAddTestCaseStatus.renameTo(extentReportNameAfterAddTestCaseStatus)) {
            Log.info("File renamed");
        } else {
            Log.info("Sorry! the file can't be renamed");
        }
    }
}

package net.fakerestapi.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fakerestapi.constants.GeneralConstants;

import java.util.Properties;

public class Log {

    public static ExtentTest test;

    //Initialize instances of properties files to be used in all tests
    static PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
    static Properties generalConfigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);
    static boolean addStepsToExtentReportFlag = (generalConfigsProps.getProperty(GeneralConstants.ADD_STEPS_TO_EXTENT_REPORT).
            equalsIgnoreCase(GeneralConstants.TRUE.toLowerCase())? true : false);



// Initialize Log4j logs

        private static Logger Log = LogManager.getLogger(net.fakerestapi.utils.Log .class.getName());//

        // This is to print log for the beginning of the test case, as we usually run so many test cases as a test suite

        public static void startTestCase(String sTestCaseName){

            if(test != null && addStepsToExtentReportFlag)
                test.log(Status.INFO, sTestCaseName + " Test is CREATED");


            Log.info("****************************************************************************************");

            Log.info("****************************************************************************************");

            Log.info("$$$$$$$$$$$$$$$$$$$$$           "+sTestCaseName+ "       $$$$$$$$$$$$$$$$$$$$$$$$$");

            Log.info("****************************************************************************************");

            Log.info("****************************************************************************************");

        }

        //This is to print log for the ending of the test case

        public static void endTestCase(String sTestCaseName){

            if(test != null && addStepsToExtentReportFlag)
                test.log(Status.INFO, sTestCaseName + " Test has ENDED");

            Log.info("XXXXXXXXXXXXXXXXXXXXXXX      "+"      -E---N---D-     "+ sTestCaseName+ "             XXXXXXXXXXXXXXXXXXXXXX");

            Log.info("X");

            Log.info("X");

            Log.info("X");

            Log.info("X");

        }

        // Need to create these methods, so that they can be called

        public static void info(String message) {

            Log.info(message);
            if(test != null && addStepsToExtentReportFlag)
                test.log(Status.INFO, message);

        }

        public static void warn(String message, Exception e) {

            Log.warn(message, e);
            if(test != null && addStepsToExtentReportFlag)
                test.log(Status.WARNING, message);

        }

        public static void error(String message, Exception e) {

            Log.error(message, e);
            if(test != null && addStepsToExtentReportFlag) {
                test.log(Status.ERROR, message);
                test.log(Status.ERROR, e.getMessage());
            }

        }

        public static void fatal(String message) {

            Log.fatal(message);
            if(test != null && addStepsToExtentReportFlag)
                test.log(Status.FATAL, message);

        }

        public static void debug(String message) {

            Log.debug(message);
            if(test != null && addStepsToExtentReportFlag)
                test.log(Status.DEBUG, message);

        }


}

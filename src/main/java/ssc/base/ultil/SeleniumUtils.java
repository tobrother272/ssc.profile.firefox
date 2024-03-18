/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ultil;

import java.io.File;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import static ssc.base.ultil.ThreadUtils.Sleep;

/**
 *
 * @author PC
 */
public class SeleniumUtils {
    public static String GECKO=System.getProperty("user.dir")+File.separator+"tool"+File.separator+"geckodriver.exe";
    public static WebDriver getFireFoxGECKOHeadLess(String useragent) {
        WebDriver driver = null;
        try {
            //System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
            System.setProperty("webdriver.gecko.driver", GECKO);
            System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "/dev/null");
            //FirefoxBinary binary = new FirefoxBinary(new File(System.getProperty("user.dir")+File.separator+"tool"+File.separator+"Firefox64"+File.separator+"firefox.exe"));
            FirefoxOptions options = new FirefoxOptions();
            FirefoxProfile ff = new FirefoxProfile();
            //options.setBinary(binary);
            ff.setAcceptUntrustedCertificates(true);
            ff.setAssumeUntrustedCertificateIssuer(false);
            ff.setPreference("general.useragent.override", useragent);
            options.setProfile(ff);
            //options.setHeadless(true);
            driver = new FirefoxDriver(options);
            //driver = new FirefoxDriver(ff);
        } catch (Exception ex) {
            ex.printStackTrace();
            //StringWriter sw = new StringWriter();
            //ex.printStackTrace(new PrintWriter(sw));
            //ex.toString();

        }finally{
        
        }
        return driver;

        // return getChromeDriver();
    }
    public static void waitForPageLoad(WebDriver d, int timeout) {
        String s = "";
        try {
            int countTime = 0;
            while (!s.equals("complete") && countTime < timeout) {
                s = (String) ((JavascriptExecutor) d).executeScript("return document.readyState");
                Sleep(1000);
                countTime++;
            }
        } catch (Exception e) {

        }
     
    }
    public static String getContentValue(String xpath, WebDriver driver) {
        String result = "";
        try {
            String query = "return document.evaluate(\"" + xpath + "\", document, null, 7, null).snapshotItem(0).value";
            //System.out.println(query);
            result = String.valueOf(((JavascriptExecutor) driver).executeScript(query, new Object[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }
}

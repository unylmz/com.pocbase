package com.pocbase.base;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

import org.openqa.selenium.support.ui.WebDriverWait;

import static java.lang.System.getenv;

public class BaseTest {


  protected static WebDriver driver;
  protected static WebDriverWait webDriverWait;

  @BeforeScenario
  public void setUp() throws Exception{
    String baseUrl = "http://www.trendyol.com";

    DesiredCapabilities capabilities = DesiredCapabilities.chrome();
   // if (StringUtils.isNotEmpty(getenv("key"))) {
   //   ChromeOptions options = new ChromeOptions();
   //   options.addArguments("test-type");
   //   options.addArguments("disable-popup-blocking");
   //   options.addArguments("ignore-certificate-errors");
   //   options.addArguments("disable-translate");
   //   options.addArguments("start-maximized");
   //   options.addArguments("--no-sandbox");
   //   capabilities.setCapability(ChromeOptions.CAPABILITY, options);
   //   capabilities.setCapability("key", System.getenv("key"));
   //   driver = new RemoteWebDriver(new URL("http://hub.testinium.io/wd/hub"), capabilities);
   //   ((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
   // } else {
   //   System.setProperty("webdriver.chrome.driver", "web_driver/chromedriver");
   //   driver = new ChromeDriver();
   // }

    System.setProperty("webdriver.chrome.driver", "web_driver/chromedriver");
    driver = new ChromeDriver();
    webDriverWait = new WebDriverWait(driver, 45, 150);
    driver.get(baseUrl);
  }

  @AfterScenario
  public void tearDown(){
    driver.quit();
  }


}

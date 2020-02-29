package com.pocbase.step;

import com.pocbase.base.BaseTest;
import com.pocbase.helper.ElementHelper;
import com.pocbase.helper.StoreHelper;
import com.pocbase.model.ElementInfo;
import com.thoughtworks.gauge.Step;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.Dimension;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class StepImplementation extends BaseTest {


  public static int DEFAULT_MAX_ITERATION_COUNT = 150;
  public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;

  private static String SAVED_ATTRIBUTE;

  private WebElement findElement(String key){
    ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
    By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
    WebDriverWait webDriverWait = new WebDriverWait(driver, 0);
    WebElement webElement = webDriverWait
        .until(ExpectedConditions.presenceOfElementLocated(infoParam));
    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
        webElement);
    return webElement;
  }

  private List<WebElement> findElements(String key){
    ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
    By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
    return driver.findElements(infoParam);
  }

  private void clickElement(WebElement element){
    element.click();
  }

  private void clickElementBy(String key){
    findElement(key).click();
  }

  private void hoverElement(WebElement element){
    Actions actions = new Actions(driver);
    actions.moveToElement(element).build().perform();
  }

  private void hoverElementBy(String key){
    WebElement webElement = findElement(key);
    Actions actions = new Actions(driver);
    actions.moveToElement(webElement).build().perform();
  }

  private void sendKeyESC(String key){
    findElement(key).sendKeys(Keys.ESCAPE);

  }

  private boolean isDisplayed(WebElement element){
    return element.isDisplayed();
  }

  private boolean isDisplayedBy(By by){
    return driver.findElement(by).isDisplayed();
  }

  private String getPageSource(){
    return driver.switchTo().alert().getText();
  }

  private String getCurrentDateThenAddDays(int daysToAdd){
    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    Date currentDate = new Date();

    // convert date to calendar
    Calendar c = Calendar.getInstance();
    c.setTime(currentDate);

    c.add(Calendar.DAY_OF_MONTH, daysToAdd);

    return dateFormat.format(c.getTime());
  }

  public static String getSavedAttribute(){
    return SAVED_ATTRIBUTE;
  }

  public String randomString(int stringLength){

    Random random = new Random();
    char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz0123456789".toCharArray();
    String stringRandom = "";
    for (int i = 0; i < stringLength; i++) {

      stringRandom = stringRandom + String.valueOf(chars[random.nextInt(chars.length)]);
    }

    return stringRandom;
  }

  public WebElement findElementWithKey(String key){
    return findElement(key);
  }

  public String getElementText(String key){
    return findElement(key).getText();
  }

  public String getElementAttributeValue(String key, String attribute){
    return findElement(key).getAttribute(attribute);
  }

  @Step("print page source")
  public void printPageSource(){
    System.out.println(getPageSource());
  }

  public void javaScriptClicker(WebDriver driver, WebElement element){

    JavascriptExecutor jse = ((JavascriptExecutor) driver);
    jse.executeScript("var evt = document.createEvent('MouseEvents');"
        + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
        + "arguments[0].dispatchEvent(evt);", element);
  }

  @Step({"Wait <value> seconds",
      "<int> saniye bekle"})
  public void waitBySeconds(int seconds){
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Step({"Wait <value> milliseconds",
      "<long> milisaniye bekle"})
  public void waitByMilliSeconds(long milliseconds){
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Step({"Wait for element then click <key>",
      "Elementi bekle ve sonra tıkla <key>"})
  public void checkElementExistsThenClick(String key){
    getElementWithKeyIfExists(key);
    clickElement(key);
  }

  @Step({"Click to element <key>",
      "Elementine tıkla <key>"})
  public void clickElement(String key){
    if (!key.equals("")) {
      WebElement element = findElement(key);
      hoverElement(element);
      waitByMilliSeconds(500);
      clickElement(element);
    }
  }

  @Step({"Check if element <key> exists",
      "Wait for element to load with key <key>",
      "Element var mı kontrol et <key>",
      "Elementinin yüklenmesini bekle <key>"})
  public WebElement getElementWithKeyIfExists(String key){
    WebElement webElement;
    int loopCount = 0;
    while (loopCount < 50) {
      try {
        webElement = findElementWithKey(key);
        return webElement;
      } catch (WebDriverException e) {
      }
      loopCount++;
      waitByMilliSeconds(100);
    }
    Assert.fail("Element: '" + key + "' doesn't exist.");
    return null;
  }

  @Step({"Go to <url> address","Bu adrese git <url>" })
  public void goToUrl(String url){
    driver.get(url);
  }

  @Step({"Wait for element to load with css <css>",
      "Elementinin yüklenmesini bekle css <css>"})
  public void waitElementLoadWithCss(String css){
    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      if (driver.findElements(By.cssSelector(css)).size() > 0) {
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element: '" + css + "' doesn't exist.");
  }

  @Step({"Wait for element to load with xpath <xpath>",
      "Elementinin yüklenmesini bekle xpath <xpath>"})
  public void waitElementLoadWithXpath(String xpath){
    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      if (driver.findElements(By.xpath(xpath)).size() > 0) {
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element: '" + xpath + "' doesn't exist.");
  }

  @Step({"Check if element <key> exists else print message <message>",
      "Element <key> var mı kontrol et yoksa hata mesajı ver <message>"})
  public void getElementWithKeyIfExistsWithMessage(String key, String message){
    ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
    By by = ElementHelper.getElementInfoToBy(elementInfo);

    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      if (driver.findElements(by).size() > 0) {
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail(message);
  }

  @Step({"Check if element <key> not exists",
      "Element yok mu kontrol et <key>"})
  public void checkElementNotExists(String key){
    ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
    By by = ElementHelper.getElementInfoToBy(elementInfo);

    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      if (driver.findElements(by).size() == 0) {
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element '" + key + "' still exist.");
  }

  @Step({"Upload file in project <path> to element <key>",
      "Proje içindeki <path> dosyayı elemente upload et <key>"})
  public void uploadFile(String path, String key){
    String pathString = System.getProperty("user.dir") + "/";
    pathString = pathString + path;
    findElement(key).sendKeys(pathString);
  }

  @Step({"Write value <text> to element <key>",
      "<text> textini elemente yaz <key>"})
  public void sendKeys(String text, String key){
    if (!key.equals("")) {
      findElement(key).sendKeys(text);
    }
  }

  @Step({"Click with javascript to css <css>",
      "Javascript ile css tıkla <css>"})
  public void javascriptClickerWithCss(String css){
    Assert.assertTrue("Element bulunamadı", isDisplayedBy(By.cssSelector(css)));
    javaScriptClicker(driver, driver.findElement(By.cssSelector(css)));

  }

  @Step({"Click with javascript to xpath <xpath>",
      "Javascript ile xpath tıkla <xpath>"})
  public void javascriptClickerWithXpath(String xpath){
    Assert.assertTrue("Element bulunamadı", isDisplayedBy(By.xpath(xpath)));
    javaScriptClicker(driver, driver.findElement(By.xpath(xpath)));

  }

  @Step({"Check if current URL contains the value <expectedURL>",
      "Şuanki URL <url> değerini içeriyor mu kontrol et"})
  public void checkURLContainsRepeat(String expectedURL){
    int loopCount = 0;
    String actualURL = "";
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      actualURL = driver.getCurrentUrl();

      if (actualURL != null && actualURL.contains(expectedURL)) {
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail(
        "Actual URL doesn't match the expected." + "Expected: " + expectedURL + ", Actual: "
            + actualURL);
  }

  @Step({"Send TAB key to element <key>",
      "Elemente TAB keyi yolla <key>"})
  public void sendKeyToElementTAB(String key){
    findElement(key).sendKeys(Keys.TAB);
  }

  @Step({"Send BACKSPACE key to element <key>",
      "Elemente BACKSPACE keyi yolla <key>"})
  public void sendKeyToElementBACKSPACE(String key){
    findElement(key).sendKeys(Keys.BACK_SPACE);
  }

  @Step({"Send ESCAPE key to element <key>",
      "Elemente ESCAPE keyi yolla <key>"})
  public void sendKeyToElementESCAPE(String key){
    findElement(key).sendKeys(Keys.ESCAPE);
  }

  @Step({"Check if element <key> has attribute <attribute>",
      "<key> elementi <attribute> niteliğine sahip mi"})
  public void checkElementAttributeExists(String key, String attribute){
    WebElement element = findElement(key);

    int loopCount = 0;

    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      if (element.getAttribute(attribute) != null) {
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element DOESN't have the attribute: '" + attribute + "'");
  }

  @Step({"Check if element <key> not have attribute <attribute>",
      "<key> elementi <attribute> niteliğine sahip değil mi"})
  public void checkElementAttributeNotExists(String key, String attribute){
    WebElement element = findElement(key);

    int loopCount = 0;

    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      if (element.getAttribute(attribute) == null) {
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element STILL have the attribute: '" + attribute + "'");
  }

  @Step({"Check if <key> element's attribute <attribute> equals to the value <expectedValue>",
      "<key> elementinin <attribute> niteliği <value> değerine sahip mi"})
  public void checkElementAttributeEquals(String key, String attribute, String expectedValue){
    WebElement element = findElement(key);

    String actualValue;
    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      actualValue = element.getAttribute(attribute).trim();
      if (actualValue.equals(expectedValue)) {
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element's attribute value doesn't match expected value");
  }

  @Step({"Check if <key> element's attribute <attribute> contains the value <expectedValue>",
      "<key> elementinin <attribute> niteliği <value> değerini içeriyor mu"})
  public void checkElementAttributeContains(String key, String attribute, String expectedValue){
    WebElement element = findElement(key);

    String actualValue;
    int loopCount = 0;
    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      actualValue = element.getAttribute(attribute).trim();
      if (actualValue.contains(expectedValue)) {
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail("Element's attribute value doesn't contain expected value");
  }

  @Step({"Write <value> to <attributeName> of element <key>",
      "<value> değerini <attribute> niteliğine <key> elementi için yaz"})
  public void setElementAttribute(String value, String attributeName, String key){
    String attributeValue = findElement(key).getAttribute(attributeName);
    findElement(key).sendKeys(attributeValue, value);
  }

  @Step({"Write <value> to <attributeName> of element <key> with Js",
      "<value> değerini <attribute> niteliğine <key> elementi için JS ile yaz"})
  public void setElementAttributeWithJs(String value, String attributeName, String key){
    WebElement webElement = findElement(key);
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("arguments[0].setAttribute('" + attributeName + "', '" + value + "')",
        webElement);
  }

  @Step({"Clear text of element <key>",
      "<key> elementinin text alanını temizle"})
  public void clearInputArea(String key){
    findElement(key).clear();
  }

  @Step({"Clear text of element <key> with BACKSPACE",
      "<key> elementinin text alanını BACKSPACE ile temizle"})
  public void clearInputAreaWithBackspace(String key){
    WebElement element = findElement(key);
    element.clear();
    element.sendKeys("a");
    Actions actions = new Actions(driver);
    actions.sendKeys(Keys.BACK_SPACE).build().perform();
  }

  @Step({"Change page zoom to <value>%",
      "Sayfanın zoom değerini değiştir <value>%"})
  public void chromeZoomOut(String value){
    JavascriptExecutor jsExec = (JavascriptExecutor) driver;
    jsExec.executeScript("document.body.style.zoom = '" + value + "%'");
  }

  @Step("Click to <key> element then load document from path <path>")
  public void sendFile(String key, String path) throws Exception{

    String myString = System.getProperty("user.dir") + "/" + path;
    System.out.println(myString);
    StringSelection stringSelection = new StringSelection(myString);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(stringSelection, null);
    waitBySeconds(2);
    Robot robot = new Robot();
    robot.delay(1000);
    robot.keyPress(KeyEvent.VK_DOWN);
    robot.keyPress(KeyEvent.VK_ENTER);
    robot.keyRelease(KeyEvent.VK_DOWN);
    robot.keyRelease(KeyEvent.VK_ENTER);
    robot.delay(1000);

    //Fullscreen kontrolü
    Dimension dimension2 = Toolkit.getDefaultToolkit().getScreenSize();
    System.out.println(dimension2.width + " " + dimension2.height);
    org.openqa.selenium.Dimension dimension3 = driver.manage().window().getSize();
    System.out.println(dimension3.width + " " + dimension3.height);

    if (dimension2.width == dimension3.width && dimension2.height == dimension3.height) {
      robot.delay(1000);
      robot.keyPress(KeyEvent.VK_META);
      robot.keyPress(KeyEvent.VK_TAB);
      robot.keyRelease(KeyEvent.VK_TAB);
      robot.keyRelease(KeyEvent.VK_META);
    }

    clickElementBy(key);

    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

    robot.delay(3000);
    robot.mouseMove(dimension.width / 2, dimension.height / 2);
    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    robot.delay(2000);
    robot.keyPress(KeyEvent.VK_META);
    robot.keyPress(KeyEvent.VK_SHIFT);
    robot.keyPress(KeyEvent.VK_G);
    robot.keyRelease(KeyEvent.VK_G);
    robot.keyRelease(KeyEvent.VK_SHIFT);
    robot.keyRelease(KeyEvent.VK_META);
    robot.delay(1000);
    robot.keyPress(KeyEvent.VK_META);
    robot.keyPress(KeyEvent.VK_V);
    robot.keyRelease(KeyEvent.VK_META);
    robot.keyRelease(KeyEvent.VK_V);
    robot.delay(1000);
    robot.keyPress(KeyEvent.VK_ENTER);
    robot.keyRelease(KeyEvent.VK_ENTER);
    robot.delay(2000);
    robot.keyPress(KeyEvent.VK_ENTER);
    robot.keyRelease(KeyEvent.VK_ENTER);
    waitBySeconds(2);
  }

  @Step({"Open new tab",
      "Yeni sekme aç"})
  public void chromeOpenNewTab(){
    ((JavascriptExecutor) driver).executeScript("window.open()");
  }

  @Step({"Focus on tab number <number>",
      "<number> numaralı sekmeye odaklan"})//Starting from 1
  public void chromeFocusTabWithNumber(int number){
    ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
    driver.switchTo().window(tabs.get(number - 1));
  }

  @Step({"Focus on last tab",
      "Son sekmeye odaklan"})
  public void chromeFocusLastTab(){
    ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
    driver.switchTo().window(tabs.get(tabs.size() - 1));
  }

  @Step({"Focus on frame with <key>",
      "Frame'e odaklan <key>"})
  public void chromeFocusFrameWithNumber(String key){
    WebElement webElement = findElement(key);
    driver.switchTo().frame(webElement);
  }

  @Step({"Save attribute <attribute> value of element <key>",
      "<attribute> niteliğini sakla <key> elementi için"})
  public void saveAttributeValueOfElement(String attribute, String key){
    SAVED_ATTRIBUTE = findElement(key).getAttribute(attribute);
    System.out.println("Saved attribute value is: " + SAVED_ATTRIBUTE);
  }

  @Step({"Write saved attribute value to element <key>",
      "Kaydedilmiş niteliği <key> elementine yaz"})
  public void writeSavedAttributeToElement(String key){
    findElement(key).sendKeys(SAVED_ATTRIBUTE);
  }

  @Step("Check if <int> amount element exists with same key <key>")
  public void checkElementCount(int expectedAmount, String key){
    int loopCount = 0;

    while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
      if (expectedAmount == findElements(key).size()) {
        return;
      }
      loopCount++;
      waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
    }
    Assert.fail(
        "Expected element count failed. Expected amount:" + expectedAmount + " Actual amount:"
            + findElements(key).size());
  }

  @Step({"Check if element <key> contains text <expectedText>",
      "<key> elementi <text> değerini içeriyor mu kontrol et"})
  public void checkElementContainsText(String key, String expectedText){
    Boolean containsText = findElement(key).getText().contains(expectedText);
    Assert.assertTrue("Expected text is not contained", containsText);
  }

  @Step({"Refresh page",
      "Sayfayı yenile"})
  public void refreshPage(){
    driver.navigate().refresh();
  }

  @Step({"Write random value to element <key>",
      "<key> elementine random değer yaz"})
  public void writeRandomValueToElement(String key){
    findElement(key).sendKeys(randomString(15));
  }

  @Step({"Write random value to element <key> starting with <text>",
      "<key> elementine <text> değeri ile başlayan random değer yaz"})
  public void writeRandomValueToElement(String key, String startingText){
    String randomText = startingText + randomString(15);
    findElement(key).sendKeys(randomText);
  }

  @Step({"Print element text by css <css>",
      "Elementin text değerini yazdır css <css>"})
  public void printElementText(String css){
    System.out.println(driver.findElement(By.cssSelector(css)).getText());
  }

  @Step({"Write value <string> to element <key> with focus",
      "<string> değerini <key> elementine focus ile yaz"})
  public void sendKeysWithFocus(String text, String key){
    Actions actions = new Actions(driver);
    actions.moveToElement(findElement(key));
    actions.click();
    actions.sendKeys(text);
    actions.build().perform();
  }

  @Step({"Click to element <key> with focus",
      "<key> elementine focus ile tıkla"})
  public void clickElementWithFocus(String key){
    Actions actions = new Actions(driver);
    actions.moveToElement(findElement(key));
    actions.click();
    actions.build().perform();
  }

  @Step({"Write date with <int> days added to current date to element <key>",
      "Bugünkü tarihe <int> gün ekle ve <key> elementine yaz"})
  public void writeDateWithDaysAdded(int daysToAdd, String key){
    if (!key.equals("")) {
      findElement(key).sendKeys(getCurrentDateThenAddDays(daysToAdd));
    }
  }

  @Step({"Accept Chrome alert popup",
      "Chrome uyarı popup'ını kabul et"})
  public void acceptChromeAlertPopup(){
    driver.switchTo().alert().accept();
  }


}

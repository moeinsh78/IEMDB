package com.example.WatchListTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

public class GetWatchListSuccessful {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  JavascriptExecutor js;
  @Before
  public void setUp() throws Exception {
    System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
    driver = new ChromeDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    js = (JavascriptExecutor) driver;
  }

  @Test
  public void testGetWatchListSuccessful() throws Exception {
    driver.get("http://localhost:8080/movie/1");
    driver.findElement(By.xpath("//form[@id='watchListForm']/input")).click();
    driver.findElement(By.xpath("//form[@id='watchListForm']/input")).clear();
    driver.findElement(By.xpath("//form[@id='watchListForm']/input")).sendKeys("mahdiye@gmail.com");
    driver.findElement(By.xpath("//form[@id='watchListForm']/button")).click();
    driver.get("http://localhost:8080/successful_operation");
    driver.get("http://localhost:8080/movie/2");
    driver.findElement(By.xpath("//form[@id='watchListForm']/input")).click();
    driver.findElement(By.xpath("//form[@id='watchListForm']/input")).clear();
    driver.findElement(By.xpath("//form[@id='watchListForm']/input")).sendKeys("mahdiye@gmail.com");
    driver.findElement(By.xpath("//form[@id='watchListForm']/button")).click();
    driver.get("http://localhost:8080/successful_operation");
    driver.get("http://localhost:8080/watchList/mahdiye@gmail.com");
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}

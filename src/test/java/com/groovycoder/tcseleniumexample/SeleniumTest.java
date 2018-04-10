package com.groovycoder.tcseleniumexample;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.io.File;

import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

public class SeleniumTest {

    @Rule
    public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withRecordingMode(RECORD_ALL, new File("build"));

    @Test
    public void searchGoogle() {
        WebDriver driver = chrome.getWebDriver();

        driver.get("https://www.google.de");

        WebElement searchInputField = driver.findElement(By.name("q"));
        searchInputField.sendKeys("Testcontainers");
        searchInputField.submit();

        new WebDriverWait(driver, 10).until(
                webDriver -> webDriver.getTitle().toLowerCase().startsWith("testcontainers")
        );

        Assert.assertEquals("Testcontainers - Google-Suche", driver.getTitle());

        driver.quit();
    }

}

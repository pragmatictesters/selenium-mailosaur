package com.pragmatic.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class HelloSelenium {

    @Test
    public void testHelloSelenium () {
        WebDriverManager.chromedriver().setup(); //Setup the web browser driver
        WebDriver webDriver = new ChromeDriver(); //Create and launch a browser instance
        webDriver.get("http://hrm.pragmatictestlabs.com"); //Navigate to the given URL
        //Provide login credentials and submit the form
        webDriver.findElement(By.id("txtUsername")).sendKeys("Admin");
        webDriver.findElement(By.id("txtPassword")).sendKeys("Ptl@#321");
        webDriver.findElement(By.id("txtPassword")).submit();
        webDriver.close(); //Close the browser
    }

}

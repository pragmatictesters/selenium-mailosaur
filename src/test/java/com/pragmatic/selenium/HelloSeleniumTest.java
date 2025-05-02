package com.pragmatic.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class HelloSeleniumTest {

    @Test
    public void testHelloSelenium () {
        WebDriver webDriver = new ChromeDriver(); //Create and launch a browser instance
        webDriver.get("http://saucedemo.com"); //Navigate to the given URL
        //Provide login credentials and submit the form
        webDriver.findElement(By.id("user-name")).sendKeys("standard_user");
        webDriver.findElement(By.id("password")).sendKeys("secret_sauce");
        webDriver.findElement(By.id("login-button")).submit();
        webDriver.close(); //Close the browser
    }

}

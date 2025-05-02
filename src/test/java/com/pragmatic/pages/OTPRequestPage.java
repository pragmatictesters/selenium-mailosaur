package com.pragmatic.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class OTPRequestPage {

    private final WebDriver driver;

    private final By emailInputField = By.id("email");
    private final By requestAccessCodeButton = By.tagName("button");

    public OTPRequestPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterEmail(String email) {
        WebElement emailElement = driver.findElement(emailInputField);
        emailElement.clear();
        emailElement.sendKeys(email);
    }

    public void clickRequestAccessCodeButton() {
        driver.findElement(requestAccessCodeButton).click();
    }

    // You can add methods to check for success/error messages or OTP input fields later

}
package com.pragmatic.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ResetPasswordEntryPage {

    private final WebDriver  driver;

    private final By passwordInputField = By.id("password");
    private final By confirmPasswordInputField = By.id("confirmPassword");
    private final By resetPasswordButton = By.tagName("button"); // Assuming the button has a generic tag

    public ResetPasswordEntryPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterNewPassword(String password) {
        WebElement passwordElement = driver.findElement(passwordInputField);
        passwordElement.clear();
        passwordElement.sendKeys(password);
    }

    public void enterConfirmNewPassword(String confirmPassword) {
        WebElement confirmPasswordElement = driver.findElement(confirmPasswordInputField);
        confirmPasswordElement.clear();
        confirmPasswordElement.sendKeys(confirmPassword);
    }

    public void clickResetPasswordButton() {
        driver.findElement(resetPasswordButton).click();
    }

    // You might add methods to check for success/error messages later

}
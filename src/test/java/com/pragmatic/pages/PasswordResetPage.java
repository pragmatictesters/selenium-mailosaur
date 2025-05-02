package com.pragmatic.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PasswordResetPage {

    private WebDriver driver;

    private final By emailInputField = By.id("email");
    private final By submitButton = By.xpath("//button");
    private final By passwordInputField = By.id("password");
    private final By confirmPasswordInputField = By.id("confirmPassword");

    public PasswordResetPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterEmail(String email) {
        WebElement emailElement = driver.findElement(emailInputField);
        emailElement.clear();
        emailElement.sendKeys(email);
    }

    public void clickSubmitButton() {
        driver.findElement(submitButton).click();
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
        driver.findElement(submitButton).click(); // Assuming the same button is used for reset
    }

    // You can add methods to check for success/error messages later

}
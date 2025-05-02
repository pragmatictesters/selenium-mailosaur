package com.pragmatic.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ActivatedAccountPage {

    private final WebDriver driver;

    private final By headingText = By.tagName("h1");
    private final By paragraphText = By.tagName("p");

    public ActivatedAccountPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getHeadingText() {
        WebElement headingElement = driver.findElement(headingText);
        return headingElement.getText();
    }

    public String getParagraphText() {
        WebElement paragraphElement = driver.findElement(paragraphText);
        return paragraphElement.getText();
    }

    // You might add other elements or checks specific to this page later

}
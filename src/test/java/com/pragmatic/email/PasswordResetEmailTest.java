package com.pragmatic.email;

import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.mailosaur.models.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.Optional;

public class PasswordResetEmailTest {

    private static final String API_KEY = "ik13CNx8nb3deBvb"; //YOUR_API_KEY;
    private static final String SERVER_ID = "l5gzya4r"; // YOUR_SERVER_ID;
    private static final String BASE_URL = "https://example.mailosaur.com/password-reset";
    private WebDriver driver;
    private MailosaurClient mailosaur;
    private String testEmail;

    @BeforeMethod
    public void beforeMethod() throws MailosaurException {
        driver = new ChromeDriver();
        mailosaur = new MailosaurClient(API_KEY);
        mailosaur.messages().deleteAll(SERVER_ID);
        String serverDomain = SERVER_ID + ".mailosaur.net";
        testEmail = String.format("selenium3@%s", serverDomain);
        System.out.println("Test Email for Password Reset: " + testEmail);
    }


    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
            driver.quit(); // Use quit to close all browser windows and the WebDriver session
        }
    }

    @Test
    public void testEmailSubjectVerification() throws IOException, MailosaurException {
        // 1. Navigate to the password reset page
        driver.get(BASE_URL);

        // 2. Enter the test email address
        driver.findElement(By.id("email")).sendKeys(testEmail);

        // 3. Click the submit button
        driver.findElement(By.xpath("//button")).click();

        // 4. Define message search parameters
        MessageSearchParams params = new MessageSearchParams();
        params.withServer(SERVER_ID);
        params.withTimeout(10000); // Timeout in milliseconds

        // 5. Define message search criteria
        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(testEmail);

        // 6. Retrieve the latest email sent to the test email address
        Message message = mailosaur.messages().get(params, criteria);

        // 7. Assert that a message was received
        Assert.assertNotNull(message, "Email message should not be null");

        // 8. Assert that the subject of the email is correct
        Assert.assertEquals(message.subject(), "Set your new password for ACME Product",
                "Email subject does not match the expected value");
    }


    @Test
    public void testInteractingWithLinksInEmail() throws IOException, MailosaurException {
        // 1. Navigate to the password reset page
        driver.get(BASE_URL);

        // 2. Enter the test email address
        driver.findElement(By.id("email")).sendKeys(testEmail);

        // 3. Click the submit button
        driver.findElement(By.xpath("//button")).click();

        // 4. Define message search parameters
        MessageSearchParams params = new MessageSearchParams();
        params.withServer(SERVER_ID);
        params.withTimeout(10000); // Timeout in milliseconds

        // 5. Define message search criteria
        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(testEmail);

        // 6. Retrieve the latest email sent to the test email address
        Message message = mailosaur.messages().get(params, criteria);

        // Get the first link with "set-password" in the href
        Optional<Link> firstLink = message.html().links().stream()
                .filter(link -> link.href().contains("set-password"))
                .findFirst();

        Assert.assertTrue(firstLink.isPresent(), "Password reset link not found in the email.");
        String resetLinkHref = firstLink.get().href();

        // 7. Navigate to the password reset link
        driver.get(resetLinkHref);

        // 8. Reset the password
        driver.findElement(By.id("password")).sendKeys("NewPassword123");
        driver.findElement(By.id("confirmPassword")).sendKeys("NewPassword123");
        driver.findElement(By.tagName("button")).click();

        // You might want to add further assertions here to verify the password reset was successful.
        // This could involve checking for a success message on the page or navigating to a login page.
        System.out.println("Successfully interacted with the password reset link and attempted to reset the password.");
    }
}
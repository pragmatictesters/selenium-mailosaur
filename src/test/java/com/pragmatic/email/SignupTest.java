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

public class SignupTest {

    private static final String API_KEY = "ik13CNx8nb3deBvb"; //YOUR_API_KEY;
    private static final String SERVER_ID = "l5gzya4r"; // YOUR_SERVER_ID;
    private static final String BASE_URL = "https://example.mailosaur.com/signup";
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
        System.out.println("Test Email for Signup: " + testEmail);
    }


    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
            driver.quit(); // Use quit to close all browser windows and the WebDriver session
        }
    }


    @Test
    public void testSignupAndAccountActivation() throws MailosaurException, IOException {
        // 1. Navigate to the signup page
        driver.get(BASE_URL);

        // 2. Fill the signup form
        driver.findElement(By.id("firstName")).sendKeys("Test");
        driver.findElement(By.id("lastName")).sendKeys("User");
        driver.findElement(By.id("email")).sendKeys(testEmail);

        // 3. Click the Sign Up button
        driver.findElement(By.tagName("button")).click();

        // 4. Define message search parameters
        MessageSearchParams params = new MessageSearchParams();
        params.withServer(SERVER_ID);
        params.withTimeout(10000); // Timeout in milliseconds

        // 5. Define message search criteria to find the activation email
        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(testEmail);
        criteria.withSubject("Welcome to ACME Product"); //Email subject

        // 6. Retrieve the latest activation email
        Message message = mailosaur.messages().get(params, criteria);

        // 7. Assert that the activation email was received
        Assert.assertNotNull(message, "Activation email should not be null");

        // 8. Find the activation link in the email
        Optional<Link> activationLink = message.html().links().stream()
                .filter(link -> link.href().contains("activated-account"))
                .findFirst();

        // 9. Assert that the activation link was found
        Assert.assertTrue(activationLink.isPresent(), "Activation link not found in the email.");
        String activationHref = activationLink.get().href();

        // 10. Navigate to the activation link
        driver.get(activationHref);

        // 11. Verify the account activation success message
        Assert.assertEquals(driver.findElement(By.tagName("h1")).getText(), "Account activated!",
                "Account activation heading is incorrect");
        Assert.assertEquals(driver.findElement(By.tagName("p")).getText(), "Your fictional account has now been activated.",
                "Account activation paragraph is incorrect");
    }
}
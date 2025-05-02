package com.pragmatic.email;

import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.mailosaur.models.Message;
import com.mailosaur.models.MessageSearchParams;
import com.mailosaur.models.SearchCriteria;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

public class OTPVerificationTest {

    private static final String API_KEY = "ik13CNx8nb3deBvb"; //YOUR_API_KEY;
    private static final String SERVER_ID = "l5gzya4r"; // YOUR_SERVER_ID;
    private static final String BASE_URL = "https://example.mailosaur.com/otp";
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
        System.out.println("Test Email for OTP: " + testEmail);
    }

    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testRequestAndVerifyOTP() throws MailosaurException, IOException {
        // 1. Navigate to the OTP request page
        driver.get(BASE_URL);

        // 2. Enter the test email and click the request button
        driver.findElement(By.id("email")).sendKeys(testEmail);
        driver.findElement(By.tagName("button")).click();

        // 3. Define message search parameters
        MessageSearchParams params = new MessageSearchParams();
        params.withServer(SERVER_ID);
        params.withTimeout(10000); // Timeout in milliseconds

        // 4. Define message search criteria for the OTP email
        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(testEmail);
        criteria.withSubject("Here is your access code for ACME Product"); // Adjust if the actual subject is different

        // 5. Retrieve the latest OTP email
        Message message = mailosaur.messages().get(params, criteria);

        // 6. Assert that the OTP email was received
        Assert.assertNotNull(message, "OTP email should not be null");

        // 7. Extract the OTP from the email content
        // Assuming the OTP is the first code found in the HTML body
        String oneTimePassword = message.html().codes().get(0).value();
        System.out.println("Retrieved OTP: " + oneTimePassword);

        // 8. Assert that the extracted OTP is a 6-digit number
        Assert.assertTrue(oneTimePassword.matches("\\d{6}"), "OTP '" + oneTimePassword + "' should be a 6-digit number.");
        Assert.assertFalse(oneTimePassword.matches("0{6}"), "OTP '" + oneTimePassword + "' should not be a 6-0-digits.");
        Assert.assertFalse(oneTimePassword.matches("123456"), "OTP should not be a predictable sequence (e.g., 123456).");
        Assert.assertFalse(oneTimePassword.matches("654321"), "OTP should not be a predictable reverse sequence.");
        Assert.assertFalse(oneTimePassword.matches("111111"), "OTP should not be all the same digit (other than zero, if that's checked).");
        // Optional: You could add further steps here to verify the OTP in the application UI
        // For example, entering the OTP in a field and submitting.
    }
}
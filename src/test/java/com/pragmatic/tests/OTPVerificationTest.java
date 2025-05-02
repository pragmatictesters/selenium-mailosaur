package com.pragmatic.tests;

import com.mailosaur.MailosaurException;
import com.mailosaur.models.Message;
import com.mailosaur.models.MessageSearchParams;
import com.mailosaur.models.SearchCriteria;
import com.pragmatic.pages.OTPRequestPage;
import com.pragmatic.util.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class OTPVerificationTest extends BaseTest {

    private OTPRequestPage otpRequestPage;

    @BeforeMethod
    public void setup() throws MailosaurException {
        super.setup();
        otpRequestPage = new OTPRequestPage(driver);
    }

    @Test
    public void testRequestAndVerifyOTP() throws MailosaurException, IOException {
        driver.get(ConfigReader.getApplicationUrl("otp"));
        otpRequestPage.enterEmail(testEmail);
        otpRequestPage.clickRequestAccessCodeButton();

        MessageSearchParams params = new MessageSearchParams();
        params.withServer(serverId);
        params.withTimeout(ConfigReader.getTimeout());

        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(testEmail);
        criteria.withSubject("Here is your access code for ACME Product"); // Adjust if needed

        Message message = mailosaur.messages().get(params, criteria);
        Assert.assertNotNull(message, "OTP email should not be null");

        String oneTimePassword = message.html().codes().get(0).value();
        System.out.println("Retrieved OTP: " + oneTimePassword);
        Assert.assertTrue(oneTimePassword.matches("\\d{6}"), "OTP '" + oneTimePassword + "' should be a 6-digit number.");
        Assert.assertFalse(oneTimePassword.matches("0{6}"), "OTP '" + oneTimePassword + "' should not be a 6-zero-digits.");
    }
}

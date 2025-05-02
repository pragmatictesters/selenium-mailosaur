package com.pragmatic.tests;

import com.mailosaur.MailosaurException;
import com.mailosaur.models.Message;
import com.mailosaur.models.MessageSearchParams;
import com.mailosaur.models.SearchCriteria;
import com.pragmatic.pages.PasswordResetPage;
import com.pragmatic.pages.ResetPasswordEntryPage;
import com.pragmatic.util.ConfigReader;
import com.pragmatic.util.EmailManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Optional;

public class PasswordResetEmailTest extends BaseTest {

    private PasswordResetPage passwordResetPage;
    private ResetPasswordEntryPage resetPasswordEntryPage;

    @BeforeMethod
    public void setup() throws MailosaurException {
        super.setup();
        passwordResetPage = new PasswordResetPage(driver);
        resetPasswordEntryPage = new ResetPasswordEntryPage(driver);
    }

    @Test
    public void testEmailSubjectVerification() throws IOException, MailosaurException {
        EmailManager emailManager = new EmailManager();
        String testEmail = emailManager.generateRandomEmail("ptl");

        driver.get(ConfigReader.getApplicationUrl("passwordreset"));
        passwordResetPage.enterEmail(testEmail);
        passwordResetPage.clickSubmitButton();


        Message message = emailManager.getLatestEmail(testEmail);
        Assert.assertNotNull(message, "Email message should not be null");
        Assert.assertEquals(message.subject(), "Set your new password for ACME Product",
                "Email subject does not match the expected value");
        emailManager.deleteEmail(message);
    }



    @Test
    public void testInteractingWithLinksInEmail() throws IOException, MailosaurException {
        driver.get(ConfigReader.getApplicationUrl("passwordreset"));
        passwordResetPage.enterEmail(testEmail);
        passwordResetPage.clickSubmitButton();

        MessageSearchParams params = new MessageSearchParams();
        params.withServer(serverId);
        params.withTimeout(ConfigReader.getTimeout());

        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(testEmail);

        Message message = mailosaur.messages().get(params, criteria);

        Optional<com.mailosaur.models.Link> firstLink = message.html().links().stream()
                .filter(link -> link.href().contains("set-password"))
                .findFirst();

        Assert.assertTrue(firstLink.isPresent(), "Password reset link not found in the email.");
        String resetLinkHref = firstLink.get().href();

        driver.get(resetLinkHref);

        resetPasswordEntryPage.enterNewPassword("NewPassword123");
        resetPasswordEntryPage.enterConfirmNewPassword("NewPassword123");
        resetPasswordEntryPage.clickResetPasswordButton();

        // Add assertions here to verify successful password reset
        System.out.println("Successfully interacted with the password reset link and attempted to reset the password.");
    }
}

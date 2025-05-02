package com.pragmatic.tests;


import com.mailosaur.MailosaurException;
import com.mailosaur.models.Message;
import com.mailosaur.models.MessageSearchParams;
import com.mailosaur.models.SearchCriteria;
import com.pragmatic.pages.ActivatedAccountPage;
import com.pragmatic.pages.SignupPage;
import com.pragmatic.util.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Optional;

public class SignupTest extends BaseTest {

    private SignupPage signupPage;
    private ActivatedAccountPage activatedAccountPage;

    @BeforeMethod
    public void setup() throws MailosaurException {
        super.setup();
        signupPage = new SignupPage(driver);
        activatedAccountPage = new ActivatedAccountPage(driver);
    }

    @Test
    public void testSignupAndAccountActivation() throws MailosaurException, IOException {
        driver.get(ConfigReader.getApplicationUrl("signup"));
        signupPage.enterFirstName("Test");
        signupPage.enterLastName("User");
        signupPage.enterEmail(testEmail);
        signupPage.clickSignUpButton();

        MessageSearchParams params = new MessageSearchParams();
        params.withServer(serverId);
        params.withTimeout(ConfigReader.getTimeout());

        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(testEmail);
        criteria.withSubject("Welcome to ACME Product"); // Adjust if needed

        Message message = mailosaur.messages().get(params, criteria);
        Assert.assertNotNull(message, "Activation email should not be null");

        Optional<com.mailosaur.models.Link> activationLink = message.html().links().stream()
                .filter(link -> link.href().contains("activated-account"))
                .findFirst();
        Assert.assertTrue(activationLink.isPresent(), "Activation link not found in the email.");
        String activationHref = activationLink.get().href();

        driver.get(activationHref);

        Assert.assertEquals(activatedAccountPage.getHeadingText(), "Account activated!",
                "Account activation heading is incorrect");
        Assert.assertEquals(activatedAccountPage.getParagraphText(), "Your fictional account has now been activated.",
                "Account activation paragraph is incorrect");
    }
}
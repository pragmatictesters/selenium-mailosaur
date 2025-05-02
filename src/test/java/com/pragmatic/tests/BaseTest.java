package com.pragmatic.tests;

import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.pragmatic.util.BrowserFactory;
import com.pragmatic.util.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    protected WebDriver driver;
    protected ConfigReader configReader;
    protected MailosaurClient mailosaur;
    protected String testEmail;
    protected String serverId;

    @BeforeMethod
    public void setup() throws MailosaurException {

        //Initialize the browser with Browser factory
        BrowserFactory.init(ConfigReader.getBrowser());
        driver = BrowserFactory.getDriver();

        // Initialize Mailosaur client
        String apiKey = ConfigReader.getMailosaurApiKey();
        System.out.println("apiKey = " + apiKey);
        serverId = ConfigReader.getMailosaurServerId();
        mailosaur = new MailosaurClient(apiKey);
        mailosaur.messages().deleteAll(serverId);

        String serverDomain = serverId + ".mailosaur.net";
        testEmail = String.format("selenium3@%s", serverDomain);
        System.out.println("Test Email: " + testEmail);
    }

    @AfterMethod
    public void teardown() {
       BrowserFactory.close();
    }
}
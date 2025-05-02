package com.pragmatic.email;

import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.mailosaur.models.Message;
import com.mailosaur.models.SearchCriteria;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class MailosaurBasicEmailTest {


    @Test
    public void testEmailSubjectWithSelenium() throws IOException, MailosaurException{
        // Available in the API tab of a server
        String apiKey = "ik13CNx8nb3deBvb" ; //YOUR_API_KEY;
        String serverId = "l5gzya4r"; // YOUR_SERVER_ID;
        String serverDomain = serverId + ".mailosaur.net";

        String testEmail = String.format("selenium3@%s", serverDomain);
        System.out.println("testEmail = " + testEmail);

        WebDriver driver = new ChromeDriver();
        driver.get("https://example.mailosaur.com/password-reset");
        driver.findElement(By.id("email")).sendKeys(testEmail);
        driver.findElement(By.xpath("//button")).click();


        MailosaurClient mailosaur = new MailosaurClient(apiKey);
        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(testEmail);

        Message message = mailosaur.messages().get(serverId, criteria);
        Assert.assertNotNull(message);
        Assert.assertEquals(message.subject(), "Set your new password for ACME Product");

        driver.close();
    }
}

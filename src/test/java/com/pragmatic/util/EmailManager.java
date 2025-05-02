package com.pragmatic.util;


import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.mailosaur.models.Link;
import com.mailosaur.models.Message;
import com.mailosaur.models.MessageSearchParams;
import com.mailosaur.models.SearchCriteria;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

public class EmailManager {

    private final MailosaurClient mailosaur;
    private final String serverId;
    private final int defaultTimeout;
    private final String serverDomain;

    public EmailManager() {
        String apiKey = ConfigReader.getMailosaurApiKey();
        this.serverId = ConfigReader.getMailosaurServerId();
        this.defaultTimeout = ConfigReader.getTimeout();
        this.mailosaur = new MailosaurClient(apiKey);
        this.serverDomain = serverId + ".mailosaur.net";
    }

    public EmailManager(String apiKey, String serverId, int defaultTimeout) {
        this.mailosaur = new MailosaurClient(apiKey);
        this.serverId = serverId;
        this.defaultTimeout = defaultTimeout;
        this.serverDomain = serverId + ".mailosaur.net";
    }

    /**
     * Retrieves the latest email sent to the specified recipient.
     *
     * @param recipientEmail The email address of the recipient.
     * @return The latest Message object, or null if no email is found within the timeout.
     * @throws IOException       If an I/O error occurs.
     * @throws MailosaurException If a Mailosaur API error occurs.
     */
    public Message getLatestEmail(String recipientEmail) throws IOException, MailosaurException {
        MessageSearchParams params = new MessageSearchParams();
        params.withServer(serverId);
        params.withTimeout(defaultTimeout);

        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(recipientEmail);

        return mailosaur.messages().get(params, criteria);
    }

    /**
     * Retrieves the latest email sent to the specified recipient with the given subject.
     *
     * @param recipientEmail The email address of the recipient.
     * @param subject        The expected subject of the email.
     * @return The latest Message object, or null if no matching email is found within the timeout.
     * @throws IOException       If an I/O error occurs.
     * @throws MailosaurException If a Mailosaur API error occurs.
     */
    public Message getLatestEmailWithSubject(String recipientEmail, String subject)
            throws IOException, MailosaurException {
        MessageSearchParams params = new MessageSearchParams();
        params.withServer(serverId);
        params.withTimeout(defaultTimeout);

        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(recipientEmail);
        criteria.withSubject(subject);

        return mailosaur.messages().get(params, criteria);
    }

    /**
     * Retrieves the reset password link from the latest email sent to the specified recipient.
     *
     * @param recipientEmail The email address of the recipient.
     * @param linkTextContains Text that the reset password link's href should contain.
     * @return The reset password link URL, or null if not found within the timeout.
     * @throws IOException       If an I/O error occurs.
     * @throws MailosaurException If a Mailosaur API error occurs.
     */
    public String getResetPasswordLink(String recipientEmail, String linkTextContains)
            throws IOException, MailosaurException {
        return getLink(recipientEmail, "set-password");
    }

    /**
     * Verifies if the latest activation email sent to the specified recipient contains the expected activation href.
     *
     * @param recipientEmail The email address of the recipient.
     * @param activationHrefContains Text that the activation link's href should contain.
     * @return The verify activation link.
     * @throws IOException       If an I/O error occurs.
     * @throws MailosaurException If a Mailosaur API error occurs.
     */
    public String verifyActivationLink(String recipientEmail, String activationHrefContains)
            throws IOException, MailosaurException {
        return getLink(recipientEmail, "activated-account");
    }

    private String getLink(String recipientEmail, String partialLink) throws IOException, MailosaurException {
        Message message = getLatestEmail(recipientEmail);
        if (message != null) {
            Optional<Link> resetLink = message.html().links().stream()
                    .filter(link -> link.href().contains(partialLink))
                    .findFirst();
            return resetLink.map(Link::href).orElse(null);
        }
        return null;
    }

    /**
     * Deletes all emails from the Mailosaur server.
     *
     * @throws IOException       If an I/O error occurs.
     * @throws MailosaurException If a Mailosaur API error occurs.
     */
    public void deleteEmail() throws IOException, MailosaurException {
        mailosaur.messages().deleteAll(serverId);
    }

    /**
     * Deletes all emails from the Mailosaur server.
     *
     * @throws IOException       If an I/O error occurs.
     * @throws MailosaurException If a Mailosaur API error occurs.
     */
    public void deleteEmail(Message message) throws IOException, MailosaurException {
        mailosaur.messages().delete(message.id());
    }




    /**
     * Generates a unique random email address for the Mailosaur server.
     *
     * @param prefix An optional prefix for the email address (e.g., "testuser").
     * @return A unique random email address.
     */
    public String generateRandomEmail(String prefix) {
        String randomString = generateRandomString(10);
        if (prefix != null && !prefix.isEmpty()) {
            return String.format("%s-%s@%s", prefix, randomString, serverDomain);
        } else {
            return String.format("%s@%s", randomString, serverDomain);
        }
    }

    /**
     * Generates a unique random email address for the Mailosaur server.
     *
     * @return A unique random email address.
     */
    public String generateRandomEmail() {
        return generateRandomEmail(null);
    }

    private String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
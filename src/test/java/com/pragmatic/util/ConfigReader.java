package com.pragmatic.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try {
            FileInputStream input = new FileInputStream("src/test/resources/config.properties");
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return resolveVariables(value);
        }
        return null;
    }

    private static String resolveVariables(String value) {
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(value);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String variableKey = matcher.group(1);
            String variableValue = properties.getProperty(variableKey);
            if (variableValue != null) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(variableValue));
            } else {
                // Handle case where variable is not found (e.g., log a warning)
                System.err.println("Warning: Variable not found: " + variableKey);
                matcher.appendReplacement(sb, matcher.group(0)); // Keep the original string
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static String getBaseURL() {
        return get("app.url.base"); // Updated key to match config.properties
    }

    public static String getBrowser() {
        return get("browser");   // Updated key to match config.properties
    }

    public static int getTimeout() {
        String timeout = get("timeout.email"); // Updated key to match config.properties
        return Integer.parseInt(timeout);
    }

    public static String getApplicationUrl(String urlKeyPart) {
        return get("app.url." + urlKeyPart);
    }

    public static String getMailosaurApiKey() {
        return get("mailosaur.api.key");
    }

    public static String getMailosaurServerId() {
        return get("mailosaur.server.id");
    }

    // Add other specific getter methods as needed
}
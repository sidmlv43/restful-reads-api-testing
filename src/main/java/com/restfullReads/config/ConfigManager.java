package com.restfullReads.config;

import com.restfullReads.utils.ConfigLoader;

public class ConfigManager {

    private static final String ENV_KEY = "env";
    private static final String DEFAULT_ENV = "uat";

    private static final String ENV =
            System.getProperty(ENV_KEY, DEFAULT_ENV);

    private static final ConfigLoader configLoader =
            new ConfigLoader("application-" + ENV + ".properties");

    private static final int TIMEOUT = parseTimeout();

    private ConfigManager() {}

    public static String getEnv() {
        return ENV;
    }

    public static String getBaseUrl() {
        return configLoader.get("base.url");
    }

    public static int getTimeout() {
        return TIMEOUT;
    }

    private static int parseTimeout() {
        try {
            return Integer.parseInt(configLoader.get("timeout", "5000"));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid timeout value in config", e);
        }
    }
}
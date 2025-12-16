package org.tzi.use.api_security.util;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    // Singleton instance
    private static Config instance;
    private final Dotenv dotenv;

    private Config() {
        dotenv = Dotenv.load(); // Load the .env file
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public String getSecretAccessTokenKey() {
        return dotenv.get("SECRET_ACCESS_TOKEN_KEY");
    }

    public String getSecretRefreshTokenKey() {
        return dotenv.get("SECRET_REFRESH_TOKEN_KEY");
    }

    public double getRateLimitPerSecond() {
        return Double.parseDouble(dotenv.get("RATE_LIMIT_PER_SECOND"));
    }

    public int getResourceLimitPerDay() {
        return Integer.parseInt(dotenv.get("RESOURCE_LIMIT_PER_DAY"));
    }
}
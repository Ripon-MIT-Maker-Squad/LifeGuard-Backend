package com.riponmakers.lifeguard.UserDatabase;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.sql.Connection;
import java.sql.DriverManager;

@Singleton
public class DatabaseConnector {
    private final String url;
    private final String username;
    private final String password;

    @Inject
    public DatabaseConnector(
            @Named("database_url") String url,
            @Named("database_username") String username,
            @Named("database_password") String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    Connection getConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

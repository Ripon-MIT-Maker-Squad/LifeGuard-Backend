package com.riponmakers.lifeguard.UserDatabase;
import java.sql.Connection;
import java.sql.DriverManager;

public class UserDatabaseConnector {
    private final String url;
    private final String username;
    private final String password;

    public UserDatabaseConnector(String url, String username, String password) {
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

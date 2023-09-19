package com.riponmakers.lifeguard.UserDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final UserDatabaseConnector databaseConnector;

    public UserService(UserDatabaseConnector dbc) {
        databaseConnector = dbc;

        tryCreateUsersTable();
    }

    public void createUser(User user) {
        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into users(username,deviceID,isHome,poolIsSupervised) values(?,?,?,?)"
            );

            pstmt.setString(1, user.username());
            pstmt.setLong(2, user.deviceID());
            pstmt.setBoolean(3, user.isHome());
            pstmt.setBoolean(4, user.poolIsSupervised());
            pstmt.execute();
        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    public void removeUser() {

    }

    public User getUser(String username) {
        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("select username,deviceID,isHome,poolIsSupervised from Users '?'");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            return new User(
                    username,
                    rs.getLong("deviceID"),
                    rs.getBoolean("isHome"),
                    rs.getBoolean("poolIsSupervised"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<User> getAllUsers() {
        List<User> Users = new ArrayList<>();

        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("select username,password,hedera_account_id from Users");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                long deviceID = rs.getLong("deviceID");
                boolean isHome = rs.getBoolean("isHome");
                boolean poolIsSupervised = rs.getBoolean("poolIsSupervised");
                Users.add(new User(username, deviceID, isHome, poolIsSupervised));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Users;
    }
    private void tryCreateUsersTable() {
        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                    """
                            create table if not exists Users (
                                username text not null,
                                deviceID bigint not null,
                                isHome boolean not null,
                                poolIsSupervised boolean not null
                            )
                            """
            );
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

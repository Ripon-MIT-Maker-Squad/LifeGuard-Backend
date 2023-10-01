package com.riponmakers.lifeguard.UserDatabase;

import com.riponmakers.lifeguard.JSONRecords.User;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final DatabaseConnector databaseConnector;
    private final String databaseName;
    private final String tableName;

    public UserService(DatabaseConnector dbc, String databaseName, String tableName) {
        databaseConnector = dbc;
        this.databaseName = databaseName;
        this.tableName = tableName;

//        tryCreateUsersTable();
    }

    public void createUser(User user) {
        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                        "insert into " + tableName + "(username,deviceid,ishome,poolissupervised) values(?,?,?,?)"
            );

            pstmt.setString(1, user.username());
            pstmt.setLong(2, user.deviceID());
            pstmt.setBoolean(3, user.isHome());
            pstmt.setBoolean(4, user.poolIsSupervised());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("createUser error\n" + e);
        }
    }

    public void removeUser(User user)    {
        try(Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                "delete from " + tableName + " where username = (?) "
            );

            pstmt.setString(1, user.username());
        } catch (SQLException e) {
            throw new RuntimeException("removeUser error\n" + e);
        }
    }

    public User getUser(String username) throws RuntimeException {
        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("select username,deviceid,ishome,poolissupervised from " + tableName + " where username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                long deviceID = rs.getLong("deviceid");
                boolean isHome = rs.getBoolean("ishome");
                boolean poolIsSupervised = rs.getBoolean("poolissupervised");

                return new User(
                        username,
                        deviceID,
                        isHome,
                        poolIsSupervised);
            }

// Close the ResultSet and statement when done
            rs.close();
            pstmt.close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("getuser error\n" + e);
        }
    }
    public List<User> getAllUsers() {
        List<User> Users = new ArrayList<>();

        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("select username,deviceid,ishome,poolissupervised from  " + tableName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                long deviceID = rs.getLong("deviceid");
                boolean isHome = rs.getBoolean("ishome");
                boolean poolIsSupervised = rs.getBoolean("poolissupervised");
                Users.add(new User(username, deviceID, isHome, poolIsSupervised));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Users;
    }
    /*
//    private void tryCreateUsersTable() {
//        try (Connection conn = this.databaseConnector.getConnection()) {
//            PreparedStatement pstmt = conn.prepareStatement(
//                    """
//                            create table if not exists %s (
//                                username text not null,
//                                deviceid bigint not null,
//                                ishome boolean not null,
//                                poolissupervised boolean not null
//                            )
//                            """.formatted(databaseName)
//            );
//            pstmt.execute();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
     */
}

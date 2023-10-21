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
    private final String tableName, deviceTableName;

    public UserService(DatabaseConnector dbc, String databaseName, String tableName, String deviceTableName) {
        databaseConnector = dbc;
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.deviceTableName = deviceTableName;
//        tryCreateUsersTable();
    }

    public void createUser(User user) {
        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                        "insert into " + tableName + "(username,ishome,poolissupervised) values(?,?,?)"
            );

            pstmt.setString(1, user.username());
            pstmt.setBoolean(2, user.isHome());
            pstmt.setBoolean(3, user.poolIsSupervised());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("createUser error\n" + e);
        }
    }

    /**
     * @return 0 if no user or device deleted
     *         1 if user deleted but no device deleted
     *         2 if user and device(s) deleted
    * */
    public int removeUser(User user)    {
        try(Connection conn = this.databaseConnector.getConnection()) {

            // You need to delete the devices first, or else the user doesn't exist for
            // it to be deleted?
            PreparedStatement pstmt = conn.prepareStatement(
                    "delete from " + deviceTableName + " where username = ?"
            );

            pstmt.setString(1, user.username());
            int deviceRowsDeleted = pstmt.executeUpdate();

            PreparedStatement pstmt2 = conn.prepareStatement(
                    "delete from " + tableName + " where username = ?"
            );

            pstmt2.setString(1, user.username());
            int rowsDeleted = pstmt2.executeUpdate();

            // this should return true if it successfully deletes,
            // although it might not throw an error, I'm not sure
            return rowsDeleted > 0 ? (deviceRowsDeleted > 0 ? 2 : 1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("removeUser error\n" + e);
        }
    }

    public User getUser(String username) throws RuntimeException {
        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("select username,ishome,poolissupervised from " + tableName + " where username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                boolean isHome = rs.getBoolean("ishome");
                boolean poolIsSupervised = rs.getBoolean("poolissupervised");

                return new User(
                        username,
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
            PreparedStatement pstmt = conn.prepareStatement("select username,ishome,poolissupervised from  " + tableName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                boolean isHome = rs.getBoolean("ishome");
                boolean poolIsSupervised = rs.getBoolean("poolissupervised");
                Users.add(new User(username, isHome, poolIsSupervised));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Users;
    }
}

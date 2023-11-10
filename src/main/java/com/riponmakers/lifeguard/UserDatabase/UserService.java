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
    private final String tableName;

    public UserService(DatabaseConnector dbc, String tableName) {
        databaseConnector = dbc;
        this.tableName = tableName;
//        tryCreateUsersTable();
    }

    public void createUser(User user) {
        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                        "insert into \"" + tableName + "\"(username,ishome,poolissupervised) values(?,?,?)"
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
    public void removeUser(User user, DeviceService deviceService, NeighborService neighborService)    {
        try(Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt2 = conn.prepareStatement(
                    "delete from " + tableName + " where username = ?"
            );

            pstmt2.setString(1, user.username());
            int rowsDeleted = pstmt2.executeUpdate();

            deviceService.removeDevices(user.username());
            neighborService.removeNeighbors(user.username());

            // this should return true if it successfully deletes,
            // although it might not throw an error, I'm not sure
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

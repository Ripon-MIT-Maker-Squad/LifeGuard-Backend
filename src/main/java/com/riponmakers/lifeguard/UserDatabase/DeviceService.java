package com.riponmakers.lifeguard.UserDatabase;

import com.riponmakers.lifeguard.JSONRecords.Device;
import com.riponmakers.lifeguard.JSONRecords.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceService {
    private final DatabaseConnector databaseConnector;
    private final String databaseName;
    private final String deviceTableName, userTableName;

    public DeviceService(DatabaseConnector dbc, String databaseName, String deviceTableName, String userDatabaseName) {
        databaseConnector = dbc;
        this.databaseName = databaseName;
        this.deviceTableName = deviceTableName;
        this.userTableName = userDatabaseName;
//        tryCreateUsersTable();
    }

    public void onboardDevice(Device device) {
        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into " + deviceTableName + "(username,deviceid) values(?,?)"
            );

            pstmt.setString(1, device.username());
            pstmt.setLong(2, device.deviceID());

            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("onboarding device error\n" + e);
        }
    }

    public void removeDevices(String username) {
        try(Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt2 = conn.prepareStatement(
                    "delete from " + deviceTableName + " where username = ?"
            );

            pstmt2.setString(1, username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Device getDevice(String deviceID) throws RuntimeException {
        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("select username from " + deviceTableName + " where deviceID = ?");
            pstmt.setLong(1, Long.parseLong(deviceID));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");

                return new Device(
                        Long.parseLong(deviceID),
                        username);
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
            PreparedStatement pstmt = conn.prepareStatement("select username,ishome,poolissupervised from  " + userTableName);
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

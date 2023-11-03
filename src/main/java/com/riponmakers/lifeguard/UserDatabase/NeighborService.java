package com.riponmakers.lifeguard.UserDatabase;

import com.riponmakers.lifeguard.JSONRecords.Neighbor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NeighborService {
    private final DatabaseConnector databaseConnector;
    private final String databaseName;
    private final String tableName;

    public NeighborService(DatabaseConnector dbc, String dbName, String tbName) {
        databaseConnector = dbc;
        databaseName = dbName;
        tableName = tbName;
    }

    public void createNeighbor(Neighbor neighbor) {
        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into " + tableName + "(id, phoneNumber, username) values(?,?,?)"
            );

            pstmt.setString(1, neighbor.id());
            pstmt.setString(2, neighbor.phoneNumber());
            pstmt.setString(3, neighbor.username());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error when creating a neighbor\n" + e);
        }
    }

    public ArrayList<Neighbor> getNeighborsByUsername(String username) {
        try (Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("select phonenumber from " + tableName + " where username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<Neighbor> returnThis = new ArrayList<>();

            while (rs.next()) {
                String phoneNumber = rs.getString("phonenumber");
                String id = username + phoneNumber;

                returnThis.add(new Neighbor(
                        id,
                        phoneNumber,
                        username));
            }

            // Close the ResultSet and statement when done
            rs.close();
            pstmt.close();
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("getuser error\n" + e);
        }
    }

    public void removeNeighbor(String username, String phoneNumber) {
        try(Connection conn = this.databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(
              "delete from " + tableName + " where id = ?"
            );

            preparedStatement.setString(1, username + phoneNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeNeighbors(String username) {
        try(Connection conn = this.databaseConnector.getConnection()) {

            // You need to delete the devices first, or else the user doesn't exist for
            // it to be deleted?
            PreparedStatement pstmt = conn.prepareStatement(
                    "delete from " + tableName + " where username = ?"
            );

            pstmt.setString(1, username);
            pstmt.executeUpdate();

            // this should return true if it successfully deletes,
            // although it might not throw an error, I'm not sure
        } catch (SQLException e) {
            throw new RuntimeException("removeUser error\n" + e);
        }
    }
}

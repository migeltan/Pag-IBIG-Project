package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;

public class AdminDAO {

    /**
     * Returns the admin's display name if credentials are valid, null otherwise.
     */
    public String verifyAdmin(String username, String password) {
        String sql = "SELECT Admin_Name FROM admincredentials WHERE Username = ? AND Password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getString("Admin_Name");

        } catch (SQLException e) {
            System.err.println("[AdminDAO] verifyAdmin error: " + e.getMessage());
        }
        return null;
    }
}
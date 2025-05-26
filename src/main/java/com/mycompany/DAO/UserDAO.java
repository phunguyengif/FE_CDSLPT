/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.DAO;

import com.mycompany.DatabaseHelper.DatabaseConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author ntphu
 */
public class UserDAO {
    private final String branch;

    public UserDAO(String branch) {
        this.branch = branch;
    }

    public boolean authenticate(String username, String password) throws SQLException {
        String query = "SELECT Password FROM NguoiDung WHERE Username = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection(branch);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                return dbPassword.equals(password); // Thay bằng so sánh băm nếu lưu mật khẩu dạng băm
            }
        }
        return false;
    }
}

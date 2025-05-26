/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.DAO;

import com.mycompany.DatabaseHelper.DatabaseConnectionManager;
import com.mycompany.model.DangKy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ntphu
 */
public class DangKyDAO {

    private String branch; // dùng để lấy kết nối đúng chi nhánh

    public DangKyDAO(String branch) {
        this.branch = branch;
    }

    // Thêm đăng ký mới (student đăng ký lớp)
    public boolean addDangKy(DangKy dk) throws SQLException {
        String query = "INSERT INTO DangKy (StudentID, ClassID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnectionManager.getConnection("central"); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dk.getStudentID());
            stmt.setString(2, dk.getClassID());
            return stmt.executeUpdate() > 0;
        }
    }

    // Xóa đăng ký (student hủy đăng ký lớp)
    public boolean deleteDangKy(String studentID, String classID) throws SQLException {
        String query = "DELETE FROM DangKy WHERE StudentID = ? AND ClassID = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection("central"); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentID);
            stmt.setString(2, classID);
            return stmt.executeUpdate() > 0;
        }
    }

    // Kiểm tra xem sinh viên đã đăng ký lớp chưa
    public boolean isStudentRegisteredInClass(String studentID, String classID) throws SQLException {
        String query = "SELECT COUNT(*) FROM DangKy WHERE StudentID = ? AND ClassID = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection(branch); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentID);
            stmt.setString(2, classID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        }
    }

    // Lấy danh sách đăng ký theo StudentID
    public List<DangKy> getDangKyByStudent(String studentID) throws SQLException {
        String query = "SELECT StudentID, ClassID FROM DangKy WHERE StudentID = ?";
        List<DangKy> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection(branch); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DangKy dk = new DangKy(rs.getString("StudentID"), rs.getString("ClassID"));
                    list.add(dk);
                }
            }
        }
        return list;
    }

    // Lấy danh sách đăng ký theo ClassID
    public List<DangKy> getDangKyByClass(String classID) throws SQLException {
        String query = "SELECT StudentID, ClassID FROM DangKy WHERE ClassID = ?";
        List<DangKy> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection(branch); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, classID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DangKy dk = new DangKy(rs.getString("StudentID"), rs.getString("ClassID"));
                    list.add(dk);
                }
            }
        }
        return list;
    }
}

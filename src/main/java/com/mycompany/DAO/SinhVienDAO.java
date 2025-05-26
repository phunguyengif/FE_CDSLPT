/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.DAO;

import com.mycompany.DatabaseHelper.DatabaseConnectionManager;
import com.mycompany.model.SinhVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ntphu
 */
public class SinhVienDAO {

    private final String branch;

    public SinhVienDAO(String branch) {
        this.branch = branch;
    }

    public boolean authenticate(String username, String password) throws Exception {
        // Kết nối cơ sở dữ liệu theo branch
        Connection conn = DatabaseConnectionManager.getConnection(branch);
        String query = "SELECT * FROM SinhVien WHERE StudentID = ? AND FullName = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public boolean isStudentIDDuplicateWithLinkedServer(String studentID) throws Exception {
        String query = "SELECT COUNT(*) FROM LINK.QLTH.DBO.SinhVien WHERE StudentID = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection("Bắc"); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentID);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Thêm sinh viên mới
    public boolean addSinhVien(SinhVien sv) throws Exception {
        if (isStudentIDDuplicateWithLinkedServer(sv.getStudentID())) {
            throw new Exception("StudentID đã tồn tại.");
        }

        String query = "INSERT INTO SinhVien (StudentID, FullName, BirthDate, Gender, Address, BranchID) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionManager.getConnection("central"); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sv.getStudentID());
            stmt.setString(2, sv.getFullName());
            stmt.setString(3, sv.getBirthDate());
            stmt.setString(4, sv.getGender());
            stmt.setString(5, sv.getAddress());
            stmt.setString(6, sv.getBranchID());

            return stmt.executeUpdate() > 0;
        }
    }

    // Sửa thông tin sinh viên
    public boolean updateSinhVien(SinhVien sv) throws Exception {
        if (isStudentIDDuplicateWithLinkedServer(sv.getStudentID())) {
            throw new Exception("StudentID đã tồn tại.");
        }
        String query = "UPDATE SinhVien SET FullName = ?, BirthDate = ?, Gender = ?, Address = ?, BranchID = ? WHERE StudentID = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection("central"); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sv.getFullName());
            stmt.setString(2, sv.getBirthDate());
            stmt.setString(3, sv.getGender());
            stmt.setString(4, sv.getAddress());
            stmt.setString(5, sv.getBranchID());
            stmt.setString(6, sv.getStudentID());

            return stmt.executeUpdate() > 0; // Trả về true nếu cập nhật thành công
        }
    }

    // Xóa sinh viên theo StudentID
    public boolean deleteSinhVien(String studentID) throws Exception {
        // Câu truy vấn kiểm tra xem sinh viên đã đăng ký lớp học chưa
        String checkQuery = "SELECT COUNT(*) FROM DangKy WHERE StudentID = ?";
        // Câu truy vấn xóa sinh viên
        String deleteQuery = "DELETE FROM SinhVien WHERE StudentID = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection("central")) {
            // Kiểm tra ràng buộc
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, studentID);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Nếu sinh viên đã đăng ký lớp học
                        throw new Exception("Sinh viên đã đăng ký lớp học và không thể xóa.");
                    }
                }
            }

            // Xóa sinh viên
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setString(1, studentID);
                return deleteStmt.executeUpdate() > 0; // Trả về true nếu xóa thành công
            }
        }
    }

    // Lấy danh sách tất cả sinh viên
    public List<SinhVien> getAllSinhVien() throws Exception {
        List<SinhVien> list = new ArrayList<>();
        String query = "SELECT * FROM SinhVien";
        try (Connection conn = DatabaseConnectionManager.getConnection(branch); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                SinhVien sv = new SinhVien();
                sv.setStudentID(rs.getString("StudentID"));
                sv.setFullName(rs.getString("FullName"));
                sv.setBirthDate(rs.getString("BirthDate"));
                sv.setGender(rs.getString("Gender"));
                sv.setAddress(rs.getString("Address"));
                sv.setBranchID(rs.getString("BranchID"));

                list.add(sv);
            }
        }
        return list;
    }

    // Lấy thông tin sinh viên theo StudentID
    public SinhVien getSinhVienByID(String studentID) throws Exception {
        String query = "SELECT * FROM SinhVien WHERE StudentID = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection("central"); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new SinhVien(
                            rs.getString("StudentID"),
                            rs.getString("FullName"),
                            rs.getString("BirthDate"),
                            rs.getString("Gender"),
                            rs.getString("Address"),
                            rs.getString("BranchID")
                    );
                }
            }
        }
        return null; // Trả về null nếu không tìm thấy sinh viên
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.DAO;

import com.mycompany.DatabaseHelper.DatabaseConnectionManager;
import com.mycompany.model.LopHoc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ntphu
 */
public class LopHocDAO {

    private final String branch;

    public LopHocDAO(String branch) {
        this.branch = branch;
    }

    /**
     * Get all LopHoc records from the database.
     *
     * @return List of LopHoc
     * @throws Exception if a database access error occurs
     */
    public List<LopHoc> getAllClasses() throws Exception {
        List<LopHoc> lstLopHoc = new ArrayList<>();
        String query = "SELECT * FROM LopHoc";

        try (Connection conn = DatabaseConnectionManager.getConnection(branch); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                LopHoc lop = new LopHoc();
                lop.setClassID(rs.getString("ClassID"));
                lop.setClassName(rs.getString("ClassName"));
                lop.setCourseID(rs.getString("CourseID"));
                lop.setBranchID(rs.getString("BranchID"));
                lstLopHoc.add(lop);
            }
        }
        return lstLopHoc;
    }

    public LopHoc getClassByID(String classID) throws Exception {
        String query = "SELECT * FROM LopHoc WHERE ClassID = ?";
        LopHoc lopHoc = null;

        try (Connection conn = DatabaseConnectionManager.getConnection(branch); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, classID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    lopHoc = new LopHoc();
                    lopHoc.setClassID(rs.getString("ClassID"));
                    lopHoc.setClassName(rs.getString("ClassName"));
                    lopHoc.setCourseID(rs.getString("CourseID"));
                    lopHoc.setBranchID(rs.getString("BranchID"));
                }
            }
        }
        return lopHoc;
    }

    public boolean addClass(LopHoc lop) throws Exception {
        // SQL kiểm tra sự tồn tại của ClassID
        String checkQuery = "SELECT COUNT(*) FROM LopHoc WHERE ClassID = ?";
        String insertQuery = "INSERT INTO LopHoc (ClassID, ClassName, CourseID, BranchID) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnectionManager.getConnection("central")) {
            // Kiểm tra xem ClassID đã tồn tại chưa
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, lop.getClassID());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Nếu ClassID đã tồn tại, không thêm mới
                        System.out.println("ClassID đã tồn tại, không thể thêm lớp học.");
                        return false;
                    }
                }
            }

            // Thêm lớp học mới
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, lop.getClassID());
                insertStmt.setString(2, lop.getClassName());
                insertStmt.setString(3, lop.getCourseID());
                insertStmt.setString(4, lop.getBranchID());

                return insertStmt.executeUpdate() > 0;
            }
        }
    }

    public boolean updateClass(LopHoc lop) throws Exception {
        // Kiểm tra xem ClassID có tồn tại trong cơ sở dữ liệu không
        String checkQuery = "SELECT COUNT(*) FROM LopHoc WHERE ClassID = ?";
        String updateQuery = "UPDATE LopHoc SET ClassName = ?, CourseID = ?, BranchID = ? WHERE ClassID = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection("central")) {
            // Kiểm tra tồn tại
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, lop.getClassID());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        throw new Exception("ClassID không tồn tại. Không thể cập nhật lớp học.");
                    }
                }
            }

            // Thực hiện cập nhật
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, lop.getClassName());
                updateStmt.setString(2, lop.getCourseID());
                updateStmt.setString(3, lop.getBranchID());
                updateStmt.setString(4, lop.getClassID());

                return updateStmt.executeUpdate() > 0;
            }
        }
    }

//    public boolean updateClass(LopHoc lop) throws Exception {
//        // Kiểm tra xem ClassID có tồn tại trong cơ sở dữ liệu không
//        String checkQuery = "SELECT COUNT(*) FROM LopHoc WHERE ClassID = ?";
//        String updateQuery = "UPDATE LopHoc SET ClassName = ?, CourseID = ?, BranchID = ? WHERE ClassID = ?";
//
//        try (Connection conn = DatabaseConnectionManager.getConnection("central")) {
//            // Kiểm tra tồn tại
//            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
//                checkStmt.setString(1, lop.getClassID());
//                try (ResultSet rs = checkStmt.executeQuery()) {
//                    if (rs.next() && rs.getInt(1) > 0) {
//                        // Nếu ClassID đã tồn tại, không thêm mới
//                        System.out.println("ClassID đã tồn tại, không thể thêm lớp học.");
//                        return false;
//                    }
//                }
//            }
//
//            // Thực hiện cập nhật
//            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
//                updateStmt.setString(1, lop.getClassName());
//                updateStmt.setString(2, lop.getCourseID());
//                updateStmt.setString(3, lop.getBranchID());
//                updateStmt.setString(4, lop.getClassID());
//
//                return updateStmt.executeUpdate() > 0;
//            }
//        }
//    }
    public boolean deleteClass(String classID) throws Exception {
        // SQL kiểm tra xem lớp học đã có sinh viên đăng ký chưa
        String checkQuery = "SELECT COUNT(*) FROM DangKy WHERE ClassID = ?";
        String deleteQuery = "DELETE FROM LopHoc WHERE ClassID = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection(branch)) {
            // Kiểm tra xem lớp học đã có sinh viên đăng ký chưa
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, classID);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // Nếu lớp học đã có sinh viên đăng ký, không cho phép xóa
                        System.out.println("Lớp học đã có sinh viên đăng ký, không thể xóa.");
                        return false;
                    }
                }
            }

            // Thực hiện xóa nếu lớp học không có sinh viên đăng ký
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setString(1, classID);
                return deleteStmt.executeUpdate() > 0;
            }
        }
    }
}

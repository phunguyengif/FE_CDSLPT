/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.DAO;

import com.mycompany.DatabaseHelper.DatabaseConnectionManager;
import com.mycompany.model.MonHoc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ntphu
 */
public class MonHocDAO {

    private final String branch;

    public MonHocDAO(String branch) {
        this.branch = branch;
    }

    // Lấy danh sách tất cả môn học
    public List<MonHoc> getAllCourses() throws SQLException {
        String query = "SELECT CourseID, CourseName, Credit FROM MonHoc";
        List<MonHoc> courses = new ArrayList<>();

        try (Connection conn = DatabaseConnectionManager.getConnection(branch); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                MonHoc course = new MonHoc();
                course.setCourseID(rs.getString("CourseID"));
                course.setCourseName(rs.getString("CourseName"));
                course.setCredits(rs.getInt("Credit"));
                courses.add(course);
            }
        }
        return courses;
    }

    // Thêm môn học mới
    public boolean addCourse(MonHoc course) throws SQLException {
        String query = "INSERT INTO MonHoc (CourseID, CourseName, Credit) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnectionManager.getConnection(branch); PreparedStatement stmt = conn.prepareStatement(query);) {

            stmt.setString(1, course.getCourseID());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getCredit());

            return stmt.executeUpdate() > 0;
        }
    }

    // Cập nhật môn học
    public boolean updateCourse(MonHoc course) throws SQLException {
        String query = "UPDATE MonHoc SET CourseName = ?, Credit = ? WHERE CourseID = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection(branch); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, course.getCourseName());
            stmt.setInt(2, course.getCredit());
            stmt.setString(3, course.getCourseID());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteCourse(String courseId) throws SQLException {
        // Truy vấn kiểm tra xem CourseID có được liên kết với lớp học nào không
        String checkQuery = "SELECT COUNT(*) FROM LopHoc WHERE CourseID = ?";
        String deleteQuery = "DELETE FROM MonHoc WHERE CourseID = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection(branch)) {
            // Kiểm tra sự tồn tại của lớp học liên quan
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, courseId);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("Môn học này đã được đăng kí lớp học. Không thể xóa.");
                    }
                }
            }

            // Thực hiện xóa nếu không có liên kết
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setString(1, courseId);
                return deleteStmt.executeUpdate() > 0;
            }
        }
    }

    // Lấy môn học theo CourseID
    public MonHoc getCourseById(String courseId) throws SQLException {
        String query = "SELECT CourseID, CourseName, Credit FROM MonHoc WHERE CourseID = ?";
        MonHoc course = null;
        try (Connection conn = DatabaseConnectionManager.getConnection(branch); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    course = new MonHoc();
                    course.setCourseID(rs.getString("CourseID"));
                    course.setCourseName(rs.getString("CourseName"));
                    course.setCredits(rs.getInt("Credit"));
                }
            }
        }
        return course;
    }
}

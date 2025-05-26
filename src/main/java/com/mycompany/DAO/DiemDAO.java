package com.mycompany.DAO;

import com.mycompany.DatabaseHelper.DatabaseConnectionManager;
import com.mycompany.model.Diem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing "Diem" (Grades) data.
 */
public class DiemDAO {

    private final String branch;

    public DiemDAO(String branch) {
        this.branch = branch;
    }

    // Lấy danh sách điểm theo mã lớp
    public List<Diem> getByClassID(String classID) throws Exception {
        List<Diem> lstDiem = new ArrayList<>();
        String sql = "SELECT * FROM Diem WHERE ClassID = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection(branch); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, classID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Diem diem = new Diem();
                    diem.setStudentID(rs.getString("StudentID"));
                    diem.setFullName(rs.getString("FullName"));
                    diem.setClassID(rs.getString("ClassID"));
                    diem.setMidtermScore(rs.getFloat("MidTermScore"));
                    diem.setFinalScore(rs.getFloat("FinalScore"));
                    diem.setAttendanceScore(rs.getFloat("AttendanceScore"));
                    lstDiem.add(diem);
                }
            }
        }
        return lstDiem;
    }

    // Lấy tất cả điểm
    public List<Diem> getAll() throws Exception {
        List<Diem> lstDiem = new ArrayList<>();
        String sql = "SELECT * FROM Diem";
        try (Connection con = DatabaseConnectionManager.getConnection(branch); Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Diem diem = new Diem();
                diem.setStudentID(rs.getString("StudentID"));
                diem.setClassID(rs.getString("ClassID"));
                lstDiem.add(diem);
            }
        }
        return lstDiem;
    }

    // Sửa điểm
    public boolean updateDiem(Diem diem) throws Exception {
        String query = "UPDATE Diem SET FullName = ?, ClassID = ?, MidtermScore = ?, FinalScore = ?, AttendanceScore = ? WHERE StudentID = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection("central"); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, diem.getFullName());
            stmt.setString(2, diem.getClassID());
            stmt.setFloat(3, diem.getMidtermScore());
            stmt.setFloat(4, diem.getFinalScore());
            stmt.setFloat(5, diem.getAttendanceScore());
            stmt.setString(6, diem.getStudentID());

//            System.out.println("Executing SQL: " + query);
//            System.out.println("With Parameters: "
//                    + "FullName=" + diem.getFullName()
//                    + ", ClassID=" + diem.getClassID()
//                    + ", MidtermScore=" + diem.getMidtermScore()
//                    + ", FinalScore=" + diem.getFinalScore()
//                    + ", AttendanceScore=" + diem.getAttendanceScore()
//                    + ", StudentID=" + diem.getStudentID());

            // Thực thi câu lệnh
            int rowsUpdated = stmt.executeUpdate();

            // Log số hàng bị ảnh hưởng
            System.out.println("Rows updated: " + rowsUpdated);

            return stmt.executeUpdate() > 0;
        }
    }

    public Diem getDiemByStudentID(String studentID) throws Exception {
        String query = "SELECT * FROM Diem WHERE StudentID = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection(branch); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Diem diem = new Diem();
                    diem.setStudentID(rs.getString("StudentID"));
                    diem.setFullName(rs.getString("FullName"));
                    diem.setClassID(rs.getString("ClassID"));
                    diem.setMidtermScore(rs.getFloat("MidtermScore"));
                    diem.setFinalScore(rs.getFloat("FinalScore"));
                    diem.setAttendanceScore(rs.getFloat("AttendanceScore"));
                    return diem;
                }
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }

}

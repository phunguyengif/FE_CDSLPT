/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

/**
 *
 * @author ntphu
 */
public class Diem {
    private int GradeID;
    private String StudentID;
    private String FullName;
    private String ClassID;
    private float MidtermScore;
    private float FinalScore;
    private float AttendanceScore;

    public Diem() {
    }

    public Diem(String ClassID) {
        this.ClassID = ClassID;
    }
    public Diem(int GradeID, String StudentID, String ClassID, String FullName, float MidtermScore, float FinalScore, float AttendanceScore) {
        this.GradeID = GradeID;
        this.StudentID = StudentID;
        this.FullName = FullName;
        this.ClassID = ClassID;
        this.MidtermScore = MidtermScore;
        this.FinalScore = FinalScore;
        this.AttendanceScore = AttendanceScore;
    }

    public int getGradeID() {
        return GradeID;
    }

    public void setGradeID(int GradeID) {
        this.GradeID = GradeID;
    }
    

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String StudentID) {
        this.StudentID = StudentID;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String FullName) {
        this.FullName = FullName;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String ClassID) {
        this.ClassID = ClassID;
    }

    public float getMidtermScore() {
        return MidtermScore;
    }

    public void setMidtermScore(float MidtermScore) {
        this.MidtermScore = MidtermScore;
    }

    public float getFinalScore() {
        return FinalScore;
    }

    public void setFinalScore(float FinalScore) {
        this.FinalScore = FinalScore;
    }

    public float getAttendanceScore() {
        return AttendanceScore;
    }

    public void setAttendanceScore(float AttendanceScore) {
        this.AttendanceScore = AttendanceScore;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

/**
 *
 * @author ntphu
 */
public class MonHoc {
    private String CourseID;
    private String CourseName;
    private int Credit;

    public MonHoc() {
    }

    public MonHoc(String CourseID, String CourseName) {
        this.CourseID = CourseID;
        this.CourseName = CourseName;
    }

    public MonHoc(String CourseID, String CourseName, int Credit) {
        this.CourseID = CourseID;
        this.CourseName = CourseName;
        this.Credit = Credit;
    }

    public String getCourseID() {
        return CourseID;
    }

    public void setCourseID(String CourseID) {
        this.CourseID = CourseID;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String CourseName) {
        this.CourseName = CourseName;
    }

    public int getCredit() {
        return Credit;
    }

    public void setCredits(int Credit) {
        this.Credit = Credit;
    }

    @Override
    public String toString(){
        return  CourseID + " " + CourseName;
    }
}

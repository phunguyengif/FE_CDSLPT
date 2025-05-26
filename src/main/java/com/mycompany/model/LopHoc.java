/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

/**
 *
 * @author ntphu
 */
public class LopHoc {
    private String ClassID;
    private String ClassName;
    private String CourseID;
    private String BranchID;

    public LopHoc() {
    }

    public LopHoc(String ClassID, String ClassName, String CourseID, String BranchID) {
        this.ClassID = ClassID;
        this.ClassName = ClassName;
        this.CourseID = CourseID;
        this.BranchID = BranchID;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String ClassID) {
        this.ClassID = ClassID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String ClassName) {
        this.ClassName = ClassName;
    }

    public String getCourseID() {
        return CourseID;
    }

    public void setCourseID(String CourseID) {
        this.CourseID = CourseID;
    }

    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String BranchID) {
        this.BranchID = BranchID;
    }
    
}

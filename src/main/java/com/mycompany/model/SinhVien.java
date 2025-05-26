/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

/**
 *
 * @author ntphu
 */
public class SinhVien {
    private String StudentID;
    private String FullName;
    private String BirthDate;
    private String Gender;
    private String Address;
    private String BranchID;

    public SinhVien() {
    }

    public SinhVien(String StudentID, String FullName, String BirthDate, String Gender, String Address, String BranchID) {
        this.StudentID = StudentID;
        this.FullName = FullName;
        this.BirthDate = BirthDate;
        this.Gender = Gender;
        this.Address = Address;
        this.BranchID = BranchID;
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

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String BirthDate) {
        this.BirthDate = BirthDate;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getBranchID() {
        return BranchID;
    }

    public void setBranchID(String BranchID) {
        this.BranchID = BranchID;
    }
    
    
}

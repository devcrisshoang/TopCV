package com.example.topcv.model;

public class SortOfUser {
    private int ID_SortOfUser;
    private int ID_Applicant;  // FK to Applicant
    private int ID_User;           // FK to User

    // Constructor
    public SortOfUser(int ID_SortOfUser, int ID_User, int ID_Applicant) {
        this.ID_SortOfUser = ID_SortOfUser;
        this.ID_Applicant = ID_Applicant; // Đặt giá trị mặc định cho ID_Applicant
        this.ID_User = ID_User;
    }

    public SortOfUser() {

    }

    // Getters and Setters
    public int getID_SortOfUser() {
        return ID_SortOfUser;
    }

    public void setID_SortOfUser(int ID_SortOfUser) {
        this.ID_SortOfUser = ID_SortOfUser;
    }

    public Integer getID_Applicant() {
        return ID_Applicant;
    }

    public void setID_Applicant(Integer ID_Applicant) {
        this.ID_Applicant = ID_Applicant;
    }

    public int getID_User() {
        return ID_User;
    }

    public void setID_User(int ID_User) {
        this.ID_User = ID_User;
    }
}

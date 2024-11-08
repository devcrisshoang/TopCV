package com.example.topcv.model;

public class Applicant {
    private int id;  // ID của Applicant, có thể được tự động tạo
    private int id_User;  // ID_User là khóa ngoại từ User
    private String applicant_Name;
    private String phone_Number;
    private String email; // Tùy chọn, có thể để trống
    private String job_Desire; // Tùy chọn, có thể để trống
    private String working_Location_Desire; // Tùy chọn, có thể để trống
    private String working_Experience; // Tùy chọn, có thể để trống

    // Constructor cho tên và số điện thoại
    public Applicant(String applicant_Name, String phone_Number, int id_User) {
        this.applicant_Name = applicant_Name;
        this.phone_Number = phone_Number;
        this.id_User = id_User;
        this.email = null; // Đặt mặc định là null
        this.job_Desire = null; // Đặt mặc định là null
        this.working_Location_Desire = null; // Đặt mặc định là null
        this.working_Experience = null; // Đặt mặc định là null
    }

    // Constructor với tất cả các tham số
    public Applicant(int id, String applicant_Name, String phone_Number, String email, String job_Desire, String working_Location_Desire, String working_Experience, int id_User) {
        this.id = id;
        this.applicant_Name = applicant_Name;
        this.phone_Number = phone_Number;
        this.email = email;
        this.job_Desire = job_Desire;
        this.working_Location_Desire = working_Location_Desire;
        this.working_Experience = working_Experience;
        this.id_User = id_User;
    }

    // Default constructor
    public Applicant() {
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_User() {
        return id_User;
    }

    public void setId_User(int id_User) {
        this.id_User = id_User;
    }

    public String getApplicant_Name() {
        return applicant_Name;
    }

    public void setApplicant_Name(String applicant_Name) {
        this.applicant_Name = applicant_Name;
    }

    public String getPhone_Number() {
        return phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        this.phone_Number = phone_Number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob_Desire() {
        return job_Desire;
    }

    public void setJob_Desire(String job_Desire) {
        this.job_Desire = job_Desire;
    }

    public String getWorking_Location_Desire() {
        return working_Location_Desire;
    }

    public void setWorking_Location_Desire(String working_Location_Desire) {
        this.working_Location_Desire = working_Location_Desire;
    }

    public String getWorking_Experience() {
        return working_Experience;
    }

    public void setWorking_Experience(String working_Experience) {
        this.working_Experience = working_Experience;
    }
}

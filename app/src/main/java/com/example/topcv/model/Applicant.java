package com.example.topcv.model;

public class Applicant {
    private int id;  // Optional, can be auto-generated
    private String applicant_Name;
    private String phone_Number;
    private String email; // Optional, can be null
    private String job_Desire; // Optional, can be null
    private String working_Location_Desire; // Optional, can be null
    private String working_Experience; // Optional, can be null

    // Constructor for name and phone number
    public Applicant(String applicant_Name, String phone_Number) {
        this.applicant_Name = applicant_Name;
        this.phone_Number = phone_Number;
        this.email = null; // Set to null by default
        this.job_Desire = null; // Set to null by default
        this.working_Location_Desire = null; // Set to null by default
        this.working_Experience = null; // Set to null by default
    }

    // Constructor with all parameters
    public Applicant(int id, String applicant_Name, String phone_Number, String email, String job_Desire, String working_Location_Desire, String working_Experience) {
        this.id = id;
        this.applicant_Name = applicant_Name;
        this.phone_Number = phone_Number;
        this.email = email;
        this.job_Desire = job_Desire;
        this.working_Location_Desire = working_Location_Desire;
        this.working_Experience = working_Experience;
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

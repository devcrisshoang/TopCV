package com.example.topcv.model;

import com.google.gson.annotations.SerializedName;

public class Applicant {

    @SerializedName("id")
    private int id;

    @SerializedName("applicant_Name")
    private String applicantName;

    @SerializedName("phone_Number")
    private String phoneNumber;

    @SerializedName("email")
    private String email;

    @SerializedName("job_Desire")
    private String jobDesire;

    @SerializedName("working_Location_Desire")
    private String workingLocationDesire;

    @SerializedName("working_Experience")
    private String workingExperience;

    @SerializedName("iD_User")
    private int iD_User;

    @SerializedName("is_Registered")
    private boolean is_Registered;

    public boolean isIs_Registered() {
        return is_Registered;
    }

    public void setIs_Registered(boolean is_Registered) {
        this.is_Registered = is_Registered;
    }

    public int getiD_User() {
        return iD_User;
    }

    public void setiD_User(int iD_User) {
        this.iD_User = iD_User;
    }

    public Applicant(int id, String applicantName, String phoneNumber, String email, String jobDesire, String workingLocationDesire, String workingExperience, boolean is_Registered) {
        this.id = id;
        this.applicantName = applicantName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.jobDesire = jobDesire;
        this.workingLocationDesire = workingLocationDesire;
        this.workingExperience = workingExperience;
        this.is_Registered = is_Registered;
    }

    public Applicant() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobDesire() {
        return jobDesire;
    }

    public void setJobDesire(String jobDesire) {
        this.jobDesire = jobDesire;
    }

    public String getWorkingLocationDesire() {
        return workingLocationDesire;
    }

    public void setWorkingLocationDesire(String workingLocationDesire) {
        this.workingLocationDesire = workingLocationDesire;
    }

    public String getWorkingExperience() {
        return workingExperience;
    }

    public void setWorkingExperience(String workingExperience) {
        this.workingExperience = workingExperience;
    }

}

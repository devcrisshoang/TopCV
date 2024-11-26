package com.example.topcv.model;

import com.google.gson.annotations.SerializedName;

public class JobDetail {

    @SerializedName("id")
    private int id;

    @SerializedName("job_Description")
    private String jobDescription;

    @SerializedName("skill_Require")
    private String skillRequire;

    @SerializedName("benefit")
    private String benefit;

    @SerializedName("gender_Require")
    private String genderRequire;

    @SerializedName("working_Time")
    private String workingTime;

    @SerializedName("working_Method")
    private String workingMethod;

    @SerializedName("working_Position")
    private String workingPosition;

    @SerializedName("number_Of_People")
    private String numberOfPeople;

    @SerializedName("iD_Job")
    private int idJob;

    public JobDetail(int id, String jobDescription, String skillRequire, String benefit, String genderRequire, String workingTime, String workingMethod, String workingPosition, String numberOfPeople, int idJob) {
        this.id = id;
        this.jobDescription = jobDescription;
        this.skillRequire = skillRequire;
        this.benefit = benefit;
        this.genderRequire = genderRequire;
        this.workingTime = workingTime;
        this.workingMethod = workingMethod;
        this.workingPosition = workingPosition;
        this.numberOfPeople = numberOfPeople;
        this.idJob = idJob;
    }

    public String getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(String numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getSkillRequire() {
        return skillRequire;
    }

    public void setSkillRequire(String skillRequire) {
        this.skillRequire = skillRequire;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public String getGenderRequire() {
        return genderRequire;
    }

    public void setGenderRequire(String genderRequire) {
        this.genderRequire = genderRequire;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    public String getWorkingMethod() {
        return workingMethod;
    }

    public void setWorkingMethod(String workingMethod) {
        this.workingMethod = workingMethod;
    }

    public String getWorkingPosition() {
        return workingPosition;
    }

    public void setWorkingPosition(String workingPosition) {
        this.workingPosition = workingPosition;
    }

    public int getIdJob() {
        return idJob;
    }

    public void setIdJob(int idJob) {
        this.idJob = idJob;
    }
}


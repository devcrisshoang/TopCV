package com.example.topcv.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Job{
    @SerializedName("id")
    private int id;

    @SerializedName("image_Id")
    private String imageId;

    @SerializedName("job_Name")
    private String jobName;

    @SerializedName("company_Name")
    private String companyName;

    @SerializedName("working_Experience_Require")
    private String experience;

    @SerializedName("working_Address")
    private String location;

    @SerializedName("salary")
    private String salary;

    @SerializedName("create_Time")
    private String createTime;

    @SerializedName("application_Date")
    private String applicationDate;

    @SerializedName("application_Status")
    private boolean applicationStatus;

    @SerializedName("iD_Recruiter")
    private int recruiterId;

    public Job(String imageId, String jobName, String companyName, String experience, String location, String salary, String createTime, String applicationDate, boolean applicationStatus, int recruiterId) {
        this.imageId = imageId;
        this.jobName = jobName;
        this.companyName = companyName;
        this.experience = experience;
        this.location = location;
        this.salary = salary;
        this.createTime = createTime;
        this.applicationDate = applicationDate;
        this.applicationStatus = applicationStatus;
        this.recruiterId = recruiterId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public boolean isApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(boolean applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public int getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(int recruiterId) {
        this.recruiterId = recruiterId;
    }

    public Job(String imageId, String jobName, String companyName, String location, String salary) {
        this.imageId = imageId;
        this.jobName = jobName;
        this.companyName = companyName;
        this.location = location;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter và Setter
    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}

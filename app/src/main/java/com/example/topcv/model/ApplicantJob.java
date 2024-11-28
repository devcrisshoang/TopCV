package com.example.topcv.model;

import com.google.gson.annotations.SerializedName;

public class ApplicantJob {

    @SerializedName("id")
    private int id;

    @SerializedName("iD_Job")
    private int jobId;

    @SerializedName("iD_Applicant")
    private int applicantId;

    @SerializedName("iD_Resume")
    private int resumeId;

    @SerializedName("isAccepted")
    private boolean isAccepted;

    @SerializedName("isRejected")
    private boolean isRejected;

    @SerializedName("time")
    private String time;

    public ApplicantJob(int jobId, int applicantId, int resumeId, boolean isAccepted, boolean isRejected, String time) {
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.resumeId = resumeId;
        this.isAccepted = isAccepted;
        this.isRejected = isRejected;
        this.time = time;
    }

    public ApplicantJob() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public int getResumeId() {
        return resumeId;
    }

    public void setResumeId(int resumeId) {
        this.resumeId = resumeId;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}

package com.example.topcv.model;

import com.google.gson.annotations.SerializedName;

public class Resume {
    @SerializedName("id")
    private int id;

    @SerializedName("applicant_Name")
    private String applicant_name;

    @SerializedName("email")
    private String email;

    @SerializedName("phone_Number")
    private String phone_number;

    @SerializedName("education")
    private String education;

    @SerializedName("skills")
    private String skills;

    @SerializedName("certificate")
    private String certificate;

    @SerializedName("job_Applying")
    private String job_applying;

    @SerializedName("introduction")
    private String introduction;

    @SerializedName("image")
    private String image;

    @SerializedName("experience")
    private String experience;

    @SerializedName("iD_Applicant")
    private int id_applicant;

    @SerializedName("file_Path")
    private String file_path;

    public String getFile_path() {
        return file_path;
    }

    public Resume(String file_path, int id_applicant) {

        this.file_path = file_path;
        this.id_applicant = id_applicant;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public Resume() {

    }

    // Constructor
    public Resume(String applicant_name, String email, String phone_number, String education, String skills, String certificate, String job_applying, String introduction, String image, String experience, int id_applicant) {
        this.applicant_name = applicant_name;
        this.email = email;
        this.phone_number = phone_number;
        this.education = education;
        this.skills = skills;
        this.certificate = certificate;
        this.job_applying = job_applying;
        this.introduction = introduction;
        this.image = image;
        this.experience = experience;
        this.id_applicant = id_applicant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplicant_name() {
        return applicant_name;
    }

    public void setApplicant_name(String applicant_name) {
        this.applicant_name = applicant_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getJob_applying() {
        return job_applying;
    }

    public void setJob_applying(String job_applying) {
        this.job_applying = job_applying;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getId_applicant() {
        return id_applicant;
    }

    public void setId_applicant(int id_applicant) {
        this.id_applicant = id_applicant;
    }
}

package com.example.topcv.model;

import com.google.gson.annotations.SerializedName;

public class Resume {
    private int id;

    @SerializedName("applicant_Name") // Ánh xạ trường JSON
    private String applicant_name;

    @SerializedName("email") // Ánh xạ trường JSON
    private String email;

    @SerializedName("phone_Number") // Ánh xạ trường JSON
    private String phone_number;

    @SerializedName("education") // Ánh xạ trường JSON
    private String education;

    @SerializedName("skills") // Ánh xạ trường JSON
    private String skills;

    @SerializedName("certificate") // Ánh xạ trường JSON
    private String certificate;

    @SerializedName("job_Applying") // Ánh xạ trường JSON
    private String job_applying;

    @SerializedName("introduction") // Ánh xạ trường JSON
    private String introduction;

    @SerializedName("image") // Ánh xạ trường JSON
    private String image;

    @SerializedName("experience") // Ánh xạ trường JSON
    private String experience;

    @SerializedName("iD_Applicant") // Ánh xạ trường JSON
    private int id_applicant;

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

    // Getter and Setter methods
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

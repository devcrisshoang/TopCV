package com.example.topcv.model;

public class CV {
    private String name;
    private String email;
    private String education;
    private String experience;
    private String skills;
    private String certifications;
    private String objectives;

    // Constructor, getters và setters
    public CV(String name, String email, String education, String experience, String skills, String certifications, String objectives) {
        this.name = name;
        this.email = email;
        this.education = education;
        this.experience = experience;
        this.skills = skills;
        this.certifications = certifications;
        this.objectives = objectives;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getEducation() { return education; }
    public String getExperience() { return experience; }
    public String getSkills() { return skills; }
    public String getCertifications() { return certifications; }
    public String getObjectives() { return objectives; }
}


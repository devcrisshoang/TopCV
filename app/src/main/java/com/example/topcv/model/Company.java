package com.example.topcv.model;
public class Company {
    private String name;
    private String industry;
    private String badge;
    private int logo;

    public Company(String name, String industry, String badge, int logo) {
        this.name = name;
        this.industry = industry;
        this.badge = badge;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public String getIndustry() {
        return industry;
    }

    public String getBadge() {
        return badge;
    }

    public int getLogo() {
        return logo;
    }
}


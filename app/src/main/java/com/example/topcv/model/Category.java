package com.example.topcv.model;

import java.util.List;

public class Category {
    private String category_name;
    private List<Jobs> jobs_list;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public List<Jobs> getJobs_list() {
        return jobs_list;
    }

    public void setJobs_list(List<Jobs> jobs_list) {
        this.jobs_list = jobs_list;
    }

    public Category(String category_name, List<Jobs> jobs_list) {
        this.category_name = category_name;
        this.jobs_list = jobs_list;
    }
}

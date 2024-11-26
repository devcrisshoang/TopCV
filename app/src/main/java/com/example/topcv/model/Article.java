package com.example.topcv.model;

import com.google.gson.annotations.SerializedName;

public class Article {

    @SerializedName("id")
    private int id;

    @SerializedName("article_Name")
    private String name;

    @SerializedName("content")
    private String content;

    @SerializedName("create_Time")
    private String time;

    @SerializedName("image")
    private String image;

    @SerializedName("iD_Recruiter")
    private int idRecruiter;

    public Article(int id, String name, String content, String time, String image, int idRecruiter) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.time = time;
        this.image = image;
        this.idRecruiter = idRecruiter;
    }

    public Article(String name, String content, String time, String image) {
        this.name = name;
        this.content = content;
        this.time = time;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIdRecruiter() {
        return idRecruiter;
    }

    public void setIdRecruiter(int idRecruiter) {
        this.idRecruiter = idRecruiter;
    }
}

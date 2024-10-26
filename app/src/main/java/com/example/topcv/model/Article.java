package com.example.topcv.model;

import java.util.Date;

public class Article {
    private int id;
    private String name;
    private String content;
    private Date time;
    private String image;
    private int idRecruiter;

    public Article(String image, Date time, String content, String name) {
        this.image = image;
        this.time = time;
        this.content = content;
        this.name = name;
    }

    public int getIdRecruiter() {
        return idRecruiter;
    }

    public void setIdRecruiter(int idRecruiter) {
        this.idRecruiter = idRecruiter;
    }

    public Article(String name, String content, String image) {
        this.name = name;
        this.content = content;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

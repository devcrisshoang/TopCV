package com.example.topcv.model;

import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("id")  // ánh xạ với "id" trong JSON
    private int ID;

    @SerializedName("content")  // ánh xạ với "content" trong JSON
    private String Content;

    @SerializedName("time")  // ánh xạ với "time" trong JSON
    private String Time;

    @SerializedName("iD_User")  // ánh xạ với "iD_User" trong JSON
    private int ID_User;

    public Notification(int ID, String content, String time, int ID_User) {
        this.ID = ID;
        this.Content = content;
        this.Time = time;
        this.ID_User = ID_User;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getID_User() {
        return ID_User;
    }

    public void setID_User(int ID_User) {
        this.ID_User = ID_User;
    }
}

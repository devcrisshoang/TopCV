package com.example.topcv.model;

public class AppItem {
    private String name;
    private int imageResourceId;

    // Constructor
    public AppItem(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for image resource ID
    public int getImageResourceId() {
        return imageResourceId;
    }
}


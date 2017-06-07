package com.example.user.mediaplayer.model;

/**
 * Created by User on 6/6/2017.
 */

public class Audio {
    private String title;
    private double size;
    private String data;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Audio(String title, double size , String data) {
        this.title = title;
        this.size = size;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}

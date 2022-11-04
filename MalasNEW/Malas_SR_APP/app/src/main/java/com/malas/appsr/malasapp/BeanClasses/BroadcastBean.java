package com.malas.appsr.malasapp.BeanClasses;

/**
 * Created by Admin on 29-Dec-17.
 */

public class BroadcastBean {
    private String imageUrl;
    private String title,message,time;

    public BroadcastBean(String imageUrl, String title, String message,String time) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.message = message;
        this.time=time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

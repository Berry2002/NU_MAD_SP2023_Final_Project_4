package com.example.cs5520finalproject;

import java.sql.Time;
import java.util.Date;

public class Review {
    private String reviewer, title, comment;
    private long time;

    public Review() {

    }

    public Review(String reviewer, String title, String comment) {
        this.reviewer = reviewer;
        this.title = title;
        this.comment = comment;
        Date date = new Date();
        this.time = date.getTime();
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

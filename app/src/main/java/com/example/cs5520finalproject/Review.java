package com.example.cs5520finalproject;

import java.sql.Time;

public class Review {
    private String reviewer, title, comment;
    private Time time;

    public Review() {

    }

    public Review(String reviewer, String title, String comment, Time time) {
        this.reviewer = reviewer;
        this.comment = comment;
        this.time = time;
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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

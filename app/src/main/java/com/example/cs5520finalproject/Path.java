package com.example.cs5520finalproject;

import java.util.ArrayList;

/**
 * A Path object that represents a Path a User can complete.
 */
public class Path {
    private String location, subject, description, image, pathID, pathName;
    private ArrayList<Quest> quests;
    private ArrayList<Review> review;
    private int numQuests;

    public Path() {

    }

    public Path(String location, String subject, String description, String image, String pathID,
                String pathName, ArrayList<Quest> quests, ArrayList<Review> review) {
        this.location = location;
        this.subject = subject;
        this.description = description;
        this.image = image;
        this.quests = quests;
        this.review = review;
        this.pathID = pathID;
        this.pathName = pathName;
        this.numQuests = this.quests.size();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public void setQuests(ArrayList<Quest> quests) {
        this.quests = quests;
    }

    public ArrayList<Review> getReview() {
        return review;
    }

    public void setReview(ArrayList<Review> review) {
        this.review = review;
    }

    public int getNumQuests() {
        return numQuests;
    }

    public void setNumQuests(int numQuests) {
        this.numQuests = numQuests;
    }

    public String getPathID() {
        return pathID;
    }

    public void setPathID(String pathID) {
        this.pathID = pathID;
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }
}

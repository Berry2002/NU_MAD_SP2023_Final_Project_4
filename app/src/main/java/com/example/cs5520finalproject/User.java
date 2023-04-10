package com.example.cs5520finalproject;

import java.util.ArrayList;

public class User {
    private String email, password, displayName, profilePicture;
    private ArrayList<String> travelLog;
    private long startDate;
    private int exp;
    private Path currentPath;
    private ArrayList<String> completedPaths;
    private ArrayList<String> completedQuests;

    public User() {

    }

    public User(String email, String password, String displayName, String profilePicture,
                ArrayList<String> travelLog, long startDate, int exp, Path currentPath,
                ArrayList<String> completedPaths, ArrayList<String> completedQuests) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.profilePicture = profilePicture;
        this.travelLog = travelLog;
        this.startDate = startDate;
        this.exp = exp;
        this.currentPath = currentPath;
        this.completedPaths = completedPaths;
        this.completedQuests = completedQuests;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public ArrayList<String> getTravelLog() {
        return travelLog;
    }

    public void setTravelLog(ArrayList<String> travelLog) {
        this.travelLog = travelLog;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(Path currentPath) {
        this.currentPath = currentPath;
    }

    public ArrayList<String> getCompletedPaths() {
        return completedPaths;
    }

    public void setCompletedPaths(ArrayList<String> completedPaths) {
        this.completedPaths = completedPaths;
    }

    public ArrayList<String> getCompletedQuests() {
        return completedQuests;
    }

    public void setCompletedQuests(ArrayList<String> completedQuests) {
        this.completedQuests = completedQuests;
    }
}

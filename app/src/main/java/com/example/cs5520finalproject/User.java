package com.example.cs5520finalproject;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private String email, password, displayName, profilePicture;
    private ArrayList<String> travelLog;
    private Date startDate;
    private int exp;
    private Path currentPath;
    private ArrayList<Path> completedPaths;

    private ArrayList<Path> pathsLeft;
    private ArrayList<Quest> currentQuestsLeft;

    public User() {

    }

    public User(String email, String password, String displayName, String profilePicture,
                ArrayList<String> travelLog, Date startDate, int exp, Path currentPath,
                ArrayList<Path> completedPaths, ArrayList<Path> pathsLeft,
                ArrayList<Quest> currentQuestsLeft) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.profilePicture = profilePicture;
        this.travelLog = travelLog;
        this.startDate = startDate;
        this.exp = exp;
        this.currentPath = currentPath;
        this.completedPaths = completedPaths;
        this.pathsLeft = pathsLeft;
        this.currentQuestsLeft = currentQuestsLeft;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
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

    public ArrayList<Path> getCompletedPaths() {
        return completedPaths;
    }

    public void setCompletedPaths(ArrayList<Path> completedPaths) {
        this.completedPaths = completedPaths;
    }

    public ArrayList<Quest> getCurrentQuestsLeft() {
        return currentQuestsLeft;
    }

    public void setCurrentQuestsLeft(ArrayList<Quest> currentQuestsLeft) {
        this.currentQuestsLeft = currentQuestsLeft;
    }

    public ArrayList<Path> getPathsLeft() {
        return pathsLeft;
    }

    public void setPathsLeft(ArrayList<Path> pathsLeft) {
        this.pathsLeft = pathsLeft;
    }
}

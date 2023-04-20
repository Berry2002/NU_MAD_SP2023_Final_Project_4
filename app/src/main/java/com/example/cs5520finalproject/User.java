package com.example.cs5520finalproject;

import java.util.ArrayList;
import java.util.Date;

/**
 * A local User object that stores the data as a Firebase user.
 */
public class User {
    private String email, password, displayName, profilePicture;
    private ArrayList<String> travelLog;
    private long startDate;
    private int exp;
    private String currentPathID, currentPathName;
    private ArrayList<String> completedPaths;
    private ArrayList<String> completedQuests;

    public User() {

    }

    public User(String email, String password, String displayName, String profilePicture,
                ArrayList<String> travelLog, long startDate, int exp, String currentPathID,
                String currentPathName, ArrayList<String> completedPaths,
                ArrayList<String> completedQuests) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.profilePicture = profilePicture;
        this.travelLog = travelLog;
        this.startDate = startDate;
        this.exp = exp;
        this.currentPathID = currentPathID;
        this.currentPathName = currentPathName;
        this.completedPaths = completedPaths;
        this.completedQuests = completedQuests;
    }

    public User(String displayName, String email, String password) {
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.profilePicture = null;
        this.travelLog = new ArrayList<String>();
        this.startDate = new Date().getTime();
        this.exp = 0;
        this.currentPathID = null;
        this.currentPathName = null;
        this.completedPaths = new ArrayList<String>();
        this.completedQuests = new ArrayList<String>();
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

    public String getCurrentPathID() {
        return currentPathID;
    }

    public void setCurrentPathID(String currentPathID) {
        this.currentPathID = currentPathID;
    }

    public String getCurrentPathName() {
        return currentPathName;
    }

    public void setCurrentPathName(String currentPathName) {
        this.currentPathName = currentPathName;
    }

    /**
     * Add the given exp to the current exp.
     * @param expAddition the given exp points
     */
    public void addExp(int expAddition) {
        this.exp += expAddition;
    }

    /**
     * Update corresponding fields when the user completes a path.
     * @param pathID pathID of the completed path
     */
    public void completedPath(String pathID) {
        ArrayList<String> newCompletedPaths = this.getCompletedPaths();
        newCompletedPaths.add(pathID);
        this.setCompletedPaths(newCompletedPaths);
        this.setCurrentPathID(null);
        this.setCurrentPathName(null);
        this.setCompletedQuests(new ArrayList<String>());
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", displayName='" + displayName + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", travelLog=" + travelLog +
                ", startDate=" + startDate +
                ", exp=" + exp +
                ", currentPathID='" + currentPathID + '\'' +
                ", currentPathName='" + currentPathName + '\'' +
                ", completedPaths=" + completedPaths +
                ", completedQuests=" + completedQuests +
                '}';
    }
}

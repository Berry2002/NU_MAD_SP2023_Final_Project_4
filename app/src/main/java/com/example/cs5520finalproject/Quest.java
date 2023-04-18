package com.example.cs5520finalproject;

/**
 * A Quest object that represents a quest to be completed in a path.
 */
public class Quest {
    private String image, name, description, summary;
    private int expValue;

    public Quest() {
    }

    public Quest(String image, String name, String description, String summary, int expValue) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.summary = summary;
        this.expValue = expValue;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getExpValue() {
        return expValue;
    }

    public void setExpValue(int expValue) {
        this.expValue = expValue;
    }
}

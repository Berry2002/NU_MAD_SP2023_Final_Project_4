package com.example.cs5520finalproject;

import com.google.firebase.auth.FirebaseUser;

/**
 * An interface for switching between fragments.
 */
public interface IFragmentToMainActivity {

    /**
     * Go to login fragment page
     */
    void toLoginPage();

    /**
     * Go to register fragment page
     */
    void toRegisterPage();

    /**
     * Display the quest home page with the currentUser's data.
     * @param currentUser current Firebase user
     */
    void goToHomePage(FirebaseUser currentUser);

    /**
     * Logout of the current authenticated Firebase user.
     */
    void logout();

    /**
     * Display the given path's highlights.
     * @param path the path for displaying path highlights fragment page
     */
    void goToPathHighlights(Path path);

    /**
     * Equip the path given.
     * @param selectedPath the path to be equipped
     */
    void equipPath(Path selectedPath);

    /**
     * Leave the current path and clear all the current data.
     */
    void leaveCurrentPath();

    /**
     * Go to the reviews page for the given path.
     * @param path path to display the reviews for
     */
    void goToPathReviews(Path path);

    /**
     * Change profile picture on the profile fragment page.
     */
    void changeProfilePicture();

    /**
     * Add a picture for the given questName.
     * @param questName the quest name of the picture to be added
     */
    void addPictureToTravelLog(String questName);

    /**
     * Update the corresponding data when the user completes the given questName.
     * @param questName name of the completed quest
     * @param questIndex quest index within a path
     * @param expValue exp value of the quest
     */
    void completeQuest(String questName, int questIndex, int expValue);

}

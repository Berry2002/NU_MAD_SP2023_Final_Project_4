package com.example.cs5520finalproject;

import com.google.firebase.auth.FirebaseUser;

public interface IFragmentToMainActivity {

    void toLoginPage();
    void toRegisterPage();
    void goToHomePage(FirebaseUser currentUser);
    void logout();
    void goToPathHighlights(Path path);
    void equipPath(Path selectedPath);
    void leaveCurrentPath();
    void goToPathReviews(Path path);
    void changeProfilePicture();
    void addPictureToTravelLog(String questName);
    void completeQuest(String questName, int questIndex, int expValue);
}

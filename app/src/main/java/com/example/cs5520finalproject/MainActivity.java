package com.example.cs5520finalproject;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.cs5520finalproject.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements IFragmentToMainActivity {

    ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private User currentUserLocalType;

    public MainActivity() {
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.currentUser = this.mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.populateScreen();

        binding.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case (R.id.fragmentQuestHomePage):
                        replaceFragment(new FragmentQuestHomePage(currentUserLocalType));
                        break;
                    case (R.id.fragmentProfilePage):
                        goToProfilePage();
                        break;
                    case (R.id.fragmentSearchPage):
                        replaceFragment(new FragmentSearchPage(currentUserLocalType));
                        break;
                    case (R.id.fragmentRankingsPage):
                        replaceFragment(new FragmentRankingsPage());
                        break;
                }
                return true;
            }
        });
    }

    // from search paths page to path highlight page
    @Override
    public void goToPathHighlights(Path path) {
        replaceFragment(new FragmentEquipPathPage(path));
    }

    @Override
    public void equipPath(Path path) {
        // can only start path if they have no current path
        if (currentUserLocalType.getCurrentPath().equals("")) {
            this.updateCurrentPath(path);
            this.updateQuestsCompleted();
            this.populateScreen();
        } else {
            Toast.makeText(this,
                    "Please finish the current path before starting another one!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void goToProfilePage() {
        Log.d("main activity", "goToProfilePage: current user == null" + (currentUser == null));
        Log.d("main activity", "goToProfilePage: current user email == null" + (currentUser.getEmail() == null));

        this.switchToProfilePageFragment();
    }

    @Override
    public void toLoginPage() {
        this.replaceFragment(new FragmentLogin());
    }

    @Override
    public void toRegisterPage() {
        this.replaceFragment(new FragmentRegister());
    }

    @Override
    public void goToHomePage(FirebaseUser currentUser) {
        this.currentUser = currentUser;
        this.populateScreen();
    }

    @Override
    public void logout() {
        this.mAuth.signOut();
        this.currentUser = null;
        this.populateScreen();
    }

    @Override
    public void leaveCurrentPath() {
        Path newPath = new Path();
        Toast.makeText(this,"Current Path removed!",Toast.LENGTH_SHORT).show();
        newPath.setLocation("");
        this.updateCurrentPath(newPath);
        this.replaceFragment(new FragmentSearchPage(currentUserLocalType));
        this.populateScreen();
    }

    @Override
    public void goToPathReviews(Path path) {
        replaceFragment(new FragmentPathReviewsPage(path));
    }

    private void populateScreen() {
        BottomNavigationView navBar = findViewById(R.id.bottomNavView);
        if (this.currentUser == null) { // if no user is logged in, we prompt login/register
            replaceFragment(new FragmentAuthentication());
            navBar.setVisibility(View.GONE);
        } else { // go to the current user's home page
            navBar.setVisibility(View.VISIBLE);
            this.switchToHomePageFragment();
        }
    }

    private void switchToHomePageFragment() {
        String email = this.currentUser.getEmail();

        if (email == null) {
            Log.e("main activity", "switchToHomePageFragment: current user's email is null");
        } else {
            this.db.collection(Tags.USERS).document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        currentUserLocalType = value.toObject(User.class);
                        // current user retrieved
                        replaceFragment(new FragmentQuestHomePage(currentUserLocalType));
                    } else {
                        Log.e("main activity", "onEvent: could not retrieve the current user from Firestore");
                    }
                }
            });
        }
    }

    private void switchToProfilePageFragment() {
        String email = this.currentUser.getEmail();

        if (email == null) {
            Log.e("main activity", "switchToHomePageFragment: current user's email is null");
        } else {
            this.db.collection(Tags.USERS).document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        currentUserLocalType = value.toObject(User.class);
                        // current user retrieved
                        replaceFragment(new FragmentProfilePage(currentUserLocalType));
                    } else {
                        Log.e("main activity", "onEvent: could not retrieve the current user from Firestore");
                    }
                }
            });
        }
    }

    private void replaceFragment(Fragment fragment) {
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void updateCurrentPath(Path path) {
        this.db.collection(Tags.USERS).document(this.currentUser.getEmail())
                .update(Tags.USERS_CURRENT_PATH, path.getLocation())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e("equip path", "onComplete: could not update the current path");
                        } else {
                            currentUserLocalType.setCurrentPath(path.getLocation());
                        }
                    }
                });
    }

    private void updateQuestsCompleted() {
        this.db.collection(Tags.USERS).document(this.currentUser.getEmail())
                .update(Tags.USERS_COMPLETED_QUESTS, new ArrayList<String>())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e("update quests completed", "onComplete: could not update the quests completed");
                        } else {
                            currentUserLocalType.setCompletedQuests(new ArrayList<String>());
                        }
                    }
                });
    }
}
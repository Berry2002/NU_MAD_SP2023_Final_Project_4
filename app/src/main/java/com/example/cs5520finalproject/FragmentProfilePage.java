package com.example.cs5520finalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class FragmentProfilePage extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private User currentUserLocalType;
    private EditText displayName;
    private TextView currentPath, exp, questingSince;
    private Button updateProfile;
    private ImageButton profilePicture, logOutButton;
    private IFragmentToMainActivity pathway;

    public FragmentProfilePage() {
        // Required empty public constructor
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.currentUser = this.mAuth.getCurrentUser();
        this.db.collection(Tags.USERS).document(this.currentUser.getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    currentUserLocalType = value.toObject(User.class);
                } else {
                    Log.e("profile page", "onEvent: could not retrieve the current user from Firestore");
                }
            }
        });
    }

    public FragmentProfilePage(User currentUserLocalType) {
        this.currentUserLocalType = currentUserLocalType;
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance(); // don't know if i'll need this
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentToMainActivity) {
            this.pathway = (IFragmentToMainActivity) context;
        } else {
            throw new IllegalStateException(context + " should implement IFragmentToMainActivity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        // fetch the display name, questing since, current path, exp, travel log
        this.displayName = view.findViewById(R.id.displayName_inProfilePage);
        this.profilePicture = view.findViewById(R.id.profilePicture_inProfilePage);
        this.currentPath = view.findViewById(R.id.currentPath_inProfilePage);
        this.exp = view.findViewById(R.id.exp_inProfilePage);
        this.updateProfile = view.findViewById(R.id.updateProfileButton_inProfilePage);
        this.questingSince = view.findViewById(R.id.questingSince_inProfilePage);
        this.logOutButton = view.findViewById(R.id.logoutButton_profilePage);

        this.logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathway.logout();
            }
        });

        // call the method to fetch the data
        if (this.currentUserLocalType != null) {
            this.displayName.setText(this.currentUserLocalType.getDisplayName());
            if (this.currentUserLocalType.getCurrentPath() != null) { // set the current path
                this.currentPath.setText(String.format("Current Path: %s", this.currentUserLocalType.getCurrentPath()));
            } else {
                this.currentPath.setText("No path equipped currently.");
            }
            this.exp.setText(String.format("EXP: %d points", this.currentUserLocalType.getExp()));
            LocalDate localDate = Instant.ofEpochMilli(this.currentUserLocalType.getStartDate())
                    .atZone(ZoneId.systemDefault()).toLocalDate();
            this.questingSince.setText(String.format("Questing since: %s %s, %s",
                    localDate.getDayOfMonth(), localDate.getMonth(), localDate.getYear()));
        }

        return view;
    }
}
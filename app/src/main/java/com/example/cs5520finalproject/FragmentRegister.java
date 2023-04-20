package com.example.cs5520finalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Register screen.
 */
public class FragmentRegister extends Fragment {

    private EditText displayName, email, password, confirmPassword;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private User mUserLocalType;
    private FirebaseFirestore db;
    private IFragmentToMainActivity pathway;

    public FragmentRegister() {
        // Required empty public constructor
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        this.displayName = view.findViewById(R.id.displayName_registerPage);
        this.email = view.findViewById(R.id.email_registerPage);
        this.password = view.findViewById(R.id.password_registerPage);
        this.confirmPassword = view.findViewById(R.id.confirmPassword_registerPage);
        this.registerButton = view.findViewById(R.id.registerButton_registerPage);

        this.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canRegister()) {
                    tryRegister();
                    mUserLocalType = new User(retrieveText(displayName), retrieveText(email), retrieveText(password));
                    addUserToFirebase();
                } else {
                    displayName.setError("Please double check your display name.");
                    email.setError("Please double check your email.");
                    Toast.makeText(getContext(), "Please check all your fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    /**
     * Can the user register - are all the fields filled in to make an attempt to register?
     * @return true - can register, false - cannot
     */
    private boolean canRegister() {
        boolean retVal = true;

        if (this.retrieveText(displayName).equals("")) {
            displayName.setError("Must input display name!");
            retVal = false;
        }
        if (this.retrieveText(email).equals("")) {
            email.setError("Must input email!");
            retVal = false;
        }
        if (this.retrieveText(password).equals("")) {
            password.setError("Password must not be empty!");
            retVal = false;
        }
        if (!this.retrieveText(confirmPassword).equals(this.retrieveText(password))){
            confirmPassword.setError("Passwords must match!");
            retVal = false;
        }

        return retVal;
    }

    /**
     * Makes the call to Firebase Authentication to try to register the user,
     * assuming that the user has all the necessary information to register.
     */
    private void tryRegister() {
        // assume we can register - all fields validated
        // create user now!
        mAuth.createUserWithEmailAndPassword(this.retrieveText(email), this.retrieveText(password))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUser = task.getResult().getUser();
                            // Adding name to the FirebaseUser...
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(retrieveText(displayName))
                                    .build();

                            mUser.updateProfile(profileChangeRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                pathway.goToHomePage(mUser);
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(getContext(), "Error while registering: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * After adding the user to the Firebase Authentication, add them to the Firebase database.
     */
    private void addUserToFirebase() {
        HashMap<String, Object> userHash = new HashMap<>();
        userHash.put(Tags.USERS_DISPLAY_NAME, mUserLocalType.getDisplayName());
        userHash.put(Tags.USERS_EMAIL, mUserLocalType.getEmail());
        userHash.put(Tags.USERS_PASSWORD, mUserLocalType.getPassword());
        userHash.put(Tags.USERS_EXP, mUserLocalType.getExp());
        userHash.put(Tags.USERS_COMPLETED_PATHS, mUserLocalType.getCompletedPaths());
        userHash.put(Tags.USERS_COMPLETED_QUESTS, mUserLocalType.getCompletedQuests());
        userHash.put(Tags.USERS_CURRENT_PATH_ID, mUserLocalType.getCurrentPathID());
        userHash.put(Tags.USERS_CURRENT_PATH_NAME, mUserLocalType.getCurrentPathName());
        userHash.put(Tags.USERS_PROFILE_PICTURE, mUserLocalType.getProfilePicture());
        userHash.put(Tags.USERS_TRAVEL_LOG, mUserLocalType.getTravelLog());
        userHash.put(Tags.USERS_START_DATE, mUserLocalType.getStartDate());

        this.db.collection(Tags.USERS)
                .document(mUserLocalType.getEmail())
                .set(userHash)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Register Fragment", "onFailure: " + e.getMessage());
                    }
                });
    }

    private String retrieveText(EditText editText) {
        return editText.getText().toString().trim();
    }
}
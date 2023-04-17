package com.example.cs5520finalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

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
                    addUserToFirebase(mUserLocalType);
                }
            }
        });

        return view;
    }

    private boolean canRegister() {
        boolean retVal = true;

        if (this.retrieveText(displayName).equals("")) {
            displayName.setError("Must input display name!");
            retVal = false;
        }
        if (this.retrieveText(email).equals("")){
            email.setError("Must input email!");
            retVal = false;
        }
        if (this.retrieveText(password).equals("")){
            password.setError("Password must not be empty!");
            retVal = false;
        }
        if (!this.retrieveText(confirmPassword).equals(this.retrieveText(password))){
            confirmPassword.setError("Passwords must match!");
            retVal = false;
        }

        return retVal;
    }

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

                        }
                    }
                });
    }

    private void addUserToFirebase(User user) {
        HashMap<String, Object> userHash = new HashMap<>();
        userHash.put(Tags.USERS_DISPLAY_NAME, user.getDisplayName());
        userHash.put(Tags.USERS_EMAIL, user.getEmail());
        userHash.put(Tags.USERS_PASSWORD, user.getPassword());
        userHash.put(Tags.USERS_EXP, user.getExp());
        userHash.put(Tags.USERS_COMPLETED_PATHS, user.getCompletedPaths());
        userHash.put(Tags.USERS_COMPLETED_QUESTS, user.getCompletedQuests());
        userHash.put(Tags.USERS_CURRENT_PATH_ID, user.getCurrentPathID());
        userHash.put(Tags.USERS_CURRENT_PATH_NAME, user.getCurrentPathName());
        userHash.put(Tags.USERS_PROFILE_PICTURE, user.getProfilePicture());
        userHash.put(Tags.USERS_TRAVEL_LOG, user.getTravelLog());
        userHash.put(Tags.USERS_START_DATE, user.getStartDate());

        this.db.collection(Tags.USERS)
                .document(user.getEmail())
                .set(userHash);
    }

    private String retrieveText(EditText editText) {
        return editText.getText().toString().trim();
    }
}
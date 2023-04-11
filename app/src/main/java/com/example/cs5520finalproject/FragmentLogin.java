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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentLogin extends Fragment {

    private EditText email, password;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private IFragmentToMainActivity pathway;

    public FragmentLogin() {
        // Required empty public constructor
        this.mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        this.email = view.findViewById(R.id.email_loginPage);
        this.password = view.findViewById(R.id.password_loginPage);
        this.loginButton = view.findViewById(R.id.loginButton_loginPage);

        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canLogin()) {
                    tryLogin();
                }
            }
        });

        return view;
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

    private boolean canLogin() {
        String emailContents = this.email.getText().toString().trim();
        String passwordContents = this.password.getText().toString().trim();

        if (emailContents.equals("")) {
            Toast.makeText(this.getContext(), "Please enter your email!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (passwordContents.equals("")) {
            Toast.makeText(this.getContext(), "Please enter your password!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void tryLogin() {
        String emailContents = this.email.getText().toString().trim();
        String passwordContents = this.password.getText().toString().trim();

        // now we know that the email and password are not empty so we can try to login
        this.mAuth.signInWithEmailAndPassword(emailContents, passwordContents)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Login unsuccessful! Error: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pathway.goToHomePage(mAuth.getCurrentUser());
                        }
                    }
                });
    }
}
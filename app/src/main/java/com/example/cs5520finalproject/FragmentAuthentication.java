package com.example.cs5520finalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Prompts the user to login or register.
 * Populates when there is no user logged in.
 */
public class FragmentAuthentication extends Fragment {

    private Button login, register;
    private IFragmentToMainActivity pathway;

    public FragmentAuthentication() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);

        this.login = view.findViewById(R.id.loginButton_authenticationPage);
        this.register = view.findViewById(R.id.registerButton_authenticationPage);

        this.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to login page
                pathway.toLoginPage();
            }
        });

        this.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to login page
                pathway.toRegisterPage();
            }
        });

        return view;
    }
}
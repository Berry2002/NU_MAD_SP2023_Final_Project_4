package com.example.cs5520finalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;

public class FragmentQuestHomePage extends Fragment {

    private FirebaseUser currUser;
    private User currUserLocalType;

    public FragmentQuestHomePage() {
        // Required empty public constructor
    }

    public FragmentQuestHomePage(User currUser) {
        this.currUserLocalType = currUser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quest_home_page, container, false);

        return view;
    }
}
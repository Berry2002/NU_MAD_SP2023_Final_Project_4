package com.example.cs5520finalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FragmentRankingsPage extends Fragment {
    private static final String ARG_USERS = "friendsarray";
    private RecyclerView recyclerViewRankings;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private RankingsAdapter rankingsAdapter;
    private ArrayList<User> mFriends;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public FragmentRankingsPage() {
        // Required empty public constructor
    }
    public static FragmentRankingsPage newInstance() {
        FragmentRankingsPage fragment = new FragmentRankingsPage();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USERS, new ArrayList<User>());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(ARG_USERS)) {
                mFriends = (ArrayList<User>) args.getSerializable(ARG_USERS);
            }

            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rankings_page, container, false);
        recyclerViewRankings = view.findViewById(R.id.recyclerViewRankings);
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        rankingsAdapter = new RankingsAdapter(mFriends, getContext());
        recyclerViewRankings.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewRankings.setAdapter(rankingsAdapter);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

}
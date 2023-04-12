package com.example.cs5520finalproject;

import static com.example.cs5520finalproject.Tags.USERS;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.example.cs5520finalproject.Tags.*;
import com.google.firebase.firestore.QuerySnapshot;

public class FragmentRankingsPage extends Fragment {
    private static final String ARG_USERS = "friendsarray";
    private RecyclerView recyclerViewRankings;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;

    private RankingsAdapter rankingsAdapter;
    private ArrayList<User> mFriends; // all users in Firebase
    private FirebaseFirestore db;
    private IFragmentToMainActivity mListener;

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
            // fetch all users in Firebase
            mFriends = fetchAllUsersInFirebase();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rankings_page, container, false);
        // set up recycler view
        recyclerViewRankings = view.findViewById(R.id.recyclerViewRankings);
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        // sort friends based on EXP
        sortFriendsBasedOnExp(this.mFriends);
        rankingsAdapter = new RankingsAdapter(this.mFriends, getContext());
        recyclerViewRankings.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewRankings.setAdapter(rankingsAdapter);

        return view;
    }

    // sort friends based on EXP points before passing them to adapter
    private void sortFriendsBasedOnExp(ArrayList<User> users) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return Integer.compare(user2.getExp(), user1.getExp());
            }
        });
    }

    private ArrayList<User> fetchAllUsersInFirebase() {
        ArrayList<User> users = new ArrayList<>();
        db.collection(USERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot: task.getResult()) {
                                User user = documentSnapshot.toObject(User.class);
                                users.add(user);
                            }
                        }
                    }
                });
        return users;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentToMainActivity) {
            this.mListener = (IFragmentToMainActivity) context;
        } else{
            throw new RuntimeException(context + " must implement IFragmentToMainActivity");
        }
    }
}
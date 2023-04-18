package com.example.cs5520finalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.firebase.firestore.QuerySnapshot;

/**
 * Displays the user's rankings.
 */
public class FragmentRankingsPage extends Fragment {
    private RecyclerView recyclerViewRankings;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private RankingsAdapter rankingsAdapter;
    private ArrayList<User> mFriends; // all users in Firebase
    private FirebaseFirestore db;
    private IFragmentToMainActivity mListener;

    public FragmentRankingsPage() {
        this.db = FirebaseFirestore.getInstance();
        this.mFriends = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rankings_page, container, false);

        // set up recycler view
        recyclerViewRankings = view.findViewById(R.id.recyclerViewRankings);
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        rankingsAdapter = new RankingsAdapter(mFriends, getContext());
        recyclerViewRankings.setLayoutManager(recyclerViewLayoutManager);
        recyclerViewRankings.setAdapter(rankingsAdapter);

        // fetch all users in Firebase
        fetchAllUsersInFirebase();
        return view;
    }

    private void fetchAllUsersInFirebase() {
        mFriends.clear();
        db.collection(Tags.USERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot: task.getResult()) {
                                User user = documentSnapshot.toObject(User.class);
                                mFriends.add(user);
                            }
                        }
                        rankingsAdapter.setFriends(mFriends);
                        rankingsAdapter.notifyDataSetChanged();
                    }
                });
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
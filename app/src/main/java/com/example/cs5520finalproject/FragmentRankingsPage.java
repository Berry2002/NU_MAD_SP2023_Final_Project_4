package com.example.cs5520finalproject;

//import static com.example.cs5520finalproject.Tags.USERS;

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
        Bundle args = getArguments();
        if (args != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rankings_page, container, false);

        // set up recycler view
        recyclerViewRankings = view.findViewById(R.id.recyclerViewRankings);
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewRankings.setLayoutManager(recyclerViewLayoutManager);

        // fetch all users in Firebase
        fetchAllUsersInFirebase();

        return view;
    }

    private void fetchAllUsersInFirebase() {
        ArrayList<User> users = new ArrayList<>();
        db.collection(Tags.USERS)
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
                        callAdapter(users); // call adapter (Firebase is async)
                    }
                });
    }

    private void callAdapter(ArrayList<User> users) {
        Log.d("callAdapyer", users.toString());
        sortFriendsBasedOnExp(users); // sort users based on exp
        rankingsAdapter = new RankingsAdapter(users, getContext());
        recyclerViewRankings.setAdapter(rankingsAdapter);
        updateRecyclerView(users);
    }

    private void updateRecyclerView(ArrayList<User> friends) {
        this.mFriends = friends;
        rankingsAdapter.notifyDataSetChanged();
    }

    // sort friends based on EXP points before passing them to adapter
    private void sortFriendsBasedOnExp(ArrayList<User> users) {
        Log.d("users in sort: ", users.toString());
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return Integer.compare(user2.getExp(), user1.getExp());
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
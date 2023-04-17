package com.example.cs5520finalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class FragmentReviewsHomePage extends Fragment {

    private RecyclerView recyclerView_ReviewsHomePage;
    private TextView textView_PathReviewsName, textView_PathReviewsSubject;
    private ArrayList<Path> mPaths;
    private FirebaseFirestore db;
    private User currentLocalUser;
    private PathsAdapter pathsAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private IFragmentToMainActivity mListener;


    public FragmentReviewsHomePage() {
        // Required empty public constructor
    }

    public FragmentReviewsHomePage (User user) {
        this.currentLocalUser = user;
        this.db = FirebaseFirestore.getInstance();
        this.mPaths = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews_home_page, container, false);

        recyclerView_ReviewsHomePage = view.findViewById(R.id.recyclerView_ReviewsHomePage);
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        pathsAdapter = new PathsAdapter(mPaths, getContext());
        recyclerView_ReviewsHomePage.setLayoutManager(recyclerViewLayoutManager);
        recyclerView_ReviewsHomePage.setAdapter(pathsAdapter);
        return view;
    }
}
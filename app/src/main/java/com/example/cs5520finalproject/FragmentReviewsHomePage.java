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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FragmentReviewsHomePage extends Fragment {

    private RecyclerView recyclerView_ReviewsHomePage;
    private ArrayList<Path> mPaths;
    private FirebaseFirestore db;
    private User currentLocalUser;
    private PathReviewsAdapter pathReviewsAdapter;
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
        pathReviewsAdapter = new PathReviewsAdapter(mPaths, getContext());
        recyclerView_ReviewsHomePage.setLayoutManager(recyclerViewLayoutManager);
        recyclerView_ReviewsHomePage.setAdapter(pathReviewsAdapter);

        fetchAllPaths();
        return view;
    }

    private void fetchAllPaths() {
        mPaths.clear();
        db.collection(Tags.PATHS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                                Path path = documentSnapshot.toObject(Path.class);
                                mPaths.add(path);
                            }
                            pathReviewsAdapter.setPaths(mPaths);
                            pathReviewsAdapter.notifyDataSetChanged();
                        }
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
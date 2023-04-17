package com.example.cs5520finalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;


public class FragmentPathReviewsPage extends Fragment {

    private FirebaseFirestore db;
    private Path selectedPath;
    private TextView textViewPathNameReviews;
    private RecyclerView recyclerViewReviews;
    private RecyclerView.LayoutManager recyclerViewReviewsLayoutManager;
    private RecyclerView.Adapter recyclerViewReviewsAdapter;
    private EditText editTextReviewTitle;
    private EditText editTextReviewComment;
    private Button buttonPostReview;

    public FragmentPathReviewsPage() {
        // Required empty public constructor
    }


    public FragmentPathReviewsPage(Path path) {
        this.selectedPath = selectedPath;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_path_reviews_page, container, false);

        textViewPathNameReviews = view.findViewById(R.id.textView_IndividualPathNameReviews);
        editTextReviewTitle = view.findViewById(R.id.editText_ReviewTitle);
        editTextReviewComment = view.findViewById(R.id.editText_ReviewComment);
        buttonPostReview = view.findViewById(R.id.button_PostReview);

        recyclerViewReviews = view.findViewById(R.id.recyclerViewReviews);
        recyclerViewReviewsLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewReviewsAdapter = new ReviewsAdapter();
        recyclerViewReviews.setLayoutManager(recyclerViewReviewsLayoutManager);
        recyclerViewReviews.setAdapter(recyclerViewReviewsAdapter);

        return view;
    }

}
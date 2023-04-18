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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Displays all the reviews for a specific path.
 */
public class FragmentPathReviewsPage extends Fragment {

    private FirebaseFirestore db;
    private Path selectedPath;
    private User currentUser;
    private ArrayList<Review> reviews;

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

    public FragmentPathReviewsPage(User user, Path path) {
        this.currentUser = user;
        this.selectedPath = path;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        this.reviews = new ArrayList<Review>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_path_reviews_page, container, false);


        textViewPathNameReviews = view.findViewById(R.id.textView_IndividualPathNameReviews);
        textViewPathNameReviews.setText(this.selectedPath.getPathName());
        editTextReviewTitle = view.findViewById(R.id.editText_ReviewTitle);
        editTextReviewComment = view.findViewById(R.id.editText_ReviewComment);
        buttonPostReview = view.findViewById(R.id.button_PostReview);

        recyclerViewReviews = view.findViewById(R.id.recyclerViewReviews);
        recyclerViewReviewsLayoutManager = new LinearLayoutManager(getContext());

        recyclerViewReviewsAdapter = new ReviewsAdapter(this.reviews, getContext());
        recyclerViewReviews.setLayoutManager(recyclerViewReviewsLayoutManager);
        recyclerViewReviews.setAdapter(recyclerViewReviewsAdapter);

        buttonPostReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reviewTitle = editTextReviewTitle.getText().toString().trim();
                String reviewComment = editTextReviewComment.getText().toString().trim();
                if (!reviewTitle.equals("") && !reviewComment.equals("")){
                    Review review = new Review(currentUser.getEmail(), reviewTitle, reviewComment);
                    uploadReviewToFirebase(review);
                } else {
                    if( !reviewTitle.equals("")) {
                        editTextReviewTitle.setError("Can't be empty!");
                    }
                    if (!reviewComment.equals("")) {
                        editTextReviewComment.setError("Can't be empty!");
                    }
                }
            }
        });

        this.fetchReviewsForThisPath();
        recyclerViewReviewsAdapter.notifyDataSetChanged();
        return view;
    }

    /**
     * Add a given review to Firebase.
     * @param review the review to be added
     */
    private void uploadReviewToFirebase(Review review) {
        db.collection(Tags.PATHS)
                .document(this.selectedPath.getPathID())
                .collection(Tags.REVIEWS)
                .add(review)

                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        editTextReviewTitle.setText("");
                        editTextReviewComment.setText("");

                        reviews.add(review);
                        fetchReviewsForThisPath();
                        recyclerViewReviewsAdapter.notifyDataSetChanged();

                        try {
                            InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void fetchReviewsForThisPath() {
        this.db.collection(Tags.PATHS).document(this.selectedPath.getPathID())
                .collection(Tags.REVIEWS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            reviews.clear();
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                Review currReview = documentSnapshot.toObject(Review.class);
                                reviews.add(currReview);
                            }

                            Collections.sort(reviews, new Comparator<Review>() {
                                @Override
                                public int compare(Review t1, Review t2) {
                                    return (int) ((t2.getTime() - t1.getTime()));
                                }
                            });
                            recyclerViewReviewsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}
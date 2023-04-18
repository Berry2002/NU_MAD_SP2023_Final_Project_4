package com.example.cs5520finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adapter that is responsible for displaying the Reviews Fragment.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private IFragmentToMainActivity mListener;

    ArrayList<Review> reviews;

    public ReviewsAdapter() {

    }

    public ReviewsAdapter(ArrayList<Review> reviews, Context context) {
        this.reviews = reviews;
        if(context instanceof IFragmentToMainActivity){
            this.mListener = (IFragmentToMainActivity) context;
        } else{
            throw new RuntimeException(context.toString()+ "must implement IFragmentToMainActivity");
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewReviewTitle, textViewReviewer, textViewReviewComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewReviewTitle = itemView.findViewById(R.id.textView_ReviewTitle);
            this.textViewReviewer = itemView.findViewById(R.id.textView_Reviewer);
            this.textViewReviewComment = itemView.findViewById(R.id.textView_ReviewComment);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRecyclerView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.review_card,parent, false);

        return new ViewHolder(itemRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder holder, int position) {
        Review currentReview = this.reviews.get(position);

        holder.textViewReviewTitle.setText(currentReview.getTitle());
        holder.textViewReviewer.setText(currentReview.getReviewer());
        holder.textViewReviewComment.setText(currentReview.getComment());
    }

    @Override
    public int getItemCount() {
        return this.reviews.size();
    }
}



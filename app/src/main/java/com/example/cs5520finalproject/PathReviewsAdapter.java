package com.example.cs5520finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A PathReviewsAdapter that is responsible for displaying the reviews.
 */
public class PathReviewsAdapter extends RecyclerView.Adapter<PathReviewsAdapter.ViewHolder> {

    private IFragmentToMainActivity mListener;

    ArrayList<Path> mPaths;

    private Context context;

    public PathReviewsAdapter() {

    }

    public PathReviewsAdapter(ArrayList<Path> mPaths, Context context) {
        this.mPaths = mPaths;
        if(context instanceof IFragmentToMainActivity){
            this.mListener = (IFragmentToMainActivity) context;
            this.context = context;
        } else{
            throw new RuntimeException(context.toString()+ "must implement IFragmentToMainActivity");
        }
    }

    public void setPaths(ArrayList<Path> mPaths) {
        this.mPaths = mPaths;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_PathReviewsName, textView_PathReviewsSubject, textView_PathReviewsDescription;
        private Button button_SeePathReviews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView_PathReviewsName = itemView.findViewById(R.id.textView_PathReviewsName);
            this.textView_PathReviewsSubject = itemView.findViewById(R.id.textView_PathReviewsSubject);
            this.textView_PathReviewsDescription = itemView.findViewById(R.id.textView_PathReviewsDescription);
            this.button_SeePathReviews = itemView.findViewById(R.id.button_SeePathReviews);
        }
    }

    @NonNull
    @Override
    public PathReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRecyclerView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.reviews_for_path_row,parent, false);

        return new PathReviewsAdapter.ViewHolder(itemRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull PathReviewsAdapter.ViewHolder holder, int position) {
        Path currentPath = this.mPaths.get(position);

        holder.textView_PathReviewsName.setText(currentPath.getLocation());
        holder.textView_PathReviewsSubject.setText(currentPath.getSubject());

        holder.textView_PathReviewsDescription.setText(currentPath.getDescription());
        holder.button_SeePathReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToPathReviews(mPaths.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mPaths.size();
    }
}

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

public class PathsAdapter extends RecyclerView.Adapter<PathsAdapter.ViewHolder> {

    private IFragmentToMainActivity mListener;

    ArrayList<Path> mPaths;

    public PathsAdapter() {

    }

    public PathsAdapter(ArrayList<Path> mPaths, Context context) {
        this.mPaths = mPaths;
        if(context instanceof IFragmentToMainActivity){
            this.mListener = (IFragmentToMainActivity) context;
        } else{
            throw new RuntimeException(context.toString()+ "must implement IFragmentToMainActivity");
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPath;
        private TextView textViewPathLocation, textViewSubject, textViewPathDescription;
        private Button buttonSeePathHighlights;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageViewPath = itemView.findViewById(R.id.imageViewPath);
            this.textViewPathLocation = itemView.findViewById(R.id.textViewPathLocation);
            this.textViewSubject = itemView.findViewById(R.id.textViewSubject);
            this.textViewPathDescription = itemView.findViewById(R.id.textViewPathDescription);
            this.buttonSeePathHighlights = itemView.findViewById(R.id.buttonSeePathHighlights);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRecyclerView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.path_card,parent, false);

        return new ViewHolder(itemRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull PathsAdapter.ViewHolder holder, int position) {
        Path currentPath = this.mPaths.get(position);

        holder.textViewPathDescription.setText(currentPath.getDescription());
        holder.textViewSubject.setText(currentPath.getSubject());
        holder.imageViewPath.setImageResource(Integer.parseInt(currentPath.getImage()));
        holder.textViewPathLocation.setText(currentPath.getLocation());
        holder.buttonSeePathHighlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToPathHighlights(mPaths.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mPaths.size();
    }
}



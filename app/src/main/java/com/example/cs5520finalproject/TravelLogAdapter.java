package com.example.cs5520finalproject;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TravelLogAdapter extends RecyclerView.Adapter<TravelLogAdapter.ViewHolder> {

    ArrayList<String> images;
    private Context context; // for Glide

    public TravelLogAdapter() {
        // required empty constructor
    }

    public TravelLogAdapter(ArrayList<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView travelLog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.travelLog = itemView.findViewById(R.id.travelLog_imageView);
        }

        public ImageView getTravelLog() {
            return travelLog;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("PathsAdapter", "here");
        View itemRecyclerView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.travel_log_row, parent, false);

        return new TravelLogAdapter.ViewHolder(itemRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelLogAdapter.ViewHolder holder, int position) {
        String currentImage = this.images.get(position);

        Log.d("travel log adapter", "onBindViewHolder: current travel log " + currentImage);
        Log.d("travel log adapter", "onBindViewHolder: holder.getTravelLog() == null is "
                + (holder.getTravelLog() == null));

        Glide.with(this.context)
                .load(Uri.parse(currentImage))
                .fitCenter()
                .thumbnail(Glide.with(context).load(R.drawable.loading_image))
                .error(R.drawable.no_image_found)
                .into(holder.getTravelLog());
    }

    @Override
    public int getItemCount() {
        return this.images.size();
    }
}



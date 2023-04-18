package com.example.cs5520finalproject;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A TravelLogAdapter that is responsible for displaying the TravelLog photos.
 */
public class TravelLogAdapter extends RecyclerView.Adapter<TravelLogAdapter.ViewHolder> {

    ArrayList<String> images;
    private Context context; // for Glide
    private String currentUser;

    public TravelLogAdapter() {
        // required empty constructor
    }

    public TravelLogAdapter(ArrayList<String> images, Context context, String currentUser) {
        this.images = images;
        this.context = context;
        this.currentUser = currentUser;
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
        View itemRecyclerView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.travel_log_row, parent, false);

        return new TravelLogAdapter.ViewHolder(itemRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelLogAdapter.ViewHolder holder, int position) {
        String imgPath = Tags.FIREBASE_STORAGE_BASE + this.currentUser + this.images.get(position);

        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = mStorage.getReference().child(imgPath);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .fitCenter()
                        .thumbnail(Glide.with(context).load(R.drawable.loading_image))
                        .error(R.drawable.profile_icon)
                        .into(holder.getTravelLog());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Glide.with(context)
                        .load(R.drawable.no_image_found)
                        .fitCenter()
                        .into(holder.getTravelLog());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.images.size();
    }
}



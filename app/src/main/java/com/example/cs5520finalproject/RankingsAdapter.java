package com.example.cs5520finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RankingsAdapter extends RecyclerView.Adapter<RankingsAdapter.ViewHolder> {
    private ArrayList<User> friends;
    private IRankings mListener;

    public RankingsAdapter() {

    }

    public RankingsAdapter(ArrayList<User> friends, Context context) {
        this.friends = friends;
        if(context instanceof IRankings){
            this.mListener = (IRankings) context;
        } else{
            throw new RuntimeException(context.toString()+ "must implement IRankings");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRecyclerView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.ranking,parent, false);

        return new ViewHolder(itemRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return this.friends.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView displayName_ranking, rankingPosition_ranking, exp_ranking;
        private final View profilePic_ranking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.displayName_ranking = itemView.findViewById(R.id.displayName_ranking);
            this.rankingPosition_ranking = itemView.findViewById(R.id.rankingPosition_ranking);
            this.exp_ranking = itemView.findViewById(R.id.exp_ranking);
            this.profilePic_ranking = itemView.findViewById(R.id.profilePic_ranking);
        }

    }

    public interface IRankings {

    }
}

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
    private IFragmentToMainActivity mListener;

    public RankingsAdapter() {

    }

    public RankingsAdapter(ArrayList<User> friends, Context context) {
        this.friends = friends;
        if(context instanceof IFragmentToMainActivity){
            this.mListener = (IFragmentToMainActivity) context;
        } else{
            throw new RuntimeException(context.toString()+ "must implement IFragmentToMainActivity");
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView displayName_ranking, rankingPosition_ranking, textView_exp_ranking;
        private final View profilePic_ranking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.displayName_ranking = itemView.findViewById(R.id.displayName_ranking);
            this.rankingPosition_ranking = itemView.findViewById(R.id.rankingPosition_ranking);
            this.textView_exp_ranking = itemView.findViewById(R.id.textView_exp_ranking);
            this.profilePic_ranking = itemView.findViewById(R.id.profilePic_ranking);
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
        User friend = friends.get(position);
        String name = friend.getDisplayName();
        int ranking = position + 1;
        int exp = friend.getExp();

        holder.displayName_ranking.setText(name);
        holder.textView_exp_ranking.setText(new StringBuilder(exp));
        holder.rankingPosition_ranking.setText(new StringBuilder(ranking));
    }

    @Override
    public int getItemCount() {
        return this.friends.size();
    }

}

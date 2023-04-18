package com.example.cs5520finalproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A RankingsAdapter responsible for displaying the Rankings Fragment.
 */
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

    /**
     * Sort the friends based on EXP points before passing them to adapter.
     * @param users list of users
     */
    private void sortFriendsBasedOnExp(ArrayList<User> users) {
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return Integer.compare(user2.getExp(), user1.getExp());
            }
        });
    }

    public void setFriends(ArrayList<User> mFriends) {
        this.friends = mFriends;
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
        sortFriendsBasedOnExp(friends); // sort users based on exp
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
        holder.textView_exp_ranking.setText(Integer.toString(exp));
        holder.rankingPosition_ranking.setText(Integer.toString(ranking));
    }

    @Override
    public int getItemCount() {
        return this.friends.size();
    }

}

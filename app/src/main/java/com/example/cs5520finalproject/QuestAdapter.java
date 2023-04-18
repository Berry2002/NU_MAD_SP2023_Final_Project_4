package com.example.cs5520finalproject;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class QuestAdapter extends RecyclerView.Adapter<QuestAdapter.ViewHolder> {

    private IFragmentToMainActivity pathway;

    ArrayList<Quest> quests;

    private Context context;

    public QuestAdapter() {

    }

    public QuestAdapter(ArrayList<Quest> quests, Context context) {
        this.quests = quests;

        if (context instanceof IFragmentToMainActivity){
            this.pathway = (IFragmentToMainActivity) context;
            this.context = context;
        } else {
            throw new RuntimeException(context.toString()+ "must implement IFragmentToMainActivity");
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView picture;
        private final TextView name, summary, description, expValue;
        private final Button completeQuestButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.picture = itemView.findViewById(R.id.imageView_QuestImage);
            this.name = itemView.findViewById(R.id.textView_QuestName);
            this.summary = itemView.findViewById(R.id.textView_QuestSummary);
            this.description = itemView.findViewById(R.id.textView_QuestDescription);
            this.expValue = itemView.findViewById(R.id.textView_ExpValue);
            this.completeQuestButton = itemView.findViewById(R.id.button_CompleteQuest);
        }

        public ImageView getPicture() {
            return picture;
        }

        public TextView getName() {
            return name;
        }

        public TextView getSummary() {
            return summary;
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getExpValue() {
            return expValue;
        }

        public Button getCompleteQuestButton() {
            return completeQuestButton;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("QuestAdapter", "here");
        View itemRecyclerView = LayoutInflater
                .from(parent.getContext())
                    .inflate(R.layout.quest_card, parent, false);

        return new QuestAdapter.ViewHolder(itemRecyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestAdapter.ViewHolder holder, int position) {
        Quest currentQuest = this.quests.get(position);

        holder.getDescription().setText(currentQuest.getDescription());
        holder.getName().setText(currentQuest.getName());
        holder.getSummary().setText(currentQuest.getSummary());
        holder.getExpValue().setText(String.format("EXP: %d points", currentQuest.getExpValue()));

        Glide.with(this.context)
                .load(Uri.parse(currentQuest.getImage()))
                .fitCenter()
                .thumbnail(Glide.with(context).load(R.drawable.loading_image))
                .error(R.drawable.no_image_found)
                .into(holder.getPicture());

        holder.getCompleteQuestButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add the exp from this quest into the user's exp
                pathway.completeQuest(currentQuest.getName(), holder.getAdapterPosition(), currentQuest.getExpValue());
                // add the quest to the list of quests done
                // must add the quest to the list of quests done
                pathway.addPictureToTravelLog(currentQuest.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.quests.size();
    }
}



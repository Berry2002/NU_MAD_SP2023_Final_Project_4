package com.example.cs5520finalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Shows us the current unfinished quests.
 */
public class FragmentQuestHomePage extends Fragment {

    private User currUserLocalType;
    private ArrayList<Quest> questsLeftToDo;
    private FirebaseFirestore db;
    private QuestAdapter questAdapter;
    private RecyclerView.LayoutManager questLayoutManager;
    private RecyclerView questRecyclerView;
    private TextView equipPathMessage;

    public FragmentQuestHomePage() {
        // Required empty public constructor
    }

    public FragmentQuestHomePage(User currUser) {
        this.currUserLocalType = currUser;
        Log.d("quest home page", "FragmentQuestHomePage: user all details = " + currUserLocalType.toString());
        this.db = FirebaseFirestore.getInstance();
        this.questsLeftToDo = new ArrayList<Quest>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quest_home_page, container, false);

        Log.d("quest home page", "onCreateView: before assigning the recycler view...");
        this.equipPathMessage = view.findViewById(R.id.pleaseEquipPath_TextView_HomePage);

        this.questRecyclerView = view.findViewById(R.id.recyclerView_Quests);
        this.questLayoutManager = new LinearLayoutManager(this.getContext());
        this.questAdapter = new QuestAdapter(this.questsLeftToDo, this.getContext()); // give the adapter only the quests that are left
        Log.d("quest home page", "onCreateView: after assigning the recycler view...");
        Log.d("quest home page", "onCreateView: before setting the adapter");
        this.questRecyclerView.setLayoutManager(this.questLayoutManager);
        this.questRecyclerView.setAdapter(this.questAdapter);
        Log.d("quest home page", "onCreateView: after setting the adapter");
        // extract the list of quests we need to display by going through the database
        this.extractQuestsLeft();

        return view;
    }

    /**
     * Loads all the user's unfinished quests from the database that we are going to display.
     */
    private void extractQuestsLeft() {
        if (this.currUserLocalType.getCurrentPathID() == null) {
            this.equipPathMessage.setVisibility(View.VISIBLE);
        } else {
            this.equipPathMessage.setVisibility(View.GONE);
            this.db.collection(Tags.PATHS).document(this.currUserLocalType.getCurrentPathID())
                    .collection(Tags.QUESTS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                questsLeftToDo.clear();
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    Quest currQuest = documentSnapshot.toObject(Quest.class);
                                    if (!currUserLocalType.getCompletedQuests().contains(currQuest.getName())) {
                                        questsLeftToDo.add(currQuest);
                                    }
                                }
                                // notify the data set changed
                                questAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("extract quests left", "onEvent: could not find the current path");
                            }
                        }
                    });
        }

    }
}
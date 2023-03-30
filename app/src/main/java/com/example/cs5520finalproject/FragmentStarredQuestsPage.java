package com.example.cs5520finalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class FragmentStarredQuestsPage extends Fragment {
    private ImageButton starred_page_back_to_search;
    private RecyclerView starred_quests_recycler_view;

    private FragmentSearchPage.ISearchPage mListener;

    public FragmentStarredQuestsPage() {
        // Required empty public constructor
    }

    public static FragmentStarredQuestsPage newInstance(String param1, String param2) {
        FragmentStarredQuestsPage fragment = new FragmentStarredQuestsPage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_starred_quests_page, container, false);
        starred_page_back_to_search = view.findViewById(R.id.starred_page_back_to_search);
        starred_quests_recycler_view = view.findViewById(R.id.starred_quests_recycler_view);

        starred_page_back_to_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.toSearchPage();
            }
        });
        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentSearchPage.ISearchPage){
            mListener = (FragmentSearchPage.ISearchPage) context;
        } else{
            throw new RuntimeException(context.toString()
                    + "must implement ISearchPage");
        }
    }
}
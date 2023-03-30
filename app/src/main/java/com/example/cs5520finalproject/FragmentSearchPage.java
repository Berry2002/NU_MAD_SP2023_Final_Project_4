package com.example.cs5520finalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

public class FragmentSearchPage extends Fragment {
    private Spinner search_page_spinner_path, search_page_spinner_all;

    private SearchView search_page_search_bar;

    private RecyclerView search_page_recycler_view;

    private ImageButton search_page_prev_button, search_page_next_button, search_page_starred_button;

    private TextView search_page_curr_page_text;

    public FragmentSearchPage() {
        // Required empty public constructor
    }

    public static FragmentSearchPage newInstance(String param1, String param2) {
        FragmentSearchPage fragment = new FragmentSearchPage();
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

        View view = inflater.inflate(R.layout.fragment_search_page, container, false);
        search_page_spinner_path = view.findViewById(R.id.search_page_spinner_path);
        search_page_spinner_all = view.findViewById(R.id.search_page_spinner_all);
        search_page_search_bar = view.findViewById(R.id.search_page_search_bar);
        search_page_recycler_view = view.findViewById(R.id.search_page_recycler_view);
        search_page_prev_button = view.findViewById(R.id.search_page_prev_button);
        search_page_next_button = view.findViewById(R.id.search_page_next_button);
        search_page_starred_button = view.findViewById(R.id.search_page_starred_button);
        search_page_curr_page_text = view.findViewById(R.id.search_page_curr_page_text);

        return view;
    }
}
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
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FragmentSearchPage extends Fragment {
    private Spinner search_page_spinner_path, search_page_spinner_all;

    private SearchView search_page_search_bar;

    private RecyclerView search_page_recycler_view;

    private ImageButton search_page_prev_button, search_page_next_button;

    private TextView search_page_curr_page_text;

    private ArrayList<Path> mPaths; // all incomplete paths for currentLocalUser
    private FirebaseFirestore db;
    private User currentLocalUser;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private PathsAdapter.IPaths mListener;

    public FragmentSearchPage() {
        // Required empty public constructor
    }

    public FragmentSearchPage(User currentLocalUser, ArrayList<Path> mPaths) {
        this.currentLocalUser = currentLocalUser;
        this.mPaths = mPaths;
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
            db = FirebaseFirestore.getInstance();
            mPaths = new ArrayList<>();
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
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
        search_page_curr_page_text = view.findViewById(R.id.search_page_curr_page_text);

        fetchCurrentPaths();

        return view;
    }

    private void fetchCurrentPaths() {
        ArrayList<Path> paths = new ArrayList<>();
//        db.collection("users")
//                .document(mUser.getEmail())
//                .collection("paths")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                    }
//                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PathsAdapter.IPaths){
            this.mListener = (PathsAdapter.IPaths) context;
        } else{
            throw new RuntimeException(context.toString()
                    + "must implement ISearchPage");
        }
    }
}
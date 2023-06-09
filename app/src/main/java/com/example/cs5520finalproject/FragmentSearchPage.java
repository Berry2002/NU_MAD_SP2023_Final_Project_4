package com.example.cs5520finalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Screen for looking at all the paths you haven't completed.
 * Also is the location for the user to either equip or leave a path.
 */
public class FragmentSearchPage extends Fragment {

    private RecyclerView search_page_recycler_view;
    private TextView search_page_current_path;
    private Button leave_path_button;
    private ArrayList<Path> mPaths; // all incomplete paths for currentLocalUser
    private FirebaseFirestore db;
    private User currentLocalUser;
    private PathsAdapter pathsAdapter;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private IFragmentToMainActivity mListener;

    public FragmentSearchPage() {
        // Required empty public constructor
    }

    public FragmentSearchPage(User user) {
        this.currentLocalUser = user;
        Log.d("search page constructor, user: ", user.getDisplayName());
        this.db = FirebaseFirestore.getInstance();
        this.mPaths = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_page, container, false);
        search_page_current_path = view.findViewById(R.id.search_page_current_path);

        String currentPath = currentLocalUser.getCurrentPathName();
        if (currentPath == null) {
            search_page_current_path.setText("No path equipped currently.");
        } else {
            search_page_current_path.setText("Current Path: " + currentPath);
        }
        leave_path_button = view.findViewById(R.id.leave_path_button);

        leave_path_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPath != null) { // leave current path
                    mListener.leaveCurrentPath();
                } else { // no current path
                    Toast.makeText(getContext(),"Click on a Path to Start Exploring!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // set up recycler view
        search_page_recycler_view = view.findViewById(R.id.search_page_recycler_view);
        recyclerViewLayoutManager = new LinearLayoutManager(getContext());
        pathsAdapter = new PathsAdapter(mPaths, getContext());
        search_page_recycler_view.setLayoutManager(recyclerViewLayoutManager);
        search_page_recycler_view.setAdapter(pathsAdapter);

        Log.d("search page", "onCreateView: currentLocalUser == null " + (currentLocalUser == null));
        Log.d("search page, user", currentLocalUser.getDisplayName());

        // get paths left for the current User
        fetchCurrentPathsLeft();

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentToMainActivity) {
            this.mListener = (IFragmentToMainActivity) context;
        } else{
            throw new RuntimeException(context + " must implement IFragmentToMainActivity");
        }
    }

    /**
     * Retrieve all the paths left for the current user.
     */
    private void fetchCurrentPathsLeft() {

        mPaths.clear();
        ArrayList<String> completedPaths = currentLocalUser.getCompletedPaths(); // user's completed paths

        // get all paths in Firebase
        db.collection(Tags.PATHS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                                Path path = documentSnapshot.toObject(Path.class);

                                // if user has neither completed the path nor on current path

                                if (!completedPaths.contains(path.getPathID())
                                        && (currentLocalUser.getCurrentPathID() == null
                                        || !currentLocalUser.getCurrentPathID().equals(path.getPathID()))) {

                                    mPaths.add(path);
                                }
                            }
                            pathsAdapter.setPaths(mPaths);
                            pathsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
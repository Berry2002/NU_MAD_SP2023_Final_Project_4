package com.example.cs5520finalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class FragmentProfilePage extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private User currentUserLocalType;
    private TextView currentPath, exp, questingSince, displayName;
    private ImageButton profilePicture, logOutButton;
    private IFragmentToMainActivity pathway;
    private Context context;
    private RecyclerView travelLogRecycler;
    private TravelLogAdapter travelLogAdapter;
    private RecyclerView.LayoutManager travelLayoutManager;

    public FragmentProfilePage() {
        // Required empty public constructor
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.currentUser = this.mAuth.getCurrentUser();
        this.db.collection(Tags.USERS).document(this.currentUser.getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    currentUserLocalType = value.toObject(User.class);
                } else {
                    Log.e("profile page", "onEvent: could not retrieve the current user from Firestore");
                }
            }
        });
    }

    public FragmentProfilePage(User currentUserLocalType) {
        this.currentUserLocalType = currentUserLocalType;
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance(); // don't know if i'll need this
        this.currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentToMainActivity) {
            this.pathway = (IFragmentToMainActivity) context;
            this.context = context;
        } else {
            throw new IllegalStateException(context + " should implement IFragmentToMainActivity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        // fetch the display name, questing since, current path, exp, travel log
        this.displayName = view.findViewById(R.id.displayName_inProfilePage);
        this.profilePicture = view.findViewById(R.id.profilePicture_inProfilePage);
        this.currentPath = view.findViewById(R.id.currentPath_inProfilePage);
        this.exp = view.findViewById(R.id.exp_inProfilePage);
        this.questingSince = view.findViewById(R.id.questingSince_inProfilePage);
        this.logOutButton = view.findViewById(R.id.logoutButton_profilePage);

        this.profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // prompt the user to open their device and select a photo to upload
                pathway.changeProfilePicture();
            }
        });

        this.logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathway.logout();
            }
        });

        this.travelLogRecycler = view.findViewById(R.id.travelLogRecyclerView);
        this.travelLogAdapter = new TravelLogAdapter(this.currentUserLocalType.getTravelLog(),
                this.getContext(), this.currentUserLocalType.getEmail());
        this.travelLayoutManager = new GridLayoutManager(this.getContext(), 2);
        this.travelLogRecycler.setLayoutManager(travelLayoutManager);
        this.travelLogRecycler.setAdapter(this.travelLogAdapter);
        this.travelLogAdapter.notifyDataSetChanged();

        this.refreshUserData();

        return view;
    }

    private void refreshUserData() {
        // call the method to fetch the data
        if (this.currentUserLocalType != null) {
            this.displayName.setText(this.currentUserLocalType.getDisplayName());
            if (this.currentUserLocalType.getCurrentPathName() != null) { // set the current path
                this.currentPath.setText(String.format("Current Path: %s", this.currentUserLocalType.getCurrentPathName()));
            } else {
                this.currentPath.setText("No path equipped currently.");
            }
            this.exp.setText(String.format("EXP: %d points", this.currentUserLocalType.getExp()));
            LocalDate localDate = Instant.ofEpochMilli(this.currentUserLocalType.getStartDate())
                    .atZone(ZoneId.systemDefault()).toLocalDate();
            this.questingSince.setText(String.format("Questing since: %s %s, %s",
                    localDate.getDayOfMonth(), localDate.getMonth(), localDate.getYear()));
        }
        refreshProfilePicture();
    }

    private void refreshProfilePicture() {
        String imgPath = Tags.FIREBASE_STORAGE_BASE + this.currentUser.getEmail() + Tags.FIREBASE_STORAGE_PROFILE_PICTURE;
//        imgPath = "https://firebasestorage.googleapis.com/v0/b/quest-8f3ba.appspot.com/o/images%2Fsanjana%40gmail.com%2Fthumbnail_20230220_162027.jpg?alt=media&token=dc64a515-bda2-4790-aa99-941f302e7eee";
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = mStorage.getReference(imgPath);
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .fitCenter()
                        .thumbnail(Glide.with(context).load(R.drawable.loading_image))
                        .error(R.drawable.profile_icon)
                        .into(profilePicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Glide.with(context)
                        .load(R.drawable.no_image_found)
                        .fitCenter()
                        .into(profilePicture);
            }
        });
    }

}
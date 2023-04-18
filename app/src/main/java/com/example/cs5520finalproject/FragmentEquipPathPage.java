package com.example.cs5520finalproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Displays more information about the path for the user to equip it.
 */
public class FragmentEquipPathPage extends Fragment {

    private TextView textViewEquipPathName, textViewEquipPathDescription;
    private Button buttonEquipPath;
    private ImageView imageViewEquipPath;

    private Path selectedPath;

    private IFragmentToMainActivity mListener;

    public FragmentEquipPathPage() {
        // Required empty public constructor
    }
    public FragmentEquipPathPage(Path path) {
        this.selectedPath = path;
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
        View view = inflater.inflate(R.layout.fragment_equip_path_page, container, false);

        textViewEquipPathName = view.findViewById(R.id.textViewEquipPathName);
        textViewEquipPathDescription = view.findViewById(R.id.textViewEquipPathDescription);
        buttonEquipPath = view.findViewById(R.id.buttonEquipPath);
        imageViewEquipPath = view.findViewById(R.id.imageViewEquipPath);

        setCurrentPathInfo(selectedPath);

        // jump to home page displaying list of quests contained in this path
        buttonEquipPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.equipPath(selectedPath);
            }
        });

        return view;
    }

    private void setCurrentPathInfo(Path path) {
        textViewEquipPathName.setText(
                new StringBuilder(path.getLocation() + " - "
                        + path.getSubject()));
        textViewEquipPathDescription.setText(path.getDescription());

        Glide.with(this)
                .load(Uri.parse(path.getImage()))
                .into(imageViewEquipPath);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IFragmentToMainActivity){
            mListener = (IFragmentToMainActivity) context;
        } else{
            throw new RuntimeException(context.toString()+ "must implement IFragmentToMainActivity");
        }
    }
}
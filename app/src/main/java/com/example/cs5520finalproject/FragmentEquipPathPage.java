package com.example.cs5520finalproject;

import static com.example.cs5520finalproject.Tags.EQUIP_PATH_PAGE;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentEquipPathPage extends Fragment {

    private TextView textViewEquipPathName, textViewEquipPathDescription;
    private Button buttonEquipPath;
    private ImageView imageViewEquipPath;

    private Path selectedPath;

    private FirebaseFirestore db;

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
            db = FirebaseFirestore.getInstance();
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

        fetchCurrentPath(selectedPath);

        // jump to home page displaying list of quests contained in this path
        imageViewEquipPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.equipPath(selectedPath);
            }
        });

        return view;
    }

    private void fetchCurrentPath(Path path) {
        DocumentReference mPath = db.collection("paths")
                .document(path.getLocation()); // get document path location??

        // get path document
        mPath.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        // set corresponding path info
                        textViewEquipPathName.setText(
                                new StringBuilder(selectedPath.getLocation()
                                        + " - "
                                        + selectedPath.getSubject()));
                        textViewEquipPathDescription.setText(selectedPath.getDescription());
                        imageViewEquipPath.setImageResource(Integer.parseInt(selectedPath.getImage()));//imageurl???
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(EQUIP_PATH_PAGE, "fetchPathInfo failed");
                    }
                });
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof IFragmentToMainActivity){
            mListener = (IFragmentToMainActivity) context;
        } else{
            throw new RuntimeException(context.toString()+ "must implement IEquipPath");
        }
    }
}
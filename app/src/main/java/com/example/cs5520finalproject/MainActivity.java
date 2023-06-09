package com.example.cs5520finalproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.cs5520finalproject.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Main Activity that is responsible for switching between fragments.
 */
public class MainActivity extends AppCompatActivity
        implements IFragmentToMainActivity,
        FragmentCameraController.DisplayTakenPhoto,
        FragmentDisplayImage.RetakePhoto {
    private static final int PERMISSIONS_CODE = 0x100;
    private String imageLocation; // image path
    ActivityMainBinding binding;
    private FirebaseAuth mAuth; // Firebase Authentication User
    private FirebaseFirestore db; // Firebase
    private FirebaseUser currentUser; // current FirebaseUser
    private FirebaseStorage storage; // FirebaseStorage for storing images
    private User currentUserLocalType; // local User object

    // from search paths page to path highlight page
    // Retrieving an image from gallery....
    private ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK){
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                        replaceFragment(FragmentDisplayImage.newInstance(selectedImageUri));
                    }
                }
            }
    );

    public MainActivity() {
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance(); // getting the instance of FirebaseStorage....
        this.currentUser = this.mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.populateScreen();

        binding.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case (R.id.fragmentQuestHomePage):
                        Log.d("current firebase user:", currentUser.getDisplayName());
//                        replaceFragment(new FragmentQuestHomePage(currentUserLocalType));
                        switchToHomePageFragment();
                        break;
                    case (R.id.fragmentProfilePage):
                        switchToProfilePageFragment();
                        break;
                    case (R.id.fragmentSearchPage):
                        replaceFragment(new FragmentSearchPage(currentUserLocalType));
                        break;
                    case (R.id.fragmentRankingsPage):
                        replaceFragment(new FragmentRankingsPage());
                        break;
                    case (R.id.fragmentReviewsHomePage):
                        replaceFragment(new FragmentReviewsHomePage());
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void goToPathHighlights(Path path) {
        replaceFragment(new FragmentEquipPathPage(path));
    }

    @Override
    public void equipPath(Path path) {
        // can only start path if they have no current path
        if (currentUserLocalType.getCurrentPathID() == null) {
//            this.updateInfo(Tags.USERS_CURRENT_PATH_ID, path.getPathID());
//            this.updateInfo(Tags.USERS_CURRENT_PATH_NAME, path.getPathName());
            this.currentUserLocalType.setCurrentPathID(path.getPathID());
            this.currentUserLocalType.setCurrentPathName(path.getPathName());
            this.updateQuestsCompleted("");
            this.updateUserOnFirebase();
            this.populateScreen();
        } else {
            Toast.makeText(this,
                    "Please finish the current path before starting another one!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void toLoginPage() {
        this.replaceFragment(new FragmentLogin());
    }

    @Override
    public void toRegisterPage() {
        this.replaceFragment(new FragmentRegister());
    }

    @Override
    public void goToHomePage(FirebaseUser currentUser) {
        this.currentUser = currentUser;

        Log.d("current firebase user:", currentUser.getDisplayName());
        this.populateScreen();
    }

    @Override
    public void logout() {
        this.mAuth.signOut();
        this.currentUser = null;
        this.populateScreen();
    }

    @Override
    public void leaveCurrentPath() {
        Toast.makeText(this,"Current Path removed!",Toast.LENGTH_SHORT).show();
        this.currentUserLocalType.setCurrentPathID(null);
        this.currentUserLocalType.setCurrentPathName(null);
        this.updateUserOnFirebase();
        this.replaceFragment(new FragmentSearchPage(currentUserLocalType));
        this.populateScreen();
    }

    @Override
    public void goToPathReviews(Path path) {
        replaceFragment(new FragmentPathReviewsPage(currentUserLocalType, path));
    }

    public void changeProfilePicture() {
        this.imageLocation = Tags.FIREBASE_STORAGE_PROFILE_PICTURE;
        binding.bottomNavView.setVisibility(View.GONE);
        checkForCameraPermission();
    }

    /**
     * Get the appropriate screen when landing.
     */
    private void populateScreen() {
        BottomNavigationView navBar = findViewById(R.id.bottomNavView);
        if (this.currentUser == null) { // if no user is logged in, we prompt login/register
            navBar.setVisibility(View.GONE);
            replaceFragment(new FragmentAuthentication());
        } else { // go to the current user's home page
            navBar.setVisibility(View.VISIBLE);
            navBar.setSelectedItemId(R.id.fragmentQuestHomePage);
            this.switchToHomePageFragment();
        }
    }

    /**
     * Go to quest home page fragment to see all quests left.
     */
    private void switchToHomePageFragment() {
        String email = this.currentUser.getEmail();
        if (email == null) {
            Log.e("main activity", "switchToHomePageFragment: current user's email is null");
        } else {
            this.db.collection(Tags.USERS).document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        currentUserLocalType = value.toObject(User.class);
                        // current user retrieved
                        replaceFragment(new FragmentQuestHomePage(currentUserLocalType));
                    } else {
                        Log.e("main activity", "onEvent: could not retrieve the current user from Firestore");
                    }
                }
            });
        }
    }

    /**
     * Go to profile page fragment.
     */
    private void switchToProfilePageFragment() {
        binding.bottomNavView.setVisibility(View.VISIBLE);
        String email = this.currentUser.getEmail();

        if (email == null) {
            Log.e("main activity", "switchToHomePageFragment: current user's email is null");
        } else {
            this.db.collection(Tags.USERS).document(email).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null) {
                        currentUserLocalType = value.toObject(User.class);
                        // current user retrieved
                        replaceFragment(new FragmentProfilePage(currentUserLocalType));
                    } else {
                        Log.e("main activity", "onEvent: could not retrieve the current user from Firestore");
                    }
                }
            });
        }
    }

    /**
     * Abstract method for replacing the current fragment with the given fragment.
     * @param fragment the fragment to be switched to
     */
    private void replaceFragment(Fragment fragment) {
        if (!this.getSupportFragmentManager().isDestroyed()) {
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Update the corresponding data when the user completes a quest.
     * @param questName name of the quest completed
     */
    private void updateQuestsCompleted(String questName) {
        ArrayList<String> questsCompleted;
        if (questName.equals("")) { // if starting a new path, set completed quests to empty list
            questsCompleted = new ArrayList<>();
        } else {
            questsCompleted = this.currentUserLocalType.getCompletedQuests();
            questsCompleted.add(questName);
        }
        this.currentUserLocalType.setCompletedQuests(questsCompleted);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 2) {
            binding.bottomNavView.setVisibility(View.GONE);
            replaceFragment(FragmentCameraController.newInstance());
        } else {
            Toast.makeText(this, "You must allow Camera and Storage permissions!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTakePhoto(Uri imageUri) {
        Log.d("onTakePhoto", "here");
        replaceFragment(FragmentDisplayImage.newInstance(imageUri));
    }

    @Override
    public void onOpenGalleryPressed() {
        openGallery();
    }

    @Override
    public void onRetakePressed() {
        replaceFragment(new FragmentCameraController());
    }

    @Override
    public void onUploadButtonPressed(Uri imageUri) {
        // Upload an image from local file....
        StorageReference storageReference = storage.getReference()
                .child(Tags.FIREBASE_STORAGE_BASE + this.currentUser.getEmail() + this.imageLocation);

        UploadTask uploadImage = storageReference.putFile(imageUri);

        uploadImage.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Upload Failed! Try again!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(MainActivity.this, "Upload successful! Check Firestore", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void addPictureToTravelLog(String questName) {
        this.imageLocation =  "/" + Tags.FIREBASE_STORAGE_TRAVEL_LOG + questName + ".jpg";
//        binding.bottomNavView.setVisibility(View.GONE);

        this.currentUserLocalType.getTravelLog().add(this.imageLocation); // add it to the local user

        // update on Firebase
        this.db.collection(Tags.USERS).document(this.currentUserLocalType.getEmail())
                .update(Tags.USERS_TRAVEL_LOG, this.currentUserLocalType.getTravelLog())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            checkForCameraPermission();
                        } else {
                            Log.e("main activity", "add picture to travel log failed");
                        }
                    }
                });
    }

    /**
     * Check for user's camera permission.
     */
    private void checkForCameraPermission() {
        // Asking for permissions in runtime......
        Boolean cameraAllowed = ContextCompat
                .checkSelfPermission(this,
                        android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        Boolean readAllowed = ContextCompat
                .checkSelfPermission(this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Boolean writeAllowed = ContextCompat
                .checkSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (cameraAllowed && readAllowed && writeAllowed) {
            replaceFragment(FragmentCameraController.newInstance());
        } else {
            requestPermissions(new String[]{
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSIONS_CODE);
        }
    }

    /**
     * Use the gallery launcher to load the gallery.
     */
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        galleryLauncher.launch(intent);
    }

    /**
     * Update the user's exp with the given exp added.
     * @param exp exp to be added
     */
    private void updateExp(int exp) {
        int newExp = currentUserLocalType.getExp() + exp;
        this.db.collection(Tags.USERS).document(this.currentUser.getEmail())
                .update(Tags.USERS_EXP, newExp)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e("updateExp", "onComplete: could not update exp");
                        } else {
                            currentUserLocalType.addExp(exp); // update local user exp
                        }
                    }
                });
    }

    @Override
    public void completeQuest(String questName, int questIndex, int expValue) {
        this.currentUserLocalType.addExp(expValue); // update user exp (local)
        this.updateQuestsCompleted(questName); // update user quests completed (local)
        this.updateUserOnFirebase(); // update user on the firebase
        this.db.collection(Tags.PATHS)
                .document(this.currentUserLocalType.getCurrentPathID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Path currentPath = task.getResult().toObject(Path.class);

                    // if there are no more quests in the path - set the path to completed
                    if (currentPath.getNumQuests() == currentUserLocalType.getCompletedQuests().size()) {
                        // finished all quests - make the current path stuff empty
                        currentUserLocalType.completedPath(currentPath.getPathID());
                        updateQuestsCompleted(""); // starting a new path, make quests completed empty
                        // also need to update the firebase on the user's completed path
                        updateUserOnFirebase();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateUserOnFirebase() {
        HashMap<String, Object> userHash = new HashMap<>();
        userHash.put(Tags.USERS_DISPLAY_NAME, this.currentUserLocalType.getDisplayName());
        userHash.put(Tags.USERS_EMAIL, this.currentUserLocalType.getEmail());
        userHash.put(Tags.USERS_PASSWORD, this.currentUserLocalType.getPassword());
        userHash.put(Tags.USERS_EXP, this.currentUserLocalType.getExp());
        userHash.put(Tags.USERS_COMPLETED_PATHS, this.currentUserLocalType.getCompletedPaths());
        userHash.put(Tags.USERS_COMPLETED_QUESTS, this.currentUserLocalType.getCompletedQuests());
        userHash.put(Tags.USERS_CURRENT_PATH_ID, this.currentUserLocalType.getCurrentPathID());
        userHash.put(Tags.USERS_CURRENT_PATH_NAME, this.currentUserLocalType.getCurrentPathName());
        userHash.put(Tags.USERS_PROFILE_PICTURE, this.currentUserLocalType.getProfilePicture());
        userHash.put(Tags.USERS_TRAVEL_LOG, this.currentUserLocalType.getTravelLog());
        userHash.put(Tags.USERS_START_DATE, this.currentUserLocalType.getStartDate());

        this.db.collection(Tags.USERS)
                .document(this.currentUserLocalType.getEmail())
                .set(userHash)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Register Fragment", "onFailure: " + e.getMessage());
                    }
                });
    }
}
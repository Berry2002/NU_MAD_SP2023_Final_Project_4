package com.example.cs5520finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.cs5520finalproject.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity
        implements FragmentSearchPage.ISearchPage {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new FragmentQuestHomePage());

        binding.bottomNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case (R.id.fragmentQuestHomePage):
                        replaceFragment(new FragmentQuestHomePage());
                        break;
                    case (R.id.fragmentProfilePage):
                        replaceFragment(new FragmentProfilePage());
                        break;
                    case (R.id.fragmentSearchPage):
                        replaceFragment(new FragmentSearchPage());
                        break;
                    case (R.id.fragmentRankingsPage):
                        replaceFragment(new FragmentRankingsPage());
                        break;
                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public void toStarredQuestsPage() {
        Fragment FragmentStarredQuestsPage = new FragmentStarredPathsPage();
        replaceFragment(FragmentStarredQuestsPage);
    }

    @Override
    public void toSearchPage() {
        Fragment FragmentSearchPage = new FragmentSearchPage();
        replaceFragment(FragmentSearchPage);
    }
}
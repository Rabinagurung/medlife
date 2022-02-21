package com.example.medlife.home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.medlife.R;
import com.example.medlife.home.fragments.AboutUsFragment;
import com.example.medlife.home.fragments.CartFragment;
import com.example.medlife.home.fragments.CategoryFragment;
import com.example.medlife.home.fragments.WishListFragment;
import com.example.medlife.home.fragments.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment;
    CartFragment cartFragment;
    CategoryFragment categoryFragment;
    AboutUsFragment aboutUsFragment;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.homeBottomNav);
        homeFragment = new HomeFragment();
        homeFragment.setBottomNavigationView(bottomNavigationView);
        currentFragment = homeFragment;

        getSupportFragmentManager().beginTransaction().add(R.id.homeFrameContainer, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if (item.getTitle().equals(getString(R.string.home))) {
                    if (homeFragment == null)
                        homeFragment = new HomeFragment();
                    changeFragment(homeFragment);
                    return true;

                }
                if (item.getTitle().equals(getString(R.string.categories))) {
                    if (categoryFragment == null)
                        categoryFragment = new CategoryFragment();
                    changeFragment(categoryFragment);
                    return true;

                } if (item.getTitle().equals(getString(R.string.cart))) {
                    if (cartFragment == null)
                        cartFragment = new CartFragment();
                    changeFragment(cartFragment);
                    return true;
                }



                if (item.getTitle().equals(getString(R.string.aboutUs))) {
                    if (aboutUsFragment == null)
                        aboutUsFragment= new AboutUsFragment();
                    changeFragment(aboutUsFragment);
                    return true;
                }
                return false;
            }
        });
    }

    void changeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(currentFragment).commit();

        if (fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().show(fragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.homeFrameContainer, fragment).commit();
        }
        currentFragment = fragment;
    }
}
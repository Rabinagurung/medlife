package com.example.medlife.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.medlife.R;
import com.example.medlife.home.fragments.CartFragment;
import com.example.medlife.home.fragments.CategoryFragment;
import com.example.medlife.home.fragments.ProfileFragment;
import com.example.medlife.home.fragments.WishListFragment;
import com.example.medlife.home.fragments.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment;
    ProfileFragment profileFragment;
    CartFragment cartFragment;
    WishListFragment wishListFragment;
    CategoryFragment categoryFragment;
    Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.homeBottomNav);
        homeFragment = new HomeFragment();
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
                if (item.getTitle().equals(getString(R.string.wishlist))) {
                    if (wishListFragment == null)
                        wishListFragment = new WishListFragment();
                    changeFragment(wishListFragment);
                    return true;

                }
                if (item.getTitle().equals(getString(R.string.profile))) {
                    if (profileFragment == null)
                        profileFragment = new ProfileFragment();
                    changeFragment(profileFragment);
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
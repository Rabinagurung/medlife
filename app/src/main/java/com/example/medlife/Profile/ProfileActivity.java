package com.example.medlife.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.medlife.Profile.Logout.LogoutActivity;
import com.example.medlife.Profile.Orders.OrdersActivity;
import com.example.medlife.Profile.Security.SecurityActivity;
import com.example.medlife.Profile.UserProfile.UserProfileActivity;
import com.example.medlife.Profile.Wishlist.WishlistActivity;
import com.example.medlife.R;
import com.example.medlife.checkout.address.AddressActivity;
import com.example.medlife.userAccount.UserAccountActivity;
import com.example.medlife.utils.SharedPrefUtils;

public class ProfileActivity extends AppCompatActivity {
    LinearLayout profileLL, wishListLL, ordersLL, locationLL, securityLL, logoutLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileLL = findViewById(R.id.profileLL);
        wishListLL = findViewById(R.id.wishListLL);
        ordersLL = findViewById(R.id.ordersLL);
        locationLL = findViewById(R.id.locationLL);
        securityLL = findViewById(R.id.securityLL);
        logoutLL = findViewById(R.id.logoutLL);
        getSupportActionBar().setTitle(" Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setClickListeners();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setClickListeners() {
        profileLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, UserProfileActivity.class);
                startActivity(intent);

            }});

        wishListLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, WishlistActivity.class);
                startActivity(intent);
            }
        });

        ordersLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, OrdersActivity.class);
                startActivity(intent);
            }
        });

        locationLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });


        securityLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SecurityActivity.class);
                startActivity(intent);

            }
        });

        logoutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefUtils.setBoolean(ProfileActivity.this, getString(R.string.isLogged),false);
                Intent intent = new Intent(ProfileActivity.this, UserAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
package com.example.medlife.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.medlife.Profile.InsideProfile.OrdersActivity;
import com.example.medlife.Profile.InsideProfile.SecurityActivity;
import com.example.medlife.Profile.InsideProfile.UserProfileActivity;
import com.example.medlife.Profile.InsideProfile.WishlistActivity;
import com.example.medlife.R;
import com.example.medlife.admin.AdminActivity;
import com.example.medlife.checkout.address.AddressActivity;
import com.example.medlife.userAccount.UserAccountActivity;
import com.example.medlife.utils.SharedPrefUtils;

public class ProfileActivity extends AppCompatActivity {
//    LinearLayout profileLL, wishListLL, ordersLL, locationLL, securityLL, logoutLL;
//    TextView adminTV;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//        profileLL = findViewById(R.id.profileLL);
//        wishListLL = findViewById(R.id.wishListLL);
//        ordersLL = findViewById(R.id.ordersLL);
//        locationLL = findViewById(R.id.locationLL);
//        securityLL = findViewById(R.id.securityLL);
//        logoutLL = findViewById(R.id.logoutLL);
//        adminTV = findViewById(R.id.adminTV);
//        getSupportActionBar().setTitle(" Profile");
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        setClickListeners();
////        checkAdmin();
//    }
//
////    private void checkAdmin() {
////        boolean is_staff = SharedPrefUtils.getBool(ProfileActivity.this, "sfk", false);
////        if (is_staff)
////            adminTV.setVisibility(View.VISIBLE);
////
////    }
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void setClickListeners() {
//        profileLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ProfileActivity.this, UserProfileActivity.class);
//                startActivity(intent);
//
//            }});
//
//        wishListLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ProfileActivity.this, WishlistActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        ordersLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ProfileActivity.this, OrdersActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        locationLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ProfileActivity.this, AddressActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//        securityLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ProfileActivity.this, SecurityActivity.class);
//                startActivity(intent);
//
//            }
//        });
//
//        logoutLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPrefUtils.setBoolean(ProfileActivity.this, getString(R.string.isLogged),false);
//                Intent userAccount = new Intent(ProfileActivity.this, UserAccountActivity.class);
//                startActivity(userAccount);
//                ProfileActivity.this.finish();
//            }
//        });
//
//        adminTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ProfileActivity.this, AdminActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//    }
}
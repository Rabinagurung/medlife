package com.example.medlife.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.medlife.Profile.InsideProfile.SecurityActivity;
import com.example.medlife.Profile.InsideProfile.WishlistActivity;
import com.example.medlife.Profile.ProfileActivity;
import com.example.medlife.R;
import com.example.medlife.admin.AdminActivity;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.AllProductResponse;
import com.example.medlife.api.response.Product;
import com.example.medlife.api.response.RegisterResponse;
import com.example.medlife.checkout.CheckOutActivity;
import com.example.medlife.checkout.address.AddNewLocationActivity;
import com.example.medlife.checkout.address.AddressActivity;
import com.example.medlife.home.fragments.home.adapters.ShopAdapter;
import com.example.medlife.uploadPrescription.UploadPrescriptionActivity;
import com.example.medlife.userAccount.UserAccountActivity;
import com.example.medlife.utils.SharedPrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {
    LinearLayout wishListLL, ordersLL, prescriptionLL, locationLL, securityLL, logoutLL;
    TextView adminTV;
    RelativeLayout profileRV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileRV = view.findViewById(R.id.profileRV);
        wishListLL = view.findViewById(R.id.wishListLL);
        ordersLL = view.findViewById(R.id.ordersLL);
        prescriptionLL = view.findViewById(R.id.prescriptionLL);
        locationLL = view.findViewById(R.id.locationLL);
        securityLL = view.findViewById(R.id.securityLL);
        logoutLL = view.findViewById(R.id.logoutLL);
        adminTV = view.findViewById(R.id.adminTV);
        checkAdmin();

        profileRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        wishListLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WishlistActivity.class);
                startActivity(intent);

            }
        });

        locationLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddressActivity.class);
                startActivity(intent);
            }
        });

        ordersLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        prescriptionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UploadPrescriptionActivity.class);
                startActivity(intent);
            }
        });

        securityLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SecurityActivity.class);
                startActivity(intent);
            }
        });

        logoutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefUtils.clear(getContext());
                Intent userAccount = new Intent(getContext(), UserAccountActivity.class);
                startActivity(userAccount);
                getActivity().finish();


            }
        });

        adminTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AdminActivity.class);
                startActivity(intent);
            }
        });



    }

    private void checkAdmin() {
        boolean is_staff = SharedPrefUtils.getBool(getActivity(), getString(R.string.staff_key), false);
        if (is_staff)
            adminTV.setVisibility(View.VISIBLE);
    }


}
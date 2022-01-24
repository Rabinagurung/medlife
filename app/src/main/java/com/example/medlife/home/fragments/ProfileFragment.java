package com.example.medlife.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medlife.R;
import com.example.medlife.userAccount.UserAccountActivity;
import com.example.medlife.utils.SharedPrefUtils;


public class ProfileFragment extends Fragment {

    Button logoutButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logoutButton = view.findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefUtils.setBoolean(getActivity(), getString(R.string.isLogged), false);
                Intent intent = new Intent(getContext(), UserAccountActivity.class);
                startActivity(intent);
            }
        });
    }
}
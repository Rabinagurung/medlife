package com.example.medlife.userAccount.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.medlife.R;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.LoginResponse;
import com.example.medlife.home.MainActivity;
import com.example.medlife.utils.Constants;
import com.example.medlife.utils.SharedPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener {

    EditText emailEt, passwordET;
    LinearLayout loginBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEt = view.findViewById(R.id.emailET);
        passwordET = view.findViewById(R.id.passwordET);
        loginBtn = view.findViewById(R.id.loginLL);
        loginBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == loginBtn) {
            String email, password;
            email = emailEt.getText().toString();
            password = passwordET.getText().toString();
            if (email.isEmpty() && password.isEmpty())
                Toast.makeText(getContext(), "Email or Password is Empty!", Toast.LENGTH_LONG).show();
            else {
                Call<LoginResponse> loginResponseCall = ApiClient.getClient().login(email, password);
                loginResponseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        LoginResponse loginResponse = response.body();
                        if(response.isSuccessful()){
                            if(loginResponse.getError()){
                                System.out.println("222222221222222222222 my error  is: "+ loginResponse.getError());

                            }else {

                                Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_LONG).show();
                                SharedPrefUtils.setBoolean(getActivity(), getString(R.string.isLogged), true);
//                                SharedPrefUtils.setBoolean(getActivity(), "sfk", loginResponse.get);
                                SharedPrefUtils.setString(getActivity(), getString(R.string.name_key), loginResponse.getName());
                                SharedPrefUtils.setString(getActivity(), getString(R.string.email_id), loginResponse.getEmail());
                                SharedPrefUtils.setString(getActivity(), getString(R.string.created_key), loginResponse.getCreatedAt());
                                SharedPrefUtils.setString(getActivity(), getString(R.string.api_key), loginResponse.getApiKey());
                               // Toast.makeText(getContext(),SharedPrefUtils.getString(getContext(),getString(R.string.api_key)),Toast.LENGTH_LONG).show();
                                getActivity().startActivity(new Intent(getContext(), MainActivity.class));
                                getActivity().finish();

                            }

                        }                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                    }
                });
            }
//            else if (email.equals("thesaugatt@gmail.com") && password.equals("Pass123")) {
//                SharedPrefUtils.setBoolean(getActivity(), getString(R.string.isLoggedKey), true);
//                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
//                getActivity().finish();
//            } else
//                Toast.makeText(getContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
        }

    }
}
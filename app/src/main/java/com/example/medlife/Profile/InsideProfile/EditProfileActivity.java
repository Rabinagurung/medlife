package com.example.medlife.Profile.InsideProfile;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medlife.R;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.AllProductResponse;
import com.example.medlife.api.response.UpdateProfileResponse;
import com.example.medlife.home.MainActivity;
import com.example.medlife.utils.SharedPrefUtils;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    TextView fullNameET, userEmailET;
    AutoCompleteTextView gender_AT;
    LinearLayout saveLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setTitle("Rabina Gurung");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fullNameET = findViewById(R.id.fullNameET);
        userEmailET= findViewById(R.id.userEmailET);
        gender_AT= findViewById(R.id.gender_AT);
        String[] items ={"Male", "Female","Other"};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(EditProfileActivity.this, R.layout.item_list, items);
        gender_AT.setAdapter(itemAdapter);
        saveLL=findViewById(R.id.saveLL);
//        fullNameET.setText(SharedPrefUtils.getString(EditProfileActivity.this, getString(R.string.name_key)));
//        userEmailET.setText(SharedPrefUtils.getString(EditProfileActivity.this, getString(R.string.email_key)));

        fullNameET.setText(SharedPrefUtils.getString(EditProfileActivity.this, getString(R.string.name_key)));
        userEmailET.setText(SharedPrefUtils.getString(EditProfileActivity.this, getString(R.string.email_id)));
        //gender_AT.setText(SharedPrefUtils.getString(EditProfileActivity.this,""));

        saveLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String key = SharedPrefUtils.getString(EditProfileActivity.this, getString(R.string.api_key));
                    Call<UpdateProfileResponse> updateProfileResponseCall = ApiClient.getClient().updateProfile(key, fullNameET.getText().toString(), userEmailET.getText().toString(), gender_AT.getText().toString());
                    updateProfileResponseCall.enqueue(new Callback<UpdateProfileResponse>() {
                        @Override
                        public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                            if (response.isSuccessful()) {
                                if (!response.body().getError()) {
                                    SharedPrefUtils.setString(EditProfileActivity.this, getString(R.string.name_key), fullNameET.getText().toString());
                                    SharedPrefUtils.setString(EditProfileActivity.this, getString(R.string.email_id), userEmailET.getText().toString());
                                    SharedPrefUtils.setString(EditProfileActivity.this, getString(R.string.gender_key), gender_AT.getText().toString());
                                    Toast.makeText(EditProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {

                        }
                    });

                }
            }

        });
    }

    public boolean validate() {
        if (userEmailET.getText().toString().isEmpty()  || fullNameET.getText().toString().isEmpty())
        {
        Toast.makeText(EditProfileActivity.this, "None of the above fields can be empty", Toast.LENGTH_SHORT).show();
        return false;
        }else if (Patterns.EMAIL_ADDRESS.matcher(userEmailET.getText().toString()).matches()){
        return true;
        }else {
        Toast.makeText(this, "Enter valid Email address !", Toast.LENGTH_SHORT).show();
        }
        return false;

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
}
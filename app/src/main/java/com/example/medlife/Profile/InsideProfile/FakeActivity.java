package com.example.medlife.Profile.InsideProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.medlife.R;

public class FakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake);
    }
}

//public class ForgotPasswordActivity extends AppCompatActivity {
//    TextInputEditText oldPassword, newPassword, confirmPassword;
//    ImageView backIV;
//    MaterialButton passUpdate;
//    String passwordText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_forgot_password);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        getWindow().setStatusBarColor(Color.WHITE);
//        getSupportActionBar().hide();
//        backIV = findViewById(R.id.backIV);
//        oldPassword = findViewById(R.id.oldPassword);
//        newPassword = findViewById(R.id.newPassword);
//        confirmPassword = findViewById(R.id.confirmPassword);
//        passUpdate = findViewById(R.id.passUpdate);
//
//        passUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                updatePassword();
//            }
//        });
//        backIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//    }
//
//    private void updatePassword() {
//        if (validateAll()) {
//            String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
//            Call<RegisterResponse> changePassword = ApiClient.getClient().forgetPassword(key, newPassword.getText().toString());
//            changePassword.enqueue(new Callback<RegisterResponse>() {
//                @Override
//                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
//                    if (response.isSuccessful()) {
//                        if (!response.body().getError()) {
//                            Toast.makeText(ForgotPasswordActivity.this, "Password Successfully changed", Toast.LENGTH_SHORT).show();
//                            SharedPrefUtils.setString(ForgotPasswordActivity.this,  DataHolder.PASSWORD_KEY, newPassword.getText().toString());
//                            finish();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<RegisterResponse> call, Throwable t) {
//
//                }
//            });
//        }
//    }
//
//    private boolean validateAll() {
//        if (validatePassword() && newPassword()) {
//            return true;
//        }
//        return false;
//    }
//
//    private boolean validatePassword() {
//        String dbPassword = SharedPrefUtils.getString(this, DataHolder.PASSWORD_KEY);
//
//        passwordText = oldPassword.getText().toString().trim();
//        if (passwordText.isEmpty()) {
//            Toast.makeText(this, "Field cannot be left empty", Toast.LENGTH_SHORT).show();
//            return false;
//        }else if (!passwordText.equals(dbPassword)) {
//            Toast.makeText(this, "Old password doesn't match", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        return true;
//    }
//
//    private boolean newPassword() {
//        passwordText = oldPassword.getText().toString().trim();
//        String confirmPasswordText = newPassword.getText().toString().trim();
//
//        if (confirmPasswordText.isEmpty()) {
//            Toast.makeText(this, "Field cannot be left empty", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (confirmPasswordText.equals(passwordText)) {
//            Toast.makeText(this, "New password must differ from old password!", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
//            confirmPassword.setError("Password does not match please check!!");
//            return false;
//        }
//        return true;
//    }
//}
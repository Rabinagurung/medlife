package com.example.medlife.userAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.medlife.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class ForgetPasswordActivity extends AppCompatActivity {
    private TextInputEditText email_ET, newPw_ET;
    private Button changePwButton;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 6 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        email_ET = findViewById(R.id.email_ET);
        newPw_ET = findViewById(R.id.newPw_ET);
        changePwButton = findViewById(R.id.changePwButton);

        newPw_ET.addTextChangedListener(newPwTextWatcher);

        changePwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
    }

    private TextWatcher newPwTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pass = newPw_ET.getText().toString().trim();
            if (!PASSWORD_PATTERN.matcher(pass).matches()){
                newPw_ET.setError("Password must be 6 character long and include one lowercase, digit, special character and uppercase");
                changePwButton.setEnabled(false);

            }
            else {
                changePwButton.setError(null);
                changePwButton.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    private void changePassword() {
        final String email, password;
        boolean error = false;
        String url = "https://www.rojiranamagar.com.np/roomrent/forgot_password.php";
        password = newPw_ET.getText().toString().trim();
        email = email_ET.getText().toString().trim();

        if (email.isEmpty()){
            email_ET.setError("Please fill this field");
            error = true;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_ET.setError("Please enter a valid email address");
            error = true;
        }
        if (password.isEmpty()){
            newPw_ET.setError("Please fill this field");
            error = true;
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            newPw_ET.setError("Password must be 6 character long and include one lowercase, digit, special character and uppercase");
            error = true;
        }


    }
}
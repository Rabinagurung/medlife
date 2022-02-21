package com.example.medlife.uploadPrescription;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.medlife.R;

public class ConfirmationActivity extends AppCompatActivity {
    LinearLayout cancelLL, okLL;
// THIRD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        cancelLL = findViewById(R.id.cancelLL);
        okLL = findViewById(R.id.okLL);
        okLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmationActivity.this, PrescriptionSubmitActivity.class);
                startActivity(intent);
            }
        });
        cancelLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){ onBackPressed();
            }
        });
    }
}
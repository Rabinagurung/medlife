package com.example.medlife.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.medlife.R;

public class ContactUsActivity extends AppCompatActivity {
TextView contact1LL, contact2LL, EmailMsgLL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setTitle("Contact Us");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contact1LL = findViewById(R.id.contact1LL);
        contact2LL = findViewById(R.id.contact2LL);
        EmailMsgLL = findViewById(R.id.EmailMsgLL);
        setOnClickListeners();
    }

    private void setOnClickListeners(){
        contact1LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("+9779819176870");

            }});

        contact2LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("+9779814146808");
            }});

        EmailMsgLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email("rabinagurung347@gmail.com");
            }});
    }

    private void call(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void email(String name){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("email:" + name));
        startActivity(intent);
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
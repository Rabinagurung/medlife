package com.example.medlife.checkout.address;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.medlife.R;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.Address;
import com.example.medlife.api.response.AddressResponse;
import com.example.medlife.utils.SharedPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewLocationActivity extends AppCompatActivity {
    EditText cityET, streetET, provinceET, phoneET, descriptionET;
    LinearLayout addAddressLL, cancelLocationLL;
    static String ADDED_KEY = "ad";
    static String ADDED_DATA_KEY = "adk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_location);
        cityET = findViewById(R.id.cityET);
        streetET = findViewById(R.id.streetET);
        provinceET = findViewById(R.id.provinceET);
        phoneET= findViewById(R.id.phoneET);
        descriptionET = findViewById(R.id.descriptionET);
        addAddressLL = findViewById(R.id.addAddressLL);
        cancelLocationLL = findViewById(R.id.cancelLocationLL);
        addAddressLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String key = SharedPrefUtils.getString(AddNewLocationActivity.this, getString(R.string.api_key));
                    Call<AddressResponse> addressResponseCall = ApiClient.getClient().addAddress(key, cityET.getText().toString(), streetET.getText().toString(),
                            provinceET.getText().toString(), phoneET.getText().toString(), descriptionET.getText().toString());

                    addressResponseCall.enqueue(new Callback<AddressResponse>() {
                        @Override
                        public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                            AddressResponse addressResponse = response.body();
                            if (response.isSuccessful()) {
                                System.out.println(addressResponse.getMessage() + "  1 1111111111111");

                                if (!addressResponse.getError()) {
                                    Address address = new Address();
                                    address.setCity(cityET.getText().toString());
                                    address.setStreet(streetET.getText().toString());
                                    address.setProvince(provinceET.getText().toString());
                                    address.setDescription(descriptionET.getText().toString());
                                    address.setDescription(phoneET.getText().toString());
                                    address.setId(address.getId());
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra(ADDED_KEY, true);
                                    resultIntent.putExtra(ADDED_DATA_KEY, address);
                                    setResult(Activity.RESULT_OK, resultIntent);
                                    finish();

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AddressResponse> call, Throwable t) {

                        }});
                }
            }});

        cancelLocationLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public boolean validate() {
        boolean validate = true;
        if (cityET.getText().toString().isEmpty()
                || streetET.getText().toString().isEmpty()
                || provinceET.getText().toString().isEmpty()
                || phoneET.getText().toString().isEmpty()
                || descriptionET.getText().toString().isEmpty()) {
            Toast.makeText(AddNewLocationActivity.this, "None of the above fields can be empty", Toast.LENGTH_SHORT).show();
            validate = false;
        }
        return validate;
    }

}
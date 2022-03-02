package com.example.medlife.checkout.address;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.medlife.R;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.AddressResponse;
import com.example.medlife.api.response.Address;
import com.example.medlife.utils.SharedPrefUtils;
import com.google.android.material.textfield.TextInputEditText;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity {
    RecyclerView addressRV;
    LinearLayout addLocationLL;
    SwipeRefreshLayout swipe_refresh;
    public static String ADDRESS_SELECTED_KEY = "DFa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        addressRV = findViewById(R.id.addressRV);
        addLocationLL = findViewById(R.id.addLocationLL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Select Address");
        getAddressOnline();
        setClickListeners();

    }

    private void setClickListeners(){
        addLocationLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this, AddNewLocationActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getAddressOnline() {
        String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
        Call<AddressResponse> addressResponseCall = ApiClient.getClient().getMyAddresses(key);
        addressResponseCall.enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(Call<AddressResponse> call, Response<AddressResponse> response) {
                if (response.isSuccessful()) {
                    listAddress(response.body().getAdresses());
                }
            }

            @Override
            public void onFailure(Call<AddressResponse> call, Throwable t) {

            }
        });
    }

    private void listAddress(List<Address> adresses) {
        addressRV.setHasFixedSize(true);
        addressRV.setLayoutManager(new LinearLayoutManager(this));
        AddressAdapter addressAdapter = new AddressAdapter(adresses, this);
        addressAdapter.setOnAddressItemClickListener(new AddressAdapter.OnAddressItemClickListener() {
            @Override
            public void onAddressClick(int position, Address adress) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(ADDRESS_SELECTED_KEY, adress);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void onRemoveAddress(int position) {

            }
        });
        addressRV.setAdapter(addressAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
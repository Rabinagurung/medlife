package com.example.medlife.admin.DashBoardActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.medlife.R;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.Dash;
import com.example.medlife.api.response.DashResponse;
import com.example.medlife.utils.SharedPrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardActivity extends AppCompatActivity {
    TextView pendingOrdersTV,  totalOrdersTV,  shippedOrdersTV, totalCategoriesTV, totalCustomersTV, totalProductsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Dash Board");
        getDash();
        findIds();
    }

    private void getDash() {
        String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
        Call<DashResponse> addressResponseCall = ApiClient.getClient().getDash(key);
        addressResponseCall.enqueue(new Callback<DashResponse>() {
            @Override
            public void onResponse(Call<DashResponse> call, Response<DashResponse> response) {
                if(response.isSuccessful()) {
                    setDash(response.body().getDash());
                }
            }

            @Override
            public void onFailure(Call<DashResponse> call, Throwable t) {
            }
        });
    }

    private void setDash(Dash dash) {
        pendingOrdersTV.setText(dash.getPendingOrders().toString());
        totalCategoriesTV.setText(dash.getCategories().toString());
        totalCustomersTV.setText(dash.getCustomers().toString());
        totalOrdersTV.setText(dash.getProcessingOrders().toString());
        shippedOrdersTV.setText(dash.getShippedOrders().toString());
        totalProductsTV.setText(dash.getProducts().toString());
    }
    private void findIds(){
        pendingOrdersTV = findViewById(R.id.pendingOrdersTV);
        totalCategoriesTV = findViewById(R.id.totalCategoriesTV);
        totalCustomersTV = findViewById(R.id.totalCustomersTV);
        totalOrdersTV = findViewById(R.id.totalOrdersTV);
        shippedOrdersTV = findViewById(R.id.shippedOrdersTV);
        totalProductsTV = findViewById(R.id.totalProductsTV);
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
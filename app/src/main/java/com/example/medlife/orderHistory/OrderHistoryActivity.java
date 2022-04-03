package com.example.medlife.orderHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.medlife.R;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.OrderHistoryResponse;
import com.example.medlife.utils.SharedPrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    ImageView backIv;
    RecyclerView orderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_order_history);
        backIv = findViewById(R.id.backIv);
        orderHistory = findViewById(R.id.orderHistory);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getOrderHistory();
    }

    private void getOrderHistory() {
        String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
        Call<OrderHistoryResponse> orderHistoryResponseCall = ApiClient.getClient().orderHistory(key);
        orderHistoryResponseCall.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                if (response.isSuccessful()){
                    if (!response.body().getError()){
                        showOrders(response.body().getOrderHistory());
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {

            }
        });

    }

    private void showOrders(List<com.example.medlife.api.response.OrderHistory>order) {
        orderHistory.setHasFixedSize(true);
        orderHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        OrderAdapter orderAdapter = new OrderAdapter(order, this);
//        orderHistory.setAdapter(orderAdapter);

    }
}
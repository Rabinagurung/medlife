package com.example.medlife.orderHistory.orderDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medlife.R;
import com.example.medlife.api.response.Bag;
import com.example.medlife.api.response.OrderHistory;
import com.example.medlife.orderHistory.OrderHistoryActivity;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {
    public static String key = "oKey";
    OrderHistory orderHistory;
    TextView orderId, status, orderDate, totalTV, paymentStatus, paymentStatus1, payStatus, houseNo, delivery_area, street;
    RecyclerView cartIV;
    double deliveryCharge = 50;
    ImageView backIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        getSupportActionBar().hide();
        orderId = findViewById(R.id.orderId);
        status = findViewById(R.id.status);
        cartIV = findViewById(R.id.cartIV);
        paymentStatus = findViewById(R.id.paymentStatus);
        paymentStatus1 = findViewById(R.id.paymentStatus1);
        payStatus = findViewById(R.id.payStatus);
        totalTV = findViewById(R.id.totalTV);
        orderDate = findViewById(R.id.orderDate);
        backIv = findViewById(R.id.backIv);
        houseNo = findViewById(R.id.houseNo);
        delivery_area = findViewById(R.id.delivery_area);
        street = findViewById(R.id.street);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (getIntent().getSerializableExtra(key) != null) {
            orderHistory = (OrderHistory) getIntent().getSerializableExtra(key);
            setOrderHistory(orderHistory);
        }
        showOrders();

    }

    private void setOrderHistory(OrderHistory orderHistory) {
        orderId.setText("#" + orderHistory.getId() + "");
        houseNo.setText(orderHistory.getAddress().getHouse_no());
        delivery_area.setText(orderHistory.getAddress().getDelivery_area());
        street.setText(orderHistory.getAddress().getStreet());
        orderDate.setText(orderHistory.getOrderDateTime());
        if (orderHistory.getPaymentType() == 1){
            payStatus.setText("Cash On Delivery");
            paymentStatus.setVisibility(View.VISIBLE);
            paymentStatus1.setVisibility(View.GONE);
        }else {
            payStatus.setText("Khalti");
            paymentStatus.setVisibility(View.GONE);
            paymentStatus1.setVisibility(View.VISIBLE);
        }
        if (orderHistory.getStatus() == 0){
            status.setText("Pending");
        }else if (orderHistory.getStatus()  == 1){
            status.setText("Processing");
        }else
            status.setText("Completed");

    }

    private void showOrders() {
        List<Bag> bagList = orderHistory.getBag();
        cartIV.setHasFixedSize(true);
        cartIV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        OrderDetailsAdapter orderAdapter = new OrderDetailsAdapter(bagList, this);
//        cartIV.setAdapter(orderAdapter);
//        setPrice(bagList);
    }

    private void setPrice(List<Bag> data) {
        List<Bag> newData = data;
        double totalPrice = 0;
        for (int i = 0; i < newData.size(); i++) {
            Bag product = newData.get(i);
            int price = product.getUnitPrice();
            int q = product.getQuantity();
            totalPrice = totalPrice + price * q;
        }
        totalTV.setText("Rs. " + (totalPrice + deliveryCharge));
    }



}
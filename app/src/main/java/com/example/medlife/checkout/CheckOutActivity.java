package com.example.medlife.checkout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.medlife.R;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.Address;
import com.example.medlife.api.response.AllProductResponse;
import com.example.medlife.api.response.Product;
import com.example.medlife.api.response.RegisterResponse;
import com.example.medlife.checkout.address.AddressActivity;
import com.example.medlife.checkout.orderComplete.OrderCompleteActivity;
import com.example.medlife.home.fragments.home.adapters.ShopAdapter;

import com.example.medlife.utils.Constants;
import com.example.medlife.utils.SharedPrefUtils;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.checkout.helper.PaymentPreference;
//import com.khalti.checkout.helper.Config;
//import com.khalti.checkout.helper.KhaltiCheckOut;
//import com.khalti.checkout.helper.OnCheckOutListener;
//import com.khalti.checkout.helper.PaymentPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {
    public static String CHECK_OUT_PRODUCTS = "sd";
    RecyclerView allProductRV;
    AllProductResponse allProductResponse;
    ImageView backIv;
    ImageView cashOnDeliveryIV, khaltiIV;
    RecyclerView allProductsRV;
    LinearLayout addressLL, checkOutLL;
    Address address;
    TextView emptyAddressTv, cityStreetTV, provinceTV, totalTV, subTotalTV, shippingTV, totalPriceTv, discountTV;
    List<Product> products;
    double subTotalPrice = 0;
    double shippingCharge = 100;
    int p_type = 1;
    String p_ref = "COD";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        setContentView(R.layout.activity_check_out);
        backIv = findViewById(R.id.backIv);
        emptyAddressTv = findViewById(R.id.emptyAddressTv);
        addressLL = findViewById(R.id.addressLL);
        checkOutLL = findViewById(R.id.checkOutLL);
        cityStreetTV = findViewById(R.id.cityStreetTV);
        provinceTV = findViewById(R.id.provinceTV);
        allProductsRV = findViewById(R.id.allProductsRV);
        totalTV = findViewById(R.id.totalTV);
        subTotalTV = findViewById(R.id.subTotalTV);
        shippingTV = findViewById(R.id.shippingTV);
        totalPriceTv = findViewById(R.id.totalPriceTv);
        discountTV = findViewById(R.id.discountTV);
        cashOnDeliveryIV= findViewById(R.id.cashOnDeliveryIV);
        khaltiIV = findViewById(R.id.khaltiIV);
        setClickListners();
        allProductResponse = (AllProductResponse) getIntent().getSerializableExtra(CHECK_OUT_PRODUCTS);
        products = allProductResponse.getProducts();
        loadCartList();
    }

    private void setClickListners() {
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addressLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutActivity.this, AddressActivity.class);
                startActivityForResult(intent, 1);
//                startActivity(intent);

            }
        });
        emptyAddressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutActivity.this, AddressActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        checkOutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address != null) {
                    if (p_type == 1) {
                        checkOut();
                    } else {
                        khaltiCheckOut();
                    }
                } else {

                    Toast.makeText(CheckOutActivity.this, "Please Select A Address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        khaltiIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p_type = 2;
                khaltiIV.setBackground(getResources().getDrawable(R.drawable.box_shape_selected));
                cashOnDeliveryIV.setBackground(getResources().getDrawable(R.drawable.box_shape_selected));
            }
        });

        cashOnDeliveryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p_type = 1;
                cashOnDeliveryIV.setBackground(getResources().getDrawable(R.drawable.box_shape_selected));
                khaltiIV.setBackground(getResources().getDrawable(R.drawable.box_shape_selected));
            }
        });

    }



    private void khaltiCheckOut() {

        Map<String, Object> map = new HashMap<>();
        map.put("merchant_extra", "This is extra data");

        Config.Builder builder = new Config.Builder("test_public_key_cf36950f07d644ccbadbe04030eae034", ""+products.get(0).getId(), products.get(0).getName(), (long) (subTotalPrice + shippingCharge)*100, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
                Toast.makeText(CheckOutActivity.this, errorMap.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
                p_type = 2;
                p_ref = data.toString();
                checkOut();

            }
        })
                .paymentPreferences(new ArrayList<PaymentPreference>() {{
                    add(PaymentPreference.KHALTI);
                    add(PaymentPreference.EBANKING);
                    add(PaymentPreference.MOBILE_BANKING);
                    add(PaymentPreference.CONNECT_IPS);
                    add(PaymentPreference.SCT);
                }})
                .additionalData(map)
                .productUrl("http://example.com/product")
                .mobile("9819110254");
        Config config = builder.build();
        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(this, config);
        khaltiCheckOut.show();


    }


    private void loadCartList() {

        allProductsRV.setHasFixedSize(true);
        allProductsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ShopAdapter shopAdapter = new ShopAdapter(products, this, true, false);
        shopAdapter.setRemoveEnabled(false);
        allProductsRV.setAdapter(shopAdapter);
        setPrice();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            assert data != null;
            if (data.getSerializableExtra(AddressActivity.ADDRESS_SELECTED_KEY) != null) {
                showSelectedAddress((Address) data.getSerializableExtra(AddressActivity.ADDRESS_SELECTED_KEY));

            }
        }
    }

    private void showSelectedAddress(Address adress) {
        address = adress;
        emptyAddressTv.setVisibility(View.GONE);
        cityStreetTV.setText(adress.getCity() + " " + adress.getStreet());
        provinceTV.setText(adress.getProvince());
        addressLL.setVisibility(View.VISIBLE);
    }

    private void setPrice() {

        double discount = 0;
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getDiscountPrice() != 0 || products.get(i).getDiscountPrice() != null) {
                subTotalPrice = subTotalPrice + products.get(i).getDiscountPrice()* products.get(i).getCartQuantity();
                discount = discount + products.get(i).getPrice() - products.get(i).getDiscountPrice();
            } else
                subTotalPrice = subTotalPrice + products.get(i).getPrice()* products.get(i).getCartQuantity();
        }
        subTotalTV.setText("Rs. " + (subTotalPrice));
        totalTV.setText("Rs. " + (subTotalPrice + shippingCharge));
        totalPriceTv.setText("( Rs. " + subTotalPrice + " )");
        shippingTV.setText("Rs. " + shippingCharge);
        discountTV.setText("-" + "Rs. " + discount);


    }

    private void checkOut() {
        String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
        Call<RegisterResponse> orderCall = ApiClient.getClient().order(key, p_type, address.getId(), p_ref);
        orderCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(CheckOutActivity.this, OrderCompleteActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

            }
        });
    }
}
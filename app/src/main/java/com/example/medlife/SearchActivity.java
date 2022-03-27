package com.example.medlife;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.AllProductResponse;
import com.example.medlife.api.response.Product;
import com.example.medlife.home.fragments.home.adapters.SearchProductsAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView product_RV;
    SearchProductsAdapter searchProductsAdapter;
    LinearLayout SearchLL;
    ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.searchView);
        product_RV = findViewById(R.id.product_RV);
        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        searchListener();

        Call<AllProductResponse> searchFoodResponseCall = ApiClient.getClient().getAllProducts();
        searchFoodResponseCall.enqueue(new Callback<AllProductResponse>() {
            @Override
            public void onResponse(Call<AllProductResponse> call, Response<AllProductResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        setSearchView(response.body().getProducts());

                    }
                }
            }


            @Override
            public void onFailure(Call<AllProductResponse> call, Throwable t) {

            }
        });

    }

    private void setSearchView(List<Product> productList) {
        product_RV.setHasFixedSize(true);
        product_RV.setLayoutManager(new GridLayoutManager(SearchActivity.this, 1));
        searchProductsAdapter = new SearchProductsAdapter(productList, this);
        product_RV.setAdapter(searchProductsAdapter);
    }

    private void searchListener() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchProductsAdapter.getFilter().filter(s);
                if (s.length() > 0) {
                    product_RV.setVisibility(View.VISIBLE);
                } else {
                    product_RV.setVisibility(View.GONE);
                }
                return false;
            }
        });
//
    }
}
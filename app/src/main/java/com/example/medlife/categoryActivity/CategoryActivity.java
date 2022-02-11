package com.example.medlife.categoryActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.medlife.R;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.AllProductResponse;
import com.example.medlife.api.response.Category;
import com.example.medlife.api.response.CategoryResponse;
import com.example.medlife.api.response.Product;
import com.example.medlife.home.fragments.home.adapters.CategoryAdapter;
import com.example.medlife.home.fragments.home.adapters.ShopAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {
    public static String CATEGORY_DATA_KEY = "cdk";
    Category category;
    RecyclerView allProductsRV;
    ImageView emptyIV;
    ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        allProductsRV = findViewById(R.id.allProductRV);
        loadingProgress = findViewById(R.id.loadingProgress);
        emptyIV = findViewById(R.id.emptyIV);

        getSupportActionBar().setTitle("Categories");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getCategoryOnline();
    }

    private void getCategoryOnline() {
        toggleLoading(true);
        Call<CategoryResponse> getAllCategories = ApiClient.getClient().getCategories();
        getAllCategories.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()){
                    toggleLoading(false);
                    if (!response.body().getError()) {
                        showCategoriesOnline(response.body().getCategories());
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });


    }

    private void showEmptyLayout() {
        emptyIV.setVisibility(View.VISIBLE);
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

    private void showCategoriesOnline(List<Category> categories) {
        allProductsRV.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        allProductsRV.setLayoutManager(layoutManager);
        CategoryAdapter shopAdapter = new CategoryAdapter(categories, this, true);
        allProductsRV.setAdapter(shopAdapter);




    }

    void toggleLoading(boolean toggle) {
        if (toggle)
            loadingProgress.setVisibility(View.VISIBLE);
        else
            loadingProgress.setVisibility(View.GONE);
    }

}
package com.example.medlife.Profile.InsideProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.medlife.R;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.AllProductResponse;
import com.example.medlife.api.response.Product;
import com.example.medlife.api.response.RegisterResponse;
import com.example.medlife.home.fragments.CartFragment;
import com.example.medlife.home.fragments.home.adapters.ShopAdapter;
import com.example.medlife.utils.SharedPrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistActivity extends AppCompatActivity {
    RecyclerView allProductRV;
    List<Product> products;
    SwipeRefreshLayout swipeRefresh;
    AllProductResponse allProductResponse;
    LinearLayout addItemsToCartLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        getSupportActionBar().setTitle("WishList");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allProductRV = findViewById(R.id.allProductRV);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        addItemsToCartLL = findViewById(R.id.addItemsToCartLL);

        addItemsToCartLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allProductResponse != null && allProductResponse.getProducts().size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), CartFragment.class);
                    //   intent.putExtra(CartFragment.WISHLIST_TO_CART_PRODUCTS,allProductResponse);
                    getApplicationContext().startActivity(intent);
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                getWishlistItems();
            }
        });

        getWishlistItems();
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

    private void getWishlistItems() {
        String key = SharedPrefUtils.getString(this, "apk");
        Call<AllProductResponse> wishlistItemsCall = ApiClient.getClient().getMyWishlist(key);
        wishlistItemsCall.enqueue(new Callback<AllProductResponse>() {
            @Override
            public void onResponse(Call<AllProductResponse> call, Response<AllProductResponse> response) {
                if (response.isSuccessful()) {
                    allProductResponse = response.body();
                    products = response.body().getProducts();
                    loadWishList();
                }
            }

            @Override
            public void onFailure(Call<AllProductResponse> call, Throwable t) {
                swipeRefresh.setRefreshing(false);

            }
        });
    }


    private void loadWishList() {
        allProductRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        allProductRV.setLayoutManager(layoutManager);
        ShopAdapter shopAdapter = new ShopAdapter(products, this,false,true);
        String key = SharedPrefUtils.getString(this,getString(R.string.api_key));
        shopAdapter.setWishlistItemClick(new ShopAdapter.WishlistItemClick() {
            @Override
            public void onRemoveWishlist(int position) {
                Call<RegisterResponse> removeWishlistCall = ApiClient.getClient().deleteFromWishlist(key, products.get(position).getWishlistID());
                removeWishlistCall.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if(response.isSuccessful()){
                            products.remove(products.get(position));
                            shopAdapter.notifyItemRemoved(position);
//                           setPrice();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {

                    }
                });

            }
        });

        allProductRV.setAdapter(shopAdapter);
    }


    public void onResume() {
        super.onResume();
        getWishlistItems();
    }


}
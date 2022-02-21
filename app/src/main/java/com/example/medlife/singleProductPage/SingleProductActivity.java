package com.example.medlife.singleProductPage;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medlife.R;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.AllProductResponse;
import com.example.medlife.api.response.Product;
import com.example.medlife.api.response.RegisterResponse;
import com.example.medlife.api.response.SingleProductResponse;
import com.example.medlife.api.response.Slider;
import com.example.medlife.home.fragments.home.adapters.SliderAdapter;
import com.example.medlife.utils.SharedPrefUtils;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleProductActivity extends AppCompatActivity {
    public static String DATA_KEY = "ds";
    public static String SINGLE_DATA_KEY = "sds";
    Product product;
    SliderView imageSlider;
//    ProgressBar addingCartPR;
    ImageView backIV, plusIV, minusIV;
    TextView name, price, desc, oldPrice, quantityTV;
    LinearLayout addToCartLL,wishListLL;
    int quantity = 1;
    boolean isAdding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);


        setContentView(R.layout.activity_single_product);
        backIV = findViewById(R.id.backIV);
        imageSlider = findViewById(R.id.imageSlider);
        name = findViewById(R.id.productNameTV);
        price = findViewById(R.id.productPriceTV);
        quantityTV = findViewById(R.id.quantityTV);
        oldPrice = findViewById(R.id.productOldPriceTV);
        addToCartLL = findViewById(R.id.addToCartLL);
        wishListLL = findViewById(R.id.wishListLL);
//        addingCartPR = findViewById(R.id.addingCartPR);
        desc = findViewById(R.id.decTV);
        plusIV = findViewById(R.id.plusIV);
        minusIV = findViewById(R.id.minusIV);

        if (getIntent().getSerializableExtra(DATA_KEY) != null) {
            product = (Product) getIntent().getSerializableExtra(DATA_KEY);
            setProduct(product);
        } else if (getIntent().getSerializableExtra(SINGLE_DATA_KEY) != null)
            getProductOnline(getIntent().getIntExtra(SINGLE_DATA_KEY, 1));

        setOnclickListners();
    }

    private void getProductOnline(int intExtra) {
        Call<SingleProductResponse> productResponseCall = ApiClient.getClient().getProductById(intExtra);
        productResponseCall.enqueue(new Callback<SingleProductResponse>() {
            @Override
            public void onResponse(Call<SingleProductResponse> call, Response<SingleProductResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        product = response.body().getProduct();
                        setProduct(product);
                    }

                }
            }

            @Override
            public void onFailure(Call<SingleProductResponse> call, Throwable t) {

            }
        });
    }

    private void setProduct(Product product) {
        setSliders(product.getImages());
        name.setText(product.getName());

        if (product.getDiscountPrice() == 0 || product.getDiscountPrice() == null) {
            price.setText("Rs. " + product.getPrice());
            oldPrice.setVisibility(View.INVISIBLE);

        } else {
            price.setText("Rs. " + product.getDiscountPrice());
            oldPrice.setText("Rs. " + product.getPrice());

        }
        desc.setText(product.getDescription());

    }

    private void setSliders(List<String> images) {
        List<Slider> sliders = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            Slider slider = new Slider();
            slider.setImage(images.get(i));
            sliders.add(slider);
        }

        SliderAdapter sliderAdapter = new SliderAdapter(sliders, this, false);
        sliderAdapter.setClickLister(new SliderAdapter.OnSliderClickLister() {
            @Override
            public void onSliderClick(int position, Slider slider) {

            }
        });
        imageSlider.setSliderAdapter(sliderAdapter);
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setIndicatorUnselectedColor(Color.GRAY);

    }

    private void setOnclickListners() {
        backIV.setOnClickListener(v -> finish());

        plusIV.setOnClickListener(v -> {

            if (quantity > 9)
                Toast.makeText(this, "You can only buy 10 items at once", Toast.LENGTH_SHORT).show();
            else
                quantity++;

            setQuantity();
        });
        minusIV.setOnClickListener(v -> {

            if (quantity < 2)
                Toast.makeText(this, "Quantity should be atleast 1", Toast.LENGTH_SHORT).show();
            else
                quantity--;
            setQuantity();
        });

        addToCartLL.setOnClickListener(v -> {
            if (!isAdding) {
                isAdding = true;
//                addingToggle(true);

                String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
//                System.out.println("1111111111111 api key is : "+key );
                Call<AllProductResponse> cart = ApiClient.getClient().addToCart(key, product.getId(), quantity);
                cart.enqueue(new Callback<AllProductResponse>() {
                    @Override
                    public void onResponse(Call<AllProductResponse> call, Response<AllProductResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
//                        addingToggle(false);
                        isAdding = false;
                    }

                    @Override
                    public void onFailure(Call<AllProductResponse> call, Throwable t) {

                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "Adding Already!!", Toast.LENGTH_SHORT).show();
            }

        });

        wishListLL.setOnClickListener(v -> {

            if (!isAdding) {
                isAdding = true;
//                addingToggle(true);

                String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
//                System.out.println("1111111111111 api key is : " + key);
                Call<AllProductResponse> wishlist = ApiClient.getClient().addToWishlist(key, product.getId(), quantity);
                wishlist.enqueue(new Callback<AllProductResponse>() {
                    @Override
                    public void onResponse(Call<AllProductResponse> call, Response<AllProductResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

//                        addingToggle(false);
                        isAdding = false;
                    }

                    @Override
                    public void onFailure(Call<AllProductResponse> call, Throwable t) {

                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "Adding Already!!", Toast.LENGTH_SHORT).show();

            }

            });
    }

    private void setQuantity() {
        quantityTV.setText(quantity + "");
    }

//    private void addingToggle(Boolean b) {
//        if (b)
//            addingCartPR.setVisibility(View.VISIBLE);
//        else {
//            addingCartPR.setVisibility(View.GONE);
//        }
//    }
}
package com.example.medlife.home.fragments.home;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medlife.PopularProducts.PopularProductsActivity;
import com.example.medlife.Profile.ProfileActivity;
import com.example.medlife.R;
import com.example.medlife.SearchActivity;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.AllProductResponse;
import com.example.medlife.api.response.Category;
import com.example.medlife.api.response.CategoryResponse;
import com.example.medlife.api.response.Product;
import com.example.medlife.api.response.Slider;
import com.example.medlife.api.response.SliderResponse;
import com.example.medlife.categoryActivity.CategoryActivity;
import com.example.medlife.home.AboutActivity;
import com.example.medlife.home.AboutMedLifeActivity;
import com.example.medlife.home.Notification.NotificationActivity;
import com.example.medlife.home.fragments.home.adapters.CategoryAdapter;
import com.example.medlife.home.fragments.home.adapters.SearchProductsAdapter;
import com.example.medlife.home.fragments.home.adapters.ShopAdapter;
import com.example.medlife.home.fragments.home.adapters.SliderAdapter;
import com.example.medlife.singleProductPage.SingleProductActivity;
import com.example.medlife.uploadPrescription.UploadPrescriptionActivity;
import com.example.medlife.utils.DataHolder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    RecyclerView allProductRV, categoryRV;
    ProgressBar loadingProgress;
    SliderView imageSlider;
    LinearLayout callPLL, supportCallLL, uploadPrescriptionLL, searchLL;
//    CircleImageView user_ProfileLL;
    TextView viewAllCategory, viewAllProducts;
    BottomNavigationView bottomNavigationView;
    ImageView aboutIV, notifyIV;
    TextView acTV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        this.bottomNavigationView = bottomNavigationView;
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        allProductRV = view.findViewById(R.id.allProductRV);
        categoryRV = view.findViewById(R.id.categoryRV);
        loadingProgress = view.findViewById(R.id.loadingProgress);
        imageSlider = view.findViewById(R.id.imageSlider);
        viewAllCategory = view.findViewById(R.id.viewAllCategory);
        viewAllProducts = view.findViewById(R.id.viewAllProducts);
        callPLL = view.findViewById(R.id.callPharmacistLL);
        supportCallLL = view.findViewById(R.id.callSupportLL);
        uploadPrescriptionLL = view.findViewById(R.id.uploadPrescriptionLL);
//        user_ProfileLL = view.findViewById(R.id.user_ProfileLL);
//        notifyIV = view.findViewById(R.id.notifyIV);
        aboutIV = view.findViewById(R.id.aboutIV);
        acTV = view.findViewById(R.id.acTV);

        acTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SearchActivity.class));
            }
        });



        // For notification

//        notifyIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), NotificationActivity.class);
//                startActivity(intent);
//
//            }
//        });


        callPLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("+9779814146808");
            }
        });
        supportCallLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call("+9779819176870");
            }
        });

        aboutIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
            }
        });




        uploadPrescriptionLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), UploadPrescriptionActivity.class);
                startActivity(intent);
            }
        });

//        user_ProfileLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getContext(), ProfileActivity.class);
//                startActivity(intent);
//            }
//        });

        viewAllCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.catMenu);
            }

        });

        viewAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PopularProductsActivity.class);
                startActivity(intent);

            }
        });

//        searchLL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

        serverCall();
        getCategoriesOnline();
        getSliders();
    }


    private void call(String number) {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }


    private void getSliders() {
        Call<SliderResponse> sliderResponseCall = ApiClient.getClient().getSliders();
        sliderResponseCall.enqueue(new Callback<SliderResponse>() {
            @Override
            public void onResponse(Call<SliderResponse> call, Response<SliderResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        setSliders(response.body().getSliders());
                    }
                }
            }

            @Override
            public void onFailure(Call<SliderResponse> call, Throwable t) {

            }
        });
    }

    private void setSliders(List<Slider> sliders) {
        SliderAdapter sliderAdapter = new SliderAdapter(sliders, getContext(), true);
        sliderAdapter.setClickLister(new SliderAdapter.OnSliderClickLister() {
            @Override
            public void onSliderClick(int position, Slider slider) {
////                Toast.makeText(getContext(), "from home This is item in position " + position, Toast.LENGTH_SHORT).show();
//                if (slider.getType() == 1) {
//                    Intent intent = new Intent(getContext(), SingleProductActivity.class);
//                    intent.putExtra(SingleProductActivity.SINGLE_DATA_KEY, slider.getRelatedId());
//                    getContext().startActivity(intent);*=

//                } else if (slider.getType() ==2) {
//                    Intent cat = new Intent(getContext(), CategoryActivity.class);
//                    Category category = new Category();
//                    category.setId(slider.getRelatedId());
//                    category.setName(slider.getDesc());
//                    cat.putExtra(CategoryActivity.CATEGORY_DATA_KEY, category);
//                    getContext().startActivity(cat);
//                }
           }
        });
        imageSlider.setSliderAdapter(sliderAdapter);
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setIndicatorUnselectedColor(Color.GRAY);
        imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
        imageSlider.startAutoCycle();

    }


    private void getCategoriesOnline() {
        Call<CategoryResponse> getCategories = ApiClient.getClient().getCategories();
        getCategories.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getError()) {
                        DataHolder.categories = response.body().getCategories();
                        showCategories(response.body().getCategories());

                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });

    }

    private void showCategories(List<Category> categories) {
        List<Category> temp;
        if (categories.size() > 8) {
            temp = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                temp.add(categories.get(categories.size() - i - 1));
            }
        } else {
            temp = categories;
        }
        categoryRV.setHasFixedSize(true);
        categoryRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        CategoryAdapter categoryAdapter = new CategoryAdapter(temp, getContext(), true, false , null);
        categoryRV.setAdapter(categoryAdapter);

    }

    private void serverCall() {
        toggleLoading(true);
        Call<AllProductResponse> allProductResponseCall = ApiClient.getClient().getAllProducts();
        allProductResponseCall.enqueue(new Callback<AllProductResponse>() {
            @Override
            public void onResponse(Call<AllProductResponse> call, Response<AllProductResponse> response) {
                toggleLoading(false);
                setProductRecyclerView(response.body().getProducts());

            }

            @Override
            public void onFailure(Call<AllProductResponse> call, Throwable t) {
                toggleLoading(false);
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setProductRecyclerView(List<Product> products) {
        allProductRV.setHasFixedSize(true);
//        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        allProductRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

//        allProductRV.setLayoutManager(layoutManager);
        ShopAdapter shopAdapter = new ShopAdapter(products, getContext(), false, false);
        allProductRV.setAdapter(shopAdapter);
    }

    void toggleLoading(boolean toggle) {
        if (toggle)
            loadingProgress.setVisibility(View.VISIBLE);
        else
            loadingProgress.setVisibility(View.GONE);
    }

}
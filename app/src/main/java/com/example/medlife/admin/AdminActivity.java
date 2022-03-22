package com.example.medlife.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medlife.R;
import com.example.medlife.admin.DashBoardActivity.DashBoardActivity;
import com.example.medlife.admin.addCategory.ListCategoryActivity;
import com.example.medlife.admin.addProduct.AddProductActivity;
import com.example.medlife.admin.products.ListProductsActivity;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.Dash;
import com.example.medlife.api.response.DashResponse;
import com.example.medlife.api.response.RegisterResponse;
import com.example.medlife.uploadPrescription.UploadPrescriptionActivity;
import com.example.medlife.utils.PermissionUtils;
import com.example.medlife.utils.SharedPrefUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {
    private static final int TAKE_PICTURE = 2;
    private static final int PICK_PICTURE = 1;
    private static final String TEMP_DiRECT = "/MedLife/Picture/.temp/";
    LinearLayout addCategory, imageLayout, categoryList, productsLL, dashBoardLL, uploadProduct;
    String currentPhotoPath;
    ImageView selectedIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Admin Area");
        addCategory = findViewById(R.id.addCategory);
        categoryList = findViewById(R.id.categoryList);
        productsLL = findViewById(R.id.productsLL);
        dashBoardLL = findViewById(R.id.dashBoardLL);
        uploadProduct = findViewById(R.id.uploadProduct);
        setClickListeners();
    }

    private void setClickListeners() {
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddCategoryView();

            }
        });

        categoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ListCategoryActivity.class);
                startActivity(intent);
            }
        });

        productsLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, ListProductsActivity.class);
                startActivity(intent);
            }
        });

        dashBoardLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, DashBoardActivity.class);
                startActivity(intent);
            }
        });

        uploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });
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

//    private void openAddProductCategoryView() {
//        LayoutInflater factory = LayoutInflater.from(this);
//        View DialogView= factory.inflate(R.layout.custom_dialog_add_product, null);
//        Dialog main_dialog =  new Dialog(this, R.style.Base_Theme_AppCompat_Dialog);
//        main_dialog.setContentView(DialogView);
//        main_dialog.show();
//
//        EditText name = (EditText) main_dialog.findViewById(R.id.proNameET);
//        EditText price = (EditText) main_dialog.findViewById(R.id.proPriceET);
//        EditText discountPrice = (EditText) main_dialog.findViewById(R.id.proDisPriceET);
//        EditText description = (EditText) main_dialog.findViewById(R.id.proDescET);
//        EditText quantity = (EditText) main_dialog.findViewById(R.id.proQuantityET);
//        EditText productionDate = (EditText) main_dialog.findViewById(R.id.proProductionDtET);
//        EditText expireDate = (EditText) main_dialog.findViewById(R.id.proExpireDtET);
////        EditText productCategory = (EditText) main_dialog.findViewById(R.id.proCategoryET);
//        Button uploadProduct = (Button) main_dialog.findViewById(R.id.uploadProduct);
//
//        uploadProduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!name.getText().toString().isEmpty() || !price.getText().toString().
//                        isEmpty() || !discountPrice.getText().toString().
//                        isEmpty() || !description.getText().toString().
//                        isEmpty() || !quantity.getText().toString().
//                        isEmpty() || !productionDate.getText().toString().
//                        isEmpty() || !expireDate.getText().toString().
//                        isEmpty() && currentPhotoPath !=null) {
//
////                    uploadProduct(new File(currentPhotoPath), name.getText().toString());
//
//                } else {
//                    Toast.makeText(AdminActivity.this, "Please >>>>>>>>", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//        LinearLayout camera_LL = (LinearLayout) main_dialog.findViewById(R.id.camera_LL);
//        LinearLayout gallery_LL = (LinearLayout) main_dialog.findViewById(R.id.gallery_LL);
//        selected_IV = (ImageView) main_dialog.findViewById(R.id.selected_IV);
//        image_Layout = (LinearLayout) main_dialog.findViewById(R.id.image_Layout);
//
//        camera_LL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                File file = null;
//                try {
//                    file = createImageFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                if (PermissionUtils.isCameraPermissionGranted(AdminActivity.this, "", 1)) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (file != null) {
//                        Uri photoURI = FileProvider.getUriForFile(AdminActivity.this,
//                                "com.example.android.fileprovider",
//                                file);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        startActivityForResult(intent, TAKE_PICTURE);
//                    }
//                }
//            }
//        });
//        gallery_LL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (PermissionUtils.isStoragePermissionGranted(AdminActivity.this, "", PICK_PICTURE)) {
//                    Intent chooseFile = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(chooseFile, PICK_PICTURE);
//                }
//            }
//        });
//        main_dialog.show();
//    }





    private void openAddCategoryView(){
        LayoutInflater factory = LayoutInflater.from(this);
        View DialogView = factory.inflate(R.layout.custom_dialog_add_category, null);
        Dialog main_dialog = new Dialog(this, R.style.Base_Theme_AppCompat_Dialog);
        main_dialog.setContentView(DialogView);
        main_dialog.show();
        EditText name = (EditText) main_dialog.findViewById(R.id.catNameET);
        Button upload = (Button) main_dialog.findViewById(R.id.upload);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty() && currentPhotoPath != null) {
                    uploadCategory(new File(currentPhotoPath), name.getText().toString(), main_dialog);
                } else {
                    Toast.makeText(AdminActivity.this, "Please check image or Name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LinearLayout cameraIV = (LinearLayout) main_dialog.findViewById(R.id.cameraIV);
        LinearLayout galleryIv = (LinearLayout) main_dialog.findViewById(R.id.galleryIV);
        selectedIV = (ImageView) main_dialog.findViewById(R.id.selectedIV);
        imageLayout = (LinearLayout) main_dialog.findViewById(R.id.imageLayout);

        cameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = null;
                try {
                    file = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (PermissionUtils.isCameraPermissionGranted(AdminActivity.this, "", 1)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (file != null) {
                        Uri photoURI = FileProvider.getUriForFile(AdminActivity.this,
                                "com.example.android.fileprovider",
                                file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, TAKE_PICTURE);
                    }

                }

            }
        });
        galleryIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.isStoragePermissionGranted(AdminActivity.this, "", PICK_PICTURE)) {
                    Intent chooseFile = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(chooseFile, PICK_PICTURE);
                }
            }
        });
        main_dialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                Uri contentUri = Uri.fromFile(f);
                selectedIV.setImageURI(contentUri);
                setCategoryImage();
            }
        } else if (requestCode == PICK_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                setCategoryImage();
                selectedIV.setImageURI(data.getData());
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(data.getData(), filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                currentPhotoPath = picturePath;
            }
        }
    }


    private void setCategoryImage() {
        imageLayout.setVisibility(View.GONE);
        selectedIV.setVisibility(View.VISIBLE);
    }

    private void uploadCategory(File file, String name, Dialog dialog){
        ProgressDialog progressDialog = ProgressDialog.show(this, "",
                "Uploading. Please wait...", false);
        String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
        RequestBody catName = RequestBody.create(MediaType.parse("text/plain"), name);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        Call<RegisterResponse> responseCall = ApiClient.getClient().uploadCategory(key, filePart, catName);
        responseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(AdminActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();

                    Toast.makeText(AdminActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(AdminActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile( imageFileName,  /* prefix */".jpg", /* suffix */storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void addProduct(View view) {
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
    }

}
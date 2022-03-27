package com.example.medlife.uploadPrescription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medlife.R;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.RegisterResponse;
import com.example.medlife.utils.PermissionUtils;
import com.example.medlife.utils.SharedPrefUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UploadPrescriptionActivity extends AppCompatActivity {
    //FIRST
    private static final int TAKE_PICTURE = 2;
    private static final int PICK_PICTURE = 1;
    private static final String TEMP_DIRECT = "/MedLife/Picture/.temp/";
    private Uri imageUri;
    LinearLayout openCameraIV, openGalleryIV, imageLayoutLL, sendPresLL;
    String currentPhotoPath1;
    ImageView selectedIv;
    TextView doctorNameET, noteET;

//    List<String> photoPath = new ArrayList<>();
//    List<Uri> photoUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_prescription);

        getSupportActionBar().setTitle("Add Prescription");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        openCameraIV = findViewById(R.id.openCameraIV);
        openGalleryIV = findViewById(R.id.openGalleryIV);
        selectedIv= findViewById(R.id.selectedIv);
        imageLayoutLL= findViewById(R.id.imageLayoutLL);
        sendPresLL= findViewById(R.id.sendPresLL);
        doctorNameET = findViewById(R.id.doctorNameET);
        noteET = findViewById(R.id.noteET);
        setClickListeners();

    }

    private void setClickListeners() {

        sendPresLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!doctorNameET.getText().toString().isEmpty() || !noteET.getText().toString().
                        isEmpty() && currentPhotoPath1 !=null) {
                    addPrescription(new File(currentPhotoPath1), doctorNameET.getText().toString(), noteET.getText().toString());
                } else {
                    Toast.makeText(UploadPrescriptionActivity.this, "Please upload prescription or doctor name or note", Toast.LENGTH_SHORT).show();
                }
            }
        });

        openCameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = null;
                try {
                    file = createImageFile1();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (PermissionUtils.isCameraPermissionGranted(UploadPrescriptionActivity.this, "", 1)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (file != null) {
                        Uri photoURI = FileProvider.getUriForFile(UploadPrescriptionActivity.this,
                                "com.example.android.fileprovider",
                                file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, TAKE_PICTURE);
                    }

                }

            }
        });

//        openCameraIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                File file = null;
//                try {
//                    file = createImageFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (PermissionUtils.isCameraPermissionGranted(UploadPrescriptionActivity.this, "", 1)) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (file != null) {
//                        Uri photoURI = FileProvider.getUriForFile
//                                (UploadPrescriptionActivity.this,
//                                        "com.example.android.fileprovider", file);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        startActivityForResult(intent, TAKE_PICTURE);
//                    }
//                }
//            }
//        });
        openGalleryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.isStoragePermissionGranted(UploadPrescriptionActivity.this, "", PICK_PICTURE)) {
                    Intent chooseFile = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(chooseFile, PICK_PICTURE);
                }
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath1);
                Uri contentUri = Uri.fromFile(f);
                selectedIv.setImageURI(contentUri);
                setCategoryImage();
            }
        } else if (requestCode == PICK_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                setCategoryImage();
                selectedIv.setImageURI(data.getData());
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(data.getData(), filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                currentPhotoPath1 = picturePath;
            }
        }
    }

    private void setCategoryImage() {
        imageLayoutLL.setVisibility(View.GONE);
        selectedIv.setVisibility(View.VISIBLE);
    }

    private void addPrescription(File file, String doctorNameET, String noteET){
        ProgressDialog progressDialog = ProgressDialog.show(this, "",
                "Uploading. Please wait...", false);
        String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
        RequestBody catName = RequestBody.create(MediaType.parse("text/plain"), doctorNameET);
        RequestBody note = RequestBody.create(MediaType.parse("text/plain"), noteET);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        // this is uploadcategory API but have to create for addPrescription
        Call<RegisterResponse> responseCall = ApiClient.getClient().uploadCategory(key, filePart, catName);
        responseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    Toast.makeText(UploadPrescriptionActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();

                    Toast.makeText(UploadPrescriptionActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(UploadPrescriptionActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();

            }
        });
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

    private File createImageFile1() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile( imageFileName,  /* prefix */".jpg", /* suffix */storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath1 = image.getAbsolutePath();
        return image;
    }
}
//public class UploadPrescriptionActivity extends AppCompatActivity {
//FIRST

//
//    private static final int TAKE_PICTURE = 2;
//    private static final int PICK_PICTURE = 1;
//    private static final String TEMP_DIRECT = "/MedLife/Picture/.temp/";
//    private Uri imageUri;
//    LinearLayout openCameraIV, openGalleryIV;
//    String currentPhotoPath;
//    ImageView testingIV;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_upload_prescription);
//
//        getSupportActionBar().setTitle("Add Prescription");
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        openCameraIV = findViewById(R.id.openCameraIV);
//        openGalleryIV = findViewById(R.id.openGalleryIV);
//        testingIV= findViewById(R.id.testingIV);
//        setClickListeners();
//
//    }
//
//    private void setClickListeners() {
//        openCameraIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                File file = null;
//                try {
//                    file = createImageFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (PermissionUtils.isCameraPermissionGranted(UploadPrescriptionActivity.this, "", 1)) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (file != null) {
//                        Uri photoURI = FileProvider.getUriForFile(UploadPrescriptionActivity.this, "com.example.android.fileProvider", file);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        startActivityForResult(intent, TAKE_PICTURE);
//                    }
//                }
//            }
//        });
//        openGalleryIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (PermissionUtils.isStoragePermissionGranted(UploadPrescriptionActivity.this, "", PICK_PICTURE)) {
//                    Intent chooseFile = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(chooseFile, PICK_PICTURE);
//                }
//            }
//        });
//
//        testingIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(UploadPrescriptionActivity.this, AddPrescriptionActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile( imageFileName,  /* prefix */".jpg", /* suffix */storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//
//}
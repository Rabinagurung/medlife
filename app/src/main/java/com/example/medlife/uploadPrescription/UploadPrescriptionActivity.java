package com.example.medlife.uploadPrescription;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.medlife.R;
import com.example.medlife.utils.PermissionUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadPrescriptionActivity extends AppCompatActivity {
//FIRST

    private static final int TAKE_PICTURE = 2;
    private static final int PICK_PICTURE = 1;
    private static final String TEMP_DIRECT = "/MedLife/Picture/.temp/";
    private Uri imageUri;
    LinearLayout openCameraIV, openGalleryIV;
    String currentPhotoPath;
    ImageView testingIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_prescription);

        getSupportActionBar().setTitle("Add Prescription");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        openCameraIV = findViewById(R.id.openCameraIV);
        openGalleryIV = findViewById(R.id.openGalleryIV);
        testingIV= findViewById(R.id.testingIV);
        setClickListeners();

    }

    private void setClickListeners() {
        openCameraIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = null;
                try {
                    file = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (PermissionUtils.isCameraPermissionGranted(UploadPrescriptionActivity.this, "", 1)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (file != null) {
                        Uri photoURI = FileProvider.getUriForFile(UploadPrescriptionActivity.this, "com.example.android.fileProvider", file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, TAKE_PICTURE);
                    }
                }
            }
        });
        openGalleryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.isStoragePermissionGranted(UploadPrescriptionActivity.this, "", PICK_PICTURE)) {
                    Intent chooseFile = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(chooseFile, PICK_PICTURE);
                }
            }
        });

        testingIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadPrescriptionActivity.this, AddPrescriptionActivity.class);
                startActivity(intent);
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


}
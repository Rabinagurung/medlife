package com.example.medlife.Profile.InsideProfile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.medlife.admin.AdminActivity;
import com.example.medlife.api.ApiClient;
import com.example.medlife.api.response.RegisterResponse;
import com.example.medlife.home.MainActivity;
import com.example.medlife.utils.PermissionUtils;
import com.example.medlife.utils.SharedPrefUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
    private static final int PICK_PICTURE = 1;
    private static final int TAKE_PICTURE = 2;
    String currentPhotoPath;
    ImageView editInfoLL, changeProfile;
    CircleImageView profilePicIV;
    ImageView selectedIV;
    LinearLayout imageLayout;
    TextView email_TV, name_TV, gender_TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editInfoLL = findViewById(R.id.editInfoLL);
        profilePicIV= findViewById(R.id.profilePicIV);
        changeProfile=findViewById(R.id.changeProfile);
        name_TV = findViewById(R.id.name_TV);
        email_TV = findViewById(R.id.email_TV);
        gender_TV= findViewById(R.id.gender_TV);

        Picasso.get().load((SharedPrefUtils.getString(UserProfileActivity.this, getString(R.string.profile_key)))).into(profilePicIV);
        name_TV.setText(SharedPrefUtils.getString(UserProfileActivity.this, getString(R.string.name_key)));
        email_TV.setText(SharedPrefUtils.getString(UserProfileActivity.this, getString(R.string.email_id)));
        gender_TV.setText(SharedPrefUtils.getString(UserProfileActivity.this, getString(R.string.gender_key)));
        setClickListener();
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

    private void setClickListener(){
        editInfoLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);

            }
        });

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddProfileView();
            }
        });
    }

    private void openAddProfileView(){
        LayoutInflater factory = LayoutInflater.from(UserProfileActivity.this);
        View DialogView = factory.inflate(R.layout.custom_dialog_add_profile, null);
        Dialog main_dialog = new Dialog(UserProfileActivity.this, R.style.Base_Theme_AppCompat_Dialog);
        main_dialog.setContentView(DialogView);
        main_dialog.show();
        Button upload = (Button) main_dialog.findViewById(R.id.upload);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPhotoPath != null) {
                    uploadProfile(new File(currentPhotoPath), main_dialog);
                } else {
                    Toast.makeText(UserProfileActivity.this, "Please check image", Toast.LENGTH_SHORT).show();
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

                if (PermissionUtils.isCameraPermissionGranted(UserProfileActivity.this, "", 1)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (file != null) {
                        Uri photoURI = FileProvider.getUriForFile(UserProfileActivity.this,
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
                if (PermissionUtils.isStoragePermissionGranted(UserProfileActivity.this, "", PICK_PICTURE)) {
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

    private void uploadProfile(File file, Dialog dialog){
        ProgressDialog progressDialog = ProgressDialog.show(UserProfileActivity.this, "",
                "Uploading. Please wait...", false);
        String key = SharedPrefUtils.getString(UserProfileActivity.this, getString(R.string.api_key));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        Call<RegisterResponse> responseCall = ApiClient.getClient().uploadProfile(key, filePart);
        responseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(UserProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    progressDialog.dismiss();

                    Toast.makeText(UserProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(UserProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
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
}
package com.example.kormoran;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kormoran.apihelper.BaseApiService;
import com.example.kormoran.apihelper.RetrofitClient;
import com.example.kormoran.data.ResponseEditProfile;
import com.example.kormoran.utils.PreferencesHelper;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    PreferencesHelper preferencesHelper;
    EditText etEmail;
    EditText etUserName;
    EditText etName;
    Button btnChoosePict;
    final static int  REQUEST_GALLERY = 1;
    String mediaPath;
    ImageView ivProfile;
    Button btnUpdate;
    File imageFile;
    Context mContext;
    BaseApiService mApiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mContext = this;
        mApiService = RetrofitClient.getService(getApplicationContext());

        preferencesHelper = new PreferencesHelper(EditProfileActivity.this);

        etEmail = findViewById(R.id.et_email);
        etUserName = findViewById(R.id.et_user_name);
        etName = findViewById(R.id.et_name);
        btnChoosePict = findViewById(R.id.btn_choose_pict);
        ivProfile = findViewById(R.id.iv_profile);
        btnUpdate = findViewById(R.id.btn_update);

        loadIvProfile();
        etEmail.setText(preferencesHelper.getSpEmail());
        etUserName.setText(preferencesHelper.getSpUserName());
        etName.setText(preferencesHelper.getSpName());

        btnChoosePict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_GALLERY);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etName.getText().toString();
                final String userName = etUserName.getText().toString();
                final String id = preferencesHelper.getSpId();


                Log.e("ID"," " + id);

                if (mediaPath != null){
                    imageFile = new File(mediaPath);
                }
                else {
                    imageFile = new File(preferencesHelper.getSpPict());
                    Log.e("TEST","image " + imageFile );
                }

                updateProfile(id,userName,name,imageFile);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // When an Image is picked
            if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                preferencesHelper.saveSPString(preferencesHelper.SP_PICT_PATH_INTERNAL,mediaPath);
                // Set the Image in ImageView for Previewing the Media
                ivProfile.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();

            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void updateProfile(String id, String name, String userName, File imageFile){
        RequestBody requestBodyPict = RequestBody.create(MediaType.parse("image/*"),imageFile);
        RequestBody requestBodyId = RequestBody.create(okhttp3.MultipartBody.FORM, id);
        RequestBody requestBodyName = RequestBody.create(okhttp3.MultipartBody.FORM, name);
        RequestBody requestBodyUserName = RequestBody.create(okhttp3.MultipartBody.FORM, userName);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("imageUpload", imageFile.getName(),requestBodyPict);
        Call<ResponseEditProfile> editProfile = mApiService.editProfileRequest(partImage,requestBodyId,requestBodyUserName,requestBodyName);
        editProfile.enqueue(new Callback<ResponseEditProfile>() {
            @Override
            public void onResponse(Call<ResponseEditProfile> call, Response<ResponseEditProfile> response) {

                if(response.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this,"Success Update Profile", Toast.LENGTH_LONG).show();
                    String pictUpdated = response.body().getPict();
                    Log.e("DEBUG", pictUpdated);
                    String nameUpdated = response.body().getName();
                    String userNameUpdated = response.body().getUser_name();
                    preferencesHelper.saveSPString(preferencesHelper.SP_PICT,pictUpdated);
                    preferencesHelper.saveSPString(preferencesHelper.SP_NAME,nameUpdated);
                    preferencesHelper.saveSPString(preferencesHelper.SP_USERNAME,userNameUpdated);
                    String pictUri = "https://kormoran.000webhostapp.com/storage/user/"+pictUpdated;
                    Glide.with(EditProfileActivity.this)
                            .load(pictUri)
                            .into(ivProfile);
                }
                else {
                    Toast.makeText(EditProfileActivity.this,"Failed Update Profile", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseEditProfile> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    public void loadIvProfile(){
        String url = "https://kormoran.000webhostapp.com/storage/user/"+preferencesHelper.getSpPict();

        Glide.with(EditProfileActivity.this)
                .load(url)
                .into(ivProfile);
    }
}

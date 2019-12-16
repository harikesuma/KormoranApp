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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kormoran.adapter.KategoriAdapter;
import com.example.kormoran.adapter.SpinnerAdapter;
import com.example.kormoran.apihelper.BaseApiService;
import com.example.kormoran.apihelper.RetrofitClient;
import com.example.kormoran.data.Kategori;
import com.example.kormoran.data.ResponseKategori;
import com.example.kormoran.data.ResponsePostQuestion;
import com.example.kormoran.utils.PreferencesHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostQuestionActivity extends AppCompatActivity {
    EditText etQuestion;
    Spinner spKategori;
    Button btnChoosePict;
    Button btnPost;
    ImageView ivPict;
    final static int  REQUEST_GALLERY = 1;
    String mediaPath;
    PreferencesHelper preferencesHelper;
    String access_token;
    BaseApiService baseApiService;
    List<Kategori> kategoriList = new ArrayList<>();
    SpinnerAdapter spinnerAdapter;
    String kategoriId;
    int newKategori;
    TextView tvKategori;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);

        preferencesHelper = new PreferencesHelper(PostQuestionActivity.this);
        baseApiService = RetrofitClient.getService(PostQuestionActivity.this);

        ivPict = findViewById(R.id.iv_pict_holder);
        btnChoosePict = findViewById(R.id.btn_choose_pict);
        etQuestion = findViewById(R.id.et_question);
        spKategori = findViewById(R.id.sp_kategori);
        btnPost = findViewById(R.id.btn_post);
        tvKategori = findViewById(R.id.tv_kategori);

        btnChoosePict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_GALLERY);
            }
        });



        spinnerAdapter = new SpinnerAdapter(PostQuestionActivity.this, kategoriList);
        spKategori.setAdapter(spinnerAdapter);

        spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Kategori kategori = (Kategori) parent.getItemAtPosition(position);
                String kategoriName = kategori.getKategori();
                kategoriId = String.valueOf(kategori.getId());

                Toast.makeText(PostQuestionActivity.this, kategoriName, Toast.LENGTH_LONG).show();
                Toast.makeText(PostQuestionActivity.this, kategoriId, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getAllKategori();


        Intent i = getIntent();

        if(i.getExtras() != null){
            if (i.getExtras().getString("ACTIVITY_FROM").equals("KATEGORI_QUESTION")){
                final int kategoriId = i.getExtras().getInt("ID_KATEGORI");
                final String kategoriIdtoString = String.valueOf(kategoriId);
                spKategori.setVisibility(View.GONE);
                tvKategori.setVisibility(View.GONE);
                btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postQuestion(kategoriIdtoString);

                    }
                });
            }


//            Toast.makeText(PostQuestionActivity.this, "INTENT EXIST", Toast.LENGTH_LONG).show();
//            final int id =i.getExtras().getInt("id");
//            String question = i.getExtras().getString("question");
//            int kategori_id =  i.getExtras().getInt("kategori_id");
//            String path =  i.getExtras().getString("pict");
//
//            btnChoosePict.setVisibility(View.GONE);
//            String imagePath = "http://10.0.2.2:8000/storage/pertanyaan/"+ path;
//
//            Picasso.get()
//                    .load(imagePath)
//                    .resize(200, 200)
//                    .into(ivPict, new com.squareup.picasso.Callback() {
//                        @Override
//                        public void onSuccess() {
//                            Toast.makeText(PostQuestionActivity.this, "Image is loaded successfully", Toast.LENGTH_LONG).show();
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            Toast.makeText(PostQuestionActivity.this, "Failed to load", Toast.LENGTH_LONG).show();
//                        }
//                    });
//            etQuestion.setText(question);
//
//            btnPost.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            Kategori kategori = (Kategori) parent.getItemAtPosition(position);
//                            newKategori = kategori.getId();
//                            Toast.makeText(PostQuestionActivity.this, "NEW KATEGORI = "+ newKategori, Toast.LENGTH_LONG).show();
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//                        }
//                    });
//
//                    String newQuestion = etQuestion.getText().toString();
//
//                    mApiService.editQuestion(id,newQuestion,newKategori).enqueue(new Callback<ResponseQuestion>() {
//                        @Override
//                        public void onResponse(Call<ResponseQuestion> call, Response<ResponseQuestion> response) {
//                            if (response.isSuccessful()){
//                                Toast.makeText(PostQuestionActivity.this,response.body().getMsg(), Toast.LENGTH_LONG).show();
//                                Intent i = new Intent(PostQuestionActivity.this, MainActivity.class);
//                                startActivity(i);
//                                finish();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseQuestion> call, Throwable t) {
//                            Toast.makeText(PostQuestionActivity.this,"UPDATE FAILED", Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//                }
//            });


        }

        else {
            btnPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("DEBUG","ELSE CLICKED");
                    postQuestion(kategoriId);
                }
            });
        }

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

                // Set the Image in ImageView for Previewing the Media
                ivPict.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();

            } else {
                Toast.makeText(this, "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }


    public void getAllKategori(){
        baseApiService.getAllKategori().enqueue(new Callback<ResponseKategori>() {
            @Override
            public void onResponse(Call<ResponseKategori> call, Response<ResponseKategori> response) {
                if (response.isSuccessful()){
                    kategoriList = response.body().getKategoriList();

                    spKategori.setAdapter(new SpinnerAdapter(PostQuestionActivity.this,kategoriList));
                    spinnerAdapter.notifyDataSetChanged();


                }
                else {
                    Toast.makeText(PostQuestionActivity.this,"GAGAL", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<ResponseKategori> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    public void postQuestion(String kategoriId){
        final String question = etQuestion.getText().toString();

        if(mediaPath == null){
            Toast.makeText(PostQuestionActivity.this, "Please Select photos", Toast.LENGTH_LONG).show();
        }

        else if (question.equals("")){
            etQuestion.setError("Please fill the question section");
        }

        else {
            String userId = preferencesHelper.getSpId();
            File imageFile = new File(mediaPath);

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),imageFile);
            RequestBody requestBodyUserId = RequestBody.create(okhttp3.MultipartBody.FORM, userId);
            RequestBody requestBodyQuestion = RequestBody.create(okhttp3.MultipartBody.FORM, question);
            RequestBody requestBodyKategoriId = RequestBody.create(okhttp3.MultipartBody.FORM, kategoriId);
            MultipartBody.Part partImage = MultipartBody.Part.createFormData("imageUpload", imageFile.getName(),requestBody);
            Call<ResponsePostQuestion> postQuestion = baseApiService.postingQuestionRequest(partImage, requestBodyUserId, requestBodyQuestion, requestBodyKategoriId);

            Log.e("DEBUG", "HASIL"+ postQuestion);

            postQuestion.enqueue(new Callback<ResponsePostQuestion>() {
                @Override
                public void onResponse(Call<ResponsePostQuestion> call, Response<ResponsePostQuestion> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(PostQuestionActivity.this,response.body().getMsg(), Toast.LENGTH_LONG).show();
                        Intent i = new Intent(PostQuestionActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        Toast.makeText(PostQuestionActivity.this,"GAGAL UPLOAD PERTANYAAN", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponsePostQuestion> call, Throwable t) {
                    Toast.makeText(PostQuestionActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}

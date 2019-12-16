package com.example.kormoran;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kormoran.adapter.LatestAdapter;
import com.example.kormoran.adapter.SpinnerAdapter;
import com.example.kormoran.apihelper.BaseApiService;
import com.example.kormoran.apihelper.RetrofitClient;
import com.example.kormoran.data.Kategori;
import com.example.kormoran.data.ResponseKategori;
import com.example.kormoran.data.ResponseQuestion;
import com.example.kormoran.utils.PreferencesHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditQuestionActivity extends AppCompatActivity {
    EditText etQuestion;
    Spinner spKategori;
    Button btnUpdate;
    ImageView ivPict;
    PreferencesHelper preferencesHelper;
    BaseApiService baseApiService;
    List<Kategori> kategoriList = new ArrayList<>();
    SpinnerAdapter spinnerAdapter;
    int kategoriId;
//    int newKategori;
    TextView tvKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        preferencesHelper = new PreferencesHelper(EditQuestionActivity.this);
        baseApiService = RetrofitClient.getService(EditQuestionActivity.this);

        ivPict = findViewById(R.id.iv_pict_holder);
        etQuestion = findViewById(R.id.et_question);
        spKategori = findViewById(R.id.sp_kategori);
        btnUpdate = findViewById(R.id.btn_update);
        tvKategori = findViewById(R.id.tv_kategori);

        spinnerAdapter = new SpinnerAdapter(EditQuestionActivity.this, kategoriList);
        spKategori.setAdapter(spinnerAdapter);

        spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Kategori kategori = (Kategori) parent.getItemAtPosition(position);
                String kategoriName = kategori.getKategori();
                kategoriId = kategori.getId();

//                Toast.makeText(EditQuestionActivity.this, kategoriName, Toast.LENGTH_LONG).show();
//                Toast.makeText(EditQuestionActivity.this, "ID = "+kategoriId, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getAllKategori();


        Intent i = getIntent();
        String imagePathUser = "https://kormoran.000webhostapp.com/storage/pertanyaan/"+ i.getExtras().getString("pict");
        String question = i.getExtras().getString("question");
        etQuestion.setText(question);

        loadPict(imagePathUser);

        if (i.getExtras() != null) {
            if (i.getExtras().getString("ACTIVITY_FROM").equals("ELSE")) {
                final int pertanyaanId = i.getExtras().getInt("id");

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String questionEdited = etQuestion.getText().toString();

                        if (questionEdited.equals("")){
                            etQuestion.setText("Question cannot be empty!");
                        }

                        editQuestion(pertanyaanId,questionEdited,kategoriId);
                    }


                });



            }
            else {
                final int pertanyaanId = i.getExtras().getInt("id");
                spKategori.setVisibility(View.GONE);
                tvKategori.setVisibility(View.GONE);
                kategoriId = i.getExtras().getInt("kategori_id");
                  btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String questionEdited = etQuestion.getText().toString();

                        if (questionEdited.equals("")){
                            etQuestion.setText("Question cannot be empty!");
                        }
                        editQuestion(pertanyaanId,questionEdited,kategoriId);

                    }
                });

            }
        }
    }

    public void getAllKategori(){
        baseApiService.getAllKategori().enqueue(new Callback<ResponseKategori>() {
            @Override
            public void onResponse(Call<ResponseKategori> call, Response<ResponseKategori> response) {
                if (response.isSuccessful()){
                    kategoriList = response.body().getKategoriList();
                    spKategori.setAdapter(new SpinnerAdapter(EditQuestionActivity.this,kategoriList));
                    spinnerAdapter.notifyDataSetChanged();

                }
                else {
                    Toast.makeText(EditQuestionActivity.this,"GAGAL", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<ResponseKategori> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }


    public void loadPict(String imagePathUser){
        Picasso.get()
                .load(imagePathUser)
                .fit()
                .centerCrop()
                .into(ivPict, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
//                        Toast.makeText(EditQuestionActivity.this, "Image is loaded successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(EditQuestionActivity.this, "Failed to load", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void editQuestion(int id, String question, int kategoriId){
        baseApiService.editQuestion(id, question, kategoriId).enqueue(new Callback<ResponseQuestion>() {
            @Override
            public void onResponse(Call<ResponseQuestion> call, Response<ResponseQuestion> response) {
                if (response.isSuccessful()){
                    Toast.makeText(EditQuestionActivity.this, "Question Edited!", Toast.LENGTH_LONG).show();
                    onClick();
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<ResponseQuestion> call, Throwable t) {

            }
        });
    }

    public void onClick (){
        Intent i = new Intent(EditQuestionActivity.this, HistoryActivity.class);
        startActivity(i);
        finish();
//        EditQuestionActivity.this.finish();

    }
}

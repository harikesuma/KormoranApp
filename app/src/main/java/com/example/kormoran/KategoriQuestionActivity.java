package com.example.kormoran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kormoran.adapter.KategoriQuestionAdapter;
import com.example.kormoran.apihelper.BaseApiService;
import com.example.kormoran.apihelper.RetrofitClient;
import com.example.kormoran.data.Question;
import com.example.kormoran.data.ResponseQuestion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KategoriQuestionActivity extends AppCompatActivity {
    TextView tvKategori;
    KategoriQuestionAdapter kategoriQuestionAdapter;
    List<Question> questionList = new ArrayList<>();
    RecyclerView recyclerView;
    BaseApiService baseApiService;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_question);
        baseApiService = RetrofitClient.getService(KategoriQuestionActivity.this);


        Intent i = getIntent();
        final int kategoriId = i.getExtras().getInt("ID_KATEGORI");
        String kategoriName = i.getExtras().getString("NAMA_KATEGORI");

        tvKategori = findViewById(R.id.tv_kategori);
        fab = findViewById(R.id.fab_create);
        tvKategori.setText(kategoriName);

        recyclerView = findViewById(R.id.rc_question_per_kategori);
        kategoriQuestionAdapter = new KategoriQuestionAdapter(KategoriQuestionActivity.this, questionList);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(KategoriQuestionActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(kategoriQuestionAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getPerKategoriQuestion(kategoriId);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(KategoriQuestionActivity.this, PostQuestionActivity.class);
                i.putExtra("ACTIVITY_FROM", "KATEGORI_QUESTION");
                i.putExtra("ID_KATEGORI", kategoriId);
                startActivity(i);
            }
        });

    }

    public void getPerKategoriQuestion(int id) {
        baseApiService.getPerKategoriQuestion(id).enqueue(new Callback<ResponseQuestion>() {
            @Override
            public void onResponse(Call<ResponseQuestion> call, Response<ResponseQuestion> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(KategoriQuestionActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    questionList = response.body().getQuestionList();
                    recyclerView.setAdapter(new KategoriQuestionAdapter(KategoriQuestionActivity.this, questionList));
                    kategoriQuestionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseQuestion> call, Throwable t) {

            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.search_bar);
//        SearchView searchView = new SearchView(KategoriQuestionActivity.this);
//        searchView.setBackgroundColor(Color.WHITE);
//        searchView.setQueryHint("Cari ");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.e("DEBUG", query);
//                Log.e("DEBUG", "DSDSDSD");
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String nextText) {
//                //Data akan berubah saat user menginputkan text/kata kunci pada SearchView
//
//                nextText = nextText.toLowerCase();
//                List<Question> dataFilter = (ArrayList) questionList;
//                Log.e("DEBUG","ARRAY :"+ dataFilter);
//                for (Question data : questionList) {
//                    String pertanyaan = data.getPertanyaan().toLowerCase();
//                    Log.e("DEBUG","pertanyaan :"+ pertanyaan);
//                    if (pertanyaan.contains(nextText)) {
//                        dataFilter.add(data);
//                    }
//                }
//                kategoriQuestionAdapter.setFilter(dataFilter);
//                return true;
//            }
//        });
//        searchItem.setActionView(searchView);
//        return true ;
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.search_bar);
//        SearchView searchView = new SearchView(KategoriQuestionActivity.this);
//        searchView.setBackgroundColor(Color.WHITE);
//        searchView.setQueryHint("Cari ");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                query = query.toLowerCase();
//                ArrayList<Question> dataFilter = new ArrayList<>();
//                for(Question data : questionList){
//                    String nama = data.getPertanyaan().toLowerCase();
//                    if(nama.contains(query)){
//                        dataFilter.add(data);
//                    }
//                }
//                kategoriQuestionAdapter.setFilter(dataFilter);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String nextText) {
//                //Data akan berubah saat user menginputkan text/kata kunci pada SearchView
//
//                nextText = nextText.toLowerCase();
//                ArrayList<Question> dataFilter = new ArrayList<>();
//                for(Question data : questionList){
//                    String nama = data.getPertanyaan().toLowerCase();
//                    if(nama.contains(nextText)){
//                        dataFilter.add(data);
//                    }
//                }
//                kategoriQuestionAdapter.setFilter(dataFilter);
//                return true;
//            }
//        });
//        searchItem.setActionView(searchView);
//        return super.onCreateOptionsMenu(menu);
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.search_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.search_bar);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                kategoriQuestionAdapter.getFilter().filter(newText);
//                Log.e("DEBUG", newText);
//                return false;
//            }
//        });
//        return true;
//    }
}

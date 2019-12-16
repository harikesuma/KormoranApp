package com.example.kormoran.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kormoran.R;
import com.example.kormoran.adapter.KategoriAdapter;
import com.example.kormoran.apihelper.BaseApiService;
import com.example.kormoran.apihelper.RetrofitClient;
import com.example.kormoran.data.Kategori;
import com.example.kormoran.data.ResponseKategori;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private BaseApiService baseApiService;
    RecyclerView recyclerView;
    KategoriAdapter kategoriAdapter;
    List<Kategori> kategoriList = new ArrayList<>();
    Context mContext;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mContext = getContext();
        baseApiService = RetrofitClient.getService(mContext);

        kategoriAdapter = new KategoriAdapter(mContext,kategoriList);
        recyclerView = root.findViewById(R.id.rc_kategori);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerView.setAdapter(kategoriAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        baseApiService.getAllKategori().enqueue(new Callback<ResponseKategori>() {
            @Override
            public void onResponse(Call<ResponseKategori> call, Response<ResponseKategori> response) {
                if (response.isSuccessful()){
                    Toast.makeText(mContext,response.body().getMsg(), Toast.LENGTH_SHORT);
                    kategoriList = response.body().getKategoriList();
                    recyclerView.setAdapter(new KategoriAdapter(mContext,kategoriList));
                    kategoriAdapter.notifyDataSetChanged();
//                    Log.e("DEBUG","hasil " +kategoriList);

                }
                else {
//                    Toast.makeText(mContext,"GAGAL", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<ResponseKategori> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });

        return root;
    }
}
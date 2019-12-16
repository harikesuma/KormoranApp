package com.example.kormoran.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kormoran.PostQuestionActivity;
import com.example.kormoran.R;
import com.example.kormoran.adapter.LatestAdapter;
import com.example.kormoran.adapter.TrendingAdapter;
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

public class HomeFragment extends Fragment {

    RecyclerView trendingrRecyclerView;
    RecyclerView latestRecyclerView;
    TrendingAdapter trendingAdapter;
    LatestAdapter latestAdapter;
    List<Question> trendingQuestionList = new ArrayList<>();
    List<Question> latestQuestionList = new ArrayList<>();
    BaseApiService baseApiService;
    Context mContext;


    FloatingActionButton fabCreate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mContext = getContext();
        baseApiService = RetrofitClient.getService(mContext);

        trendingrRecyclerView = root.findViewById(R.id.rc_trending);
        latestRecyclerView = root.findViewById(R.id.rc_latest);


        trendingAdapter = new TrendingAdapter(mContext,trendingQuestionList);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(getContext());
        trendingrRecyclerView.setHasFixedSize(true);
        trendingrRecyclerView.setLayoutManager(verticalLayoutManager);
        trendingrRecyclerView.setAdapter(trendingAdapter);
        trendingrRecyclerView.setItemAnimator(new DefaultItemAnimator());

        latestAdapter = new LatestAdapter(mContext,latestQuestionList);
        RecyclerView.LayoutManager verticalLayoutManager1 = new LinearLayoutManager(getContext());
        latestRecyclerView.setHasFixedSize(true);
        latestRecyclerView.setLayoutManager(verticalLayoutManager1);
        latestRecyclerView.setAdapter(latestAdapter);
        latestRecyclerView.setItemAnimator(new DefaultItemAnimator());



        fabCreate = root.findViewById(R.id.fab_create);
        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PostQuestionActivity.class);
                startActivity(i);
            }
        });


        getTrendingQuestion();
        getLatestQuestion();


        return root;
    }

    public void getTrendingQuestion(){
        baseApiService.getTrendingQuestion().enqueue(new Callback<ResponseQuestion>() {
            @Override
            public void onResponse(Call<ResponseQuestion> call, Response<ResponseQuestion> response) {
                if (response.isSuccessful()){
                    Toast.makeText(mContext, response.body().getMsg()+" trending",Toast.LENGTH_LONG);
                    trendingQuestionList = response.body().getQuestionList();
                    trendingrRecyclerView.setAdapter(new TrendingAdapter(mContext, trendingQuestionList));
                    trendingAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(mContext, "gagal trending",Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ResponseQuestion> call, Throwable t) {

            }
        });
    }

    public void getLatestQuestion(){
        baseApiService.getLatestQuestion().enqueue(new Callback<ResponseQuestion>() {
            @Override
            public void onResponse(Call<ResponseQuestion> call, Response<ResponseQuestion> response) {
                if (response.isSuccessful()){
                    Toast.makeText(mContext, response.body().getMsg()+" latest",Toast.LENGTH_LONG);
                    latestQuestionList = response.body().getQuestionList();
                    latestRecyclerView.setAdapter(new LatestAdapter(mContext, latestQuestionList));
                    latestAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(mContext, "gagal latest",Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<ResponseQuestion> call, Throwable t) {

            }
        });
    }
}
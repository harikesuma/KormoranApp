package com.example.kormoran.ui;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kormoran.R;
import com.example.kormoran.adapter.CommentAdapter;
import com.example.kormoran.adapter.LatestAdapter;
import com.example.kormoran.apihelper.BaseApiService;
import com.example.kormoran.apihelper.RetrofitClient;
import com.example.kormoran.data.Comment;
import com.example.kormoran.data.Question;
import com.example.kormoran.data.ResponseQuestion;
import com.example.kormoran.utils.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionFragment extends Fragment {

    BaseApiService baseApiService;
    PreferencesHelper preferencesHelper;
    LatestAdapter latestAdapter;
    List<Question> questionList = new ArrayList<>();
    Context mContext;
    RecyclerView recyclerView;

    public static QuestionFragment newInstance() {
        return new QuestionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_fragment, container, false);

        mContext = getContext();
        baseApiService = RetrofitClient.getService(mContext);
        preferencesHelper = new PreferencesHelper(mContext);

        String id = preferencesHelper.getSpId();

        recyclerView = view.findViewById(R.id.rv_question);
        latestAdapter= new LatestAdapter(mContext,questionList);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(latestAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getUserQuestionHistory(id);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void getUserQuestionHistory(String id){
        baseApiService.getUserQuestionHistory(id).enqueue(new Callback<ResponseQuestion>() {
            @Override
            public void onResponse(Call<ResponseQuestion> call, Response<ResponseQuestion> response) {
                if(response.isSuccessful()){
                    questionList = response.body().getQuestionList();
                    recyclerView.setAdapter(new LatestAdapter(mContext, questionList));
                    latestAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseQuestion> call, Throwable t) {

            }
        });
    }

}

package com.example.kormoran.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kormoran.R;
import com.example.kormoran.adapter.CommentAdapter;
import com.example.kormoran.apihelper.BaseApiService;
import com.example.kormoran.apihelper.RetrofitClient;
import com.example.kormoran.data.Comment;
import com.example.kormoran.data.ResponseComment;
import com.example.kormoran.utils.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerFragment extends Fragment {
    BaseApiService baseApiService;
    PreferencesHelper preferencesHelper;
    CommentAdapter commentAdapter;
    List<Comment> commentList = new ArrayList<>();
    Context mContext;
    RecyclerView recyclerView;



    public static AnswerFragment newInstance() {
        return new AnswerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.answer_fragment, container, false);


        mContext = getContext();
        baseApiService = RetrofitClient.getService(mContext);
        preferencesHelper = new PreferencesHelper(mContext);

        String id = preferencesHelper.getSpId();

        recyclerView = view.findViewById(R.id.rv_comment);
        commentAdapter = new CommentAdapter(mContext,commentList);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getUserAnswerHistory(id);

        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void getUserAnswerHistory(String id){
        baseApiService.getUserCommentHistory(id).enqueue(new Callback<ResponseComment>() {
            @Override
            public void onResponse(Call<ResponseComment> call, Response<ResponseComment> response) {
                if(response.isSuccessful()){
                    commentList = response.body().getCommentList();
                    recyclerView.setAdapter(new CommentAdapter(mContext, commentList));
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseComment> call, Throwable t) {

            }
        });
    }

}

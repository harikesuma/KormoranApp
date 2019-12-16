package com.example.kormoran;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kormoran.adapter.CommentAdapter;
import com.example.kormoran.adapter.LatestAdapter;
import com.example.kormoran.apihelper.BaseApiService;
import com.example.kormoran.apihelper.RetrofitClient;
import com.example.kormoran.data.Comment;
import com.example.kormoran.data.ResponseComment;
import com.example.kormoran.data.ResponseDetailQuestion;
import com.example.kormoran.data.ResponsePostComment;
import com.example.kormoran.utils.PreferencesHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    BaseApiService baseApiService;
    Context mContext;
    private ImageView ivProfile;
    private TextView tvName;
    private TextView tvKategori;
    private TextView tvDate;
    private TextView tvQuestion;
    private ImageView ivPict;
    private TextView tvEdited;
    String imagePath = "TAG_IMAGEPATH";
    private EditText etComment;
    private Button btnPost;
    PreferencesHelper preferencesHelper;
    List<Comment> commentList = new ArrayList<>();
    CommentAdapter commentAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        mContext = this;
        baseApiService = RetrofitClient.getService(mContext);
        preferencesHelper = new PreferencesHelper(mContext);

        ivProfile = findViewById(R.id.iv_profile);
        ivPict = findViewById(R.id.iv_pict);
        tvName = findViewById(R.id.tv_name);
        tvKategori = findViewById(R.id.tv_kategori);
        tvDate = findViewById(R.id.tv_date);
        tvQuestion = findViewById(R.id.tv_question);
        ivPict = findViewById(R.id.iv_pict);
        tvEdited = findViewById(R.id.tv_edited);
        etComment = findViewById(R.id.et_comment);
        btnPost = findViewById(R.id.btn_post);
        tvEdited = findViewById(R.id.tv_edited);
        recyclerView = findViewById(R.id.rv_comment);

        commentAdapter = new CommentAdapter(CommentActivity.this, commentList);
        recyclerView = findViewById(R.id.rv_comment);
        RecyclerView.LayoutManager verticalLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        Intent i = getIntent();
        final int id = i.getExtras().getInt("ID");


        getDetailQuestion(id);
        getComment(id);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = preferencesHelper.getSpId();
                String jawaban = etComment.getText().toString();

                if (jawaban.equals("")){
                    etComment.setError("Comment cannot empty!");
                }
                else {
                    postComment(id,userId,jawaban);
                }

            }
        });



    }

    public void getDetailQuestion(int id){
        baseApiService.getDetailQuestion(id).enqueue(new Callback<ResponseDetailQuestion>() {
            @Override
            public void onResponse(Call<ResponseDetailQuestion> call, Response<ResponseDetailQuestion> response) {
                if (response.isSuccessful()){
//                    Toast.makeText(mContext,"Success get detail", Toast.LENGTH_LONG);

                    if (response.body().getEdited().equals("1")){
                        tvEdited.setVisibility(View.VISIBLE);
                    }
                    else {
                        tvEdited.setVisibility(View.GONE);
                    }

                    String createdDate = response.body().getCreated_at();
                    createdDate = createdDate.substring(0,10);

                    String pathUser = response.body().getUser_pict();
                    String imagePathUser = "https://kormoran.000webhostapp.com/storage/user/"+ pathUser;

                    Picasso.get()
                            .load(imagePathUser)
                            .resize(200, 200)
                            .into(ivProfile, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
//                                    Toast.makeText(CommentActivity.this, "Image is loaded successfully", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(Exception e) {
                                    Toast.makeText(CommentActivity.this, "Failed to load", Toast.LENGTH_LONG).show();
                                }
                            });

                    tvName.setText(response.body().getUser_name());
                    tvKategori.setText(response.body().getKategori());
                    tvDate.setText(createdDate);
                    tvQuestion.setText(response.body().getPertanyaan());

                    String path = response.body().getPict();
                    imagePath =  "https://kormoran.000webhostapp.com/storage/pertanyaan/"+ path;

                    Picasso.get()
                            .load(imagePath)
                            .fit()
                            .centerCrop()
                            .into(ivPict, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {
//                                    Toast.makeText(CommentActivity.this, "Image is loaded successfully", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(Exception e) {
                                    Toast.makeText(CommentActivity.this, "Failed to load", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }

            @Override
            public void onFailure(Call<ResponseDetailQuestion> call, Throwable t) {

            }
        });
    }

    public void postComment(int id, String userId, String jawaban){
        baseApiService.postingCommentRequest(id,userId,jawaban).enqueue(new Callback<ResponsePostComment>() {
            @Override
            public void onResponse(Call<ResponsePostComment> call, Response<ResponsePostComment> response) {
                if(response.isSuccessful()){
                    Toast.makeText(mContext, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    onClick();
                }
                else {
                    Toast.makeText(mContext, "failed upload comment!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePostComment> call, Throwable t) {

            }
        });

    }

    public void getComment(int id){
        baseApiService.getComment(id).enqueue(new Callback<ResponseComment>() {
            @Override
            public void onResponse(Call<ResponseComment> call, Response<ResponseComment> response) {
                if (response.isSuccessful()){
//                    Toast.makeText(mContext, response.body().getMsg(), Toast.LENGTH_SHORT).show();
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

    public void onClick (){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}

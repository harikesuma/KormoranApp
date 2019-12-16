package com.example.kormoran.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kormoran.CommentActivity;
import com.example.kormoran.HistoryActivity;
import com.example.kormoran.R;
import com.example.kormoran.apihelper.BaseApiService;
import com.example.kormoran.apihelper.RetrofitClient;
import com.example.kormoran.data.Comment;
import com.example.kormoran.data.Question;
import com.example.kormoran.data.ResponseComment;
import com.example.kormoran.data.ResponseCommentLike;
import com.example.kormoran.utils.PreferencesHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> commentList;
    private Context context;
    BaseApiService baseApiService;
    PreferencesHelper preferencesHelper;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.commentList = commentList;
        this.context = context;
    }


    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        preferencesHelper = new PreferencesHelper(context);
        baseApiService = RetrofitClient.getService(context);
        final String userId = preferencesHelper.getSpId();
        final int id = commentList.get(position).getId();
        String userIdApi = commentList.get(position).getUser_id();

        String createdDate = commentList.get(position).getCreated_at();
        createdDate = createdDate.substring(0, 10);


        String pathUser = commentList.get(position).getPict();
//        "http://192.168.1.15:8000/"
        String imagePathUser = "https://kormoran.000webhostapp.com/storage/user/" + pathUser;

        if (userId.equals(userIdApi)) {
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onClickDelete(id);

                    return true;
                }
            });
        }

        loadProfile(imagePathUser, holder);


        holder.tvName.setText(commentList.get(position).getName());
        holder.tvComment.setText(commentList.get(position).getComment());
        holder.tvDate.setText(createdDate);
        holder.tvLike.setText(commentList.get(position).getLike());
        holder.ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeComment(id, userId);


            }
        });
    }

    @Override
    public int getItemCount() {
        return (commentList != null) ? commentList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfile;
        private TextView tvName;
        private TextView tvDate;
        private TextView tvComment;
        private ImageButton ibLike;
        private TextView tvLike;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvComment = itemView.findViewById(R.id.tv_answer);
            tvDate = itemView.findViewById(R.id.tv_date);
            ibLike = itemView.findViewById(R.id.ib_like);
            tvLike = itemView.findViewById(R.id.tv_like);
            cardView = itemView.findViewById(R.id.cv_answer);
        }
    }



    public void loadProfile(String imagePath, CommentAdapter.ViewHolder holder) {
        Picasso.get()
                .load(imagePath)
                .fit()
                .centerCrop()
                .into(holder.ivProfile, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
//                        Toast.makeText(context, "Image is loaded successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(context, "Failed to load", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void likeComment(int id, String userId) {
        baseApiService.postCommentLike(id, userId).enqueue(new Callback<ResponseCommentLike>() {
            @Override
            public void onResponse(Call<ResponseCommentLike> call, Response<ResponseCommentLike> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCommentLike> call, Throwable t) {

            }
        });
    }

    private void onClickDelete(final int id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                baseApiService = RetrofitClient.getService(context);
                baseApiService.deleteComment(id).enqueue(new Callback<ResponseComment>() {
                    @Override
                    public void onResponse(Call<ResponseComment> call, Response<ResponseComment> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseComment> call, Throwable t) {

                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }


}

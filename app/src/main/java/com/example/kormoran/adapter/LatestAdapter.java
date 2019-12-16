package com.example.kormoran.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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
import com.example.kormoran.EditQuestionActivity;
import com.example.kormoran.FullScreenImageActivity;
import com.example.kormoran.HistoryActivity;
import com.example.kormoran.PostQuestionActivity;
import com.example.kormoran.R;
import com.example.kormoran.apihelper.BaseApiService;
import com.example.kormoran.apihelper.RetrofitClient;
import com.example.kormoran.data.Question;
import com.example.kormoran.data.ResponseDetailQuestion;
import com.example.kormoran.data.ResponseQuestion;
import com.example.kormoran.utils.PreferencesHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LatestAdapter extends RecyclerView.Adapter<LatestAdapter.ViewHolder> {
    private List<Question> latestQuestionList;
    private Context context;
    BaseApiService baseApiService;
    PreferencesHelper preferencesHelper;


    public LatestAdapter(Context context,List<Question> latestQuestionList) {
        this.latestQuestionList = latestQuestionList;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_latest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        preferencesHelper = new PreferencesHelper(context);
        baseApiService = RetrofitClient.getService(context);
        final String userId = preferencesHelper.getSpId();
        final int id = latestQuestionList.get(position).getId();
        String userIdApi = latestQuestionList.get(position).getUser_id();



        if (userId.equals(userIdApi)) {
            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Post");
                    builder.setMessage("What you want ot do?");

                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            onClickDelete(id);
                        }
                    });

                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                        }
                    });


                    builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            onClickEdit(id);
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    return true;
                }
            });
        }

        if (latestQuestionList.get(position).getEdited().equals("1")){
            holder.tvEdited.setVisibility(View.VISIBLE);
        }
        else {
            holder.tvEdited.setVisibility(View.GONE);
        }

        String createdDate = latestQuestionList.get(position).getCreated_at();
        createdDate = createdDate.substring(0,10);

        String pathUser = latestQuestionList.get(position).getUser_pict();
        String imagePathUser = "https://kormoran.000webhostapp.com/storage/user/"+ pathUser;
        loadProfile(imagePathUser, holder);


        String path = latestQuestionList.get(position).getPict();
        String imagePath =  "https://kormoran.000webhostapp.com/storage/pertanyaan/"+ path;
        loadPict(imagePath,holder);


        holder.tvName.setText(latestQuestionList.get(position).getUser_name());
        holder.tvkategori.setText(latestQuestionList.get(position).getKategori());
        holder.tvQuestion.setText(latestQuestionList.get(position).getPertanyaan());
        holder.tvDate.setText(createdDate);
        holder.tvComment.setText(latestQuestionList.get(position).getTotal_jawaban());

        holder.ivPict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FullScreenImageActivity.class);
                i.putExtra("IVPICT",latestQuestionList.get(position).getPict());
                context.startActivity(i);
            }
        });


        holder.ibComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = latestQuestionList.get(position).getId();

                baseApiService = RetrofitClient.getService(context);
                baseApiService.postOnClickIncrease(id).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
//                            Toast.makeText(context, "CLICKED ADDED ON ", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
                Toast.makeText(context, "ID "+id, Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, CommentActivity.class);
                i.putExtra("ID", id);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (latestQuestionList != null) ? latestQuestionList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfile;
        private TextView tvName;
        private TextView tvkategori;
        private TextView tvDate;
        private TextView tvQuestion;
        private ImageView ivPict;
        private ImageButton ibComment;
        private TextView tvComment;
        private CardView cardView;
        private TextView tvEdited;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfile = itemView.findViewById(R.id.iv_profile);
            tvName = itemView.findViewById(R.id.tv_name);
            tvkategori = itemView.findViewById(R.id.tv_kategori);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvQuestion = itemView.findViewById(R.id.tv_question);
            ivPict = itemView.findViewById(R.id.iv_pict);
            ibComment = itemView.findViewById(R.id.ib_comment);
            tvComment = itemView.findViewById(R.id.tv_count_comment);
            cardView = itemView.findViewById(R.id.cv_question);
            tvEdited = itemView.findViewById(R.id.tv_edited);
        }
    }

    public void loadProfile(String imagePath, LatestAdapter.ViewHolder holder){
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

    public void loadPict(String imagePathUser, LatestAdapter.ViewHolder holder){
        Picasso.get()
                .load(imagePathUser)
                .fit()
                .centerCrop()
                .into(holder.ivPict, new com.squareup.picasso.Callback() {
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

    private void onClickDelete(final int id){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog

                baseApiService = RetrofitClient.getService(context);
                baseApiService.deleteQuestion(id).enqueue(new Callback<ResponseQuestion>() {
                    @Override
                    public void onResponse(Call<ResponseQuestion> call, Response<ResponseQuestion> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(context, response.body().getMsg(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseQuestion> call, Throwable t) {

                        Toast.makeText(context, t.getMessage(),Toast.LENGTH_LONG).show();
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

    public void onClickEdit(final int id){
        baseApiService = RetrofitClient.getService(context);
        baseApiService.showEditQuestion(id).enqueue(new Callback<ResponseDetailQuestion>() {
            @Override
            public void onResponse(Call<ResponseDetailQuestion> call, Response<ResponseDetailQuestion> response) {
                if (response.isSuccessful()){
                    Toast.makeText(context, response.body().getMsg(),Toast.LENGTH_LONG).show();
                    int id = response.body().getId();
                    String question = response.body().getPertanyaan();
                    int kategori_id = response.body().getKategori_id();
                    String pict = response.body().getPict();

                    Intent i = new Intent(context, EditQuestionActivity.class);
                    i.putExtra("id",id);
                    i.putExtra("pict", pict);
                    i.putExtra("question",question);
                    i.putExtra("kategori_id", kategori_id);
                    i.putExtra("ACTIVITY_FROM","ELSE");
                    context.startActivity(i);

                }
            }

            @Override
            public void onFailure(Call<ResponseDetailQuestion> call, Throwable t) {
                Toast.makeText(context, t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}

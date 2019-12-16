package com.example.kormoran.ui.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.kormoran.EditProfileActivity;
import com.example.kormoran.HistoryActivity;
import com.example.kormoran.LoginActivity;
import com.example.kormoran.R;
import com.example.kormoran.adapter.AchievementAdapter;
import com.example.kormoran.apihelper.BaseApiService;
import com.example.kormoran.apihelper.RetrofitClient;
import com.example.kormoran.data.Achivement;
import com.example.kormoran.data.ResponseDetailUser;
import com.example.kormoran.room.RoomDatabase;
import com.example.kormoran.utils.PreferencesHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private PreferencesHelper preferencesHelper;
    private Button btnLogOut;
    ProgressDialog progressDialog;
    ImageButton ibEditProfile;
    TextView tvName;
    TextView tvEmail;
    ImageView ivProfile;
    BaseApiService baseApiService;
    TextView tvQuestion;
    TextView tvAnswer;
    AchievementAdapter achievementAdapter;
    RecyclerView recyclerView;
    private List<Achivement> achivementList = new ArrayList<>();
    RoomDatabase roomDatabase;
    TextView tvTrophy;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);
        btnLogOut = root.findViewById(R.id.btn_log_out);
        ibEditProfile = root.findViewById(R.id.ib_edit);
        tvName = root.findViewById(R.id.tv_name);
        tvEmail = root.findViewById(R.id.tv_email);
        preferencesHelper = new PreferencesHelper(getContext());
        ivProfile = root.findViewById(R.id.iv_profile);
        tvQuestion = root.findViewById(R.id.tv_question);
        tvAnswer = root.findViewById(R.id.tv_answer);
        tvTrophy = root.findViewById(R.id.tv_trophy);
        recyclerView = root.findViewById(R.id.rv_achievement);

        achievementAdapter = new AchievementAdapter(getContext(),achivementList);
        RecyclerView.LayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.setAdapter(achievementAdapter);

        baseApiService = RetrofitClient.getService(getContext());

        String urlProfile = "https://kormoran.000webhostapp.com/storage/user/"+preferencesHelper.getSpPict();

        Picasso.get()
                .load(urlProfile)
                .resize(200, 200)
                .into(ivProfile, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
//                        Toast.makeText(getContext(), "Image is loaded successfully", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(getContext(), "Failed to load", Toast.LENGTH_LONG).show();
                    }
                });



        tvName.setText(preferencesHelper.getSpName());
        tvEmail.setText(preferencesHelper.getSpEmail());


        ibEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Bye... Logging Out");
                progressDialog.show();

                Runnable progressRunnable = new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.hide();
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 4000);
                logOut();
            }
        });

        String id = preferencesHelper.getSpId();

        countQuestionAndAnswer(id);

        return root;
    }

    public void logOut(){

        preferencesHelper= new PreferencesHelper(getContext());
        preferencesHelper.saveSPBoolean(preferencesHelper.SP_SUDAH_LOGIN, false);
        preferencesHelper.deleteSharedPreferenced();
        startActivity(new Intent(getContext(), LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

        getActivity().finish();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.history_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.history_menu:
                i = new Intent(getContext(), HistoryActivity.class);
                startActivity(i);
                return true;

        }
        return onOptionsItemSelected(item);
    }

    public void editProfile(){
        Intent i = new Intent(getContext(), EditProfileActivity.class);
        startActivity(i);
    }

    public void  countQuestionAndAnswer(String id){
        baseApiService.countQuestionAndAnswer(id).enqueue(new Callback<ResponseDetailUser>() {
            @Override
            public void onResponse(Call<ResponseDetailUser> call, Response<ResponseDetailUser> response) {
                if (response.isSuccessful()){
                    tvQuestion.setText(response.body().getQuestion());
                    tvAnswer.setText(response.body().getAnswer());
                    int like = response.body().getLike();
//                    Toast.makeText(getContext(), "LIKE = "+ like, Toast.LENGTH_SHORT).show();

                    roomDatabase = RoomDatabase.getInstance(getContext());

                    achivementList = roomDatabase.achievementDao().getAchievment(like);
                    Log.e("DEBUG", "A" + achivementList);
                    recyclerView.setAdapter(new AchievementAdapter(getContext(), achivementList));
                    achievementAdapter.notifyDataSetChanged();

                    String nextTrophy = roomDatabase.achievementDao().getNextAchievement(like);
                    tvTrophy.setText(nextTrophy);



                }
            }

            @Override
            public void onFailure(Call<ResponseDetailUser> call, Throwable t) {

            }
        });
    }

}
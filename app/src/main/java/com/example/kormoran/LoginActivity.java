package com.example.kormoran;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kormoran.apihelper.BaseApiService;
import com.example.kormoran.apihelper.RetrofitClient;
import com.example.kormoran.utils.PreferencesHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText user_name;
    private EditText password;
    private Button login;
    private TextView register;
    Boolean isError = false;

    ProgressDialog loading;
    Context mContext;
    BaseApiService mApiService;
    PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        user_name = findViewById(R.id.et_log_Username);
        password = findViewById(R.id.et_log_Password);
        login = findViewById(R.id.bt_Login);
        register = findViewById(R.id.to_Regis);

        mContext = this;
        mApiService = RetrofitClient.getService(mContext);
        preferencesHelper = new PreferencesHelper(this);

        if (preferencesHelper.getSPSudahLogin()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (user_name.getText().toString().equals("")){
                    user_name.setError("Username is required");
                    isError = true;
                }
                else if (password.getText().toString().equals("")){
                    password.setError("Password is required");
                    isError = true;
                }
                else{
                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                    requestLogin();

                }

            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intentregis = new Intent(mContext, RegisterActivity.class);
                startActivity(intentregis);
            }
        });
    }

    public void requestLogin(){
        mApiService.loginRequest(user_name.getText().toString(), password.getText().toString())
                .enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {
                                    Toast.makeText(mContext, "BERHASIL LOGIN", Toast.LENGTH_SHORT).show();

                                    String userName = jsonRESULTS.getString("user_name");
                                    String name = jsonRESULTS.getString("name");
                                    String email = jsonRESULTS.getString("email");
                                    String token = jsonRESULTS.getString("token");
                                    String id = jsonRESULTS.getString("id");
                                    String pict = jsonRESULTS.getString("pict");

                                    Log.e("DEBUG", name);

                                    Log.e("DEBUG", email);

                                    preferencesHelper.saveSPString(preferencesHelper.SP_USERNAME, userName);
                                    preferencesHelper.saveSPString(preferencesHelper.SP_TOKEN, token);
                                    preferencesHelper.saveSPString(preferencesHelper.SP_NAME, name);
                                    preferencesHelper.saveSPString(preferencesHelper.SP_EMAIL, email);
                                    preferencesHelper.saveSPString(preferencesHelper.SP_ID,id);
                                    preferencesHelper.saveSPString(preferencesHelper.SP_PICT,pict);
                                    preferencesHelper.saveSPBoolean(preferencesHelper.SP_SUDAH_LOGIN, true);
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "GAGAL LOGIN", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t){
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

}

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
import android.widget.Toast;

import com.example.kormoran.apihelper.BaseApiService;
import com.example.kormoran.apihelper.RetrofitClient;
import com.example.kormoran.apihelper.UtilsApi;
import com.example.kormoran.utils.PreferencesHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText user_name;
    private EditText email;
    private EditText password;
    private EditText c_password;
    private Button regis;

    ProgressDialog loading;
    Context mContext;
    BaseApiService mApiService;
    PreferencesHelper preferencesHelper;

    Boolean isError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Registrasi");

        name = findViewById(R.id.et_reg_Name);
        user_name = findViewById(R.id.et_reg_Name);
        email = findViewById(R.id.et_reg_Email);
        password = findViewById(R.id.et_reg_Password);
        c_password = findViewById(R.id.et_reg_Cpassword);
        regis = findViewById(R.id.bt_Register);

        mContext = this;
        mApiService = RetrofitClient.getService(mContext);
        preferencesHelper = new PreferencesHelper(RegisterActivity.this);

        regis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                if (email.getText().toString().equals("")){
                    email.setError("Email is required");
                    isError = true;
                }
                if (password.getText().toString().equals("")){
                    password.setError("Password is required");
                    isError = true;
                }
                if (c_password.getText().toString().equals("")){
                    c_password.setError("Confirm Password is required");
                    isError = true;
                }

                else {
//                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, true);
                    requestRegister();
                }

            }
        });
    }

    private void requestRegister(){
        mApiService.registerRequest(name.getText().toString(),
                user_name.getText().toString(),
                email.getText().toString(),
                password.getText().toString(),
                c_password.getText().toString())
                .enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: BERHASIL");
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")){
                                    Toast.makeText(mContext, "BERHASIL REGISTRASI", Toast.LENGTH_SHORT).show();
                                    String userName = jsonRESULTS.getString("user_name");
                                    String name = jsonRESULTS.getString("name");
                                    String email = jsonRESULTS.getString("email");
                                    String token = jsonRESULTS.getString("token");
                                    String id = jsonRESULTS.getString("id");
//                                    String pict = jsonRESULTS.getString("pict");

                                    Log.e("DEBUG", name);

                                    Log.e("DEBUG", email);

                                    preferencesHelper.saveSPString(preferencesHelper.SP_USERNAME, userName);
                                    preferencesHelper.saveSPString(preferencesHelper.SP_TOKEN, token);
                                    preferencesHelper.saveSPString(preferencesHelper.SP_NAME, name);
                                    preferencesHelper.saveSPString(preferencesHelper.SP_EMAIL, email);
                                    preferencesHelper.saveSPString(preferencesHelper.SP_ID,id);
//                                    preferencesHelper.saveSPString(preferencesHelper.SP_PICT,pict);
                                    preferencesHelper.saveSPBoolean(preferencesHelper.SP_SUDAH_LOGIN, true);
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e){
                                e.printStackTrace();
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "GAGAL REGISTRASI", Toast.LENGTH_SHORT).show();
                            Log.i("debug", "onResponse: GA BERHASIL");
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t){
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
    }

}

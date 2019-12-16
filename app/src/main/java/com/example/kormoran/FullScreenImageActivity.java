package com.example.kormoran;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.kormoran.utils.PreferencesHelper;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends AppCompatActivity {
    PreferencesHelper preferencesHelper;
    String spPictUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        PhotoView photoView = (PhotoView) findViewById(R.id.pv_full_screen);
        Intent i = getIntent();

        String url = "https://kormoran.000webhostapp.com/storage/pertanyaan/"+i.getExtras().getString("IVPICT");



        Picasso.get()
                .load(url)
                .resize(200,200)
                .into(photoView);

    }
    }

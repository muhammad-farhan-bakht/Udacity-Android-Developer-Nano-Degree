package com.farhan.example.androidjokelib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidJokeLibMainActivity extends AppCompatActivity {

    public static final String KEY_JOKE = "joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_joke_lib_main);

        TextView androidLibJokeView = findViewById(R.id.tv_android_lib_joke_viewer);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(KEY_JOKE)) {
            String joke = intent.getStringExtra(KEY_JOKE);
            Toast.makeText(this, joke, Toast.LENGTH_SHORT).show();
            androidLibJokeView.setText(joke);
        } else {
            androidLibJokeView.setText(R.string.sad_no_joke);
            Toast.makeText(this, R.string.sad_no_joke, Toast.LENGTH_SHORT).show();
        }
    }
}

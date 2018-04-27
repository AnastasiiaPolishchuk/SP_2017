package com.annapol04.munchkin.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.annapol04.munchkin.R;
import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {
        startActivity(new Intent(this, PlayDeskActivity.class));
    }

    @Override
    public void onBackPressed() {
    }
}

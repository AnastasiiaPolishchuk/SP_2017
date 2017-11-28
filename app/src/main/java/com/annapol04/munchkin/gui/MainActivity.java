package com.annapol04.munchkin.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.network.Match;

public class MainActivity extends AppCompatActivity {
    private Match match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {
        startActivity(new Intent(this, PlayDeskActivity.class));
    }

    public void goToHighscore(View view) {
        startActivity(new Intent(MainActivity.this, HighscoreActivity.class));
    }
}

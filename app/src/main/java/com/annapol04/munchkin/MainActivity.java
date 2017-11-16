package com.annapol04.munchkin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.annapol04.munchkin.gui.HighscoreActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void start(View view) {

        TextView quantityTextView = (TextView) findViewById(R.id.name_of_player_1);
        String nameOfPlayer = quantityTextView.getText().toString();

        ArrayList<String> playerNames = new ArrayList<>(6);
        playerNames.add("Anna");
        playerNames.add("Yurii");
        playerNames.add("Falccco");

        Intent myIntent = new Intent(MainActivity.this, PlayDesk.class);
        myIntent.putStringArrayListExtra("playerNames", playerNames);   //Optional parameters
        startActivity(myIntent);
    }

    public void goToHighscore(View view) {
        startActivity(new Intent(MainActivity.this, HighscoreActivity.class));
    }
}

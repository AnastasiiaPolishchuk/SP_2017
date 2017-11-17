package com.annapol04.munchkin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.annapol04.munchkin.gui.HighscoreActivity;

import java.util.ArrayList;
import java.util.Objects;

import static android.R.attr.name;
import static android.R.attr.value;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {

        ArrayList<String> playerNames = new ArrayList<>(6);
        addPlayerNameIfNotEmpty(R.id.name_of_player_1, playerNames);
        addPlayerNameIfNotEmpty(R.id.name_of_player_2, playerNames);
        addPlayerNameIfNotEmpty(R.id.name_of_player_3, playerNames);
        addPlayerNameIfNotEmpty(R.id.name_of_player_4, playerNames);
        addPlayerNameIfNotEmpty(R.id.name_of_player_5, playerNames);
        addPlayerNameIfNotEmpty(R.id.name_of_player_6, playerNames);

        Intent myIntent = new Intent(MainActivity.this, PlayDesk.class);
        myIntent.putStringArrayListExtra("playerNames", playerNames);
        startActivity(myIntent);
    }

    public void goToHighscore(View view) {
        startActivity(new Intent(MainActivity.this, HighscoreActivity.class));
    }

    private void addPlayerNameIfNotEmpty(int textViewId, ArrayList<String> playerNames) {
        TextView textView = (TextView) findViewById(textViewId);
        String playerName = textView.getText().toString();
        if (null != playerName && 0 < playerName.length()) {
            playerNames.add(playerName);
        }
    }
}

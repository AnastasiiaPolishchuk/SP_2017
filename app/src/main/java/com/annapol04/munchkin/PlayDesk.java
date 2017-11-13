package com.annapol04.munchkin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.annapol04.munchkin.engine.Game;
import com.annapol04.munchkin.engine.Player;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlayDesk extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_desk);
        ArrayList<String> playerNames = this.getIntent().getExtras().getStringArrayList("playerNames");

        game = new Game(playerNames.stream()
                .map(playerName -> new Player(playerName))
                .collect(Collectors.toList()));
    }

    private void showForCurrentPlayer() {
        Player player = game.getCurrentPlayer();
        player.getName();
    }


}

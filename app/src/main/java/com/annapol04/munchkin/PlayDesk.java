package com.annapol04.munchkin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.annapol04.munchkin.engine.Game;
import com.annapol04.munchkin.engine.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static android.R.id.message;

public class PlayDesk extends AppCompatActivity {

    private Game game;
    private int playerIndex;
    List<Player> playerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_desk);
        ArrayList<String> playerNames = this.getIntent().getExtras().getStringArrayList("playerNames");


        game = new Game(playerNames.stream()
                .map(playerName -> new Player(playerName))
                .collect(Collectors.toList()));

        playerList = game.getPlayers();

        showForCurrentPlayer(game.getCurrentPlayer());
        playerIndex = 0;

    }

    private void showForCurrentPlayer(Player player) {
        TextView view = (TextView) findViewById(R.id.test);
        view.setText(player.getName());

    }

    public void showNextPlayer(View view) {

        playerIndex++;
        if (playerIndex == playerList.size()) {
            playerIndex = 0;
        }
        game.setCurrentPlayer(playerList.get(playerIndex));
        showForCurrentPlayer(game.getCurrentPlayer());

    }
}

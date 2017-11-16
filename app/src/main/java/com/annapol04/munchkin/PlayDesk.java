package com.annapol04.munchkin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.annapol04.munchkin.engine.CardDeck;
import com.annapol04.munchkin.engine.Game;
import com.annapol04.munchkin.engine.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PlayDesk extends AppCompatActivity {

    List<Player> playerList;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_desk);
        ArrayList<String> playerNames = this.getIntent().getExtras().getStringArrayList("playerNames");

        game = new Game(playerNames.stream()
                .map(playerName -> new Player(playerName, 1))
                .collect(Collectors.toList()));

        playerList = game.getPlayers();
        CardDeck doorDeck = new CardDeck();
        CardDeck doorDeckX = new CardDeck();
        game.schuffleCards(doorDeck);
        game.initializePlayersHand(playerList, doorDeck, doorDeckX);

        showForCurrentPlayer(game.getCurrentPlayer());
    }

    private void showForCurrentPlayer(Player player) {
        TextView view = (TextView) findViewById(R.id.name_of_player);
        view.setText(player.getName());

        TextView view1 = (TextView) findViewById(R.id.level_of_player);
        view1.setText("Level : " + player.getLevel());

        TextView view2 = (TextView) findViewById(R.id.first_card_name);
        view2.setText(player.getCard(0).getCardName());
    }

    public void showNextPlayer(View view) {

        showForCurrentPlayer(game.getNextPlayer(game.getPlayerIndex()));
    }


}

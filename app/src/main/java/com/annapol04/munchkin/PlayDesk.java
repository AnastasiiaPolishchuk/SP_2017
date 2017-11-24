package com.annapol04.munchkin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.annapol04.munchkin.engine.CardDeck;
import com.annapol04.munchkin.engine.Game;
import com.annapol04.munchkin.engine.MyAdapter;
import com.annapol04.munchkin.engine.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.annapol04.munchkin.R.layout.activity_play_desk;


public class PlayDesk extends AppCompatActivity {

    List<Player> playerList;
    private Game game;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_play_desk);
        ArrayList<String> playerNames = this.getIntent().getExtras().getStringArrayList("playerNames");

        game = new Game(playerNames.stream()
                .map(playerName -> new Player(playerName, 1))
                .collect(Collectors.toList()));

        playerList = game.getPlayers();
        CardDeck doorDeck = new CardDeck();
        CardDeck doorDeckX = new CardDeck();
        game.schuffleCards(doorDeck);
        game.initializePlayersHand(doorDeck, doorDeckX);


        showForCurrentPlayer(game.getCurrentPlayer());
    }

    private void showForCurrentPlayer(Player player) {
        TextView nameOfPlayer = (TextView) findViewById(R.id.name_of_player);
        nameOfPlayer.setText(player.getName());

        TextView levelOfPlayer = (TextView) findViewById(R.id.level_of_player);
        levelOfPlayer.setText("Level : " + player.getLevel());


        RecyclerView handOfPlayer = (RecyclerView) findViewById(R.id.recycler_view_hand_of_player);
        RecyclerView.LayoutManager handLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        handOfPlayer.setLayoutManager(handLayoutManager);
        MyAdapter hatdAdapter = new MyAdapter(game.getCurrentPlayer().getHand());
        handOfPlayer.setAdapter(hatdAdapter);

        mRecyclerView = (RecyclerView) findViewById((R.id.recycler_view_cards));
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(game.getCurrentPlayer().getHand());
        mRecyclerView.setAdapter(mAdapter);

        addListenerOnButton();
/*
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ListView listView = (ListView) findViewById(R.id.list_view_cards);
        ArrayAdapter adapter = new ArrayAdapter (this,R.layout.card_list_item, items);
        listView.setAdapter(adapter);*/
    }

    public void showNextPlayer(View view) {

        showForCurrentPlayer(game.getNextPlayer());
    }

    public void addListenerOnButton() {
        ImageButton doorButton = (ImageButton) findViewById(R.id.deck_doors);
        doorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayDesk.this, "doors is clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton treasureButton = (ImageButton) findViewById(R.id.deck_treasure);
        treasureButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayDesk.this, "treasure is clicked!", Toast.LENGTH_SHORT).show();
            }
        }));
    }
}

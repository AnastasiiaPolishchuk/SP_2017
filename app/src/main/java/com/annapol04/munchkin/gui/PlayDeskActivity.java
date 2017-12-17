package com.annapol04.munchkin.gui;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.di.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


public class PlayDeskActivity extends AppCompatActivity {
    @Inject
    ToastManager toastManager;

    @Inject
    ViewModelFactory viewModelFactory;
    private PlayDeskViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_desk);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayDeskViewModel.class);

        TextView nameOfPlayer = findViewById(R.id.name_of_player);
        viewModel.getPlayerName().observe(this, nameOfPlayer::setText);

        TextView levelOfPlayer = findViewById(R.id.level_of_player);
        viewModel.getPlayerLevel().observe(this, levelOfPlayer::setText);

        RecyclerView handOfPlayer = findViewById(R.id.recycler_view_hand_of_player);
        RecyclerView.LayoutManager handLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        handOfPlayer.setLayoutManager(handLayoutManager);
        CardAdapter handAdapter = new CardAdapter(R.layout.card_item, R.id.card_item, viewModel.getPlayerHand().getValue());
        handOfPlayer.setAdapter(handAdapter);
        viewModel.getPlayerHand().observe(this, handAdapter::setCards);

        RecyclerView playedCards = findViewById((R.id.recycler_view_played_cards));
        playedCards.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        playedCards.setLayoutManager(layoutManager);
        CardAdapter playedCardsAdapter = new CardAdapter(R.layout.card_item, R.id.card_item, viewModel.getPlayedCards().getValue());
        playedCards.setAdapter(playedCardsAdapter);
        viewModel.getPlayedCards().observe(this, playedCardsAdapter::setCards);

        RecyclerView playDesk = findViewById(R.id.recycler_view_desk);
        RecyclerView.LayoutManager deskLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        playDesk.setLayoutManager(deskLayoutManager);
        CardAdapter deskCardsAdapter = new CardAdapter(R.layout.card_item_desk, R.id.card_item_desk, viewModel.getDeskCards().getValue());
        playDesk.setAdapter(deskCardsAdapter);
        viewModel.getDeskCards().observe(this, deskCardsAdapter::setCards);


        View rootView = findViewById(R.id.root_view);
        viewModel.getGameStarted().observe(this, isStarted -> {
            rootView.setVisibility(isStarted ? View.VISIBLE : View.INVISIBLE);
        });

        // prevent screen from sleeping during handshake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        addListenerOnButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.resume(this);
        toastManager.resume(this);
    }

    @Override
    protected void onPause() {
        toastManager.pause();
        viewModel.pause();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.processActivityResults(requestCode, resultCode, data);
    }

    public void addListenerOnButton() {
        ImageButton doorButton = findViewById(R.id.deck_doors);
        doorButton.setOnClickListener(v -> {
            toastManager.show( "doors is clicked!");

            viewModel.drawDoorCard();
            // WTF
        });

        ImageButton treasureButton = findViewById(R.id.deck_treasure);
        treasureButton.setOnClickListener((v -> {
            toastManager.show("treasure is clicked!");
        }));

        ImageButton exitButton = findViewById(R.id.munchkin_exit);
        exitButton.setOnClickListener(v -> {
            viewModel.quitGame();
            finish();
        });

        ImageButton infoButton = findViewById(R.id.munchkin_info);
        infoButton.setOnClickListener(v -> toastManager.show("hier wird INFO!"));
    }

    @Override
    public void onBackPressed() {
        viewModel.quitGame();
        super.onBackPressed();
    }
}

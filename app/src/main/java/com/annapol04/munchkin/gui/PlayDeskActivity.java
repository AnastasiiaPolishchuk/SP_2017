package com.annapol04.munchkin.gui;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.di.ViewModelFactory;
import com.annapol04.munchkin.engine.Card;
import com.annapol04.munchkin.engine.Player;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class PlayDeskActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    ToastManager toastManager;
    @Inject
    ViewModelFactory viewModelFactory;

    private PlayDeskViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_play_desk);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayDeskViewModel.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewModel.setTestPlayers(); // was mit dem observer ?

        for(int i = 0; i < viewModel.getPlayers().getValue().size(); i++) {
            Player p = viewModel.getPlayers().getValue().get(i);
            MenuItem item =   navigationView.getMenu().add(R.id.players_group, i + 1, (i + 1) * 100, p.getName());
            item.setIcon(R.drawable.ic_menu_camera);
        }


//        View rootView = findViewById(R.id.root_view);
//        viewModel.getGameStarted().observe(this, isStarted -> {
//            rootView.setVisibility(isStarted ? View.VISIBLE : View.INVISIBLE);
//        });

        Activity activity = this;
        viewModel.getGameFinished().observe(this, isFinished -> {
            if (isFinished) {
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.finish);
                Button okButton = dialog.findViewById(R.id.finish_ok);
                okButton.setOnClickListener(v -> {
                    activity.finish();
                });
                dialog.show();
            }
        });

        // prevent screen from sleeping during handshake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        addListenerOnButton();

        updateDeskView(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_play_desk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId() - 1;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        updateDeskView(id);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateDeskView(int playerID) {
        TextView nameOfPlayer = findViewById(R.id.name_of_player);
        nameOfPlayer.setText( viewModel.getPlayerName(playerID));

        TextView levelOfPlayer = findViewById(R.id.level_of_player);
        viewModel.getPlayerLevel().observe(this, val -> levelOfPlayer.setText(val.toString()));

        RecyclerView handOfPlayer = findViewById(R.id.recycler_view_hand_of_player);
        RecyclerView.LayoutManager handLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        handOfPlayer.setLayoutManager(handLayoutManager);
        CardAdapter handAdapter = new CardAdapter(R.layout.card_item, R.id.card_item, CardAdapter.ButtonSetup.HAND,
                viewModel.getPlayerHand(playerID).getValue(), viewModel);
        handOfPlayer.setAdapter(handAdapter);
        viewModel.getPlayerHand(playerID).observe(this, handAdapter::setCards);

        RecyclerView playedCards = findViewById((R.id.recycler_view_played_cards));
        playedCards.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        playedCards.setLayoutManager(layoutManager);
        CardAdapter playedCardsAdapter = new CardAdapter(R.layout.card_item, R.id.card_item, CardAdapter.ButtonSetup.PLAYED,
                viewModel.getPlayedCards(playerID).getValue(), viewModel);
        playedCards.setAdapter(playedCardsAdapter);
        viewModel.getPlayedCards(playerID).observe(this, playedCardsAdapter::setCards);

        RecyclerView playDesk = findViewById(R.id.recycler_view_desk);
        RecyclerView.LayoutManager deskLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        playDesk.setLayoutManager(deskLayoutManager);
        CardAdapter deskCardsAdapter = new CardAdapter(R.layout.card_item_desk, R.id.card_item_desk, CardAdapter.ButtonSetup.DESK,
                viewModel.getDeskCards().getValue(), viewModel);
        playDesk.setAdapter(deskCardsAdapter);
        viewModel.getDeskCards().observe(this, deskCardsAdapter::setCards);
    }

    public void addListenerOnButton() {
        Activity activity = this;

        ImageButton doorButton = findViewById(R.id.deck_doors);
        doorButton.setOnClickListener(v -> {
            viewModel.drawDoorCard();
        });

        ImageButton treasureButton = findViewById(R.id.deck_treasure);
        treasureButton.setOnClickListener((v -> {
            viewModel.drawTreasureCard();
        }));

        Button fightButton = findViewById(R.id.fight_button);
        fightButton.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_fight_button);
            ImageButton monsterCardImageButton = dialog.findViewById(R.id.monster_card_image);
            Card monsterCard = viewModel.getDeskCards().getValue().get(0);
            monsterCardImageButton.setImageResource(monsterCard.getImageResourceID());
            dialog.setContentView(R.layout.dialog_fight_button);
            dialog.show();

        });
    }

    public void onRunButtonPressed(View view) {
        viewModel.runAwayFromMonster();
    }

    public void onFightButtonPressed(View view) {

        viewModel.fightMonster();
    }

    public  void onNextPlayerButtonPressed(View view) { }
}

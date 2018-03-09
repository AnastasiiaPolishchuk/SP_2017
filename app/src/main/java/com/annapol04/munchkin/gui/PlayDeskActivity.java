package com.annapol04.munchkin.gui;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.di.ViewModelFactory;
import com.annapol04.munchkin.engine.Card;
import com.annapol04.munchkin.engine.Monster;
import com.annapol04.munchkin.engine.Player;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.internal.operators.maybe.MaybeOnErrorComplete;

public class PlayDeskActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    ToastManager toastManager;
    @Inject
    ViewModelFactory viewModelFactory;

    private PlayDeskViewModel viewModel;

    private View mRootView;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_play_desk);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayDeskViewModel.class);

        mRootView = findViewById(R.id.root_view);
        mRootView.setVisibility(View.INVISIBLE);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        View header = mNavigationView.getHeaderView(0);
        TextView name = header.findViewById(R.id.name_of_player_nav_header);
        name.setText(viewModel.getMyName().getValue() + ":  " + viewModel.getMyLevel().getValue());

        // ProgressBar to show time for an action
        // TODO: anschließen !
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.time_to_action_bar);
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", 100, 0);
        progressAnimator.setDuration(30000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();

        viewModel.getPlayers().observe(this, this::updatePlayers);

  //      viewModel.isMyself().observe(this, mRootView::setClickable);

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

//        TextView headgear = findViewById(R.id.headgear);
//        viewModel.getIsHeadgearEquiped().observe(this, equiped -> {
//            headgear.setTextColor(equiped ? Color.BLACK : Color.GRAY);
//        });
        ImageView headgear_ = findViewById(R.id.man_head);
        viewModel.getIsHeadgearEquiped().observe(this, equiped -> {
            headgear_.setVisibility(equiped ? View.VISIBLE : View.INVISIBLE);
        });

        ImageView armor_ = findViewById(R.id.man_armor);
        viewModel.getIsArmorEquiped().observe(this, equiped -> {
            armor_.setVisibility(equiped ? View.VISIBLE : View.INVISIBLE);
        });

        ImageView shoes_ = findViewById(R.id.man_shoes);
        viewModel.getAreShoesEquiped().observe(this, equiped -> {
            shoes_.setVisibility(equiped ? View.VISIBLE : View.INVISIBLE);
        });

        ImageView leftHand_ = findViewById(R.id.man_left_hand);
        viewModel.getIsLeftHandEquiped().observe(this, equiped -> {
            leftHand_.setVisibility(equiped ? View.VISIBLE : View.INVISIBLE);
        });

        ImageView rightHand_ = findViewById(R.id.man_right_hand);
        viewModel.getIsRightHandEquiped().observe(this, equiped -> {
            rightHand_.setVisibility(equiped ? View.VISIBLE : View.INVISIBLE);
        });

        // prevent screen from sleeping during handshake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.processActivityResults(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_info:
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle).setTitle("Game Rules").setMessage(R.string.game_rules);
                // Get the layout inflater
                LayoutInflater inflater = this.getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                AlertDialog infoDialog = builder.create();
                infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                infoDialog.setContentView(R.layout.dialog_info);
                infoDialog.show();
                return true;

            case R.id.action_exit:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        viewModel.displayPlayer(id);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateTitle() {
        mToolbar.setTitle(viewModel.getPlayerName().getValue() + ": " + viewModel.getPlayerLevel().getValue());
    }

    private void updatePlayers(List<Player> players) {
        if (players.size() == 0)
            return;

        Log.d(PlayDeskActivity.class.getSimpleName(), "Updating GUI for players");

        mNavigationView.getMenu().clear();

        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            MenuItem item = mNavigationView.getMenu().add(R.id.players_group, i + 1, (i + 1) * 100, p.getName());
            item.setIcon(R.drawable.ic_menu_camera);
        }

        updateTitle();
        viewModel.getPlayerName().observe(this, name -> updateTitle());
        viewModel.getPlayerLevel().observe(this, lvl -> updateTitle());

        TextView logView = findViewById(R.id.log);
        viewModel.getLog().observe(this, logView::setText);

        RecyclerView handOfPlayer = findViewById(R.id.recycler_view_hand_of_player);
        RecyclerView.LayoutManager handLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        handOfPlayer.setLayoutManager(handLayoutManager);
        CardAdapter handAdapter = new CardAdapter(R.layout.card_item, R.id.card_item, CardAdapter.ButtonSetup.HAND,
                viewModel.getPlayerHand().getValue(), viewModel);
        handOfPlayer.setAdapter(handAdapter);
        viewModel.getPlayerHand().observe(this, handAdapter::setCards);

        RecyclerView playedCards = findViewById((R.id.recycler_view_played_cards));
        playedCards.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        playedCards.setLayoutManager(layoutManager);
        CardAdapter playedCardsAdapter = new CardAdapter(R.layout.card_item, R.id.card_item, CardAdapter.ButtonSetup.PLAYED,
                viewModel.getPlayedCards().getValue(), viewModel);
        playedCards.setAdapter(playedCardsAdapter);
        viewModel.getPlayedCards().observe(this, playedCardsAdapter::setCards);

        RecyclerView playDesk = findViewById(R.id.recycler_view_desk);
        RecyclerView.LayoutManager deskLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        playDesk.setLayoutManager(deskLayoutManager);
        CardAdapter deskCardsAdapter = new CardAdapter(R.layout.card_item_desk, R.id.card_item_desk, CardAdapter.ButtonSetup.DESK,
                viewModel.getDeskCards().getValue(), viewModel);
        playDesk.setAdapter(deskCardsAdapter);
        viewModel.getDeskCards().observe(this, deskCardsAdapter::setCards);

        addListenerOnButton();

        mRootView.setVisibility(View.VISIBLE);
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
            if (viewModel.getDeskCards().getValue().size() == 0)
                return;

            Card monsterCard = viewModel.getDeskCards().getValue().get(0);

            if (!(monsterCard instanceof Monster))
                return;

            Monster monster = (Monster) monsterCard;


            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.dialog_fight_button);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // TODO: Image für den Player, derzeit statisch
            ImageButton monsterCardImageButton = dialog.findViewById(R.id.monster_card_image);
            monsterCardImageButton.setImageResource(monsterCard.getImageResourceID());

            TextView yourLevel = dialog.findViewById(R.id.your_fightlevel_value);
            yourLevel.setText(viewModel.getMyLevel().getValue().toString());
            TextView monsterLevel = dialog.findViewById(R.id.monster_fightlevel_value);
            monsterLevel.setText(Integer.toString(monster.getLevel()));

            dialog.show();
            dialog.setCancelable(false);


            Button dialogFightButton = dialog.findViewById(R.id.fight_diaolog_fight_button);
            dialogFightButton.setEnabled(viewModel.isMyLevelGreater().getValue());
            dialogFightButton.setOnClickListener(vv -> {
                viewModel.fightMonster();
                dialog.dismiss();
            });

            Button dialogRunAwayButton = dialog.findViewById(R.id.fight_diaolog_run_button_button);
            dialogRunAwayButton.setEnabled(!(viewModel.isMyLevelGreater().getValue()));
            dialogRunAwayButton.setOnClickListener(vv -> {
                viewModel.runAwayFromMonster();
                dialog.dismiss();
            });
        });

        Button nextPlayerButton = findViewById(R.id.next_player_button);
        nextPlayerButton.setOnClickListener(v -> {

        });

    }

}

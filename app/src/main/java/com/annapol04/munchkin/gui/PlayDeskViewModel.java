package com.annapol04.munchkin.gui;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.annapol04.munchkin.engine.Card;
import com.annapol04.munchkin.engine.Game;
import com.annapol04.munchkin.engine.Player;
import com.annapol04.munchkin.network.GooglePlayClient;
import com.annapol04.munchkin.network.PlayClient;

import java.util.List;

import javax.inject.Inject;

public class PlayDeskViewModel extends AndroidViewModel implements PlayClient.OnMatchStateChangedListener {
    private MutableLiveData<String> playerName = new MutableLiveData<>();
    private MutableLiveData<String> playerLevel = new MutableLiveData<>();
    private MutableLiveData<List<Card>> playerHand = new MutableLiveData<>();
    private MutableLiveData<List<Card>> playedCards = new MutableLiveData<>();
    private MutableLiveData<List<Card>> deskCards = new MutableLiveData<>();

    private final List<Player> playerList;
    private MutableLiveData<Boolean> isStarted = new MutableLiveData<>();
    private boolean isStartingAlready = false;
    private PlayClient client;
    private Game game;

    @Inject
    public PlayDeskViewModel(@NonNull Application application, PlayClient client, Game game) {
        super(application);

        this.client = client;
        this.game = game;
        client.setMatchStateChangedListener(this);
        isStarted.setValue(false);

        Player p = game.getCurrentPlayer();

        playerName.setValue(p.getName());
        playerLevel.setValue(Integer.toString(p.getLevel()));
        playerHand.setValue(p.getHand());
        playedCards.setValue(p.getPlayedCards());
        deskCards.setValue(game.getDeskCards());
        playerList = game.getPlayers();
    }

    @Override
    protected void onCleared() {
        client.setMatchStateChangedListener(null);
        super.onCleared();
    }

    public void processActivityResults(int requestCode, int resultCode, Intent data) {
        client.processActivityResults(requestCode, resultCode, data);
    }

    public LiveData<String> getPlayerName() {
        return playerName;
    }

    public LiveData<String> getPlayerLevel() {
        return playerLevel;
    }

    public LiveData<List<Card>> getPlayerHand() {
        return playerHand;
    }

    public LiveData<List<Card>> getPlayedCards() {
        return playedCards;
    }

    public LiveData<List<Card>> getDeskCards() {
        return deskCards;
    }

    public LiveData<Boolean> getGameStarted() {
        return isStarted;
    }

    public void quitGame() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void resume(Activity fromActivity) {
        client.setActivity(fromActivity);

        if (!client.isLoggedIn())
            client.login();
        else
            startGameIfPossible();
    }

    public void pause() {
        client.setActivity(null);
    }

    private void startGameIfPossible() {
        if (!getGameStarted().getValue() && !isStartingAlready) {
            isStartingAlready = true;

            client.startQuickGame();
        }
    }

    @Override
    public void onMatchStateChanged(GooglePlayClient.MatchState state) {
        switch (state) {
            case LOGGED_IN:
                startGameIfPossible();
                break;
            case STARTED:
                isStarted.setValue(true);
                isStartingAlready = false;
                break;
            case ABORTED:
                isStarted.setValue(false);
                isStartingAlready = false;
                break;
            case DISCONNECTED:
                isStarted.setValue(false);
                isStartingAlready = false;
                break;
        }
    }
}

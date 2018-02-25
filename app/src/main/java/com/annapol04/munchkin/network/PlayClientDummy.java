package com.annapol04.munchkin.network;

import android.app.Activity;
import android.content.Intent;

import com.annapol04.munchkin.engine.PlayClient;

public class PlayClientDummy extends PlayClient {
    private boolean loggedIn = false;

    @Override
    public void setActivity(Activity activity) {

    }

    @Override
    public boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public void login() {
        loggedIn = true;

        changeMatchState(MatchState.LOGGED_IN);
    }

    @Override
    public void processActivityResults(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void startQuickGame() {
        changeMatchState(MatchState.STARTED);
    }

    @Override
    public void sendToAll(byte[] message) {
        messageReceived(message);
    }

    @Override
    public int getAmountOfPlayers() {
        return 2;
    }
}

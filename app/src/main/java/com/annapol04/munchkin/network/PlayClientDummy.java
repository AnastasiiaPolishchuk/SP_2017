package com.annapol04.munchkin.network;

import android.app.Activity;
import android.content.Intent;

public class PlayClientDummy extends PlayClient {
    @Override
    public void setActivity(Activity activity) {

    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    @Override
    public void login() {
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
    public void sendToAllReliably(byte[] message) {

    }
}

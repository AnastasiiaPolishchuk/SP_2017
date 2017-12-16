package com.annapol04.munchkin.network;

import android.app.Activity;
import android.content.Intent;

public abstract class PlayClient {
    public enum MatchState {
        LOGGED_OUT,
        LOGGED_IN,
        STARTED,
        ABORTED,
        DISCONNECTED,
    }

    public interface OnMatchStateChangedListener {
        void onMatchStateChanged(MatchState state);
    }

    protected OnMatchStateChangedListener matchStateChangedListener;

    public void setMatchStateChangedListener(OnMatchStateChangedListener matchStateChangedListener) {
        this.matchStateChangedListener = matchStateChangedListener;
    }

    protected void changeMatchState(MatchState state) {
        if (matchStateChangedListener != null)
            matchStateChangedListener.onMatchStateChanged(state);
    }

    public abstract void setActivity(Activity activity);

    public abstract boolean isLoggedIn();

    public abstract void login();

    public abstract void processActivityResults(int requestCode, int resultCode, Intent data);

    public abstract void startQuickGame();

    public abstract void sendToAllReliably(byte[] message);
}

package com.annapol04.munchkin.gui;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.annapol04.munchkin.network.GooglePlayClient;

import javax.inject.Inject;

public class SignInViewModel extends AndroidViewModel implements GooglePlayClient.OnMatchStateChangedListener {
    private GooglePlayClient client;
    private MutableLiveData<Boolean> isLoggedIn;

    @Inject
    public SignInViewModel(@NonNull Application application, GooglePlayClient client) {
        super(application);
        this.client = client;
    }

    public LiveData<Boolean> getLoggedIn() {
        if (isLoggedIn == null) {
            isLoggedIn = new MutableLiveData<>();
            isLoggedIn.setValue(false);
        }
        return isLoggedIn;
    }

    public void login(Activity fromActivity) {
        client.setActivity(fromActivity);
        client.setMatchStateChangedListener(this);
        client.login();
    }

    public void logout() {
        client.setMatchStateChangedListener(null);
        client.setActivity(null);
    }

    public void processActivityResults(int requestCode, int resultCode, Intent data) {
        client.processActivityResults(requestCode, resultCode, data);
    }

    @Override
    public void onMatchStateChanged(GooglePlayClient.MatchState state) {
        switch (state) {
            case LOGGED_OUT:
                isLoggedIn.setValue(false);
                break;
            case LOGGED_IN:
                isLoggedIn.setValue(true);
                break;
            case STARTED:
                break;
            case ABORTED:
                break;
            case DISCONNECTED:
                break;
        }
    }
}

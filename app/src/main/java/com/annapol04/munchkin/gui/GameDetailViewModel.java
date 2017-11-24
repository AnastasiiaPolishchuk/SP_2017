package com.annapol04.munchkin.gui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.annapol04.munchkin.data.GameDetails;
import com.annapol04.munchkin.data.GameDetailsRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameDetailViewModel extends ViewModel {
    private GameDetailsRepository repository;
    private MutableLiveData<GameDetails> gameDetails;

    @Inject
    public GameDetailViewModel(GameDetailsRepository repository) {
        this.repository = repository;
    }

    public void initialize(long gameId) {
        gameDetails = new MutableLiveData<>();
        gameDetails.setValue(repository.loadGameDetails(gameId));
    }

    public LiveData<GameDetails> getDetails() {
        return gameDetails;
    }
}

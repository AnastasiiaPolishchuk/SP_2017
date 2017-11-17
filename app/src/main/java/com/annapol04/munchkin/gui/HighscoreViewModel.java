package com.annapol04.munchkin.gui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.annapol04.munchkin.data.HighscoreEntry;
import com.annapol04.munchkin.data.HighscoreRepository;

import java.util.List;

import javax.inject.Inject;

public class HighscoreViewModel extends ViewModel {
    private LiveData<List<HighscoreEntry>> users;
    private HighscoreRepository repository;

    @Inject
    public HighscoreViewModel(HighscoreRepository repository) {
        this.repository = repository;
    }

    public void initialize() {
        repository.initialize();
    }

    public LiveData<List<HighscoreEntry>> getEntries() {
        if (users == null)
            users = repository.getEntries();

        return users;
    }

    public String getTitle() { return "Hello World!"; }

}

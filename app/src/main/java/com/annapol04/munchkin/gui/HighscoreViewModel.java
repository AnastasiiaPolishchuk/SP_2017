package com.annapol04.munchkin.gui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.annapol04.munchkin.data.HighscoreEntry;

import java.util.List;

public class HighscoreViewModel extends ViewModel {
    private MutableLiveData<List<HighscoreEntry>> users;

    public LiveData<List<HighscoreEntry>> getEntries() {
        if (users == null) {
            users = new MutableLiveData<>();
            loadEntries();
        }
        return users;
    }

    private void loadEntries() {
        // Do an asyncronous operation to fetch users.
    }

    public String getTitle() { return "Hello World!"; }

}

package com.annapol04.munchkin.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.annapol04.munchkin.AppExecutors;
import com.annapol04.munchkin.db.HighscoreEntryDao;
import com.annapol04.munchkin.network.Webservice;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

public class HighscoreRepository {
    private Webservice webservice;
    private HighscoreEntryDao highscoreDao;
    private Executor executor;

    @Inject
    public HighscoreRepository(Webservice webservice, HighscoreEntryDao highscoreDao, AppExecutors executors) {
        this.webservice = webservice;
        this.highscoreDao = highscoreDao;
        this.executor = executors.diskIO();
    }

    public void initialize() {
        executor.execute(() -> highscoreDao.deleteAll());
    }

    public LiveData<List<HighscoreEntry>> getEntries() {
        MutableLiveData<List<HighscoreEntry>> data = new MutableLiveData<>();
        data.setValue(Collections.emptyList());
        executor.execute(() -> data.postValue(highscoreDao.loadEntries()));
        return data;
    }
}

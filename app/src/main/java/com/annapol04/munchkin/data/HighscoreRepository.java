package com.annapol04.munchkin.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.annapol04.munchkin.AppExecutors;
import com.annapol04.munchkin.db.HighscoreEntryDao;
import com.annapol04.munchkin.network.Webservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

public class HighscoreRepository {
    private Webservice webservice;
    private HighscoreEntryDao highscoreDao;
    private Executor executor;

    private MutableLiveData<List<HighscoreEntry>> data = new MutableLiveData<>();

    @Inject
    public HighscoreRepository(Webservice webservice, HighscoreEntryDao highscoreDao, AppExecutors executors) {
        this.webservice = webservice;
        this.highscoreDao = highscoreDao;
        this.executor = executors.diskIO();

        List<HighscoreEntry> l = new ArrayList<>(5);
        l.add(new HighscoreEntry(1, "Anastasiia", 2));
        l.add(new HighscoreEntry(2, "Christian", 3));
        l.add(new HighscoreEntry(3, "Falco", 5));

        data.setValue(l);
    }

    public void initialize() {
       // executor.execute(() -> highscoreDao.deleteAll());
    }

    public LiveData<List<HighscoreEntry>> getEntries() {
        /* data.setValue(Collections.emptyList());
        executor.execute(() -> data.postValue(highscoreDao.loadEntries()));*/
        return data;
    }

    public HighscoreEntry getEntry(long id) {
        for (HighscoreEntry entry : data.getValue())
            if (entry.getId() == id)
                return entry;

        return null;
    }
}

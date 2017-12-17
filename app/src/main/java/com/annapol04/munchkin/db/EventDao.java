package com.annapol04.munchkin.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.annapol04.munchkin.engine.Event;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * from events")
    LiveData<List<Event>> loadEntries();

    @Insert
    void insert(Event event);
}

package com.annapol04.munchkin.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.annapol04.munchkin.data.HighscoreEntry;

import java.util.List;

@Dao
public interface HighscoreEntryDao {
    @Query("SELECT * from highscores")
    List<HighscoreEntry> loadEntries();

    @Insert
    void insert(HighscoreEntry entry);

    @Query("DELETE FROM highscores")
    void deleteAll();
}

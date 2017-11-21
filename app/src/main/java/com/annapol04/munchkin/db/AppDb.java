package com.annapol04.munchkin.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.annapol04.munchkin.data.GameDetails;
import com.annapol04.munchkin.data.HighscoreEntry;

@Database(entities = {HighscoreEntry.class, GameDetails.class}, version = 1, exportSchema = false)
public abstract class AppDb extends RoomDatabase {
    public abstract HighscoreEntryDao highscoreEntryDao();
    public abstract GameDetailsDao gameDetailsDao();
}

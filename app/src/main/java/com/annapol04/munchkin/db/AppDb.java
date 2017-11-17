package com.annapol04.munchkin.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {HighscoreEntry.class}, version = 1)
public abstract class AppDb extends RoomDatabase {
    public abstract HighscoreEntryDao highscoreEntryDao();
}

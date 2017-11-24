package com.annapol04.munchkin.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "game_details")
public class GameDetails {
    @PrimaryKey
    @ForeignKey(entity = HighscoreEntry.class, parentColumns = "id", childColumns = "id")
    private long id;

    public GameDetails(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

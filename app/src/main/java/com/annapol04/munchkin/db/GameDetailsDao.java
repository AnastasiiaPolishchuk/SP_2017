package com.annapol04.munchkin.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.annapol04.munchkin.data.GameDetails;

import java.util.List;

@Dao
public interface GameDetailsDao {
    @Insert
    void insert(GameDetails gameDetails);

    @Query("SELECT * FROM game_details WHERE :id")
    GameDetails getFromId(long id);
}

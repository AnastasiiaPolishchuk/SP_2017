package com.annapol04.munchkin.data;

import com.annapol04.munchkin.db.GameDetailsDao;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameDetailsRepository {
    private GameDetailsDao dao;

    @Inject
    public GameDetailsRepository(GameDetailsDao dao) {
        this.dao = dao;
    }

    public GameDetails loadGameDetails(long gameId) {
        return dao.getFromId(gameId);
    }
}

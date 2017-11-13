package com.annapol04.munchkin.engine;

import java.util.List;

/**
 * Created by anastasiiapolishchuk on 13.11.17.
 */

public class Game {

    private final List<Player> players;
    private Player currentPlayer;

    public Game(List<Player> players) {
        this.players = players;
        this.currentPlayer = players.get(0);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}

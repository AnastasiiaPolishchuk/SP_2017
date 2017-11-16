package com.annapol04.munchkin.engine;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by anastasiiapolishchuk on 13.11.17.
 */

public class Game {

    private final List<Player> players;
    private int playerIndex;


    public Game(List<Player> players) {
        this.players = players;
        playerIndex = 0;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }


    public Player getNextPlayer(int playerIndex) {
        playerIndex++;
        if (playerIndex == players.size()) {
            setPlayerIndex(0);
        } else setPlayerIndex(playerIndex);
        return players.get(getPlayerIndex());
    }

    public Player getCurrentPlayer() {
        return players.get(playerIndex);
    }


    public void initializePlayersHand(List<Player> players, CardDeck doorDeck, CardDeck doorDeckX) {
        for (Player p : players) {
            p.setHand(getInitializeCards(doorDeck, doorDeckX));
        }
    }

    public LinkedList getInitializeCards(CardDeck doorDeck, CardDeck doorDeckX) {
        LinkedList<Card> initializeCards = new LinkedList<>();
        initializeCards.add(doorDeck.deck.poll());

        moveFromActiveToPassiveDeck(doorDeck, doorDeckX, initializeCards.getFirst());

        return initializeCards;
    }

    public void schuffleCards(CardDeck deck) {
        Collections.shuffle(deck.getCardDeck());
    }


    public void moveFromActiveToPassiveDeck(CardDeck doorDeck, CardDeck doorDeckX, Card card) {
        doorDeck.deck.remove(card);
        doorDeckX.doorDeckX.add(card);
    }
}

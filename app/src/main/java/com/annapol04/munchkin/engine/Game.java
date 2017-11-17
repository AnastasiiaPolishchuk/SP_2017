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
        this.playerIndex = 0;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getNextPlayer() {
        playerIndex++;
        if (playerIndex == players.size()) {
            playerIndex = 0;
        }
        return players.get(playerIndex);
    }

    public Player getCurrentPlayer() {
        return players.get(playerIndex);
    }


    public void initializePlayersHand(CardDeck doorDeck, CardDeck doorDeckX) {
        for (Player p : players) {
            p.setHand(getInitializeCards(doorDeck, doorDeckX));
        }
    }

    public LinkedList getInitializeCards(CardDeck doorDeck, CardDeck doorDeckX) {

        LinkedList<Card> initializeCards = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            initializeCards.add(doorDeck.deck.poll());
            moveFromActiveToPassiveDeck(doorDeck, doorDeckX, initializeCards.getFirst());
        }
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

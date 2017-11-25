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

    public CardDeck getDeck() {
        return deck;
    }

    private CardDeck deck;


    public Game(List<Player> players) {
        this.players = players;
        this.playerIndex = 0;
        this.deck = new CardDeck();
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


    public void initializePlayersHand(CardDeck deck) {
        for (Player p : players) {
            p.setHand(getInitializeCards(deck));
        }
    }

    public LinkedList getInitializeCards(CardDeck deck) {

        LinkedList<Card> initializeCards = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            initializeCards.add(deck.doorDeck.poll());
            deck.moveFromActiveToPassiveDeck(initializeCards.getFirst());
        }
        for (int i = 0; i < 4; i++) {
            initializeCards.add(deck.treasureDeck.poll());
            deck.moveFromActiveTreasureToPassiveDeck(initializeCards.getFirst()); // alle carten werden in einem stapel abgelert. Mockup Version
        }
        return initializeCards;
    }

}

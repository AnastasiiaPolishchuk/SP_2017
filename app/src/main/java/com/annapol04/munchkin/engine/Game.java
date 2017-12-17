package com.annapol04.munchkin.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Game {
    private final List<Player> players = new ArrayList<>(1);
    private List<Card> deskCards = new ArrayList<>();
    private int playerIndex = 0;
    private CardDeck deck;

    @Inject
    public Game(@Named("myself") Player player, CardDeck deck) {
        this.deck = deck;
        this.players.add(player);
        initializePlayersHand();
    }

    public void addPlayer(Player player) {
        players.add(player);
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


    private void initializePlayersHand() {
        for (Player p : players)
            p.setHand(getInitializeCards(deck));
    }

    public LinkedList<Card> getInitializeCards(CardDeck deck) {

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

    public List<Card> getDeskCards() {
        return deskCards;
    }

    public void moveFromDoorDeckToDesk(){
       deskCards.add(deck.doorDeck.poll());
       deck.moveFromActiveToPassiveDeck(deskCards.get(0));

    }
}

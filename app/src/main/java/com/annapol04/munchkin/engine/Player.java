package com.annapol04.munchkin.engine;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final String name;
    private int level = 0;
    private List<Card> hand;
    private List<Card> playedCards = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public void setPlayedCards(List<Card> playedCards) {
        this.playedCards = playedCards;
    }

    public List<Card> getPlayedCards() {
        return playedCards;
    }

    public Card getCard(int index) {
        return hand.get(index);
    }

    public List<Card> getHand() {
        return hand;
    }

}

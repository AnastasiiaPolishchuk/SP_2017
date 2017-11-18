package com.annapol04.munchkin.engine;


import java.util.List;

/**
 * Created by anastasiiapolishchuk on 13.11.17.
 */

public class Player {

    private final String name;
    private List<Card> hand;
    private final int level;

    public Player(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public void setHand(List hand) {
        this.hand = hand;
    }

    public Card getCard(int index) {
        return hand.get(index);
    }

    public List<Card> getHand() {
        return hand;
    }

}

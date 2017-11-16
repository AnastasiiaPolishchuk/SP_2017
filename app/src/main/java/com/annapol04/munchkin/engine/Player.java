package com.annapol04.munchkin.engine;

import java.util.LinkedList;

/**
 * Created by anastasiiapolishchuk on 13.11.17.
 */

public class Player {

    private final String name;
    private LinkedList<Card> hand;
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

    public void setHand(LinkedList hand) {
        this.hand = hand;
    }

    public Card getCard(int index) {
        return hand.get(index);
    }

}

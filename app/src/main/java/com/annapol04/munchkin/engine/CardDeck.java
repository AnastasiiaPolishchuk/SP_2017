package com.annapol04.munchkin.engine;

import java.util.LinkedList;

/**
 * Created by anastasiiapolishchuk on 14.11.17.
 */

public class CardDeck {

    LinkedList<Card> deck = new LinkedList<>();
    LinkedList<Card> doorDeckX = new LinkedList<>();


    public CardDeck() {
        this.deck = makeDeck();
    }

    private LinkedList<Card> makeDeck() {

        LinkedList<Card> deck = new LinkedList<>();
        deck.add(new Card(1, "Filzläuse"));
        deck.add(new Card(2, "Sabbernder Schleim"));
        deck.add(new Card(3, "Topfpflanze"));
        deck.add(new Card(4, "Harfien"));
        deck.add(new Card(5, "Anwalt"));
        deck.add(new Card(6, "Amazone"));
        deck.add(new Card(7, "Leprachaun"));
        return deck;
    }

    public LinkedList getCardDeck() {
        return deck;
    }
}

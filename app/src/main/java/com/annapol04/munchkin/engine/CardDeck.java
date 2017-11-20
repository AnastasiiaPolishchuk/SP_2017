package com.annapol04.munchkin.engine;

import com.annapol04.munchkin.R;

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
        deck.add(new Card(1, "bigfoot", R.drawable.bigfoot));
        deck.add(new Card(2, "Orcs", R.drawable.orcs));
        deck.add(new Card(3, "chickenOnYourHead", R.drawable.chickenonyourhead));
        deck.add(new Card(4, "cleric", R.drawable.cleric));
        deck.add(new Card(5, "duckOfDoom", R.drawable.duckofdoom));
        deck.add(new Card(6, "elf", R.drawable.elf));
        deck.add(new Card(8, "gazebo", R.drawable.gazebo));
        deck.add(new Card(9, "halfling", R.drawable.halfling));
        deck.add(new Card(10, "incomeTax", R.drawable.incometax));
        deck.add(new Card(11, "insuranceSalesman", R.drawable.insurancesalesman));
        deck.add(new Card(12, "lawyers", R.drawable.lawyers));
        deck.add(new Card(13, "platycore", R.drawable.platycore));

        return deck;
    }

    public LinkedList getCardDeck() {
        return deck;
    }
}

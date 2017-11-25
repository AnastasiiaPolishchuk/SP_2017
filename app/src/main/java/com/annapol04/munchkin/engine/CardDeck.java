package com.annapol04.munchkin.engine;

import com.annapol04.munchkin.R;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by anastasiiapolishchuk on 14.11.17.
 */

public class CardDeck {

    LinkedList<Card> doorDeck = new LinkedList<>();
    LinkedList<Card> stored = new LinkedList<>();
    LinkedList<Card> treasureDeck = new LinkedList<>();


    public CardDeck() {
        this.doorDeck = makeDoorDeck();
        this.treasureDeck = makeTreasureDeck();
    }

    private LinkedList<Card> makeDoorDeck() {

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

    private LinkedList<Card> makeTreasureDeck() {

        LinkedList<Card> deck = new LinkedList<>();
        deck.add(new Card(1, "additionerror", R.drawable.tacidpotion));
        deck.add(new Card(2, "Orcs", R.drawable.tadditionerror));
        deck.add(new Card(3, "chickenOnYourHead", R.drawable.tanchovysandwich));
        deck.add(new Card(4, "cleric", R.drawable.tboilananthill));
        deck.add(new Card(5, "duckOfDoom", R.drawable.tbribegmwithfood));
        deck.add(new Card(6, "elf", R.drawable.tcloakofobscurity));
        deck.add(new Card(8, "gazebo", R.drawable.tdopplerganger));
        deck.add(new Card(9, "halfling", R.drawable.thammerofkneecapping));
        deck.add(new Card(10, "incomeTax", R.drawable.thornyhelmet));
        deck.add(new Card(11, "insuranceSalesman", R.drawable.thugerock));
        deck.add(new Card(12, "lawyers", R.drawable.tleatherarmor));
        deck.add(new Card(13, "platycore", R.drawable.tloadeddie));

        return deck;
    }

    public LinkedList getDoorCardDeck() {
        return doorDeck;
    }

    public LinkedList getTreasureCardDeck() {
        return treasureDeck;
    }

    public void schuffleCards() {
        Collections.shuffle(this.getDoorCardDeck());
        Collections.shuffle(this.getTreasureCardDeck());
    }

    public void moveFromActiveToPassiveDeck(Card card) {

        this.doorDeck.remove(card);
        stored.add(card);
    }

    public void moveFromActiveTreasureToPassiveDeck(Card card) {

        this.treasureDeck.remove(card);
        stored.add(card);
    }
}

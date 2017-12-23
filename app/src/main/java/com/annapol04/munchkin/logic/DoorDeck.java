package com.example.admin.ezmunchkin;

import java.util.ArrayList;

/**
 * Created by Falco on 26.10.2017.
 */

public class DoorDeck {
    static ArrayList<Card> deck = new ArrayList<Card>();
    static ArrayList<Card> discardDeck = new ArrayList<Card>();

    public static Card draw(){
        int indexOfRandomCard = Dice.getRandom(0, deck.size() - 1); // stapel.size() returnt anzahl der Elemente in stapel, daher - 1
        return deck.remove(indexOfRandomCard);
    }
}

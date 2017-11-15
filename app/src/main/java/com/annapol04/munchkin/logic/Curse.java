package com.annapol04.munchkin.logic;

/**
 * Created by Falco on 26.10.2017.
 */

public class Curse extends Card {

    public String consequence;

    public Curse (int id, int deck, String name, String description) {
        this.deck = 0; //Must be hardcoded
        this.id = id;
        this.name = name;
        this.description = description;
    }
}

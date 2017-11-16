package com.annapol04.munchkin.logic;

/**
 * Created by Falco on 26.10.2017.
 */

public class Special extends Card {
    public Special (int id, String name, String description) {
        this.deck = 1; //Muss hardcoded sein
        this.id = id;
        this.name = name;
        this.description = description;
    }
}

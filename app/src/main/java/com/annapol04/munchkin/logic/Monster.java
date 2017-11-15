package com.annapol04.munchkin.logic;

/**
 * Created by Falco on 29.10.2017.
 */

public class Monster extends Card {

    public int level;
    public int treasure;
    public String badThings;

    public Monster(int id, int deck, String name, String description, int level, int treasure, String badThings) {
        this.deck = 0; //Must be hardcoded
        this.id = id;
        this.name = name;
        this.description = description;
        this.level = level;
        this.treasure = treasure;
        this.badThings = badThings;
    }
}

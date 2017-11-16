package com.annapol04.munchkin.logic;

/**
 * Created by Falco on 26.10.2017.
 */

public class Race extends Card { // nur Elf und Zwerg

    public Race(int id, int deck, String name, String description) {
        this.deck = 0; //Muss hardcoded sein
        this.id = id;
        this.name = name;
        this.description = description;
    }
}

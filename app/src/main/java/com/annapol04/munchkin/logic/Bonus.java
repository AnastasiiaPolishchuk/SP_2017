package com.annapol04.munchkin.logic;

/**
 * Created by Falco on 26.10.2017.
 */

public class Bonus extends Card {

    public int bonus;
    public int blocks; //0 = nothing, 1 = 1 hand, 2 = 2 hands
    public int size; //0 = small, 1 = big

    public Bonus(int id, String name, int bonus, int blocks, int size) {
        this.deck = 1; //must be hardcoded
        this.id = id;
        this.name = name;
    }
}

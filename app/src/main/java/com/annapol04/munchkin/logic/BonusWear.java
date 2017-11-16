package com.annapol04.munchkin.logic;

/**
 * Created by Falco on 26.10.2017.
 */

public class BonusWear extends Card { //Schatzstapel

    public int bonus;
    public int blocking; //0 = nothing, 1 = 1 hand, 2 = 2 hand, 3 = armour, 4 = head, 5 = shoes
    public int size; //0 = small, 1 = big

    public BonusWear (int id, String name, int bonus, int blocking, int size) {
        this.deck = 1; //Must be hardcoded
        this.id = id;
        this.name = name;
        this.bonus = bonus;
        this.blocking = blocking;
        this.size = size;
    }
}

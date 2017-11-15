package com.annapol04.munchkin.logic;

/**
 * Created by Falco on 26.10.2017.
 */

public class BonusSide extends Card {

    public int bonus;

    public BonusSide (int id, String name, int bonus) {
        this.deck = 1; //Must be hardcoded
        this.id = id;
        this.name = name;
        this.bonus = bonus;
    }
}

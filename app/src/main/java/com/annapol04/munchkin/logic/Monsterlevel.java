package com.annapol04.munchkin.logic;

/**
 * Created by Falco on 26.10.2017.
 */

public class Monsterlevel extends Card { //keine Extraschätze

    public int monsterBonus;

    public Monsterlevel (int id, String name, int monsterBonus) {
        this.deck = 0; //Muss hardcoded sein
        this.id = id;
        this.name = name;
        this.monsterBonus = monsterBonus;
    }
}

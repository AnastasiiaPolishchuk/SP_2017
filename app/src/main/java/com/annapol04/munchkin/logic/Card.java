package com.annapol04.munchkin.logic;

/**
 * Created by Falco on 26.10.2017.
 */

/**
 * Repräsentiert eine Karte im Spiel
 */
public abstract class Card { //Spezialisations: Monster0, Rasse0, Fluch0, Bonus0, Monsterstufe0 | BonusAnzug1, BonusSeite1, Spezial1
    public int id;
    public int deck; //membership: 0 = Doorstack, 1 = Treasuredeck

    public String name;
    public String description;

}

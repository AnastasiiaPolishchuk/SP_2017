package com.example.admin.ezmunchkin;

/**
 * Created by Falco on 26.10.2017.
 */

/**
 * Repräsentiert eine Karte im Spiel
 */
public abstract class Card { //Spezialisations: Monster0, Rasse0, Fluch0, Bonus0, Monsterstufe0 | BonusAnzug1, BonusSeite1, Spezial1

    public enum Membership{
        DOOR,
        TREASURE;
    }

    public String name;
    public Membership member;

    public Card (Membership member, String name){
        this.member = member;
        this.name = name;
    }

}

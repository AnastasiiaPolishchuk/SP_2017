package com.annapol04.munchkin.engine;


/**
 * Created by anastasiiapolishchuk on 14.11.17.
 */

public class Card {

    private final int id;
    //   public int deck; //membership: 0 = Doorstack, 1 = Treasuredeck

    private final String name;
//    public String description;

    public Card(int id, String name) {

        this.name = name;
        this.id = id;
    }

    public String getCardName() {
        return this.name;
    }

}

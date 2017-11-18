package com.annapol04.munchkin.engine;


import com.annapol04.munchkin.R;

/**
 * Created by anastasiiapolishchuk on 14.11.17.
 */

public class Card {

    private final int id;
    //   public int deck; //membership: 0 = Doorstack, 1 = Treasuredeck

    private final String name;
//    public String description;
private final int imageResourceID;

    public int getImageResourceID() {
        return imageResourceID;
    }

    public Card(int id, String name) {

        this.name = name;
        this.id = id;
        this.imageResourceID = R.drawable.door;
    }

    public String getCardName() {
        return this.name;
    }


}

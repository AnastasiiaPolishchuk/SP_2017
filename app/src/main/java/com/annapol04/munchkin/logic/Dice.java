package com.annapol04.munchkin.logic;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Falco on 26.10.2017.
 */

public class Dice {


    /**
     *
     * @return eine zufaelige Zahl zwischen 1 und 6 (beide inklusiv)
     */
    public static int throwDice(){
        return ThreadLocalRandom.current().nextInt(1, 6 + 1);
    }

    /**
     *
     * @return eine zufaelige Zahl zwischen ab und bis (beide inklusiv)
     */
    public static int getRandom(int ab, int bis){
        return ThreadLocalRandom.current().nextInt(ab, bis + 1);
    }
}

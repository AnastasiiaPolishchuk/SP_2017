package com.annapol04.munchkin.engine;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Created by Admin on 09.03.2018.
 */

public class Monsterlevel extends Card {

    public int monsterBonus;

    public Monsterlevel (@StringRes int name, @StringRes int description, @DrawableRes int imageResourceID, int bonus) {
        super(name, description, imageResourceID);
        this.monsterBonus = bonus;
    }
}

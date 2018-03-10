package com.annapol04.munchkin.engine;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class BonusSide extends Card {

    private int bonus;

    public BonusSide (@StringRes int name, @StringRes int description, @DrawableRes int imageResourceID, int bonus) {
        super(name, description, imageResourceID);
        this.bonus = bonus;
    }

    public int getBonus(){
        return this.bonus;
    }
}
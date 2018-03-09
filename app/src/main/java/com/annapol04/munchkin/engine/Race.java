package com.annapol04.munchkin.engine;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class Race extends Card {

    public Race (@StringRes int name, @StringRes int description, @DrawableRes int imageResourceID) {
        super(name, description, imageResourceID);
    }

}

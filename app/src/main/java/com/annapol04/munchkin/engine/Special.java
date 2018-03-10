package com.annapol04.munchkin.engine;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class Special extends Card {

    public Special (@StringRes int name, @StringRes int description, @DrawableRes int imageResourceID) {
        super(name, description, imageResourceID);
    }

}
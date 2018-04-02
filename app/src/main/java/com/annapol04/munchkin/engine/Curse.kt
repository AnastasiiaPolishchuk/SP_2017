package com.annapol04.munchkin.engine

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

class Curse(@StringRes name: Int,
            @StringRes description: Int,
            @DrawableRes imageResourceID: Int)
    : Card(name, description, imageResourceID)

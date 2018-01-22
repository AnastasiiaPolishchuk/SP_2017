package com.annapol04.munchkin.engine;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private static final List<Card> lookup = new ArrayList<>();
    private static int idSource = 0;

    private final int id;
    private final @StringRes int name;
    private final @StringRes int description;
    private final @DrawableRes int imageResourceID;

    public Card(@StringRes int name, @StringRes int description, @DrawableRes int imageResourceID) {
        this.id = idSource++;
        this.name = name;
        this.description = description;
        this.imageResourceID = imageResourceID;

        lookup.add(this);
    }

    public int getId() {
        return id;
    }

    public int getName() {
        return this.name;
    }

    public int getImageResourceID() {
        return imageResourceID;
    }

    public static Card fromId(int id) {
        if (id >= lookup.size())
            throw new IllegalArgumentException("Invalid event card id " + id);

        return lookup.get(id);
    }
}

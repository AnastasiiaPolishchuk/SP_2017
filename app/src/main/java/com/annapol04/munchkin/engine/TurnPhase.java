package com.annapol04.munchkin.engine;

import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

import com.annapol04.munchkin.R;

public enum TurnPhase {
    IDLE(R.string.tp_idle),
    EQUIPMENT(R.string.tp_equipment),
    KICK_OPEN_THE_DOOR(R.string.tp_kick_open_the_door),
    KICK_OPEN_THE_DOOR_AND_FIGHT(R.string.tp_kick_open_the_door_and_fight),
    KICK_OPEN_THE_DOOR_AND_DRAW(R.string.tp_kick_open_the_door_and_draw),
    LOOK_FOR_TROUBLE(R.string.tp_look_for_trouble),
    LOOT_THE_ROOM(R.string.tp_loot_the_room),
    CHARITY(R.string.tp_charity);

    private @StringRes int stringId;

    TurnPhase(@StringRes int string) {
        stringId = string;
    }

    public int getStringId() {
        return stringId;
    }

    public static TurnPhase fromId(int id) {
        if (id >= lookup.length)
            throw new IllegalArgumentException("Invalid turn phase id " + id);

        return lookup[id];
    }

    private static final TurnPhase[] lookup = new TurnPhase[]{
            IDLE,
            EQUIPMENT,
            KICK_OPEN_THE_DOOR,
            KICK_OPEN_THE_DOOR_AND_FIGHT,
            KICK_OPEN_THE_DOOR_AND_DRAW,
            LOOK_FOR_TROUBLE,
            LOOT_THE_ROOM,
            CHARITY
    };
}

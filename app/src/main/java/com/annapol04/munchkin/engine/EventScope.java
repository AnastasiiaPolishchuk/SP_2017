package com.annapol04.munchkin.engine;

/**
 * Created by chris_000 on 08.12.2017.
 */

public enum EventScope {
    GAME,
    PLAYER1,
    PLAYER2,
    PLAYER3,
    PLAYER4,
    PLAYER5,
    PLAYER6;

    public static EventScope fromId(int id) {
        if (id >= lookup.length)
            throw new IllegalArgumentException("Invalid event scope id " + id);

        return lookup[id];
    }

    private static final EventScope[] lookup = new EventScope[]{
            GAME,
            PLAYER1,
            PLAYER2,
            PLAYER3,
            PLAYER4,
            PLAYER5,
            PLAYER6
    };
}

package com.annapol04.munchkin.engine;

public enum Scope {
    GAME,
    PLAYER1,
    PLAYER2,
    PLAYER3,
    PLAYER4,
    PLAYER5,
    PLAYER6;

    public static Scope fromId(int id) {
        if (id >= lookup.length)
            throw new IllegalArgumentException("Invalid event scope id " + id);

        return lookup[id];
    }

    private static final Scope[] lookup = new Scope[]{
            GAME,
            PLAYER1,
            PLAYER2,
            PLAYER3,
            PLAYER4,
            PLAYER5,
            PLAYER6
    };
}

package com.annapol04.munchkin.engine;

public enum DataType {
    EMPTY,
    INTEGER,
    STRING,
    CARD,
    TURN_PHASE;

    public static DataType fromId(int id) {
        if (id >= lookup.length)
            throw new IllegalArgumentException("Invalid event data type id " + id);

        return lookup[id];
    }

    private static final DataType[] lookup = new DataType[]{
            EMPTY,
            INTEGER,
            STRING,
            CARD,
            TURN_PHASE
    };
}
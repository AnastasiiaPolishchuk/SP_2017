package com.annapol04.munchkin.engine;

/**
 * Created by chris_000 on 08.12.2017.
 */

public enum EventDataType {
    EMPTY,
    INTEGER,
    STRING;

    public static EventDataType fromId(int id) {
        if (id >= lookup.length)
            throw new IllegalArgumentException("Invalid event data type id " + id);

        return lookup[id];
    }

    private static final EventDataType[] lookup = new EventDataType[]{
            EMPTY,
            INTEGER,
            STRING
    };
}
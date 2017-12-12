package com.annapol04.munchkin.engine;

import java.util.function.Function;

/**
 * Created by chris_000 on 08.12.2017.
 */

public class EventMessage {
    private static int idSource = 0;
    private static final EventMessage[] lookup = new EventMessage[] {
            new EventMessage(""),
    };

    private final int id;
    private final String message;

    public EventMessage(String message) {
        this.id = idSource++;
        this.message = message;
    }

    public static EventMessage fromId(int id) {
        if (id >= lookup.length)
            throw new IllegalArgumentException("Invalid event message id " + id);

        return lookup[id];
    }
}

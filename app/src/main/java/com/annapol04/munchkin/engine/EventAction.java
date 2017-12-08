package com.annapol04.munchkin.engine;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by chris_000 on 08.12.2017.
 */

public class EventAction {
    private static int idSource = 0;
    private static final EventAction[] lookup = new EventAction[] {
            new EventAction(g -> { }),
    };

    private final int id;
    private final Consumer<Game> modifier;

    public EventAction(Consumer<Game> modifier) {
        this.id = idSource++;
        this.modifier = modifier;
    }

    public void execute(Game game) {
        modifier.accept(game);
    }

    public static EventAction fromId(int id) {
        if (id >= lookup.length)
            throw new IllegalArgumentException("Invalid event action id " + id);

        return lookup[id];
    }
}

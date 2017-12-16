package com.annapol04.munchkin.engine;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public class Action {
    public static final Action NOTHING = new Action((g, o) -> { });
    public static final Action JOIN_PLAYER = new Action((g, o) -> g.addPlayer(new Player((String)o)));

    private static int idSource = 0;
    private static final Action[] lookup;

    static {
        Field[] fields = Action.class.getFields();

        lookup = new Action[fields.length];

        int i = 0;

        try {
            for (Field field : fields)
                if (field.getType() == Action.class)
                    lookup[i++] = (Action) field.get(null);
        } catch (IllegalAccessException e) { /* wont happen... */ }
    }

    private final int id;
    private final BiConsumer<Game, Object> modifier;

    public Action(BiConsumer<Game, Object> modifier) {
        this.id = idSource++;
        this.modifier = modifier;
    }

    public void execute(Game game, Object data) {
        modifier.accept(game, data);
    }

    public int getId() {
        return id;
    }

    public static Action fromId(int id) {
        if (id >= lookup.length)
            throw new IllegalArgumentException("Invalid event action id " + id);

        return lookup[id];
    }
}

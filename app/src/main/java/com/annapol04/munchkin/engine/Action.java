package com.annapol04.munchkin.engine;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public class Action {
    public static final Action NOTHING = new Action((g, o) -> { });
    public static final Action JOIN_PLAYER = new Action((g, o) -> g.join(new Player((String)o)));
    public static final Action LEAVE_PLAYER = new Action((g, o) -> { throw new UnsupportedOperationException(); });
    public static final Action DRAW_DOORCARD = new Action((g, o) -> g.drawDoorCard(Card.fromId((int)o)));
    public static final Action DRAW_TREASURECARD = new Action((g, o) -> g.drawTreasureCard(Card.fromId((int)o)));
    public static final Action PICKUP_CARD = new Action((g, o) -> g.pickupCard(Card.fromId((int)o)));
    public static final Action PLAY_CARD = new Action((g, o) -> g.playCard(Card.fromId((int)o)));
    public static final Action FIGHT_MONSTER = new Action((g, o) -> g.fightMonster());
    public static final Action RUN_AWAY = new Action((g, o) -> g.runAwayFromMonster());

    private static int idSource = 0;
    private static final Action[] lookup;

    static {
        Field[] fields = Action.class.getFields();

        lookup = new Action[fields.length];

        int i = 0;

        try {
            for (Field field : fields)
                if (field.getType() == Action.class) {
                    Action action = (Action) field.get(null);

                    lookup[action.getId()] = action;
                }
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

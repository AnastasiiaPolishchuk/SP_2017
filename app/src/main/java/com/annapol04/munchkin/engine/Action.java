package com.annapol04.munchkin.engine;

import java.lang.reflect.Field;

public class Action {
    public static final Action NOTHING = new Action((match, game, event) -> { });
    public static final Action JOIN_PLAYER = new Action((match, game, event) -> {
            match.joinPlayer(event.getInteger());
    });
    public static final Action ASSIGN_PLAYER_NUMBER = new Action((match, game, event) -> {
        match.assignPlayerNumber(event.getScope().ordinal(), event.getInteger());
    });
    public static final Action NAME_PLAYER = new Action((match, game, event) -> {
        match.namePlayer(event.getScope().ordinal(), event.getString());
    });
    public static final Action FINISH_ROUND = new Action((match, game, event) -> {
        match.finishRound(event.getScope().ordinal());
    });
    public static final Action LEAVE_PLAYER = new Action((match, game, event) -> {
        throw new UnsupportedOperationException();
    });
    public static final Action DRAW_DOORCARD = new Action((match, game, event) -> {
        match.getPlayer(event.getScope()).drawDoorCard(Card.fromId(event.getInteger()));
    });
    public static final Action DRAW_TREASURECARD = new Action((match, game, event) -> {
        match.getPlayer(event.getScope()).drawTreasureCard(Card.fromId((event.getInteger())));
    });
    public static final Action PICKUP_CARD = new Action((match, game, event) -> {
        game.pickupCard(Card.fromId(event.getInteger()));
    });
    public static final Action PLAY_CARD = new Action((match, game, event) -> {
        game.playCard(Card.fromId(event.getInteger()));
    });
    public static final Action FIGHT_MONSTER = new Action((match, game, event) -> {
        game.fightMonster();
    });
    public static final Action RUN_AWAY = new Action((match, game, event) -> {
        game.runAwayFromMonster();
    });

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

                    action.setName(field.getName());
                    lookup[action.getId()] = action;
                }
        } catch (IllegalAccessException e) { /* wont happen... */ }
    }

    private final int id;
    private final TriConsumer<Match, Game, Event> modifier;
    private String name = null;

    public Action(TriConsumer<Match, Game, Event> modifier) {
        this.id = idSource++;
        this.modifier = modifier;
    }

    public void execute(Match match, Game game, Event data) {
        modifier.accept(match, game, data);
    }

    public int getId() {
        return id;
    }

    public static Action fromId(int id) {
        if (id >= lookup.length)
            throw new IllegalArgumentException("Invalid event action id " + id);

        return lookup[id];
    }

    private void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

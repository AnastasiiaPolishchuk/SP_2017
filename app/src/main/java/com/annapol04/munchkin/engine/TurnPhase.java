package com.annapol04.munchkin.engine;

public enum TurnPhase {
    IDLE,
    EQUIPMENT,
    KICK_OPEN_THE_DOOR,
    KICK_OPEN_THE_DOOR_AND_FIGHT,
    LOOK_FOR_TROUBLE,
    LOOT_THE_ROOM,
    CHARITY;

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
            LOOK_FOR_TROUBLE,
            LOOT_THE_ROOM,
            CHARITY
    };
}

package com.annapol04.munchkin.engine;

/**
 * Created by chris_000 on 08.12.2017.
 */

public class Event {
    private final EventScope scope;
    private final  EventAction action;
    private final  EventMessage message;
    private final  EventDataType dataType;

    public Event(EventScope scope, EventAction action, EventMessage message, EventDataType dataType) {
        this.scope = scope;
        this.action = action;
        this.message = message;
        this.dataType = dataType;
    }

    public void execute(Game game) {
        action.execute(game);
    }
}

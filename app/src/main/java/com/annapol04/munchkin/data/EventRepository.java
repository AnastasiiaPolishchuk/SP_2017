package com.annapol04.munchkin.data;

import com.annapol04.munchkin.engine.Event;
import com.annapol04.munchkin.engine.EventAction;
import com.annapol04.munchkin.engine.EventDataType;
import com.annapol04.munchkin.engine.EventMessage;
import com.annapol04.munchkin.engine.EventScope;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by chris_000 on 08.12.2017.
 */

public class EventRepository {
    public interface NewEventListener {
        void onNewEvent(Event event);
    }

    private NewEventListener listener;
    private List<Event> events;

    public EventRepository() {
        events = new ArrayList<>();
        events.add(new Event(EventScope.GAME, EventAction.fromId(0), EventMessage.fromId(0), EventDataType.EMPTY));
        events.add(new Event(EventScope.GAME, EventAction.fromId(0), EventMessage.fromId(0), EventDataType.EMPTY));
        events.add(new Event(EventScope.GAME, EventAction.fromId(0), EventMessage.fromId(0), EventDataType.EMPTY));
    }

    public void reset() {
        if (listener != null) {
            for (Event event : events)
                listener.onNewEvent(event);
        }
    }

    public void setNewEventListener(NewEventListener listener) {
        this.listener = listener;
    }
}

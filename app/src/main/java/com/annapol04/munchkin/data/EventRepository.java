package com.annapol04.munchkin.data;

import com.annapol04.munchkin.engine.Event;
import com.annapol04.munchkin.engine.Action;
import com.annapol04.munchkin.engine.Scope;

import java.util.ArrayList;
import java.util.List;

public class EventRepository {
    public interface NewEventListener {
        void onNewEvent(Event event);
    }

    private NewEventListener listener;
    private List<Event> events;

    public EventRepository() {
        events = new ArrayList<>();
        events.add(new Event(Scope.GAME, Action.NOTHING, 0));
        events.add(new Event(Scope.GAME, Action.NOTHING, 0));
        events.add(new Event(Scope.GAME, Action.NOTHING, 0));
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

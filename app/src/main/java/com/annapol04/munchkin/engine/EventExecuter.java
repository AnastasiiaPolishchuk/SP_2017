package com.annapol04.munchkin.engine;

/**
 * Created by chris_000 on 08.12.2017.
 */

import com.annapol04.munchkin.data.EventRepository;

import javax.inject.Inject;

public class EventExecuter implements EventRepository.NewEventListener {
    private final Game game;
    private final EventRepository repository;

    @Inject
    public EventExecuter(Game game, EventRepository repository) {
        this.game = game;
        this.repository = repository;
        }

public void start() {
        repository.setNewEventListener(this);
        repository.reset();
        }

@Override
public void onNewEvent(Event event) {
        event.execute(game);
        }
        }
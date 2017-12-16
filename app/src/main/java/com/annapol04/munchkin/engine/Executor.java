package com.annapol04.munchkin.engine;

import com.annapol04.munchkin.data.EventRepository;

public class Executor implements EventRepository.NewEventListener {
    private final Game game;
    private final EventRepository repository;

    public Executor(Game game, EventRepository repository) {
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
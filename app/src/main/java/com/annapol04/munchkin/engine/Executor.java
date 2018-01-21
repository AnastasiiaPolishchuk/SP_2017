package com.annapol04.munchkin.engine;

import android.util.Log;

import com.annapol04.munchkin.data.EventRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Executor implements EventRepository.OnNewEventListener {
    private static final String TAG = Executor.class.getSimpleName();
    private Match match;
    private final Game game;
    private final EventRepository repository;
    private MessageBook messageBook;

    @Inject
    public Executor(Match match, Game game, EventRepository repository, MessageBook messageBook) {
        this.match = match;
        this.game = game;
        this.repository = repository;
        this.messageBook = messageBook;

        this.repository.setNewEventListener(this);
    }

    @Override
    public void onNewEvent(Event event) {
        Log.d(TAG, "executing: " + event.toString(messageBook));
        Log.d(TAG, "game: " + game);

        event.execute(match, game);
        match.log(event.getMessage(messageBook));
    }
}
package com.annapol04.munchkin.engine;

import android.util.Log;

import com.annapol04.munchkin.data.EventRepository;

import java.util.Arrays;

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
        Player player = event.getScope() == Scope.GAME ? null : match.getPlayer(event.getScope());

        if (Arrays.equals(repository.getTopHash(), event.getPreviousHash())) {
            Log.d(TAG, "executing: " + event.toString(messageBook, player));

            repository.setTopHash(event.getHash());

            String message = event.getMessage(messageBook, player);

            if (message.length() > 0)
                match.log(message);

            event.execute(match, game);

            Log.d(TAG, "game: " + game);
        } else {
            String msg = "failed to execute: " + event.toString(messageBook, player) + " because of wrong hash value";

            Log.e(TAG, msg);
            throw new IllegalStateException(msg);
        }
    }
}
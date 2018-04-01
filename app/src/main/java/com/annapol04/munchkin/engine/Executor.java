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
    private int errorCount = 0;

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
        boolean anonymized = isAnonymized(player, event.getAction());

        if (Arrays.equals(repository.getTopHash(), event.getPreviousHash())) {
            Log.d(TAG, "executing: " + event.toString(messageBook, player, anonymized));

            repository.setTopHash(event.getHash());

            logMessage(event, player, anonymized);

            try {
                event.execute(match, game);
            } catch (IllegalEngineStateException exception) {
                match.undoLog();

                errorCount++;

                Log.e(TAG, "ERROR: " + exception.getMessage());
            }
        } else {
            String msg = "failed to execute: " + event.toString(messageBook, player, anonymized) + " because of wrong hash value";

            Log.e(TAG, msg);
            throw new IllegalStateException(msg);
        }
    }

    private boolean isAnonymized(Player player, Action action) {
        return !match.isMyself(player)
                && action == Action.DRAW_TREASURECARD;
    }

    private void logMessage(Event event, Player player, boolean anonymized) {
        String message = event.getMessage(messageBook, player, anonymized);

        if (message.length() > 0)
            match.log(message);
    }

    public int getErrorCount() {
        return errorCount;
    }
}
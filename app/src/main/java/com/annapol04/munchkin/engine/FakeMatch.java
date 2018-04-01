package com.annapol04.munchkin.engine;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.data.EventRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Named;

public class FakeMatch extends Match {
    @Inject
    public FakeMatch(Game game, @Named("myself") Player myself, EventRepository eventRepository) {
        super(game, myself, eventRepository);
    }

    @Override
    public void start(int amountOfPlayers) {
        super.start(amountOfPlayers);

        for (int i = 0; i < amountOfPlayers - 1; i++)
            eventRepository.push(new Event(Scope.GAME, Action.JOIN_PLAYER, 0, i));
    }

    @Override
    protected void namingRound() {
        super.namingRound();

        List<String> names = new ArrayList<>(Arrays.asList("Helga", "Cannabiene"));

        List<Event> events = new ArrayList<>((amountOfPlayers - 1) * 2);

        for (int i = 1; i < amountOfPlayers; i++) {
            events.add(new Event(current.getScope(), Action.NAME_PLAYER, R.string.ev_join_player, names.get(i - 1)));
            events.add(new Event(current.getScope(), Action.HAND_OVER_TOKEN, 0, players.get((i + 1) % players.size()).getScope().ordinal()));
        }

        eventRepository.push(events);
    }

    @Override
    public void handOverToken(Scope scope, int playerNr) throws IllegalEngineStateException {
        super.handOverToken(scope, playerNr);

        if (current != myself) {
            if (state == State.HAND_CARDS)
                drawInitialHandcards();
        }
    }
}

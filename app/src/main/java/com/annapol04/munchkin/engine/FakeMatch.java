package com.annapol04.munchkin.engine;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.data.EventRepository;

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

        String[] names = { "Helga", "Cannabiene", "Falco", "Anastasiia", "Christian" };

        for (int i = 0; i < amountOfPlayers - 1; i++) {
            eventRepository.push(
                    new Event(Scope.fromId(i + 2), Action.NAME_PLAYER, R.string.join_player, names[i]),
                    new Event(Scope.fromId(i + 2), Action.FINISH_ROUND, 0)
            );
        }
    }
}

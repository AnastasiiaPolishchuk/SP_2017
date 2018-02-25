package com.annapol04.munchkin.engine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.data.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class Match {
    protected static final int AMOUNT_OF_HAND_CARDS = 2;

    protected Game game;
    protected EventRepository eventRepository;

    protected MutableLiveData<String> log = new MutableLiveData<>();
    protected MutableLiveData<List<Player>> observablePlayers = new MutableLiveData<>();
    protected MutableLiveData<Player> current = new MutableLiveData<>();

    protected List<Player> players = new ArrayList<>();
    protected Player host = null;
    protected final Player myself;

    protected State state = State.JOINING;
    protected int amountOfPlayers = 0;
    protected int playersRenamed = 0;

    protected enum State {
        JOINING,
        NAMING,
        INITIAL,
        STARTED,
    }

    @Inject
    public Match(Game game, @Named("myself") Player myself, EventRepository eventRepository) {
        this.game = game;
        this.myself = myself;
        this.eventRepository = eventRepository;

        observablePlayers.setValue(new ArrayList<>());
        log.setValue("");
    }

    public void log(String message) {
        log.setValue(new StringBuilder()
                .append(log.getValue())
                .append(message)
                .append("\n")
                .toString());
    }

    public LiveData<String> getLog() {
        return log;
    }

    public LiveData<List<Player>> getPlayers() {
        return observablePlayers;
    }

    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public Player getPlayer(int playerNr) {
        return players.get(playerNr);
    }

    public Player getPlayer(Scope scope) {
        return getPlayer(scope.ordinal() - 1);
    }

    public LiveData<Player> getCurrentPlayer() {
        return current;
    }

    public void start(int amountOfPlayers) {
        this.amountOfPlayers = amountOfPlayers;
        playersRenamed = 0;

        observablePlayers.getValue().clear();
        players.clear();
        current.setValue(null);

        game.reset();
        myself.reset();

        log.setValue("");

        state = State.JOINING;

        eventRepository.push(new Event(Scope.GAME, Action.JOIN_PLAYER, 0, myself.getId()));
    }

    private Player evaluateHost() {
        Player highestId = null;

        for (Player player : players) {
            if (highestId == null)
                highestId = player;
            else if (player.getId() > highestId.getId())
                highestId = player;
        }

        return highestId;
    }

    private void specifyPlayerNumbers() {
        myself.setScope(Scope.PLAYER1);

        eventRepository.push(
                new Event(myself.getScope(), Action.ASSIGN_PLAYER_NUMBER, 0, myself.getId()));

        int nr = 2;

        for (Player player : players)
            if (player != myself)
                eventRepository.push(
                        new Event(Scope.fromId(nr++), Action.ASSIGN_PLAYER_NUMBER, 0, player.getId()));
    }

    private <T> void update(MutableLiveData<T> liveData) {
        liveData.setValue(liveData.getValue());
    }

    public void joinPlayer(int randomNumber) {
        players.add(randomNumber == myself.getId() ?
                myself : new Player(randomNumber, game, eventRepository));

        if (players.size() == amountOfPlayers) {
            state = State.NAMING;

            host = evaluateHost();

            if (host == myself) {
                specifyPlayerNumbers();

                namingRound();
            }
        }
    }

    protected void namingRound() {
        eventRepository.push(
                new Event(myself.getScope(), Action.NAME_PLAYER, R.string.ev_join_player, myself.getName()),
                new Event(myself.getScope(), Action.FINISH_ROUND, 0)
        );
    }

    private Player nextPlayer(int playerNr) {
        return players.get(playerNr % players.size());
    }

    private void handoutCards() {
        List<Card> cards = game.getRandomTreasureCards(AMOUNT_OF_HAND_CARDS * amountOfPlayers);

        for (Player player : players) {
            for (int i = 0; i < AMOUNT_OF_HAND_CARDS; i++) {
                Card card = cards.remove(0);

                eventRepository.push(
                        new Event(player.getScope(), Action.DRAW_TREASURECARD, 0, card.getId())
                );
            }
        }
    }

    public void finishRound(int playerNr) {
        current.setValue(nextPlayer(playerNr));

        if (current.getValue() == myself) {
            if (state == State.INITIAL && host == myself) {
                handoutCards();

                state = State.STARTED;
            } else if (state == State.NAMING) {
                namingRound();

                state = State.INITIAL;
            } else
                current.getValue().playRound();
        }
    }

    public void assignPlayerNumber(int playerNr, int randomNumber) {
        boolean allScopesAssigned = true;

        for (Player player : players) {
            if (player.getId() == randomNumber)
                player.setScope(Scope.fromId(playerNr));
            if (player.getScope() == null)
                allScopesAssigned = false;
        }

        if (allScopesAssigned)
            players = players.stream()
                    .sorted(Comparator.comparingInt(p -> p.getScope().ordinal()))
                    .collect(Collectors.toList());
    }

    public void namePlayer(int playerNr, String name) {
        players.get(playerNr).rename(name);

        if (++playersRenamed == amountOfPlayers) {
            observablePlayers.setValue(players);

            state = State.INITIAL;
        }
    }
}
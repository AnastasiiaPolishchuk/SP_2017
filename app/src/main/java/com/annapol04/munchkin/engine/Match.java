package com.annapol04.munchkin.engine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.annapol04.munchkin.data.EventRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class Match {
    private MutableLiveData<List<Player>> players = new MutableLiveData<>();
    private List<Integer> playerOrder;
    private Player myself;
    private Scope myScope;
    private MutableLiveData<Player> current = new MutableLiveData<>();
    private EventRepository eventRepository;
    private State state = State.JOINING;
    private int amountOfPlayers;

    private enum State {
        JOINING,
        INITIAL,
        STARTED,
    }

    @Inject
    public Match(@Named("myself") Player myself, EventRepository eventRepository) {
        this.myself = myself;
        this.current.setValue(myself);
        this.eventRepository = eventRepository;
    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

    public LiveData<Player> getCurrentPlayer() {
        return current;
    }

    public void start(int amountOfPlayers) {
        this.amountOfPlayers = amountOfPlayers;
        players.setValue(new ArrayList<>(amountOfPlayers));
        playerOrder = new ArrayList<>(amountOfPlayers);
        state = State.JOINING;

        eventRepository.push(new Event(Scope.GAME, Action.JOIN_PLAYER, 0, (int)myself.getId()));
    }

    private Player evaluateInitialPlayer() {
        Player highestId = null;

        for (Player player : players.getValue()) {
            if (highestId == null)
                highestId = player;
            else if (player.getId() > highestId.getId())
                highestId = player;
        }

        return highestId;
    }

    private void specifyPlayerNumbers() {
        myScope = Scope.PLAYER1;

        eventRepository.push(
                new Event(Scope.PLAYER1, Action.ASSIGN_PLAYER_NUMBER, 0, (int)myself.getId()));

        int nr = 2;

        for (Player player : players.getValue())
            if (player != myself)
                eventRepository.push(
                        new Event(Scope.fromId(nr++), Action.ASSIGN_PLAYER_NUMBER, 0, (int)player.getId()));
    }

    private <T> void update(MutableLiveData<T> liveData) {
        liveData.setValue(liveData.getValue());
    }

    public void joinPlayer(long randomNumber) {
        players.getValue().add(new Player(randomNumber));
        update(players);

        if (players.getValue().size() == amountOfPlayers) {
            current.setValue(evaluateInitialPlayer());

            if (current.getValue() == myself) {
                specifyPlayerNumbers();

                initalRound();
            }
        }
    }

    private void initalRound() {
        eventRepository.push(
                new Event(myScope, Action.NAME_PLAYER, 0, myself.getName()),
                new Event(myScope, Action.FINISH_ROUND, 0)
        );
    }

    private Player nextPlayer(int playerNr) {
        int next = (playerNr + 1) % players.getValue().size();

        return playerWithNumber(next);
    }

    public void finishRound(int playerNr) {
        current.setValue(nextPlayer(playerNr));
    }

    public void assignPlayerNumber(long randomNumber, int playerNr) {
        List<Player> players = this.players.getValue();

        for (int i = 0; i < playerOrder.size(); i++)
            if (players.get(i).getId() == randomNumber) {
                playerOrder.set(i, playerNr - 1);

                if (players.get(i) == myself)
                    myScope = Scope.fromId(playerNr);
            }
    }

    private Player playerWithNumber(int nr) {
        return players.getValue().get(playerOrder.get(nr - 1));
    }

    public void namePlayer(int playerNr, String name) {
        playerWithNumber(playerNr).rename(name);
    }
}
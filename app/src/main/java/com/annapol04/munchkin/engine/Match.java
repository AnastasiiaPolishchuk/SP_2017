package com.annapol04.munchkin.engine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.data.EventRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class Match {
    protected static final int AMOUNT_OF_HAND_CARDS = 2;

    protected Game game;
    protected MutableLiveData<String> log = new MutableLiveData<>();
    protected MutableLiveData<List<Player>> players = new MutableLiveData<>();
    protected List<Integer> playerOrder;
    protected Player myself;
    protected Scope myScope;
    protected MutableLiveData<Player> current = new MutableLiveData<>();
    protected EventRepository eventRepository;
    protected State state = State.JOINING;
    protected int amountOfPlayers;
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
        this.current.setValue(myself);
        this.eventRepository = eventRepository;

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
        return players;
    }

    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public Player getPlayer(int playerNr) {
        return playerWithNumber(playerNr);
    }

    public Player getPlayer(Scope scope) {
        return getPlayer(scope.ordinal());
    }

    public LiveData<Player> getCurrentPlayer() {
        return current;
    }

    public void start(int amountOfPlayers) {
        this.amountOfPlayers = amountOfPlayers;
        playersRenamed = 0;
        players.setValue(new ArrayList<>(amountOfPlayers));
        playerOrder = new ArrayList<>(amountOfPlayers);

        for (int i = 0; i < amountOfPlayers; i++)
            playerOrder.add(0);

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
        Log.e(Match.class.getSimpleName(), Integer.toString((int)randomNumber));
        players.getValue().add(randomNumber == myself.getId() ? myself : new Player(randomNumber, game, eventRepository));
        update(players);

        if (players.getValue().size() == amountOfPlayers) {
            state = State.NAMING;

            current.setValue(evaluateInitialPlayer());

            if (current.getValue().getId() == myself.getId()) {
                specifyPlayerNumbers();

                namingRound();
            }
        }
    }

    protected void namingRound() {
        eventRepository.push(
                new Event(myScope, Action.NAME_PLAYER, R.string.join_player, myself.getName()),
                new Event(myScope, Action.FINISH_ROUND, 0)
        );
    }

    private Player nextPlayer(int playerNr) {
        int next = playerNr % players.getValue().size() + 1;

        return playerWithNumber(next);
    }

    private void handoutCards() {
        List<Card> cards = game.getRandomTreasureCards(AMOUNT_OF_HAND_CARDS * amountOfPlayers);

        for (Player player : players.getValue()) {
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
            if (state == State.INITIAL) {
                handoutCards();

                state = State.STARTED;
            } else
                current.getValue().playRound();
        }
    }

    public void assignPlayerNumber(int playerNr, long randomNumber) {
        List<Player> players = this.players.getValue();

        for (int i = 0; i < playerOrder.size(); i++)
            if (players.get(i).getId() == randomNumber) {
                playerOrder.set(i, playerNr - 1);
                players.get(i).setScope(Scope.fromId(playerNr));

                if (players.get(i) == myself)
                    myScope = Scope.fromId(playerNr);
            }
    }

    private Player playerWithNumber(int nr) {
        return players.getValue().get(playerOrder.get(nr - 1));
    }

    public void namePlayer(int playerNr, String name) {
        playerWithNumber(playerNr).rename(name);

        if (++playersRenamed == amountOfPlayers)
            state = State.INITIAL;
    }
}
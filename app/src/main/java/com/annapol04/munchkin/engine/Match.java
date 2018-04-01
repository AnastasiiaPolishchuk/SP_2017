package com.annapol04.munchkin.engine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.StringRes;
import android.util.Log;
import android.util.Pair;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.data.EventRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static com.annapol04.munchkin.engine.EngineTest.test;

@Singleton
public class Match {
    protected static final int AMOUNT_OF_HAND_CARDS = 2;
    protected static final int MAX_AMOUNT_OF_HAND_CARDS = 4;

    protected static final String TAG = Match.class.getSimpleName();

    protected Game game;
    protected EventRepository eventRepository;

    protected MutableLiveData<String> log = new MutableLiveData<>();
    protected MutableLiveData<List<Player>> observablePlayers = new MutableLiveData<>();
    protected MutableLiveData<Player> currentPlayer = new MutableLiveData<>();

    protected List<Player> players = new ArrayList<>();
    protected Player current = null;
    protected Player first = null;
    protected final Player myself;
    protected TurnPhase turnPhase;

    protected State state = State.JOINING;
    protected int amountOfPlayers = 0;
    protected int playersRenamed = 0;

    protected boolean named = false;
    protected boolean handCardsDrawn = false;

    protected enum State {
        JOINING,
        NAMING,
        HAND_CARDS,
        STARTED,
    }

    @Inject
    public Match(Game game, @Named("myself") Player myself, EventRepository eventRepository) {
        this.game = game;
        this.myself = myself;
        this.eventRepository = eventRepository;

        observablePlayers.setValue(new ArrayList<>());

        reset();
    }

    protected void reset() {
        players.clear();
        currentPlayer.setValue(null);
        playersRenamed = 0;
        observablePlayers.getValue().clear();
        log.setValue("");

        game.reset();
        myself.reset();

        turnPhase = TurnPhase.IDLE;
        state = State.JOINING;

        named = false;
        handCardsDrawn = false;
    }

    public boolean isMyself(Player player) {
        return player == myself;
    }

    public void log(String message) {
        log.setValue(new StringBuilder()
                .append(log.getValue())
                .append(message)
                .append("\n")
                .toString());
    }

    public void undoLog() {
        log.setValue(log.getValue().replaceAll("\n[^\n]+\n+$", "\n"));
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
        return players.get(playerNr - 1);
    }

    public Player getPlayer(Scope scope) {
        return getPlayer(scope.ordinal());
    }

    public LiveData<Player> getCurrentPlayer() {
        return currentPlayer;
    }

    public void start(int amountOfPlayers) {
        reset();

        this.amountOfPlayers = amountOfPlayers;

        eventRepository.push(new Event(Scope.GAME, Action.JOIN_PLAYER, 0, myself.getId()));
    }

    protected Player evaluateHost() {
        Player highestId = null;

        for (Player player : players) {
            if (highestId == null)
                highestId = player;
            else if (player.getId() > highestId.getId())
                highestId = player;
        }

        return highestId;
    }

    protected void specifyPlayerNumbers() {
        myself.setScope(Scope.PLAYER1);

        eventRepository.push(
                new Event(myself.getScope(), Action.ASSIGN_PLAYER_NUMBER, 0, myself.getId()));

        int nr = 2;

        for (Player player : players)
            if (player != myself)
                eventRepository.push(
                        new Event(Scope.fromId(nr++), Action.ASSIGN_PLAYER_NUMBER, 0, player.getId()));
    }

    protected <T> void update(MutableLiveData<T> liveData) {
        liveData.setValue(liveData.getValue());
    }

    public void joinPlayer(int randomNumber) {
        Player player = randomNumber == myself.getId() ?
                myself : new Player(randomNumber, game, eventRepository);

        player.allowToDrawTreasureCards(AMOUNT_OF_HAND_CARDS);

        players.add(player);

        if (players.size() == amountOfPlayers) {
            state = State.NAMING;

            current = evaluateHost();
            currentPlayer.setValue(current);

            first = current;

            if (current == myself) {
                specifyPlayerNumbers();

                named = true;

                namingRound();
            }
        }
    }

    protected void namingRound() {
        eventRepository.push(
                new Event(current.getScope(), Action.NAME_PLAYER, R.string.ev_join_player, myself.getName()),
                new Event(current.getScope(), Action.FINISH_ROUND, 0)
        );
    }

    protected Player nextPlayer(int playerNr) {
        return players.get(playerNr % players.size());
    }

    protected void drawInitialHandcards() {
        game.getRandomTreasureCards(AMOUNT_OF_HAND_CARDS)
                .forEach(card -> {
                    eventRepository.push(
                            new Event(current.getScope(), Action.DRAW_TREASURECARD, 0, card.getId())
                    );           });

        eventRepository.push(
                new Event(current.getScope(), Action.FINISH_ROUND, 0)
        );
    }

    public void finishRound(int playerNr) throws IllegalEngineStateException {
        current = nextPlayer(playerNr);
        currentPlayer.setValue(current);

        switch (state) {
            default:
                throw new IllegalEngineStateException("Illegal state");
            case NAMING:
                if (!named && current == myself) {
                    named = true;

                    namingRound();
                }
                if (named && current == first)
                    state = State.HAND_CARDS;
                break;
            case HAND_CARDS:
                if (!handCardsDrawn && current == myself) {
                    handCardsDrawn = true;

                    Log.e(TAG, "" + handCardsDrawn);
                    drawInitialHandcards();
                }
                if (handCardsDrawn && current == first)
                    state = State.STARTED;
                break;
            case STARTED:
                playRound();
                break;
        }
    }

    protected void emitFinishRound(Scope scope) {
        eventRepository.push(
                new Event(current.getScope(), Action.FINISH_ROUND, 0)
        );
    }

    protected void playRound() throws IllegalEngineStateException {
        if (turnPhase == TurnPhase.IDLE)
            emitEnterTurnPhase(current.getScope(), TurnPhase.EQUIPMENT);
        else
            throw new IllegalEngineStateException("We can start the turn only from \"idle\" turn phase");
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
        players.get(playerNr - 1).rename(name);

        if (++playersRenamed == amountOfPlayers)
            observablePlayers.setValue(players);
    }

    protected void testHost(Player player) throws IllegalEngineStateException {
        if (player != current)
            throw new IllegalEngineStateException("can not play a card in another player's round");
    }


    /****************************************************************************************
     *                      ==== Action implementations ====
     ****************************************************************************************/

    public void emitMessage(Scope scope, @StringRes int message, int integer) {
        eventRepository.push(
                new Event(scope, Action.MESSAGE, message, integer)
        );
    }

    public void emitDrawDoorCard(Scope scope) {
        Card card = game.getRandomDoorCards(1).get(0);

        emitEnterTurnPhase(scope, TurnPhase.KICK_OPEN_THE_DOOR);

        eventRepository.push(
            new Event(scope, Action.DRAW_DOORCARD, R.string.ev_draw_card, card.getId())
        );
    }

    public void drawDoorCard(Scope scope, Card card) throws IllegalEngineStateException {
        testHost(getPlayer(scope));

        test(turnPhase == TurnPhase.KICK_OPEN_THE_DOOR,
                "it's not allowed to draw door cards in \"" + turnPhase + "\" phase!");
        test(game.getDeskCards().getValue().size() == 0,
                "you can not draw a door card from an empty deck!");

        getPlayer(scope).drawDoorCard(card);
    }


    public void emitDrawTreasureCard(Scope scope) {
        Card card = game.getRandomTreasureCards(1).get(0);

        eventRepository.push(
            new Event(scope, Action.DRAW_TREASURECARD, R.string.ev_draw_card, card.getId())
        );
    }

    public void drawTreasureCard(Scope scope, Card card) throws IllegalEngineStateException {
        testHost(getPlayer(scope));

        test(turnPhase == TurnPhase.KICK_OPEN_THE_DOOR_AND_DRAW || turnPhase == TurnPhase.IDLE,
                "it's not allowed to draw treasure cards in \"" + turnPhase + "\" phase!");
        test(game.getDeskCards().getValue().size() == 0,
                "you can not draw a treasure card from an empty deck!");

        getPlayer(scope).drawTreasureCard(card);

        if (turnPhase == TurnPhase.KICK_OPEN_THE_DOOR_AND_DRAW
                && scope == myself.getScope()
                && !myself.isAllowedToDrawTreasureCard())
            emitEnterTurnPhase(scope, TurnPhase.CHARITY);
    }


    public void emitFightMonster(Scope scope) {
        emitEnterTurnPhase(scope, TurnPhase.KICK_OPEN_THE_DOOR_AND_FIGHT);

        eventRepository.push(
                new Event(scope, Action.FIGHT_MONSTER, R.string.ev_fight_monster)
        );
    }

    public void fightMonster(Scope scope) throws IllegalEngineStateException {
        testHost(getPlayer(scope));

        test(turnPhase == TurnPhase.KICK_OPEN_THE_DOOR_AND_FIGHT,
                "monsters can not fought in \"" + turnPhase + "\" phase!");

        Pair<Monster, Integer> result = getPlayer(scope).fightMonster();

        final int cardsToDraw = 1;

        if (scope == myself.getScope()) {
            emitMessage(scope, R.string.player_killed_monster, result.first.getId());
            emitMessage(scope, R.string.player_gets_level, result.second);

            emitEnterTurnPhase(scope, TurnPhase.KICK_OPEN_THE_DOOR_AND_DRAW);
            emitMessage(scope, R.string.player_draws_treasure_cards, cardsToDraw);
        }

        getPlayer(scope).allowToDrawTreasureCards(cardsToDraw);
    }


    public void emitRunAway(Scope scope) {
        eventRepository.push(
                new Event(scope, Action.RUN_AWAY, R.string.ev_run_away)
        );
    }

    public void runAway(Scope scope) throws IllegalEngineStateException {
        testHost(getPlayer(scope));

        test(turnPhase == TurnPhase.KICK_OPEN_THE_DOOR_AND_FIGHT,
                "can not run away from monster in \"" + turnPhase + "\" phase!");

        getPlayer(scope).runAway();
    }


    public void emitPlayCard(Scope scope, Card card) {
        eventRepository.push(
                new Event(scope, Action.PLAY_CARD, R.string.ev_play_card, card.getId())
        );
    }


    public void playCard(Scope scope, Card card) throws IllegalEngineStateException {
        testHost(getPlayer(scope));

        test(turnPhase == TurnPhase.EQUIPMENT,
                "can not equip items in \"" + turnPhase + "\" phase!");

        getPlayer(scope).playCard(card);
    }


    protected void emitEnterTurnPhase(Scope scope, TurnPhase phase) {
        eventRepository.push(
                new Event(scope, Action.ENTER_TURN_PHASE, R.string.ev_enter_turn_phase, phase.ordinal())
        );
    }

    public void enterTurnPhase(Scope scope, TurnPhase phase) throws IllegalEngineStateException {
        switch (turnPhase) {
            case IDLE:
                test(phase == TurnPhase.EQUIPMENT,
                        "it is not allowed to enter phase \"" + phase + "\" from \"" + turnPhase + "\"");

                getPlayer(scope).allowToDrawDoorCards(1);
                break;
            case EQUIPMENT:
                test(phase == TurnPhase.KICK_OPEN_THE_DOOR,
                        "it is not allowed to enter phase \"" + phase + "\" from \"" + turnPhase + "\"");
                break;
            case KICK_OPEN_THE_DOOR:
                test(phase == TurnPhase.KICK_OPEN_THE_DOOR_AND_FIGHT,
                        "it is not allowed to enter phase \"" + phase + "\" from \"" + turnPhase + "\"");
                break;
            case KICK_OPEN_THE_DOOR_AND_FIGHT:
                test(phase == TurnPhase.KICK_OPEN_THE_DOOR_AND_DRAW,
                        "it is not allowed to enter phase \"" + phase + "\" from \"" + turnPhase + "\"");
                break;
            case KICK_OPEN_THE_DOOR_AND_DRAW:
            case LOOK_FOR_TROUBLE:
            case LOOT_THE_ROOM:
                test(phase == TurnPhase.CHARITY,
                        "it is not allowed to enter phase \"" + phase + "\" from \"" + turnPhase + "\"");

                if (scope == myself.getScope()) {
                    emitEnterTurnPhase(scope, TurnPhase.IDLE);
                    emitFinishRound(scope);
                }
                break;
            case CHARITY:
                break;
        }

        turnPhase = phase;
    }
}
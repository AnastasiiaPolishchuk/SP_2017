package com.annapol04.munchkin.gui;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.annapol04.munchkin.data.EventRepository;
import com.annapol04.munchkin.engine.Action;
import com.annapol04.munchkin.engine.Card;
import com.annapol04.munchkin.engine.Event;
import com.annapol04.munchkin.engine.Executor;
import com.annapol04.munchkin.engine.Game;
import com.annapol04.munchkin.engine.Match;
import com.annapol04.munchkin.engine.Player;
import com.annapol04.munchkin.network.GooglePlayClient;
import com.annapol04.munchkin.network.PlayClient;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PlayDeskViewModel extends AndroidViewModel implements PlayClient.OnMatchStateChangedListener {
    private static final String TAG = PlayDeskViewModel.class.getName();

    private MutableLiveData<String> playerName = new MutableLiveData<>();

    private MutableLiveData<Boolean> isStarted = new MutableLiveData<>();
    private boolean isStartingAlready = false;
    private PlayClient client;
    private Game game;
    private Match match;
    private EventRepository eventRepository;
    private Executor executor;

    @Inject
    public PlayDeskViewModel(@NonNull Application application, PlayClient client, Game game, Match match, EventRepository eventRepository, Executor executor) {
        super(application);

        this.client = client;
        this.game = game;
        this.match = match;
        this.eventRepository = eventRepository;
        this.executor = executor;

        client.setMatchStateChangedListener(this);
        isStarted.setValue(false);

        Player p = game.getCurrentPlayer();

        playerName.setValue(p.getName());
    }

    @Override
    protected void onCleared() {
        client.setMatchStateChangedListener(null);
        super.onCleared();
    }

    public void processActivityResults(int requestCode, int resultCode, Intent data) {
        client.processActivityResults(requestCode, resultCode, data);
    }

    public Player getPlayer(int position){
        return ( game.getPlayer(position));
    }
    public LiveData<String> getPlayerName() {
        return playerName;
    }

    public LiveData<Integer> getPlayerLevel() {
        return game.getCurrentPlayer().getLevel();
    }

    public LiveData<List<Card>> getPlayerHand() {
        return game.getCurrentPlayer().getHandCards();
    }

    public LiveData<List<Card>> getPlayedCards() {
        return game.getCurrentPlayer().getPlayedCards();
    }

    public LiveData<List<Card>> getPlayedCards(int playerID) {
        return game.getPlayer(playerID).getPlayedCards();
    }

    public LiveData<List<Card>> getDeskCards() {
        return game.getDeskCards();
    }

    public LiveData<Boolean> getGameStarted() {
        return isStarted;
    }

    public LiveData<Boolean> getGameFinished() { return game.getGameFinished(); }

    public LiveData<List<Player>> getPlayers(){
        return game.getPlayers();
    }

    public void quitGame() {
    //    throw new UnsupportedOperationException("Not implemented");
    }

    public void resume(Activity fromActivity) {
        client.setActivity(fromActivity);

        if (!client.isLoggedIn())
            client.login();
        else
            startGameIfPossible();
    }

    public void pause() {
        client.setActivity(null);
    }

    private void startGameIfPossible() {
        if (!getGameStarted().getValue() && !isStartingAlready) {
            isStartingAlready = true;

            client.startQuickGame();
        }
    }

    @Override
    public void onMatchStateChanged(GooglePlayClient.MatchState state) {
        switch (state) {
            case LOGGED_IN:
                startGameIfPossible();
                break;
            case STARTED:
                isStarted.setValue(true);
                match.start(client.getAmountOfPlayers());
                isStartingAlready = false;
                break;
            case ABORTED:
                isStarted.setValue(false);
                isStartingAlready = false;
                break;
            case DISCONNECTED:
                isStarted.setValue(false);
                isStartingAlready = false;
                break;
        }
    }

    public void drawDoorCard() {
        Card c = game.getRandomDoorCard();
        Log.d(TAG, "drawing door card with id=" + c.getId() + " name=" + c.getName());
        eventRepository.push(
                new Event(game.getCurrentPlayer().getScope(), Action.DRAW_DOORCARD, 0, c.getId()));
    }

    public void drawTreasureCard() {
        eventRepository.push(
                new Event(game.getCurrentPlayer().getScope(), Action.DRAW_TREASURECARD, 0, game.getRandomTreasureCard().getId()));
    }

    public void playCard(Card card) {
        eventRepository.push(
                new Event(game.getCurrentPlayer().getScope(), Action.PLAY_CARD, 0, card.getId()));
    }

    public void pickupCard(Card card) {
        eventRepository.push(
                new Event(game.getCurrentPlayer().getScope(), Action.PICKUP_CARD, 0, card.getId()));
    }

    public void runAwayFromMonster() {
        eventRepository.push(
                new Event(game.getCurrentPlayer().getScope(), Action.RUN_AWAY, 0));
    }

    public void fightMonster() {
        eventRepository.push(
                new Event(game.getCurrentPlayer().getScope(), Action.FIGHT_MONSTER, 0));
    }

    public void moveToPlayDesk(Card selected) {
        //hier wird die Karte aus der Hand auf PlayDesk verschoben
    }

    // --------------------------------- Für GUI TEST ----------------------------------------------------
    public void setTestPlayers(){
        game.addTestPlayer();
    }

    public String getPlayerName(int playerID) {
        return game.getPlayer(playerID).getName();
    }

    public LiveData<Integer> getPlayerLevel(int playerID) {
        return game.getPlayer(playerID).getLevel();
    }

    public LiveData<List<Card>> getPlayerHand(int playerID) {
        return game.getPlayer(playerID).getHandCards();
    }


}

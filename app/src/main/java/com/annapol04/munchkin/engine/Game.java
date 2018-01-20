package com.annapol04.munchkin.engine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Named;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Game {
    private static final String TAG = Game.class.getSimpleName();

    private MutableLiveData<List<Player>> players = new MutableLiveData<>();
    private MutableLiveData<List<Card>> deskCards = new MutableLiveData<>();
    private MutableLiveData<Boolean> isGameFinished = new MutableLiveData<>();
    private List<Card> doorDeck;
    private List<Card> treasureDeck;
    private Player currentPlayer;
    private Match matchManager;

    @Inject
    public Game(@Named("myself") Player player,
                @Named("doorDeck") List<Card> doorDeck,
                @Named("treasureDeck") List<Card> treasureDeck,
                Match matchManager) {
        this.players.setValue(new ArrayList<>(1));
        this.players.getValue().add(player);

        this.doorDeck = doorDeck;
        this.treasureDeck = treasureDeck;
        this.deskCards.setValue(new ArrayList<>());
        this.isGameFinished.setValue(false);
        this.matchManager = matchManager;

        currentPlayer = player;
        player.setScope(Scope.PLAYER1);

        drawTreasureCard(getRandomTreasureCard());
        drawTreasureCard(getRandomTreasureCard());
    }

    public Match matchManager() { return matchManager; }

    private <T> void update(MutableLiveData<T> liveData) {
        liveData.setValue(liveData.getValue());
    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

    public LiveData<List<Card>> getDeskCards() {
        return deskCards;
    }

    public LiveData<Boolean> getGameFinished() { return isGameFinished; }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    public void join(Player player) {
        players.getValue().add(player);
        update(players);
    }

    public void leave(Player player) {
        players.getValue().remove(player);
        update(players);
    }

    public void drawDoorCard(Card card) {
        deskCards.getValue().add(card);
        update(deskCards);
        doorDeck.remove(card);
    }

    public void drawTreasureCard(Card card) {
        currentPlayer.pickupCard(card);
        treasureDeck.remove(card);
    }

    public void pickupCard(Card card) {
        currentPlayer.takePlayedCard(card);
        currentPlayer.pickupCard(card);
    }

    public void playCard(Card card) {
        currentPlayer.playCard(card);
    }

    public Card getRandomTreasureCard() {
        Random rnd = new Random();
        int i = rnd.nextInt(treasureDeck.size());
        return treasureDeck.get(i);
    }

    public Card getRandomDoorCard() {
        Random rnd = new Random();
        int i = rnd.nextInt(doorDeck.size());
        return doorDeck.get(i);
    }

    public void runAwayFromMonster() {
        deskCards.getValue().remove(deskCards.getValue().get(0));
        update(deskCards);
    }

    public void fightMonster() {
        currentPlayer.levelUp();
        if ((int)currentPlayer.getLevel().getValue() >= 3)
            isGameFinished.setValue(true);

        deskCards.getValue().remove(deskCards.getValue().get(0));
        update(deskCards);
    }

    public Player getPlayer(int position) {
        return players.getValue().get(position);
    }


    // ----------------------------------------
    public void addTestPlayer(){
        List<Player> list = players.getValue();
        list.add(new Player("Marvin"));
        list.add(new Player(("Helga")));
        players.setValue(list);
    }
}

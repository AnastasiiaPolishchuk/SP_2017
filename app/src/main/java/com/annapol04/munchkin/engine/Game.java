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
    private MutableLiveData<List<Player>> players = new MutableLiveData<>();
    private MutableLiveData<List<Card>> deskCards = new MutableLiveData<>();
    private List<Card> doorDeck;
    private List<Card> treasureDeck;
    private Player currentPlayer;

    @Inject
    public Game(@Named("myself") Player player, @Named("doorDeck") List<Card> doorDeck, @Named("treasureDeck") List<Card> treasureDeck) {
        this.players.setValue(new ArrayList<>(1));
        this.players.getValue().add(player);
        this.doorDeck = doorDeck;
        this.treasureDeck = treasureDeck;
        this.deskCards.setValue(new ArrayList<>());

        currentPlayer = player;

        Card c = getRandomDoorCard();
        drawDoorCard(c);
        pickupCard(c);
        c = getRandomDoorCard();
        drawDoorCard(c);
        pickupCard(c);
        c = getRandomDoorCard();
        drawDoorCard(c);
        pickupCard(c);
        c = getRandomDoorCard();
        drawDoorCard(c);
        pickupCard(c);
        drawTreasureCard(getRandomTreasureCard());
        drawTreasureCard(getRandomTreasureCard());
        drawTreasureCard(getRandomTreasureCard());
        drawTreasureCard(getRandomTreasureCard());
    }

    private <T> void update(MutableLiveData<T> liveData) {
        liveData.setValue(liveData.getValue());
    }

    public LiveData<List<Player>> getPlayers() {
        return players;
    }

    public LiveData<List<Card>> getDeskCards() {
        return deskCards;
    }

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
        deskCards.getValue().remove(card);
        update(deskCards);
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

    public void moveFromDoorDeckToDesk(){
       deskCards.add(deck.doorDeck.poll());
       deck.moveFromActiveToPassiveDeck(deskCards.get(0));

    }
}

package com.annapol04.munchkin.engine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private MutableLiveData<Integer> level = new MutableLiveData<>();
    private MutableLiveData<List<Card>> handCards = new MutableLiveData<>();
    private MutableLiveData<List<Card>> playedCards = new MutableLiveData<>();
    private Scope scope;

    public Player(String name) {
        this.name = name;
        this.level.setValue(1);
        this.handCards.setValue(new ArrayList<>());
        this.playedCards.setValue(new ArrayList<>());
    }

    public void pickupCard(Card card) {
        handCards.getValue().add(card);
        update(handCards);
    }

    public void playCard(Card card) {
        handCards.getValue().remove(card);
        update(handCards);
        playedCards.getValue().add(card);
        update(playedCards);
    }

    private <T> void update(MutableLiveData<T> liveData) {
        liveData.setValue(liveData.getValue());
    }

    public String getName() {
        return name;
    }

    public LiveData<Integer> getLevel() {
        return level;
    }

    public LiveData<List<Card>> getHandCards() {
        return handCards;
    }

    public LiveData<List<Card>> getPlayedCards() {
        return playedCards;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }
}

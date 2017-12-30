package com.annapol04.munchkin.engine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public enum PlayerRace{
        HUMAN,
        ELF,
        DWARF; //Zwerg
    }

    private String name;
    private MutableLiveData<Integer> level = new MutableLiveData<>();
    private MutableLiveData<Integer> fightLevel = new MutableLiveData<>();
    private MutableLiveData<PlayerRace> race = new MutableLiveData<>();
    private MutableLiveData<Integer> runAway = new MutableLiveData<>();
    private MutableLiveData<Integer> bonus = new MutableLiveData<>();

    private MutableLiveData<List<Card>> handCards = new MutableLiveData<>();
    private MutableLiveData<List<Card>> playedCards = new MutableLiveData<>();
    private Scope scope;

    public Player(String name) {
        this.name = name;
        this.level.setValue(1);
        this.fightLevel.setValue(1);
        this.race.setValue(PlayerRace.HUMAN);
        this.runAway.setValue(0);
        this.handCards.setValue(new ArrayList<>());
        this.playedCards.setValue(new ArrayList<>());
        this.bonus.setValue(0);
    }

    //public Player(String name, int level, int fightLevel, PlayerRace race, int runAway, ArrayList<Card> hand, ArrayList<Card> table, int bonus)
    // TODO: Should there be an extra constructor for defining a special player, where you can explicitly say, which values ha has?

//    public Player(String name) {
//        this.name = name;
//        this.level.setValue(1);
//        this.handCards.setValue(new ArrayList<>());
//        this.playedCards.setValue(new ArrayList<>());
//    }

    public void pickupCard(Card card) {
        handCards.getValue().add(card);
        update(handCards);
    }

    public void takePlayedCard(Card card) {
        playedCards.getValue().remove(card);
        update(playedCards);
    }

    public void playCard(Card card) {
        handCards.getValue().remove(card);
        update(handCards);
        playedCards.getValue().add(card);
        update(playedCards);
    }

    public void levelUp() {
        level.setValue(level.getValue() + 1);
        update(level);
    }

    public void reset() {
        level.setValue(1);
        update(level);
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

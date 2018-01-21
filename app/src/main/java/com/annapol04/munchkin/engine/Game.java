package com.annapol04.munchkin.engine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.annapol04.munchkin.data.EventRepository;
import com.annapol04.munchkin.data.EventRepository_Factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Named;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Game {
    private EventRepository eventRepository;

    private enum State {
        HAND_OUT_CARDS
    }

    private static final String TAG = Game.class.getSimpleName();

    private MutableLiveData<List<Player>> players = new MutableLiveData<>();
    private MutableLiveData<List<Card>> deskCards = new MutableLiveData<>();
    private MutableLiveData<Boolean> isGameFinished = new MutableLiveData<>();
    private List<Card> doorDeck;
    private List<Card> treasureDeck;
    private State state = State.HAND_OUT_CARDS;
    private Random randomDoor = new Random();
    private Random randomTreasure = new Random();

    @Inject
    public Game(@Named("doorDeck") List<Card> doorDeck,
                @Named("treasureDeck") List<Card> treasureDeck,
                EventRepository eventRepository) {
        this.eventRepository = eventRepository;

        this.doorDeck = doorDeck;
        this.treasureDeck = treasureDeck;
        this.deskCards.setValue(new ArrayList<>());
        this.isGameFinished.setValue(false);
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

    public LiveData<Boolean> getGameFinished() { return isGameFinished; }

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
        treasureDeck.remove(card);
    }

    private <T extends Comparable<T>> boolean hasElement(List<T> list, T element) {
        return list.stream().anyMatch(e -> e.compareTo(element) == 0);
    }

    public List<Card> getRandomDoorCards(int amount) {
        if (amount > doorDeck.size())
            throw new IllegalArgumentException("Can not get " + amount + " door cards with deck size: " + doorDeck.size());

        List<Card> cards = new ArrayList<>(amount);
        List<Integer> indices = new ArrayList<>(amount);

        for (int i = 0; i < amount; i++) {
            Integer index;
            do {
                index = randomDoor.nextInt(doorDeck.size());
            } while (hasElement(indices, index));

            indices.add(index);
            cards.add(doorDeck.get(index));
        }

        return cards;
    }

    public void pickupCard(Card card) {
        /*   Player current = match.getCurrentPlayer().getValue();

        current.takePlayedCard(card);
        current.pickupCard(card);*/
    }

    public void playCard(Card card) {
       // match.getCurrentPlayer().getValue().playCard(card);
    }

    public List<Card> getRandomTreasureCards(int amount) {
        if (amount > treasureDeck.size())
            throw new IllegalArgumentException("Can not get " + amount + " treasure cards with deck size: " + treasureDeck.size());

        List<Card> cards = new ArrayList<>(amount);
        List<Integer> indices = new ArrayList<>(amount);

        for (int i = 0; i < amount; i++) {
            Integer index;
            do {
                index = randomTreasure.nextInt(treasureDeck.size());
            } while (hasElement(indices, index));

            indices.add(index);
            cards.add(treasureDeck.get(index));
        }

        return cards;
    }

    public void runAwayFromMonster() {
        deskCards.getValue().remove(deskCards.getValue().get(0));
        update(deskCards);
    }

    public void fightMonster() {
   /*     match.getCurrentPlayer().getValue().levelUp();
        if ((int)match.getCurrentPlayer().getValue().getLevel().getValue() >= 3)
            isGameFinished.setValue(true);

        deskCards.getValue().remove(deskCards.getValue().get(0));
        update(deskCards);*/
    }

    @Override
    public String toString() {
        return "";
    }

}

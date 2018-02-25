package com.annapol04.munchkin.engine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Game {
    private enum State {
        HAND_OUT_CARDS
    }

    private static final String TAG = Game.class.getSimpleName();

    private MutableLiveData<List<Card>> deskCards = new MutableLiveData<>();
    private MutableLiveData<Boolean> isGameFinished = new MutableLiveData<>();

    private List<Card> doorDeck = new ArrayList<>();
    private List<Card> treasureDeck = new ArrayList<>();
    private State state = State.HAND_OUT_CARDS;
    private Random randomDoor = new Random();
    private Random randomTreasure = new Random();

    @Inject
    public Game() {
        deskCards.setValue(new ArrayList<>());
        isGameFinished.setValue(false);
    }

    public void reset() {
        deskCards.getValue().clear();
        isGameFinished.setValue(false);

        doorDeck.clear();
        treasureDeck.clear();

        Field[] doorCardFields = DoorCards.class.getFields();
        Field[] treasureCardFields = TreasureCards.class.getFields();

        try {
            for (Field field : doorCardFields)
                if (field.getType() == Card.class)
                    doorDeck.add((Card) field.get(null));

            for (Field field : treasureCardFields)
                if (field.getType() == Card.class)
                    treasureDeck.add((Card) field.get(null));

        } catch (IllegalAccessException e) { /* wont happen... */ }
    }

    private <T> void update(MutableLiveData<T> liveData) {
        liveData.setValue(liveData.getValue());
    }

    public LiveData<List<Card>> getDeskCards() {
        return deskCards;
    }

    public LiveData<Boolean> getGameFinished() { return isGameFinished; }


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

    @Override
    public String toString() {
        return new StringBuilder()
                .append("door cards: ")
                .append(doorDeck.size())
                .append(", treasure cards: ")
                .append(treasureDeck.size())
                .append(", finished: ")
                .append(isGameFinished.getValue())
                .toString();
    }
}

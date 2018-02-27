package com.annapol04.munchkin.engine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.data.EventRepository;

import java.util.ArrayList;
import java.util.List;

public class Player extends LiveData<Player> {

    public enum PlayerRace{
        HUMAN,
        ELF,
        DWARF; //Zwerg
    }

    private int id;
    private Game game;
    private EventRepository eventRepository;
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<Integer> level = new MutableLiveData<>();
    private MutableLiveData<Integer> fightLevel = new MutableLiveData<>();
    private MutableLiveData<PlayerRace> race = new MutableLiveData<>();
    private MutableLiveData<Integer> runAway = new MutableLiveData<>();
    private MutableLiveData<Integer> bonus = new MutableLiveData<>();

    private BonusWear headGeer = null;
    private BonusWear armor = null;
    private BonusWear shoes = null;
    private BonusWear firstOneHander = null;
    private BonusWear secondOneHander = null;
    private BonusWear twoHander = null;

    private MutableLiveData<Boolean> canPlayBigEquipment = new MutableLiveData<>();
    private MutableLiveData<Boolean> canPlayHeadgeer = new MutableLiveData<>();
    private MutableLiveData<Boolean> canPlayArmor = new MutableLiveData<>();
    private MutableLiveData<Boolean> canPlayShoes = new MutableLiveData<>();
    private MutableLiveData<Boolean> canPlayOneHander = new MutableLiveData<>();
    private MutableLiveData<Boolean> canPlayTwoHander = new MutableLiveData<>();

    private MutableLiveData<List<Card>> handCards = new MutableLiveData<>();
    private MutableLiveData<List<Card>> playedCards = new MutableLiveData<>();
    private Scope scope;

    public Player(int id, Game game, EventRepository eventRepository) {
        this.id = id;
        this.game = game;
        this.eventRepository = eventRepository;
        name.setValue("");

        reset();
    }

    public void reset() {
        level.setValue(1);
        fightLevel.setValue(1);
        race.setValue(PlayerRace.HUMAN);
        runAway.setValue(0);
        handCards.setValue(new ArrayList<>());
        playedCards.setValue(new ArrayList<>());
        bonus.setValue(0);

        canPlayBigEquipment.setValue(true);
        canPlayHeadgeer.setValue(true);
        canPlayArmor.setValue(true);
        canPlayShoes.setValue(true);
        canPlayOneHander.setValue(true);
        canPlayTwoHander.setValue(true);
    }

    public void rename(String name) {
        this.name.setValue(name);
    }

    public int getId() {
        return id;
    }

    public void takePlayedCard(Card card) {
        playedCards.getValue().remove(card);
        update(playedCards);
    }

    public void levelUp() {
        level.setValue(level.getValue() + 1);
        update(level);
    }

    private <T> void update(MutableLiveData<T> liveData) {
        liveData.setValue(liveData.getValue());
    }

    public String getName() {
        return name.getValue();
    }

    public LiveData<Integer> getLevel() {
        return level;
    }

    public LiveData<Integer> getFightLevel() {
        return fightLevel;
    }

    public LiveData<Boolean> getCanPlayBigEquipment() {
        return canPlayBigEquipment;
    }

    public LiveData<Boolean> getCanPlayHeadgeer() {
        return canPlayHeadgeer;
    }

    public LiveData<Boolean> getCanPlayArmor() {
        return canPlayArmor;
    }

    public LiveData<Boolean> getCanPlayShoes() {
        return canPlayShoes;
    }

    public LiveData<Boolean> getCanPlayOneHander() {
        return canPlayOneHander;
    }

    public LiveData<Boolean> getCanPlayTwoHander() {
        return canPlayTwoHander;
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

    public void emitDrawTreasureCard() {
        eventRepository.push(
                new Event(scope, Action.DRAW_TREASURECARD, R.string.ev_draw_card,
                        game.getRandomTreasureCards(1).get(0).getId())
        );
    }

    public void drawTreasureCard(Card card) {
        game.drawTreasureCard(card);

        handCards.getValue().add(card);
        update(handCards);
    }

    public void emitDrawDoorCard() {
        eventRepository.push(
                new Event(scope, Action.DRAW_DOORCARD, R.string.ev_draw_card,
                    game.getRandomDoorCards(1).get(0).getId())
        );
    }

    public void drawDoorCard(Card card) {
        game.drawDoorCard(card);
    }

    public void emitPlayCard(Card card) {
        eventRepository.push(
                new Event(scope, Action.PLAY_CARD, R.string.ev_play_card, card.getId())
        );
    }

    public void playCard(Card card) {
        if (card instanceof BonusWear) {
            BonusWear wear = (BonusWear) card;

            if (wear.size == BonusWear.Size.BIG) {
                if (!canPlayBigEquipment.getValue())
                    throw new IllegalStateException("Can not equip " + wear + " for player " + this);
                else
                    canPlayBigEquipment.setValue(false);
            }

            switch (wear.blocking) {
                case NOTHING:
                    break;
                case ONEHAND:
                    if (twoHander == null && firstOneHander == null) {
                        firstOneHander = wear;

                        canPlayOneHander.setValue(secondOneHander == null);
                    } else if (twoHander == null && secondOneHander == null) {
                        secondOneHander = wear;

                        canPlayOneHander.setValue(false);
                    } else
                        throw new IllegalStateException("Can not equip " + wear + " for player " + this);
                    break;
                case TWOHANDS:
                    if (firstOneHander == null && secondOneHander == null && twoHander == null) {
                        twoHander = wear;

                        canPlayTwoHander.setValue(false);
                    } else
                        throw new IllegalStateException("Can not equip " + wear + " for player " + this);
                    break;
                case ARMOR:
                    if (armor == null) {
                        armor = wear;

                        canPlayArmor.setValue(false);
                    } else
                        throw new IllegalStateException("Can not equip " + wear + " for player " + this);
                    break;
                case HEAD:
                    if (headGeer == null) {
                        headGeer = wear;

                        canPlayHeadgeer.setValue(false);
                    } else
                        throw new IllegalStateException("Can not equip " + wear + " for player " + this);
                    break;
                case SHOES:
                    if (shoes == null) {
                        shoes = wear;

                        canPlayShoes.setValue(false);
                    } else
                        throw new IllegalStateException("Can not equip " + wear + " for player " + this);
                    break;
            }

            fightLevel.setValue(fightLevel.getValue() + wear.bonus);
        } else
            throw new IllegalArgumentException("Unknown card type " + card);

        handCards.getValue().remove(card);
        update(handCards);

        playedCards.getValue().add(card);
        update(playedCards);
    }

    public void emitPickupCard(Card card) {
        eventRepository.push(
            new Event(scope, Action.PICKUP_CARD, R.string.ev_pickup_card, card.getId())
        );
    }

    public void pickupCard(Card card) {
        if (card instanceof BonusWear) {
            BonusWear wear = (BonusWear) card;

            if (wear.size == BonusWear.Size.BIG)
                canPlayBigEquipment.setValue(true);

            switch (wear.blocking) {
                case NOTHING:
                    break;
                case ONEHAND:
                    if (firstOneHander == wear)
                        firstOneHander = null;
                    else if (secondOneHander == wear)
                        secondOneHander = null;
                    else
                        throw new IllegalStateException("Can not unequip " + wear + " for player " + this);

                    canPlayOneHander.setValue(true);
                    break;
                case TWOHANDS:
                    if (twoHander == wear) {
                        twoHander = null;

                        canPlayTwoHander.setValue(true);
                    } else
                        throw new IllegalStateException("Can not unequip " + wear + " for player " + this);
                    break;
                case ARMOR:
                    if (armor == wear) {
                        armor = null;

                        canPlayArmor.setValue(true);
                    } else
                        throw new IllegalStateException("Can not unequip " + wear + " for player " + this);
                    break;
                case HEAD:
                    if (headGeer == wear) {
                        headGeer = null;

                        canPlayHeadgeer.setValue(true);
                    } else
                        throw new IllegalStateException("Can not unequip " + wear + " for player " + this);
                    break;
                case SHOES:
                    if (shoes == wear) {
                        shoes = null;

                        canPlayShoes.setValue(true);
                    } else
                        throw new IllegalStateException("Can not unequip " + wear + " for player " + this);
                    break;
            }

            fightLevel.setValue(fightLevel.getValue() - wear.bonus);
        } else
            throw new IllegalArgumentException("Unknown card type " + card);

        playedCards.getValue().remove(card);
        update(playedCards);

        handCards.getValue().add(card);
        update(handCards);
    }

    public void emitRunAway() {
        eventRepository.push(
                new Event(scope, Action.RUN_AWAY, R.string.ev_run_away)
        );
    }

    public void runAway() {

    }

    public void emitFightMonster() {
        eventRepository.push(
                new Event(scope, Action.FIGHT_MONSTER, R.string.ev_fight_monster)
        );
    }

    public void fightMonster() {
        level.setValue(level.getValue() + 1);
        game.pushAwayCard();
    }

    public void playRound() {
    }
}

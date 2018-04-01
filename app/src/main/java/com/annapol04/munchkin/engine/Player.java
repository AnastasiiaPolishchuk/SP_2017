package com.annapol04.munchkin.engine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Pair;

import static com.annapol04.munchkin.engine.EngineTest.test;

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

    private int numberOfAllowedTreasureCardsToDraw = 0;
    private int numberOfAllowedDoorCardsToDraw = 0;

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

    private MutableLiveData<Boolean> isHeadgearEquiped = new MutableLiveData<>();
    private MutableLiveData<Boolean> isArmorEquiped = new MutableLiveData<>();
    private MutableLiveData<Boolean> areShoesquiped = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRightHandEquiped = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLeftHandEquiped = new MutableLiveData<>();

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
        numberOfAllowedTreasureCardsToDraw = 0;
        numberOfAllowedDoorCardsToDraw = 0;

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

        isHeadgearEquiped.setValue(false);
        isArmorEquiped.setValue(false);
        areShoesquiped.setValue(false);
        isLeftHandEquiped.setValue(false);
        isRightHandEquiped.setValue(false);
    }

    public void allowToDrawTreasureCards(int numberOfCards) {
        numberOfAllowedTreasureCardsToDraw = numberOfCards;
    }

    public boolean isAllowedToDrawTreasureCard() {
        return numberOfAllowedTreasureCardsToDraw > 0;
    }

    public void allowToDrawDoorCards(int numberOfCards) {
        numberOfAllowedDoorCardsToDraw = numberOfCards;
    }

    public boolean isAllowedToDrawDoorCard() {
        return numberOfAllowedDoorCardsToDraw > 0;
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

    public LiveData<Boolean> getIsHeadgearEquiped() {
        return isHeadgearEquiped;
    }

    public LiveData<Boolean> getIsArmorEquiped() {
        return isArmorEquiped;
    }

    public LiveData<Boolean> getAreShoesEquiped() {
        return areShoesquiped;
    }

    public LiveData<Boolean> getIsRightHandEquiped() {
        return isRightHandEquiped;
    }

    public LiveData<Boolean> getIsLeftHandEquiped() {
        return isLeftHandEquiped;
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

    public void drawTreasureCard(Card card) throws IllegalEngineStateException {
        if (numberOfAllowedTreasureCardsToDraw == 0)
            throw new IllegalEngineStateException("not allowed to draw a treasure card!");

        numberOfAllowedTreasureCardsToDraw--;

        game.drawTreasureCard(card);

        handCards.getValue().add(card);
        update(handCards);
    }

    public void drawDoorCard(Card card) throws IllegalEngineStateException {
        if (numberOfAllowedDoorCardsToDraw == 0)
            throw new IllegalEngineStateException("not allowed to draw a door card!");

        numberOfAllowedDoorCardsToDraw--;

        game.drawDoorCard(card);
    }

    public void playCard(Card card) throws IllegalEngineStateException {
        if (card instanceof BonusWear) {
            BonusWear wear = (BonusWear) card;

            if (wear.size == BonusWear.Size.BIG) {
                if (!canPlayBigEquipment.getValue())
                    throw new IllegalEngineStateException("Can not equip " + wear + " for player " + this);
                else
                    canPlayBigEquipment.setValue(false);
            }

            switch (wear.blocking) {
                case NOTHING:
                    break;
                case ONEHAND:
                    if (twoHander == null && firstOneHander == null) {
                        firstOneHander = wear;

                        isLeftHandEquiped.setValue(true);
                        canPlayOneHander.setValue(secondOneHander == null);
                    } else if (twoHander == null && secondOneHander == null) {
                        secondOneHander = wear;

                        isRightHandEquiped.setValue(true);
                        canPlayOneHander.setValue(false);
                    } else
                        throw new IllegalEngineStateException("Can not equip " + wear + " for player " + this);
                    break;
                case TWOHANDS:
                    if (firstOneHander == null && secondOneHander == null && twoHander == null) {
                        twoHander = wear;

                        isLeftHandEquiped.setValue(true);
                        isRightHandEquiped.setValue(true);
                        canPlayTwoHander.setValue(false);
                    } else
                        throw new IllegalEngineStateException("Can not equip " + wear + " for player " + this);
                    break;
                case ARMOR:
                    if (armor == null) {
                        armor = wear;

                        isArmorEquiped.setValue(true);
                        canPlayArmor.setValue(false);
                    } else
                        throw new IllegalEngineStateException("Can not equip " + wear + " for player " + this);
                    break;
                case HEAD:
                    if (headGeer == null) {
                        headGeer = wear;

                        isHeadgearEquiped.setValue(true);
                        canPlayHeadgeer.setValue(false);
                    } else
                        throw new IllegalEngineStateException("Can not equip " + wear + " for player " + this);
                    break;
                case SHOES:
                    if (shoes == null) {
                        shoes = wear;

                        areShoesquiped.setValue(true);
                        canPlayShoes.setValue(false);
                    } else
                        throw new IllegalEngineStateException("Can not equip " + wear + " for player " + this);
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

    public void pickupCard(Card card) throws IllegalEngineStateException {
        if (card instanceof BonusWear) {
            BonusWear wear = (BonusWear) card;

            if (wear.size == BonusWear.Size.BIG)
                canPlayBigEquipment.setValue(true);

            switch (wear.blocking) {
                case NOTHING:
                    break;
                case ONEHAND:
                    if (secondOneHander == wear) {
                        secondOneHander = null;

                        isRightHandEquiped.setValue(false);
                    } else if (firstOneHander == wear) {
                        firstOneHander = null;

                        isLeftHandEquiped.setValue(false);
                    } else
                        throw new IllegalEngineStateException("Can not unequip " + wear + " for player " + this);

                    canPlayOneHander.setValue(true);
                    break;
                case TWOHANDS:
                    if (twoHander == wear) {
                        twoHander = null;

                        isLeftHandEquiped.setValue(false);
                        isRightHandEquiped.setValue(false);
                        canPlayTwoHander.setValue(true);
                    } else
                        throw new IllegalEngineStateException("Can not unequip " + wear + " for player " + this);
                    break;
                case ARMOR:
                    if (armor == wear) {
                        armor = null;

                        isArmorEquiped.setValue(false);
                        canPlayArmor.setValue(true);
                    } else
                        throw new IllegalEngineStateException("Can not unequip " + wear + " for player " + this);
                    break;
                case HEAD:
                    if (headGeer == wear) {
                        headGeer = null;

                        isHeadgearEquiped.setValue(false);
                        canPlayHeadgeer.setValue(true);
                    } else
                        throw new IllegalEngineStateException("Can not unequip " + wear + " for player " + this);
                    break;
                case SHOES:
                    if (shoes == wear) {
                        shoes = null;

                        areShoesquiped.setValue(false);
                        canPlayShoes.setValue(true);
                    } else
                        throw new IllegalEngineStateException("Can not unequip " + wear + " for player " + this);
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

    public void runAway() throws IllegalEngineStateException {
        game.pushAwayMonsterCard();
    }

    public Pair<Monster, Integer> fightMonster() throws IllegalEngineStateException {
        List<Card> cards = game.getDeskCards().getValue();

        test(cards.size() > 0,
                "a monster has to be on the table to fight against it!");

        Card card = cards.get(0);

        test(card instanceof Monster,
                "the card on the playdesk has to be a monster to fight against it!");

        Monster monster = (Monster)card;

        test(fightLevel.getValue() > monster.getLevel(),
            "can not fight against a monster with greater or equal fight level!");

        level.setValue(level.getValue() + 1);
        game.pushAwayMonsterCard();

        return new Pair<>(monster, 1);
    }
}

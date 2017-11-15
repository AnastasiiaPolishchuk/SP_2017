package com.annapol04.munchkin.logic;

import java.util.ArrayList;

/**
 * Created by Falco on 26.10.2017.
 */

public class Player {
    public int id;
    public int level;
    public int fightLevel; // fightLevel = level + Boni, die am Table liegen (also im table deck sind)
    public int race; //0 = keine Race, 1 = elf, 2 = zwerg
    public int runAway;
    public int bonus;

    public static int numberOfPlayers;
    public static ArrayList<Player> playerList = new ArrayList<Player>();

    //Static stuff
    public ArrayList<Card> hand = new ArrayList<Card>();
    public ArrayList<Card> table = new ArrayList<Card>();


    public Player(int id, int level, int fightLevel, int race, int runAway, ArrayList<Card> hand, ArrayList<Card> table, int bonus) {
        this.id = id;
        this.level = level;
        this.fightLevel = fightLevel;
        this.race = race;
        this.runAway = runAway;
        this.hand = hand;
        this.table = table;
        this.bonus = bonus;

        numberOfPlayers++;
        playerList.add(this);
    }

    //Konstruktor für das erste Erstellen eine Spielers (default)
    public Player(int id){
        this.id = id;
        this.level = 1;
        this.fightLevel = 1;
        this.race = 0;
        this.runAway = 0;
        this.hand = new ArrayList<Card>();
        this.table = new ArrayList<Card>();
        computeBonus();

        numberOfPlayers++;
        playerList.add(this);
    }


    public static int getLowestLevelOfAllPlayers(){
        int currentLowestLevel = playerList.get(0).level;
        for (Player currentPlayer: playerList) {
            if(currentLowestLevel > currentPlayer.level){
                currentLowestLevel = currentPlayer.level;
            }
        }
        return currentLowestLevel;
    }

    public static void handOutCards(){
        for (Player currentPlayer: playerList) {
            for(int i = 0; i < 4; i++){
                currentPlayer.hand.add(TreasureDeck.draw());

            }

            for(int i = 0; i < 4; i++){
                currentPlayer.hand.add(DoorDeck.draw());

            }
        }

    }

    /**
     * Berrechnet den Bonus des Spielers - setzt also das "bonus" Feld
     */
    public void computeBonus(){
        this.bonus = 0;
        for (Card currentCard: table) {
            if(currentCard instanceof Bonus){
                this.bonus = this.bonus + ((Bonus) currentCard).bonus;
            }
            if(currentCard instanceof BonusWear){
                this.bonus = this.bonus + ((BonusWear) currentCard).bonus;
            }
        }
    }

    public void setLevel(int i) {
        this.level = i;
        computeBonus();
        this.fightLevel = this.level + this.bonus;
    }

    public void levelUp() {
        this.level++;
        computeBonus();
        this.fightLevel = this.level + this.bonus;
    }

    public void levelDown() {
        this.level = this.level - 1;
        computeBonus();
        this.fightLevel = this.level + this.bonus;
    }

    public void death() {
        playerList.remove(this);

        for (Card currentCard : table) {
            table.remove(currentCard);
            //auf den richtigen deck ablegen
            if(currentCard.deck == 0){
                DoorDeck.discardDeck.add(currentCard);
            }
            else {
                TreasureDeck.discardDeck.add(currentCard);
            }
            //TODO: log ausgeben "player xy hat item verloren"
        }

        for (Card currentCard : hand) {
            hand.remove(currentCard);
            //auf den richtigen deck ablegen
            if(currentCard.deck == 0){
                DoorDeck.discardDeck.add(currentCard);
            }
            else {
                TreasureDeck.discardDeck.add(currentCard);
            }
            //TODO: log ausgeben "player xy hat item verloren"
        }
    }


    public void loseAllCards() {
        for (Card currentCard : table) {
            table.remove(currentCard);
            //auf den richtigen deck ablegen
            if(currentCard.deck == 0){
                DoorDeck.discardDeck.add(currentCard);
            }
            else {
                TreasureDeck.discardDeck.add(currentCard);
            }
            //TODO: log ausgeben "player xy hat item verloren"
        }

        for (Card currentCard : hand) {
            hand.remove(currentCard);
            //auf den richtigen deck ablegen
            if(currentCard.deck == 0){
                DoorDeck.discardDeck.add(currentCard);
            }
            else {
                TreasureDeck.discardDeck.add(currentCard);
            }
            //TODO: log ausgeben "player xy hat item verloren"
        }
    }


    public void loseHand() {
        for (Card currentCard : hand) {
            hand.remove(currentCard);
            //auf den richtigen deck ablegen
            if(currentCard.deck == 0){
                DoorDeck.discardDeck.add(currentCard);
            }
            else {
                TreasureDeck.discardDeck.add(currentCard);
            }
            //TODO: log ausgeben "player xy hat item verloren"
        }
    }



    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ H I L F S M E T H O D E N ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public boolean searchBoth(int id){
        for (Card currentCard: hand) {
            if (currentCard.id == id){
                return true;
            }
        }

        for (Card currentCard: table) {
            if (currentCard.id == id){
                return true;
            }
        }
        return false;
    }

    public boolean searchHand(int id) {
        for (Card currentCard : hand) {
            if (currentCard.id == id) {
                return true;
            }
        }
        return false;
    }

    public boolean searchTable(int id) {
        for (Card currentCard : table) {
            if (currentCard.id == id) {
                return true;
            }
        }
        return false;
    }

    public Card getHand(int id){
        for (Card currentCard : hand) {
            if (currentCard.id == id) {
                return currentCard;
            }
        }
        return null;
    }

    public Card getTable(int id){
        for (Card currentCard : table) {
            if (currentCard.id == id) {
                return currentCard;
            }
        }
        return null;
    }

    public Card getBoth(int id){
        for (Card currentCard : table) {
            if (currentCard.id == id) {
                return currentCard;
            }
        }
        for (Card currentCard : hand) {
            if (currentCard.id == id) {
                return currentCard;
            }
        }
        return null;
    }

    public Card getAndRemoveBoth(int id){
        for (Card currentCard : table) {
            if (currentCard.id == id) {
                table.remove(currentCard);
                return currentCard;
            }
        }
        for (Card currentCard : hand) {
            if (currentCard.id == id) {
                hand.remove(currentCard);
                return currentCard;
            }
        }
        return null;
    }


    //Methoden zum Ablegen (verschiedene Optionen einen Gegenstand abzulegen)
    public void discardTableOfPlayer(int id){
        for (Card currentCard : table) {
            if (currentCard.id == id) {
                table.remove(currentCard);
                //auf den richtigen deck ablegen
                if(currentCard.deck == 0){
                    DoorDeck.discardDeck.add(currentCard);
                }
                else {
                    TreasureDeck.discardDeck.add(currentCard);
                }
                //TODO: log ausgeben "player xy hat item verloren"
            }
        }
    }


    public void discardTableOfPlayerOnlyBonusWearBlocking(int blockiertLokal){ //0 = nichts, 1 = 1 Hand, 2 = 2 Hände, 3 = Rüstung, 4 = Kopf, 5 = Schuhe
        for (Card currentCard : table) {
            if(currentCard instanceof BonusWear){
                if (((BonusWear) currentCard).blocking == blockiertLokal) {
                    table.remove(currentCard);
                    //auf den richtigen deck ablegen
                    if (currentCard.deck == 0) {
                        DoorDeck.discardDeck.add(currentCard);
                    } else {
                        TreasureDeck.discardDeck.add(currentCard);
                    }
                    //TODO: log ausgeben "player xy hat item verloren"
                }
            }
        }
    }

    public void discardTableOfPlayerOnlyBonusWearSize(int groesseLokal){ //0 = klein, 1 = groß
        for (Card currentCard : table) {
            if(currentCard instanceof BonusWear){
                if (((BonusWear) currentCard).size == groesseLokal) {
                    table.remove(currentCard);
                    //auf den richtigen deck ablegen
                    if (currentCard.deck == 0) {
                        DoorDeck.discardDeck.add(currentCard);
                    } else {
                        TreasureDeck.discardDeck.add(currentCard);
                    }
                    //TODO: log ausgeben "player xy hat item verloren"
                }
            }
        }
    }

    public boolean hasShoes(){
        for (Card currentCard : table) {
            if(currentCard instanceof BonusWear){
                if (((BonusWear) currentCard).blocking == 5) {
                    return true;
                }
            }
        }
        return false;
    }


    public void discardHand() {
        for (Card currentCard: hand) {
            hand.remove(currentCard);
            if (currentCard.deck == 0) {
                DoorDeck.discardDeck.add(currentCard);
            } else {
                TreasureDeck.discardDeck.add(currentCard);
            }
        }
    }

    /**
     * Gibt die erste gefundene Rassenkarte, die am Table liegt zurück und legt diese auf den richtigen Stapel ab.
     * In diesem Spiel kann man maximal eine Rassenkarte auf dem Table liegen haben, weswegen muss man nach dem ersten Finden einer Rassenkarte nicht weitersuchen, weil es nur maximal eine pro Player geben kann.
     * Dann wird diese Card zurückgegeben und abgelegt.
     * @return erste gefundene Rassenkarte oder null, falls keine gefunden wurde
     *
     */
    public Card getRaceCardFormTableAndDiscard(){
        for (Card currentCard: table) {
            if(currentCard instanceof Race){
                table.remove(currentCard);
                DoorDeck.discardDeck.add(currentCard);

                return currentCard;
            }
        }
        return null;
    }


    public void searchAndDiscardBiggestBonus() {
        int currentBonus = 0;
        Card discardCard = null;
         for (Card currentCard: table) {
            if(currentCard instanceof Bonus){
                if(currentBonus < ((Bonus) currentCard).bonus){
                    currentBonus = ((Bonus) currentCard).bonus;
                    discardCard = currentCard;
                }
            }
            if(currentCard instanceof BonusWear){
                if(currentBonus < ((BonusWear) currentCard).bonus){
                    currentBonus = ((Bonus) currentCard).bonus;
                    discardCard = currentCard;
                }
            }
        }
        if(discardCard != null){
            table.remove(discardCard);
            //Bonuskarteablegen
            TreasureDeck.discardDeck.add(discardCard);
        }
    }




}

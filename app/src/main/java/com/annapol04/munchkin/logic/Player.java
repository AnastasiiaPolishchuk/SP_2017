//package com.example.admin.ezmunchkin;
//
//import java.util.ArrayList;
//
///**
// * Created by Falco on 26.10.2017.
// */
//
//public class Player {
//
//    public enum PlayerRace{
//        HUMAN,
//        ELF,
//        DWARF; //Zwerg
//    }
//
//    public int id;
//    public int level;
//    public int fightLevel; // fightLevel = level + Boni, die am Table liegen (also im table deck sind)
//    public PlayerRace playerRace; //0 = keine Race, 1 = elf, 2 = zwerg
//    //TODO: Race to Enum
//    public int runAway;
//    public int bonus;
//
//    public static int numberOfPlayers;
//    public static ArrayList<Player> playerList = new ArrayList<Player>();
//
//    //Static stuff
//    public ArrayList<Card> hand = new ArrayList<Card>();
//    public ArrayList<Card> table = new ArrayList<Card>();
//
//
//    public Player(int id, int level, int fightLevel, PlayerRace race, int runAway, ArrayList<Card> hand, ArrayList<Card> table, int bonus) {
//        this.id = id;
//        this.level = level;
//        this.fightLevel = fightLevel;
//        this.playerRace = race;
//        this.runAway = runAway;
//        this.hand = hand;
//        this.table = table;
//        this.bonus = bonus;
//
//        numberOfPlayers++;
//        playerList.add(this);
//    }
//
//    //Konstruktor für das erste Erstellen eine Spielers (default)
//    public Player(int id){
//        this.id = id;
//        this.level = 1;
//        this.fightLevel = 1;
//        this.playerRace = PlayerRace.HUMAN;
//        this.runAway = 0;
//        this.hand = new ArrayList<Card>();
//        this.table = new ArrayList<Card>();
//        computeBonus();
//
//        numberOfPlayers++;
//        playerList.add(this);
//    }
//
//
//
//    public Special playerChoosesCard() {
//        //TODO: karte aus der Hand des Sielers entfernen (hand.remove())
//        //TODO: parameter adden, dass ich auswählen kann, welche art von karten ich dem Player zur auswahl geben will
//        return null;
//    }
//
//    /**
//     * Repräsentiert den Zustand gewinnen. Hier komme ich nur rein, wenn schon alle Stufen ausgewertet wurde und alle Boni (Egal für welche Seite, Monsterdtufe) eingespielt wurden.
//     * Also wenn der Kampf eindeutig zu Gunsten des Players entschieden ist.
//     * @param treasuresToDraw Ich bekomme für diese Methode bereits die fertig addierte Zahl der zu drawden Schätze. Muss also vor dem Aufruf fertig berechnet werden.
//     */
//    public void drawTresures(int treasuresToDraw) {
//
//        //richtige Anzahl an Schätzen draw
//        for(int i=0; i < treasuresToDraw; i++){
//            this.hand.add(TreasureDeck.draw());
//        }
//        //TODO: log ausgeben, WIE VIELE karten gezogen wurden
//
//        this.levelUp();
//    }
//
//    /**
//     * Ablegen einer Card auf den treasure-discardDeck und entfernen der Card vom fightDeck
//     * @param currentCard
//     */
//    public void discardTreasure(Card currentCard, Game currentGame){
//        currentGame.fightDeck.remove(currentCard);
//        TreasureDeck.discardDeck.add(currentCard);
//    }
//
//
//    /**
//     * Ablegen einer Card auf den TÜR-discardDeck und entfernen der Card vom fightDeck
//     * @param currentCard
//     */
//    public void discardDoor(Card currentCard, Game currentGame){
//        currentGame.fightDeck.remove(currentCard);
//        DoorDeck.discardDeck.add(currentCard);
//    }
//
//    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//    public static int getLowestLevelOfAllPlayers(){
//        int currentLowestLevel = playerList.get(0).level;
//        for (Player currentPlayer: playerList) {
//            if(currentLowestLevel > currentPlayer.level){
//                currentLowestLevel = currentPlayer.level;
//            }
//        }
//        return currentLowestLevel;
//    }
//
//    public static void handOutCards(){
//        for (Player currentPlayer: playerList) {
//            for(int i = 0; i < 4; i++){
//                currentPlayer.hand.add(TreasureDeck.draw());
//
//            }
//
//            for(int i = 0; i < 4; i++){
//                currentPlayer.hand.add(DoorDeck.draw());
//
//            }
//        }
//
//    }
//
//    /**
//     * Berrechnet den Bonus des Spielers - setzt also das "bonus" Feld
//     */
//    public void computeBonus(){
//        this.bonus = 0;
//        for (Card currentCard: table) {
//            if(currentCard instanceof Bonus){
//                this.bonus = this.bonus + ((Bonus) currentCard).bonus;
//            }
//            if(currentCard instanceof BonusWear){
//                this.bonus = this.bonus + ((BonusWear) currentCard).bonus;
//            }
//        }
//    }
//
//    public void setLevel(int i) {
//        this.level = i;
//        computeBonus();
//        this.fightLevel = this.level + this.bonus;
//    }
//
//    public void levelUp() {
//        this.level++;
//        computeBonus();
//        this.fightLevel = this.level + this.bonus;
//    }
//
//    public void levelDown() {
//        this.level = this.level - 1;
//        computeBonus();
//        this.fightLevel = this.level + this.bonus;
//    }
//
//    public void death() {
//        playerList.remove(this);
//
//        for (Card currentCard : table) {
//            table.remove(currentCard);
//            //auf den richtigen deck ablegen
//            if(currentCard.member == Card.Membership.DOOR){
//                DoorDeck.discardDeck.add(currentCard);
//            }
//            else {
//                TreasureDeck.discardDeck.add(currentCard);
//            }
//            //TODO: log ausgeben "player xy hat item verloren"
//        }
//
//        for (Card currentCard : hand) {
//            hand.remove(currentCard);
//            //auf den richtigen deck ablegen
//            if(currentCard.member == Card.Membership.DOOR){
//                DoorDeck.discardDeck.add(currentCard);
//            }
//            else {
//                TreasureDeck.discardDeck.add(currentCard);
//            }
//            //TODO: log ausgeben "player xy hat item verloren"
//        }
//    }
//
//
//    public void loseAllCards() {
//        for (Card currentCard : table) {
//            table.remove(currentCard);
//            //auf den richtigen deck ablegen
//            if(currentCard.member == Card.Membership.DOOR){
//                DoorDeck.discardDeck.add(currentCard);
//            }
//            else {
//                TreasureDeck.discardDeck.add(currentCard);
//            }
//            //TODO: log ausgeben "player xy hat item verloren"
//        }
//
//        for (Card currentCard : hand) {
//            hand.remove(currentCard);
//            //auf den richtigen deck ablegen
//            if(currentCard.member == Card.Membership.DOOR){
//                DoorDeck.discardDeck.add(currentCard);
//            }
//            else {
//                TreasureDeck.discardDeck.add(currentCard);
//            }
//            //TODO: log ausgeben "player xy hat item verloren"
//        }
//    }
//
//
//    public void loseHand() {
//        for (Card currentCard : hand) {
//            hand.remove(currentCard);
//            //auf den richtigen deck ablegen
//            if(currentCard.member == Card.Membership.DOOR){
//                DoorDeck.discardDeck.add(currentCard);
//            }
//            else {
//                TreasureDeck.discardDeck.add(currentCard);
//            }
//            //TODO: log ausgeben "player xy hat item verloren"
//        }
//    }
//
//
//
//    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~ H I L F S M E T H O D E N ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//    public boolean searchBoth(Card parameterCard){
//        for (Card currentCard: hand) {
//            if (currentCard == parameterCard){
//                return true;
//            }
//        }
//
//        for (Card currentCard: table) {
//            if (currentCard == parameterCard){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean searchBoth(String cardToSearch){
//        for (Card currentCard: hand) {
//            if (currentCard.name.equals(cardToSearch)){
//                return true;
//            }
//        }
//
//        for (Card currentCard: table) {
//            if (currentCard.name.equals(cardToSearch)){
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    public boolean searchBoth(Special.SpecialName SpecialNameParameter){
//        for (Card currentCard: hand) {
//            if(currentCard instanceof Special){
//                if (((Special) currentCard).specialName == SpecialNameParameter){
//                    return true;
//                }
//            }
//        }
//
//        for (Card currentCard: table) {
//            if (((Special) currentCard).specialName == SpecialNameParameter){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean searchHand(Card parameterCard) {
//        for (Card currentCard : hand) {
//            if (currentCard == parameterCard) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean searchTable(Card parameterCard) {
//        for (Card currentCard : table) {
//            if (currentCard == parameterCard) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public Card getHand(Card parameterCard){
//        for (Card currentCard : hand) {
//            if (currentCard == parameterCard) {
//                return currentCard;
//            }
//        }
//        return null;
//    }
//
//    public Card getTable(Card parameterCard){
//        for (Card currentCard : table) {
//            if (currentCard == parameterCard) {
//                return currentCard;
//            }
//        }
//        return null;
//    }
//
//    public Card getBoth(Card parameterCard){
//        for (Card currentCard : table) {
//            if (currentCard == parameterCard) {
//                return currentCard;
//            }
//        }
//        for (Card currentCard : hand) {
//            if (currentCard == parameterCard) {
//                return currentCard;
//            }
//        }
//        return null;
//    }
//
//
//    public Card getAndRemoveBoth(String parameterCard){
//        for (Card currentCard : table) {
//            if (currentCard.name.equals(parameterCard)){
//                table.remove(currentCard);
//                return currentCard;
//            }
//        }
//        for (Card currentCard : hand) {
//            if (currentCard.name.equals(parameterCard)){
//                hand.remove(currentCard);
//                return currentCard;
//            }
//        }
//        return null;
//    }
//
//
//    //Methoden zum Ablegen (verschiedene Optionen einen Gegenstand abzulegen)
//    public void discardTableOfPlayer(Card parameterCard){
//        for (Card currentCard : table) {
//            if (currentCard == parameterCard) {
//                table.remove(currentCard);
//                //auf den richtigen deck ablegen
//                if(currentCard.member == Card.Membership.DOOR){
//                    DoorDeck.discardDeck.add(currentCard);
//                }
//                else {
//                    TreasureDeck.discardDeck.add(currentCard);
//                }
//                //TODO: log ausgeben "player xy hat item verloren"
//            }
//        }
//    }
//
//
//    //0 = nichts, 1 = 1 Hand, 2 = 2 Hände, 3 = Rüstung, 4 = Kopf, 5 = Schuhe
//    public void discardTableOfPlayerOnlyBonusWearBlocking(BonusWear.Blocking BonusWearNameParameter){
//        for (Card currentCard : table) {
//            if(currentCard instanceof BonusWear){
//                if (((BonusWear) currentCard).blocking == BonusWearNameParameter) {
//                    table.remove(currentCard);
//
//                    //auf den richtigen deck ablegen
//                    if (currentCard.member == Card.Membership.DOOR) {
//                        DoorDeck.discardDeck.add(currentCard);
//                    } else {
//                        TreasureDeck.discardDeck.add(currentCard);
//                    }
//                    //TODO: log ausgeben "player xy hat item verloren"
//                }
//            }
//        }
//    }
//
//    public void discardTableOfPlayerOnlyBonusWearSize(BonusWear.Size BonusWearNameParameter){ //0 = klein, 1 = groß
//        for (Card currentCard : table) {
//            if(currentCard instanceof BonusWear){
//                if (((BonusWear) currentCard).size == BonusWearNameParameter) {
//                    table.remove(currentCard);
//                    //auf den richtigen deck ablegen
//                    if (currentCard.member == Card.Membership.DOOR) {
//                        DoorDeck.discardDeck.add(currentCard);
//                    } else {
//                        TreasureDeck.discardDeck.add(currentCard);
//                    }
//                    //TODO: log ausgeben "player xy hat item verloren"
//                }
//            }
//        }
//    }
//
//
//    public boolean searchForWunschringAndRemove(Game currentGame){
//        //Wunschring in der Hand des Players suchen
//        for (Card searchCard: this.hand) {
//            if (searchCard instanceof Special){
//                if(((Special) searchCard).specialName == Special.SpecialName.WUNSCHRING){
//                    this.hand.remove(searchCard);
//                    discardTreasure(searchCard, currentGame);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//
//
//    public boolean hasShoes(){
//        for (Card currentCard : table) {
//            if(currentCard instanceof BonusWear){
//                if (((BonusWear) currentCard).blocking == BonusWear.Blocking.SHOES) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//
//    public void discardHand() {
//        for (Card currentCard: hand) {
//            hand.remove(currentCard);
//            if (currentCard.member == Card.Membership.DOOR) {
//                DoorDeck.discardDeck.add(currentCard);
//            } else {
//                TreasureDeck.discardDeck.add(currentCard);
//            }
//        }
//    }
//
//    /**
//     * Gibt die erste gefundene Rassenkarte, die am Table liegt zurück und legt diese auf den richtigen Stapel ab.
//     * In diesem Spiel kann man maximal eine Rassenkarte auf dem Table liegen haben, weswegen muss man nach dem ersten Finden einer Rassenkarte nicht weitersuchen, weil es nur maximal eine pro Player geben kann.
//     * Dann wird diese Card zurückgegeben und abgelegt.
//     * @return erste gefundene Rassenkarte oder null, falls keine gefunden wurde
//     *
//     */
//    public Card getRaceCardFormTableAndDiscard(){
//        for (Card currentCard: table) {
//            if(currentCard instanceof com.example.admin.ezmunchkin.Race){
//                table.remove(currentCard);
//                DoorDeck.discardDeck.add(currentCard);
//
//                return currentCard;
//            }
//        }
//        return null;
//    }
//
//
//    public void searchAndDiscardBiggestBonus() {
//        int currentBonus = 0;
//        Card discardCard = null;
//         for (Card currentCard: table) {
//            if(currentCard instanceof Bonus){
//                if(currentBonus < ((Bonus) currentCard).bonus){
//                    currentBonus = ((Bonus) currentCard).bonus;
//                    discardCard = currentCard;
//                }
//            }
//            if(currentCard instanceof BonusWear){
//                if(currentBonus < ((BonusWear) currentCard).bonus){
//                    currentBonus = ((Bonus) currentCard).bonus;
//                    discardCard = currentCard;
//                }
//            }
//        }
//        if(discardCard != null){
//            table.remove(discardCard);
//            //Bonuskarteablegen
//            TreasureDeck.discardDeck.add(discardCard);
//        }
//    }
//
//
//
//
//}

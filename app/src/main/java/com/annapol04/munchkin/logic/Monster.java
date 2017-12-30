//package com.example.admin.ezmunchkin;
//
//import java.util.ArrayList;
////import com.example.admin.ezmunchkin.Player;
//
///**
// * Created by Falco on 29.10.2017.
// */
//
//public class Monster extends Card {
//
//    public enum Monstername{
//        FILZLAEUSE,
//        SABBERNDERSCHLEIM,
//        LAHMERGOBLIN,
//        HAMMERRATTE,
//        TOPFPFLANZE,
//        FLIEGENDEFROESCHE,
//        GALLERTOKTAEDER,
//        HUHN,
//        MRBONES,
//        PITBULL,
//        HARFIEN,
//        LEPRACHAUN,
//        SCHNECKEN,
//        UNTOTESPFERD,
//        ANWALT,
//        ENTIKORE,
//        PIKOTZU,
//        KREISCHENDERDEPP,
//        AMAZONE,
//        GESICHTSSAUGER,
//        PAVILLON,
//        GEMEINEGHOULE,
//        ORKS,
//        LAUFENDENASE,
//        NETZTROLL,
//        BIGFOOT,
//        ZUNGENDÄMON,
//        VAMPIR,
//        VERSICHERUNGSVERTRETER,
//        BEKIFFTERGOLEM,
//        SCHRECKEN,
//        HIPPOGREIF,
//        KOENIGTUT,
//        GRUFTIGEGEBRUEDER,
//        KRAKZILLA,
//        BULLROG,
//        PLUTONIUMDRACHE;
//    }
//
//    public int level;
//    public int treasure;
//    public String badThings;
//    public Monstername monsterName;
//    public Membership stack;
//    public String description;
//
//    public Monster(String name, Monstername monsterName, String description, int level, int treasure, String badThings) {
//        super(Membership.DOOR, name);
//        this.description = description;
//        this.member = Membership.DOOR; //Must be hardcoded
//        this.monsterName = monsterName;
//        this.name = name;
//        this.level = level;
//        this.treasure = treasure;
//        this.badThings = badThings;
//    }
//
//
//
//    public void fight(Player currentPlayer, Game currentGame){
//
//        int levels[] = computeLevelOfCounterparties(currentPlayer);
//        int playerTotal = levels[0];
//        int monsterTotal = levels[1];
//
//
//        switch(this.monsterName){
//            case ANWALT:{
//                //Anwalt: Würfle. Bei einer 1 kannst/musst du, anstatt ihn zu bekämpfen, 2 Schätze ablegen und verdeckt 2 neue draw." Ansonsten normal kämpfen
//                if(Dice.throwDice() == 1){
//                    currentPlayer.discardTreasure(currentPlayer.playerChoosesCard(), currentGame);
//                    currentPlayer.discardTreasure(currentPlayer.playerChoosesCard(), currentGame);
//
//                    currentPlayer.hand.add(TreasureDeck.draw());
//                    currentPlayer.hand.add(TreasureDeck.draw());
//                    return;
//                }
//
//            }
//            break;
//
//            case PIKOTZU:{
//                //Pikotzu: extrastufe, wenn du es ohne hilfe und boni besiegst
//                if(currentPlayer.level > this.level){ // Player gewinnt gegen pikotzu. ANsonsten geht es normal weiter
//                    int treasuresToDraw = this.treasure;
//                    currentPlayer.drawTresures(treasuresToDraw);
//                    currentPlayer.discardDoor(this, currentGame);
//                    return;
//                }
//            }
//            break;
//            case PAVILLON:{
//                //Pavillon: Boni können dir nicht helfen. Du musst dich dem Pavillon allein stellen.
//                if (currentPlayer.level > this.level){
//                    int treasuresToDraw = this.treasure;
//                    currentPlayer.drawTresures(treasuresToDraw);
//                    currentPlayer.discardDoor(this, currentGame);
//                    return;
//                }
//                else if(currentPlayer.level == this.level){
//                    draw(currentPlayer);
//                    currentPlayer.discardDoor(this, currentGame);
//                    return;
//                }
//                else {
//                    lose(currentPlayer, currentGame);
//                    currentPlayer.discardDoor(this, currentGame);
//                    return;
//                }
//
//            }
//            //break;
//            case GEMEINEGHOULE:{
//                //Gemeine Ghoule: Gegen sie dürfen keine Gegenstände oder andere Boni eingesetzt werden - kämpfe nur mit deiner Charakterstufe
//                if (currentPlayer.level > this.level){
//                    int treasuresToDraw = this.treasure;
//                    currentPlayer.drawTresures(treasuresToDraw);
//                    currentPlayer.discardDoor(this, currentGame);
//                    return;
//                }
//                else if(currentPlayer.level == this.level){
//                    draw(currentPlayer);
//                    currentPlayer.discardDoor(this, currentGame);
//                    return;
//                }
//                else {
//                    lose(currentPlayer, currentGame);
//                    currentPlayer.discardDoor(this, currentGame);
//                    return;
//                }
//            }
//            //break;
//
//            case ZUNGENDÄMON:{
//                //Zungendämon: Höllenkreatur. Lege einen Gegenstand deiner Wahl vor dem Kapf ab
//                //TODO: fertig machen, in KOmbination, dass ich bei der Methode playerChoosesCard() einen Parameter übergeben lassen kann, der bestimmt, aus welchen karten der Player auswählen kann
//                currentPlayer.playerChoosesCard();
//
//            }
//            break;
//            case VERSICHERUNGSVERTRETER:{
//                if (currentPlayer.fightLevel > this.level){
//                    int treasuresToDraw = this.treasure;
//                    currentPlayer.drawTresures(treasuresToDraw);
//                    currentPlayer.discardDoor(this, currentGame);
//                    return;
//                }
//                else if(currentPlayer.fightLevel == this.level){
//                    draw(currentPlayer);
//                    currentPlayer.discardDoor(this, currentGame);
//                    return;
//                }
//                else {
//                    lose(currentPlayer, currentGame);
//                    currentPlayer.discardDoor(this, currentGame);
//                    return;
//                }
//
//            }
//            //break;
//            case BEKIFFTERGOLEM:{        //Bekiffter Golem: Wähle aus: kämpfen oder einfach vorbeigehen lassen.
//                //askPlayerIfHeWantsToFight();
//                //TODO: fertig machen
//
//            }
//            break;
//
//            default:
//        }
//
//        ArrayList<Card> forThePlayer = new ArrayList<Card>();
//        ArrayList<Card> forTheMonster = new ArrayList<Card>();
//
//        playerEntersCardsForPlayer(currentPlayer, forThePlayer, playerTotal, currentGame);
//        playerEntersCardsForMonster(currentPlayer, forTheMonster, monsterTotal, currentGame);
//
//
//        // ~~~~~~~~~~~~~~~~~~~ AUSWERTUNG ~~~~~~~~~~~~~~~~~~~
//        if(playerTotal > monsterTotal){
//            //TODO: log ausgeben
//
//            int treasuresToDraw = this.treasure;
//            if(this.monsterName == Monstername.TOPFPFLANZE && currentPlayer.playerRace == Player.PlayerRace.ELF){
//                treasuresToDraw++;
//            }
//
//            currentPlayer.drawTresures(treasuresToDraw);
//            currentPlayer.discardDoor(this, currentGame);
//        }
//        else if(playerTotal == monsterTotal){
//            //TODO: log ausgeben
//            draw(currentPlayer);
//            currentPlayer.discardDoor(this, currentGame);
//        }
//        else if(playerTotal < monsterTotal){
//            //TODO: log ausgeben
//            lose(currentPlayer, currentGame);
//            currentPlayer.discardDoor(this, currentGame);
//        }
//
//    }
//
//    private void playerEntersCardsForPlayer(Player currentPlayer, ArrayList<Card> forThePlayer, int playerTotal, Game currentGame) {
//        //  a) "Nur einmal einsetzbar" karte (Doppelgänger, Freundschaftstrank, Polly)
//        //  b) Monsterlevel erhöhen
//
//        // ~~~~~~~~~~~~~~~~~~~ Player ~~~~~~~~~~~~~~~~~~~
//        //TODO: log augeben, dass der Player nun für sich karten einspielt & dass die karten in der eingegebenen Reihenfolge abgearbeitet werden
//        //Karten für Player: Klassenoptionen: BonusSide, Special
//        for (int i = 0; i < 5; i++){
//            //TODO: einfügen, wenn der Player nichts (mehr) einspielen will. Muss eventuell mit der playerChoosesCard() Methode verknüpft werden
//            Card playedCard = currentPlayer.playerChoosesCard();
//            forThePlayer.add(playedCard);
//            currentGame.fightDeck.add(playedCard);
//        }
//        for (Card current: forThePlayer) {
//            if(current instanceof BonusSide){ //Card wird im falle "true" automatisch gecastet
//                playerTotal = playerTotal + ((BonusSide) current).bonus;
//            }
//            if(current instanceof Special){
//                //Magische Lampe: (112,"Nur in der Runde spielbar. Sie beschwört einen Geist, der ein Monster verschwinden lässt, selbst wenn dein Weglaufenwurf verpatzt wurde und es dich fangen würde. War es das einzige Monster, erhälst du seinen treasure, aber keine level. Nur einmal einsetzbar.");
//                if (((Special) current).specialName == Special.SpecialName.LAMPE){
//                    currentPlayer.drawTresures(this.treasure);
//                    return;
//                }
//
//                //Doppelgänger
//                if (((Special) current).specialName == Special.SpecialName.DOPPELGAENGER){
//                    playerTotal = playerTotal * 2;
//                }
//
//                //Freunschaftstrank: den raum durchsuchen: eine türkarte verdeckt draw
//                if (((Special) current).specialName == Special.SpecialName.FREUNDSCHAFTSTRANK){
//                    currentPlayer.hand.add(DoorDeck.draw());
//                    return;
//                }
//
//                //polly
//                if (((Special) current).specialName == Special.SpecialName.POLLY){
//                    currentPlayer.drawTresures(1);
//                    return;
//                }
//            }
//        }
//        //TODO: log ausgeben: So in der Art: "Player hat nun die Stärke...."
//    }
//
//    private void playerEntersCardsForMonster(Player currentPlayer, ArrayList<Card> forTheMonster, int monsterTotal, Game currentGame) {
//        //  a) "Nur einmal einsetzbar" karte (Doppelgänger, Freundschaftstrank, Polly)
//        //  b) Monsterlevel erhöhen
//
//        //TODO: log augeben, dass der Player nun für das Monster karten einspielt & dass die karten in der eingegebenen Reihenfolge abgearbeitet werden
//        //Karten für Monster: Klassenoptionen: BonusSide, Special(ist hier theoretisch möglich, macht praktisch aber keinen Sinn), Monsterlevel
//        for (int i = 0; i < 5; i++){
//            //TODO: einfügen, wenn der Player nichts (mehr) einspielen will. Muss eventuell mit der playerChoosesCard() Methode verknüpft werden
//            //Card playedCard = currentPlayer.currentPlayerplayerChoosesCard();
//            Card playedCard = null;
//            forTheMonster.add(playedCard);
//            currentGame.fightDeck.add(playedCard);
//        }
//        for (Card current: forTheMonster) {
//            if (current instanceof BonusSide){
//                monsterTotal = monsterTotal + ((BonusSide) current).bonus;
//            }
//            if(current instanceof  Monsterlevel){
//                monsterTotal = monsterTotal + ((Monsterlevel) current).monsterBonus;
//            }
//        }
//
//        //TODO: Log ausgeben
//    }
//
//    private int[] computeLevelOfCounterparties(Monster this, Player currentPlayer) {
//        int playerTotal = currentPlayer.fightLevel;
//        int monsterTotal = this.level;
//
//        if(currentPlayer.playerRace == Player.PlayerRace.ELF){
//            switch(this.monsterName){
//                case SABBERNDERSCHLEIM:{
//                    monsterTotal = monsterTotal + 4;
//                }
//                break;
//                case LEPRACHAUN:{
//                    monsterTotal = monsterTotal + 5;
//                }
//                break;
//                case KRAKZILLA:{
//                    playerTotal = playerTotal + 4;
//                }
//                break;
//                case GESICHTSSAUGER:{
//                    monsterTotal = monsterTotal + 6;
//                }
//                break;
//            }
//
//        }
//        else if(currentPlayer.playerRace == Player.PlayerRace.DWARF){
//            switch(this.monsterName){
//                case UNTOTESPFERD:{
//                    monsterTotal = monsterTotal + 5;
//                }
//                break;
//                case ORKS:{
//                    monsterTotal = monsterTotal + 6;
//                }
//                break;
//                case BIGFOOT:{
//                    monsterTotal = monsterTotal + 3;
//                }
//                break;
//            }
//        }
//        int[] output = new int[2];
//        output[0] = playerTotal;
//        output[1] = monsterTotal;
//        return output;
//    }
//
//    /**
//     * Berechnet das runaway: erst wird überprüft, ob der Player eine Spezialkarte gespielt hat, die ihn rettet oder hilft, dann wird das eigentliche runaway berechnet (in eigener Methode)
//     * Auch wird mit einberechnet, ob das Monster spezielle Eigenschaften, das runaway betreffend, hat
//     * @param
//     * @param currentPlayer
//     */
//    public void lose(Player currentPlayer, Game currentGame){
//        if(this.monsterName == Monstername.LAUFENDENASE){
//            currentPlayer.levelDown();
//            currentPlayer.levelDown();
//            currentPlayer.levelDown();
//            return;
//        }
//
//        int currentRunaway = currentPlayer.runAway;
//
//        switch(this.monsterName){
//            case LAHMERGOBLIN: currentRunaway++;
//                break;
//            case FLIEGENDEFROESCHE: currentRunaway--;
//                break;
//            case GALLERTOKTAEDER: currentRunaway++;
//                break;
//            case SCHNECKEN: currentRunaway = currentRunaway - 2;
//                break;
//
//        }
//
//        if(this.monsterName == Monstername.PITBULL){
//            //hammer, scharferStreitkolben, hellebarde, napalstab, stange, rapier
//            if(currentPlayer.searchBoth("Kniescheiben zertrümmernder Hammer")){
//                currentGame.fightDeck.add(currentPlayer.getAndRemoveBoth("Kniescheiben zertrümmernder Hammer"));
//                return;
//            }
//
//            if(currentPlayer.searchBoth("Scharfer Streitkolben")){
//                currentGame.fightDeck.add(currentPlayer.getAndRemoveBoth("Scharfer Streitkolben"));
//                return;
//            }
//
//            if(currentPlayer.searchBoth("Schweizer Armee-Hellebarde")){
//                currentGame.fightDeck.add(currentPlayer.getAndRemoveBoth("Schweizer Armee-Hellebarde"));
//                return;
//            }
//
//            if(currentPlayer.searchBoth("Napalmstab")){
//                currentGame.fightDeck.add(currentPlayer.getAndRemoveBoth("Napalmstab"));
//                return;
//            }
//
//            if(currentPlayer.searchBoth("Stange. 11-Fuß")){
//                currentGame.fightDeck.add(currentPlayer.getAndRemoveBoth("Stange. 11-Fuß"));
//                return;
//            }
//
//            if(currentPlayer.searchBoth("Unfairer Rapier")){
//                currentGame.fightDeck.add(currentPlayer.getAndRemoveBoth("Unfairer Rapier"));
//                return;
//            }
//        }
//
//
//
//
//
//        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~ EXTRAKARTEN (von der Hand des Players) ~~~~~~~~~~~~~~~~~~~~~~~~~~~
//        Special playedCard = currentPlayer.playerChoosesCard();
//        if(playedCard.specialName == Special.SpecialName.LAMPE){ // Magische Lampe: Nur in der Runde spielbar. Sie beschwört einen Geist, der ein Monster verschwinden lässt, selbst wenn dein Weglaufenwurf verpatzt wurde und es dich fangen würde. War es das einzige Monster, erhälst du seinen treasure, aber keine level. Nur einmal einsetzbar.
//            //TODO: log ausgeben, dass der Player nochmal davon gekommen ist...
//            currentGame.fightDeck.add(playedCard);
//            currentPlayer.drawTresures(this.treasure);
//            currentPlayer.discardTreasure(this, currentGame); //magische lampe ablegen
//            return;
//        }
//        else if (playedCard.specialName == Special.SpecialName.STIEFEL){ //Stiefel
//            //TODO: log ausgeben, dass das runaway sich durch die stiefel jz verändert hat...
//            currentGame.fightDeck.add(playedCard);
//            currentRunaway = currentRunaway + 2;
//            currentPlayer.discardTreasure(this, currentGame);
//            //gehe zum letzten if dieser methode
//        }
//        else if (playedCard.specialName == Special.SpecialName.FERTIGMAUER){ //Fertigmauer: sofortiges Entkommen
//            //TODO: log ausgeben, dass das runaway sich durch die stiefel jz verändert hat...
//            currentGame.fightDeck.add(playedCard);
//            return;
//        }
//        else if (playedCard.specialName == Special.SpecialName.TUBA){ //tuba
//            currentRunaway = currentRunaway + 3;
//            currentGame.fightDeck.add(playedCard);
//            currentPlayer.discardTreasure(this, currentGame);
//
//            boolean escaped = runaway(currentPlayer, currentRunaway); //muss hier gemacht werden, da man durch die tuba extra einen treasure draw darf
//            if(escaped){
//                currentPlayer.drawTresures(1);
//            }
//            else {
//                //Gezinkter wuerfel oder unsichtsbarkeittrank kann einen nochmal retten. man muss abfragen, ob der Player diesen spielen will (falls er ihn hat)
//                Special extraCard2 = currentPlayer.playerChoosesCard();
//                if (playedCard.specialName == Special.SpecialName.GEZINKTERWUERFEL){
//                    //TODO: ausgeben, dass der Player glueck gehabt hat
//                    currentGame.fightDeck.add(playedCard);
//                    return;
//                }
//                else if (playedCard.specialName == Special.SpecialName.UNSICHTBARKEITSTRANK){ // unsichtsbarkeitstrank
//                    //TODO: ausgeben, dass der Player glueck gehabt hat
//                    currentGame.fightDeck.add(playedCard);
//                    return;
//                }
//
//                badThings(currentPlayer);
//            }
//        }//tuba ende
//
//
//        boolean escaped = runaway(currentPlayer, currentRunaway); //true wenn er es geschafft hat
//
//        //wenn er es geschafft hat
//        if(escaped){
//            //TODO: phase 1 ist beendet und der Player muss gefragt werden, was er nun machen will
//
//        }
//        //wenn er es nicht geschafft hat
//        else{
//
//            //Gezinkter wuerfel oder unsichtsbarkeittrank kann einen nochmal retten. man muss abfragen, ob der Player diesen spielen will (falls er ihn hat)
//            Special extraCard2 = currentPlayer.playerChoosesCard();
//            if (playedCard.specialName == Special.SpecialName.GEZINKTERWUERFEL){
//                //TODO: ausgeben, dass der Player glueck gehabt hat
//                currentGame.fightDeck.add(playedCard);
//                return;
//            }
//            else if (playedCard.specialName == Special.SpecialName.UNSICHTBARKEITSTRANK){ // unsichtsbarkeitstrank
//                //TODO: ausgeben, dass der Player glueck gehabt hat
//                currentGame.fightDeck.add(playedCard);
//                return;
//            }
//            badThings(currentPlayer);
//        }
//
//
//    }
//
//    /**
//     * Computes the win
//     */
//    public void win(Monster this, Player currentPlayer){
//
//    }
//
//
//    /**
//     * Computes the draw
//     * @param currentPlayer
//     */
//    public void draw(Player currentPlayer){
//        int diced = Dice.getRandom(1, 6);
//        if (diced != 1){
//            currentPlayer.drawTresures(this.treasure);
//        }
//        else{
//            badThings(currentPlayer);
//        }
//    }
//
//    /**
//     * Computes the badThins (schlimme Dinge)
//     */
//    public void badThings(Player currentPlayer){
//        switch (this.monsterName){
//            case FILZLAEUSE : {
//                currentPlayer.discardTableOfPlayerOnlyBonusWearBlocking(BonusWear.Blocking.ARMOR);
//                currentPlayer.discardTableOfPlayerOnlyBonusWearBlocking(BonusWear.Blocking.SHOES);
//            }
//            break;
//            case SABBERNDERSCHLEIM: {
//                if(currentPlayer.hasShoes()){
//                    currentPlayer.discardTableOfPlayerOnlyBonusWearBlocking(BonusWear.Blocking.SHOES);
//                    return;
//                }
//                else {
//                    currentPlayer.levelDown();
//                }
//            }
//            break;
//            case LAHMERGOBLIN: {
//                currentPlayer.levelDown();
//            }
//            break;
//            case HAMMERRATTE: {
//                currentPlayer.levelDown();
//            }
//            break;
//            case TOPFPFLANZE: { //Topfplanze: Automatische Flucht
//                //TODO: muss hier noch was gemacht werden?
//            }
//            break;
//            case FLIEGENDEFROESCHE: {
//                currentPlayer.levelDown();
//                currentPlayer.levelDown();
//            }
//            break;
//            case GALLERTOKTAEDER: {
//                currentPlayer.discardTableOfPlayerOnlyBonusWearSize(BonusWear.Size.BIG);
//
//            }
//            break;
//            case HUHN: {
//                currentPlayer.levelDown();
//            }
//            break;
//            case MRBONES: {
//                currentPlayer.levelDown();
//                currentPlayer.levelDown();
//
//            }
//            break;
//            case PITBULL: {
//                currentPlayer.levelDown();
//                currentPlayer.levelDown();
//
//            }
//            break;
//            case HARFIEN: {
//                currentPlayer.levelDown();
//                currentPlayer.levelDown();
//            }
//            break;
//            case LEPRACHAUN: {
//                //TODO: Ordentlich die Card des Players einlesen, die er ablegen will
//                Card cardToDiscard1 = currentPlayer.playerChoosesCard();
//                Card cardToDiscard2 = currentPlayer.playerChoosesCard();
//            }
//            break;
//            case SCHNECKEN: {
//                int diced = Dice.throwDice();
//                for(int i = 0; i < diced; i++){
//                    //TODO: input, welche Karten der currentPlayer ablegen will
//                }
//            }
//            break;
//            case UNTOTESPFERD: {
//                currentPlayer.levelDown();
//                currentPlayer.levelDown();
//            }
//            break;
//            case ANWALT: {
//                for(int i = 0; i < currentPlayer.numberOfPlayers; i++){
//                    //TODO: input, welche Karten der currentPlayer ablegen will
//                }
//            }
//            break;
//            case ENTIKORE: {
//                //TODO: currentPlayer durch die GUI fragen, was er machen will
//                int inputPlayer = 0;//Playerfragen();
//                if(inputPlayer == 1){
//                    currentPlayer.discardHand();
//                }
//                else{
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                }
//            }
//            break;
//            case PIKOTZU: {
//                currentPlayer.discardHand();
//            }
//            break;
//            case KREISCHENDERDEPP: {
//                if(currentPlayer.playerRace != Player.PlayerRace.HUMAN){
//                    currentPlayer.playerRace = Player.PlayerRace.HUMAN;
//                    currentPlayer.getRaceCardFormTableAndDiscard();
//                }
//            }
//            break;
//            case AMAZONE: {
//                if(currentPlayer.playerRace != Player.PlayerRace.HUMAN){
//                    currentPlayer.playerRace = Player.PlayerRace.HUMAN;
//                    currentPlayer.getRaceCardFormTableAndDiscard();
//                }
//                else {
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                }
//            }
//            break;
//            case GESICHTSSAUGER: { //0 = nichts, 1 = 1 Hand, 2 = 2 Hände, 3 = Rüstung, 4 = Kopf, 5 = Schuhe
//                currentPlayer.discardTableOfPlayerOnlyBonusWearBlocking(BonusWear.Blocking.HEAD);
//                currentPlayer.levelDown();
//            }
//            break;
//            case PAVILLON: {
//                currentPlayer.levelDown();
//                currentPlayer.levelDown();
//                currentPlayer.levelDown();
//            }
//            break;
//            case GEMEINEGHOULE: {
//                currentPlayer.setLevel(currentPlayer.getLowestLevelOfAllPlayers());
//            }
//            break;
//            case ORKS: {
//                int diced = Dice.throwDice();
//                if(diced == 1 || diced == 2){
//                    currentPlayer.death();
//                    return;
//                }
//                else if(diced == 3){
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    return;
//                }
//                else if(diced == 4){
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    return;
//                }
//                else if(diced == 5){
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    return;
//                }
//                else if(diced == 6){
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    currentPlayer.levelDown();
//                    return;
//                }
//            }
//            break;
//            case LAUFENDENASE: {
//                //SPEZIALFALL!! Sollte schon abgehandelt sein
//            }
//            break;
//            case NETZTROLL: {
//                if(currentPlayer.hasShoes()){
//                    currentPlayer.discardTableOfPlayerOnlyBonusWearBlocking(BonusWear.Blocking.HEAD);
//                    currentPlayer.levelDown();
//                }
//            }
//            break;
//            case BIGFOOT: {
//                currentPlayer.discardTableOfPlayerOnlyBonusWearBlocking(BonusWear.Blocking.HEAD);
//            }
//            break;
//            case ZUNGENDÄMON: {
//                currentPlayer.levelDown();
//                currentPlayer.levelDown();
//                if(currentPlayer.playerRace == Player.PlayerRace.ELF){
//                    currentPlayer.levelDown();
//                }
//            }
//            break;
//            case VAMPIR: {
//                currentPlayer.levelDown();
//                currentPlayer.levelDown();
//                currentPlayer.levelDown();
//            }
//            break;
//            case VERSICHERUNGSVERTRETER: {
//                //TODO: Du kaufst eine Versicherung. Verliere 3 Gegenstände. Hast du nicht genug, verlierst du alles, was du hast.
//            }
//            break;
//            case BEKIFFTERGOLEM: {
//                currentPlayer.death();
//            }
//            break;
//            case SCHRECKEN: {
//                currentPlayer.death();
//            }
//            break;
//            case HIPPOGREIF: {
//                currentPlayer.setLevel(5);
//                currentPlayer.loseHand();
//            }
//            break;
//            case KOENIGTUT: {
//                currentPlayer.loseHand();
//            }
//            break;
//            case GRUFTIGEGEBRUEDER: {
//                currentPlayer.setLevel(1);
//            }
//            break;
//            case KRAKZILLA:
//            case BULLROG:
//            case PLUTONIUMDRACHE: {
//                currentPlayer.death();
//            }
//            break;
//            default: //TODO: Error ausgeben
//                break;
//
//        }
//
//    }
//
//    /**
//     * Computes the Runnaway (weglaufen)
//     */
//    public boolean runaway(Monster this, Player currentPlayer, int currentRunaway){
//        int result = Dice.getRandom(1,6) + currentRunaway;
//        if (result >= 5){ //positiv für Player; Zustand: "escaped" & "Entkommen"
//
//            //Mr. Bones Monster. Man verliert auch beim erfolgreichen runaway eine level
//            if(this.monsterName == Monstername.MRBONES){
//                currentPlayer.levelDown();
//                return true;
//            }
//            //König Tut: man verliert 2 Stufen, auch wenn man entkommen ist
//            else if(this.monsterName == Monstername.KOENIGTUT){
//                currentPlayer.levelDown();
//                currentPlayer.levelDown();
//                return true;
//            }
//            return true;
//
//        }//negativ für Player
//        else {
//            return false;
//        }
//    }
//
//    /**
//     * Checkt ob der Player aufgrund seines leveln (nicht kampfstufe) gegen das monster kämpfen muss
//     * @param currentPlayer
//     * @return true wenn der Player kämpfen muss
//     */
//    public boolean checkIfFight(Player currentPlayer) {
//        //hippogreif, könig tut, gruftige gebrüder
//        if(this.monsterName == Monstername.HIPPOGREIF || this.monsterName == Monstername.KOENIGTUT || this.monsterName == Monstername.GRUFTIGEGEBRUEDER){
//            if(currentPlayer.level <= 3){
//                return false;
//            }
//        }
//        //krakzilla
//        else if(this.monsterName == Monstername.KRAKZILLA){
//            if(currentPlayer.playerRace == Player.PlayerRace.ELF){ //wenn Player ein Elf ist, muss er kämpfen
//                return true;
//            }
//            else if(currentPlayer.level <= 4) {
//                return false;
//            }
//        }
//        //bullrog
//        else if(this.monsterName == Monstername.BULLROG){
//            if(currentPlayer.level <= 4){
//                return false;
//            }
//        }
//        //plutoniumdrache
//        else if (this.monsterName == Monstername.PLUTONIUMDRACHE){
//            if(currentPlayer.level <= 5){
//                return false;
//            }
//        }
//        return true;
//    }
//}

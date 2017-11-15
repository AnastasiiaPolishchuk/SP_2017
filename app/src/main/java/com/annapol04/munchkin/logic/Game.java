package com.annapol04.munchkin.logic;

import java.util.ArrayList;

/**
 * Created by Falco on 26.10.2017.
 */

public class Game {

    ////~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ F I E L D S ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public int session;
    public ArrayList<Card> fightDeck = new ArrayList<Card>();
    //Alle aktiven an dieser Session spielenden Player: In der Playerklasse die ArrayList: playerList

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ M E T H O D S ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Initialisiere Spiel
     */
    public void startGame(){
        //Player objekte Erzeugen
        createCards();

        //Karten austeilen
        handOutCardsForAllPlayers();

        //Player IDs erstellen
        //start Player auswählen

        while(checkWin(Player.playerList)){
            for (Player currentPlayer: Player.playerList) {
                move(currentPlayer);
            }
            return;
        }
    }



    /**
     * Repräsentiert ein move
     * @param player aktueller Player
     */
    public void move(Player player){
        phase1(player);
//        phase2(player);
//        phase3(player);
        phase4(player);
        fightDeck.clear();
    }


    /**
     * Repräsentiert die Phase1 aus dem Automaten
     * @param Player
     */
    public void phase1(Player Player){

        //TODO: Einfügen, dass der Player Karten von seiner Hand auf den Tisch legen kann

        Card currentCard = DoorDeck.draw();
        fightDeck.add(currentCard);

        if(currentCard instanceof Monster){ //Entspricht im Automaten dem Zustand 1a
            if(checkIfFight(Player, (Monster)currentCard)){
                fight(Player, (Monster)currentCard);
                phase4(Player);
                return;
            }
        }

        else if(currentCard instanceof Curse){ //Entspricht im Automaten dem Zustand 1b
            fluch(Player, (Curse)currentCard);
            //TODO: Player fragen, wie er fortfahren möchte: Phase 2 oder 3
            if(){
                phase2(Player);
            }
            else{
                phase3(Player);
            }
        }

        else { // TODO: Monsterlevel, race: --> entweder auf die Hand nehmen, auf den Tisch legen oder ablegen //Entspricht im Automaten dem Zustand 1c
            if(){ //Hand nehmen
                Player.hand.add(currentCard);
                fightDeck.remove(currentCard);
            }
            else if(){
                Player.table.add(currentCard);
                fightDeck.remove(currentCard);
            }
            else{
                discardDoor(currentCard);
            }

            //TODO: Player fragen, wie er fortfahren möchte: Phase 2 oder 3
            if(){
                phase2(Player);
            }
            else{
                phase3(Player);
            }
        }
    }


    /**
     * Repräsentiert die Phase2 aus dem Automaten
     * @param Player
     */
    public void phase2(Player Player){
        Monster monsterToFight = PlayerWaehltMonsterAus(Player);
        fight(Player, monsterToFight);
        phase4(Player);
    }



    /**
     * Repräsentiert die Phase3 aus dem Automaten
     * @param Player
     */
    public void phase3(Player Player){
        Player.hand.add(DoorDeck.draw());
        phase4(Player);
    }



    /**
     * Repräsentiert die Phase4 aus dem Automaten
     * @param Player
     */
    public void phase4(Player Player){
        if(Player.race == 2 && Player.hand.size() > 6){ //zwerg
            //TODO: Player fragen, welche Karten er ablegen möchte und dann ablegen

        }
        else if(Player.hand.size() > 5){
            //TODO: Player fragen, welche Karten er ablegen möchte und dann ablegen
        }
    }



    /**
     * Checkt ob der Player aufgrund seines leveln (nicht kampfstufe) gegen das monster kämpfen muss
     * @param Player
     * @param currentCard
     * @return true wenn der Player kämpfen muss
     */
    private boolean checkIfFight(Player Player, Monster currentCard) {
        //hippogreif, könig tut, gruftige gebrüder
        if(currentCard.id == 32 || currentCard.id == 33 || currentCard.id == 34){
            if(Player.level <= 3){
                return false;
            }
        }
        //krakzilla
        else if(currentCard.id == 35){
            if(Player.race == 1){ //wenn Player ein Elf ist, muss er kämpfen
                return true;
            }
            else if(Player.level <= 4) {
                return false;
            }
        }
        //bullrog
        else if(currentCard.id == 36){
            if(Player.level <= 4){
                return false;
            }
        }
        //plutoniumdrache
        else if (currentCard.id == 37){
            if(Player.level <= 5){
                return false;
            }
        }
        return true;
    }


    /**
     * Repräsentiert den Kampf
     * @param Player der den Kampf durchführen muss
     * @param currentCard Card, also in diesem Fall immer das Monster, dass gegen den Player kämpft
     */
    private void fight(Player Player, Monster currentCard) {

        //In diese ArrayListen kommen die Karten rein, die der Player für sich oder das Monster einspielt
        ArrayList<Card> forThePlayer = new ArrayList<Card>();
        ArrayList<Card> forTheMonster = new ArrayList<Card>();



        //hier werden erstmal die ausgangsstufen der beiden Kontrahenten berechnet
        int playerTotal = Player.fightLevel;
        int monsterTotal = currentCard.level;

        //TODO: log ausgeben

        //TODO: elfen oder zwergen bonus/malus berechnen
        // Monster die einen bonus gegenüber Elfen haben
        if(Player.race == 1){
            if (currentCard.id == 2){ //sabbernder schleim
                monsterTotal = monsterTotal + 4;
            }

            if (currentCard.id == 12){ //Leprachaun
                monsterTotal = monsterTotal + 5;
            }
            if (currentCard.id == 35){
                playerTotal = playerTotal + 4;
            }


            if (currentCard.id == 20){ //Gesichtssauger
                monsterTotal = monsterTotal + 6;
            }
        }
        //MOnster, die einen Bonus gegenüber Zwergen haben
        if(Player.race == 2){
            if (currentCard.id == 14){
                monsterTotal = monsterTotal + 5;
            }
            if (currentCard.id == 23){
                monsterTotal = monsterTotal + 6;
            }
            if (currentCard.id == 26){
                monsterTotal = monsterTotal + 3;
            }
        }

        // ~~~~~~~~~~~~~~~~~~~ SPEZIELLE EIGENSCHAFTEN VON BESTIMMTEN MONSTERN ~~~~~~~~~~~~~~~~~~~
        //Anwalt: Würfle. Bei einer 1 kannst du, anstatt ihn zu bekämpfen, 2 Schätze ablegen und verdeckt 2 neue draw." Ansonsten normal kämpfen
        if(currentCard.id == 15){
            if(Dice.throwDice() == 1){
//                Card karte1 = playerChoosesCard(Player);
//                Card karte2 = playerChoosesCard(Player);
//                discardTreasure(karte1);
//                discardTreasure(karte2);

                discardTreasure(playerChoosesCard(Player));
                discardTreasure(playerChoosesCard(Player));

                Player.hand.add(TreasureDeck.draw());
                Player.hand.add(TreasureDeck.draw());
                return;
            }
        }
        //Pikotzu: extrastufe, wenn du es ohne hilfe und boni besiegst
        if (currentCard.id == 17){
            if(Player.level > currentCard.level){ // Player gewinnt gegen pikotzu. ANsonsten geht es normal weiter
                int treasuresToDraw = currentCard.treasure;
                drawTresures(Player, treasuresToDraw);
                discardDoor(currentCard);
                return;
            }
        }
        //Pavillon: Boni können dir nicht helfen. Du musst dich dem Pavillon allein stellen.
        if(currentCard.id == 21){
            if (Player.level > currentCard.level){
                int treasuresToDraw = currentCard.treasure;
                drawTresures(Player, treasuresToDraw);
                discardDoor(currentCard);
                return;
            }
            else if(Player.level == currentCard.level){
                draw(Player, currentCard);
                discardDoor(currentCard);
                return;
            }
            else {
                lose(Player, currentCard);
                discardDoor(currentCard);
                return;
            }
        }
        //Gemeine Ghoule: Gegen sie dürfen keine Gegenstände oder andere Boni eingesetzt werden - kämpfe nur mit deiner Charakterstufe
        if(currentCard.id == 22){
            if (Player.level > currentCard.level){
                int treasuresToDraw = currentCard.treasure;
                drawTresures(Player, treasuresToDraw);
                discardDoor(currentCard);
                return;
            }
            else if(Player.level == currentCard.level){
                draw(Player, currentCard);
                discardDoor(currentCard);
                return;
            }
            else {
                lose(Player, currentCard);
                discardDoor(currentCard);
                return;
            }
        }
        //Zungendämon: Höllenkreatur. Lege einen Gegenstand deiner Wahl vor dem Kapf ab
        if(currentCard.id == 27){
            //TODO: fertig machen, in KOmbination, dass ich bei der Methode playerChoosesCard() einen Parameter übergeben lassen kann, der bestimmt, aus welchen karten der Player auswählen kann
            playerChoosesCard(Player);
        }
        //Versicherungsvertreter
        if(currentCard.id == 29){
            if (Player.fightLevel > currentCard.level){
                int treasuresToDraw = currentCard.treasure;
                drawTresures(Player, treasuresToDraw);
                discardDoor(currentCard);
                return;
            }
            else if(Player.fightLevel == currentCard.level){
                draw(Player, currentCard);
                discardDoor(currentCard);
                return;
            }
            else {
                lose(Player, currentCard);
                discardDoor(currentCard);
                return;
            }
        }
        //Bekiffter Golem: Wähle aus: kämpfen oder einfach vorbeigehen lassen.
        if (currentCard.id == 30){
            askPlayerIfHeWantsToFight();
            //TODO: fertig machen
        }


        //Jetzt muss der Player die Möglichkeit bekommen, alle Karten einzuspielen, auf die er Lust hat (& und die möglich sind)
        //BEDENKE: es können theoretisch alle Handkarten des Players in den Kampf eingespielt werden! Also muss fünf Mal überprüft werden, ob der Player eine Card noch einspielen will
        //Dieser Einspielprozess wird in Player und Monster unterteilt. Also zuerst wird der Player gefragt, welche Karten er alle für sich einspielen will, und dann kann er alle Karten für/gegen das Monster einspielen.


        //  a) "Nur einmal einsetzbar" karte (Doppelgänger, Freundschaftstrank, Polly)
        //  b) Monsterlevel erhöhen



        // ~~~~~~~~~~~~~~~~~~~ Player ~~~~~~~~~~~~~~~~~~~
        //TODO: log augeben, dass der Player nun für sich karten einspielt & dass die karten in der eingegebenen Reihenfolge abgearbeitet werden
        //Karten für Player: Klassenoptionen: BonusSide, Special
        for (int i = 0; i < 5; i++){
            //TODO: einfügen, wenn der Player nichts (mehr) einspielen will. Muss eventuell mit der playerChoosesCard() Methode verknüpft werden
            Card playedCard = playerChoosesCard(Player);
            forThePlayer.add(playedCard);
            fightDeck.add(playedCard);
        }
        for (Card current: forThePlayer) {
            if(current instanceof BonusSide){ //Card wird im falle "true" automatisch gecastet
                playerTotal = playerTotal + ((BonusSide) current).bonus;
            }
            if(current instanceof Special){
                //Magische Lampe: (112,"Nur in der Runde spielbar. Sie beschwört einen Geist, der ein Monster verschwinden lässt, selbst wenn dein Weglaufenwurf verpatzt wurde und es dich fangen würde. War es das einzige Monster, erhälst du seinen treasure, aber keine level. Nur einmal einsetzbar.");
                if (current.id == 112){
                    drawTresures(Player, currentCard.treasure);
                    return;
                }

                //Doppelgänger
                if(current.id == 117){
                    playerTotal = playerTotal * 2;
                }

                //Freunschaftstrank: den raum durchsuchen: eine türkarte verdeckt draw
                if (current.id == 119){
                    Player.hand.add(DoorDeck.draw());
                    return;
                }

                //polly
                if(current.id == 120){
                    drawTresures(Player, 1);
                    return;
                }
            }
        }
        //TODO: log ausgeben: So in der Art: "Player hat nun die Stärke...."



        // ~~~~~~~~~~~~~~~~~~~ MONSTER ~~~~~~~~~~~~~~~~~~~
        //TODO: log augeben, dass der Player nun für das Monster karten einspielt & dass die karten in der eingegebenen Reihenfolge abgearbeitet werden
        //Karten für Monster: Klassenoptionen: BonusSide, Special(ist hier theoretisch möglich, macht praktisch aber keinen Sinn), Monsterlevel
        for (int i = 0; i < 5; i++){
            //TODO: einfügen, wenn der Player nichts (mehr) einspielen will. Muss eventuell mit der playerChoosesCard() Methode verknüpft werden
            Card playedCard = playerChoosesCard(Player);
            forThePlayer.add(playedCard);
            fightDeck.add(playedCard);
        }
        for (Card current: forTheMonster) {
            if (current instanceof BonusSide){
                monsterTotal = monsterTotal + ((BonusSide) current).bonus;
            }
            if(current instanceof  Monsterlevel){
                monsterTotal = monsterTotal + ((Monsterlevel) current).monsterBonus;
            }
        }

        //TODO: Log ausgeben


        // ~~~~~~~~~~~~~~~~~~~ AUSWERTUNG ~~~~~~~~~~~~~~~~~~~
        if(playerTotal > monsterTotal){
            //TODO: log ausgeben

            int treasuresToDraw = currentCard.treasure; //TODO: zusätzliches dazuzählen
            if(currentCard.id == 5 && Player.race == 1){ //Topfpflanze + Player ist ein Elf
                treasuresToDraw++;
            }

            drawTresures(Player, treasuresToDraw);
            discardDoor(currentCard);
        }
        if(playerTotal == monsterTotal){
            //TODO: log ausgeben
            draw(Player, currentCard);
            discardDoor(currentCard);
        }
        if(playerTotal < monsterTotal){
            //TODO: log ausgeben
            lose(Player, currentCard);
            discardDoor(currentCard);
        }
    }




    /**
     * Berechnet das runaway: erst wird überprüft, ob der Player eine Spezialkarte gespielt hat, die ihn rettet oder hilft, dann wird das eigentliche runaway berechnet (in eigener Methode)
     * Auch wird mit einberechnet, ob das Monster spezielle Eigenschaften, das runaway betreffend, hat
     * @param Player
     * @param currentCard
     */
    private void lose(Player Player, Monster currentCard) {

        //Laufende Nase: super spezialfall
        if(currentCard.id == 24){
            Player.levelDown();
            Player.levelDown();
            Player.levelDown();
        }

        int currentRunaway = Player.runAway;

        //Überprüfen, ob das Monster spezielle Eigenschaften, das runaway betreffend, hat
        if(currentCard.id == 3){ //Lahmer Goblin
            currentRunaway++;
        }
        if(currentCard.id == 6){ // fliegende Frösche
            currentRunaway = currentRunaway - 1;
        }
        if(currentCard.id == 7){ //Gallert-Oktaoeder
            currentRunaway++;
        }
        if(currentCard.id == 13){ //schnecken auf speed
            currentRunaway = currentRunaway - 2;
        }


        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~ Monster (spezielle Eigenschaften aus der Beschreibung des Monsters) ~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //TODO: monsterespezialitäten hinzufügen
        //Pit Bull: keine schätze
        if(currentCard.id == 10){
            //hammer, scharferStreitkolben, hellebarde, napalstab, stange, rapier
            if(Player.searchBoth(64)){
                fightDeck.add(Player.getAndRemoveBoth(64));
                return;
            }

            if(Player.searchBoth(65)){
                fightDeck.add(Player.getAndRemoveBoth(65));
                return;
            }

            if(Player.searchBoth(67)){
                fightDeck.add(Player.getAndRemoveBoth(67));
                return;
            }

            if(Player.searchBoth(69)){
                fightDeck.add(Player.getAndRemoveBoth(69));
                return;
            }

            if(Player.searchBoth(75)){
                fightDeck.add(Player.getAndRemoveBoth(75));
                return;
            }

            if(Player.searchBoth(99)){
                fightDeck.add(Player.getAndRemoveBoth(99));
                return;
            }
        }



        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~ EXTRAKARTEN (von der Hand des Players) ~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Special playedCard = playerChoosesCard(Player);
        if(playedCard.id == 112){ // Magische Lampe: Nur in der Runde spielbar. Sie beschwört einen Geist, der ein Monster verschwinden lässt, selbst wenn dein Weglaufenwurf verpatzt wurde und es dich fangen würde. War es das einzige Monster, erhälst du seinen treasure, aber keine level. Nur einmal einsetzbar.
            //TODO: log ausgeben, dass der Player nochmal davon gekommen ist...
            fightDeck.add(playedCard);
            drawTresures(Player, currentCard.treasure);
            discardTreasure(playedCard); //magische lampe ablegen
            return;
        }
        else if (playedCard.id == 113){ //Stiefel
            //TODO: log ausgeben, dass das runaway sich durch die stiefel jz verändert hat...
            fightDeck.add(playedCard);
            currentRunaway = currentRunaway + 2;
            discardTreasure(playedCard);
            //gehe zum letzten if dieser methode
        }
        else if (playedCard.id == 116){ //Fertigmauer: sofortiges Entkommen
            //TODO: log ausgeben, dass das runaway sich durch die stiefel jz verändert hat...
            fightDeck.add(playedCard);
            return;
        }
        else if (playedCard.id == 114){ //tuba
            currentRunaway = currentRunaway + 3;
            fightDeck.add(playedCard);
            discardTreasure(playedCard);

            boolean escaped = runaway(Player, currentRunaway, currentCard); //muss hier gemacht werden, da man durch die tuba extra einen treasure draw darf
            if(escaped){
                drawTresures(Player, 1);
            }
            else {
                //Gezinkter wuerfel oder unsichtsbarkeittrank kann einen nochmal retten. man muss abfragen, ob der Player diesen spielen will (falls er ihn hat)
                Special extraCard2 = playerChoosesCard(Player);
                if (extraCard2.id == 115){
                    //TODO: ausgeben, dass der Player glueck gehabt hat
                    fightDeck.add(playedCard);
                    return;
                }
                else if (extraCard2.id == 118){ // unsichtsbarkeitstrank
                    //TODO: ausgeben, dass der Player glueck gehabt hat
                    fightDeck.add(playedCard);
                    return;
                }

                badThings(Player, currentCard);
            }
        }//tuba ende


        boolean escaped = runaway(Player, currentRunaway, currentCard); //true wenn er es geschafft hat

        //wenn er es geschafft hat
        if(escaped){
            //TODO: phase 1 ist beendet und der Player muss gefragt werden, was er nun machen will
            return;

        }
        //wenn er es nicht geschafft hat
        else{

            //Gezinkter wuerfel oder unsichtsbarkeittrank kann einen nochmal retten. man muss abfragen, ob der Player diesen spielen will (falls er ihn hat)
            Special extraCard2 = playerChoosesCard(Player);
            if (extraCard2.id == 115){
                //TODO: ausgeben, dass der Player glueck gehabt hat
                fightDeck.add(playedCard);
                return;
            }
            else if (extraCard2.id == 118){ // unsichtsbarkeitstrank
                //TODO: ausgeben, dass der Player glueck gehabt hat
                fightDeck.add(playedCard);
                return;
            }
            badThings(Player, currentCard);
        }
    }


    /**
     * Berechnet das runaway eine Players. (random zahl + currentRunaway) muss mindestens 5 sein, um weg zu laufen
     * @param Player der runaway muss
     * @param currentRunaway der bonus, mit dem der Player versucht weg zu laufen: immer größer als 0
     * @return true wenn der Player erfolreich escaped ist
     *          false ansonsten
     */
    private boolean runaway(Player Player, int currentRunaway, Monster currentMonster) {

        int result = Dice.getRandom(1,6) + currentRunaway;
        if (result >= 5){ //positiv für Player; Zustand: "escaped" & "Entkommen"

            //Mr. Bones Monster. Man verliert auch beim erfolgreichen runaway eine level
            if(currentMonster.id == 9){
                Player.levelDown();
            }
            //König Tut: man verliert 2 Stufen, auch wenn man entkommen ist
            if(currentMonster.id == 33){
                Player.levelDown();
                Player.levelDown();
            }

            return true;

        }//negativ für Player
        else {
            return false;
        }
    }


    private Special playerChoosesCard(Player Player) {
        //TODO: karte aus der Hand des Sielers entfernen (hand.remove())
        //TODO: parameter adden, dass ich auswählen kann, welche art von karten ich dem Player zur auswahl geben will
    }


    private void badThings(Player Player, Monster currentMonster) {

        //Filzläuse
        if(currentMonster.id == 1){
            //0 = nichts, 1 = 1 Hand, 2 = 2 Hände, 3 = Rüstung, 4 = Kopf, 5 = Schuhe
            Player.discardTableOfPlayerOnlyBonusWearBlocking(3);
            Player.discardTableOfPlayerOnlyBonusWearBlocking(5);
            return;
        }

        //Sabbernder Schleim
        if(currentMonster.id == 2){
            if(Player.hasShoes()){
                Player.discardTableOfPlayerOnlyBonusWearBlocking(5);
                return;
            }
            else {
                Player.levelDown();
            }
            return;
        }

        //Lahmer Goblin
        if(currentMonster.id == 3){
            Player.levelDown();
            return;
        }

        //Hammer-Ratte
        if(currentMonster.id == 4){
            Player.levelDown();
            return;
        }

        //Topfplanze: Automatische Flucht
        if(currentMonster.id == 5){
            //TODO: muss hier noch was gemacht werden?
            return;
        }

        //Fliegende Frösche
        if(currentMonster.id == 6){
            Player.levelDown();
            Player.levelDown();
            return;
        }

        //Gallert-Oktaeder
        if(currentMonster.id == 7){
            Player.discardTableOfPlayerOnlyBonusWearSize(1);
            return;
        }

        //Großes wüntendes Huhn
        if(currentMonster.id == 8){
            Player.levelDown();
            return;
        }

        //mr. bones
        if(currentMonster.id == 9){
            Player.levelDown();
            Player.levelDown();
            return;
        }

        //Pit Bull
        if(currentMonster.id == 10){
            Player.levelDown();
            Player.levelDown();
            return;
        }

        //Harfien
        if(currentMonster.id == 11){
            Player.levelDown();
            Player.levelDown();
            return;
        }

        //Leprachaun
        if(currentMonster.id == 12){
            //TODO: Ordentlich die Card des Players einlsen, die er ablegen will
            Card cardToDiscard1 = Game.playerChoosesCard();
            Card cardToDiscard2 = Game.playerChoosesCard();
            return;
        }

        //Schnecken auf Speed
        if(currentMonster.id == 13){
            int diced = Dice.throwDice();
            for(int i = 0; i < diced; i++){
                //TODO: input, welche Karten der Player ablegen will
            }

            return;
        }

        //Untotes Pferd
        if(currentMonster.id == 14){
            Player.levelDown();
            Player.levelDown();
            return;
        }

        //Anwalt
        if(currentMonster.id == 15){
            for(int i = 0; i < Player.numberOfPlayers; i++){
                //TODO: input, welche Karten der Player ablegen will
            }
            return;
        }

        //Entikore
        if(currentMonster.id == 16){
            //TODO: Player durch die GUI fragen, was er machen will
            int inputPlayer = Playerfragen();
            if(inputPlayer == 1){
                Player.discardHand();
            }
            else{
                Player.levelDown();;
                Player.levelDown();
            }
            return;
        }

        //Pikotzu
        if(currentMonster.id == 17){
            Player.discardHand();
            return;
        }

        //Kreischender Depp
        if(currentMonster.id == 18){ //0 = keine race, 1 = elf, 2 = zwerg
            if(Player.race != 0){
                Player.race = 0;
                Player.getRaceCardFormTableAndDiscard();
            }
            return;
        }

        //Amazone: Dir wird von einer Frau in den Arsch getreten. Dein Macho Munchkin-Stolz ist dahin. Du verlierst alle racen. Wenn du keine hast, verliere 3 Stufen.
        if(currentMonster.id == 19){
            if(Player.race != 0){
                Player.race = 0;
                Player.getRaceCardFormTableAndDiscard();

                return;
            }
            else {
                Player.levelDown();
                Player.levelDown();
                return;
            }
        }

        //Gesichtssauger
        if(currentMonster.id == 20){
            Player.discardTableOfPlayerOnlyBonusWearBlocking(4);
            Player.levelDown();

            return;
        }

        //Pavillon
        if(currentMonster.id == 21){
            Player.levelDown();
            Player.levelDown();
            Player.levelDown();

            return;
        }

        //Gemeine Ghoule
        if(currentMonster.id == 22){
            Player.setLevel(Player.getLowestLevelOfAllPlayers());
            return;
        }

        //Orks
        if(currentMonster.id == 23){
            int diced = Dice.throwDice();
            if(diced == 1 || diced == 2){
                Player.death();
                return;
            }
            else if(diced == 3){
                Player.levelDown();
                Player.levelDown();
                Player.levelDown();
                return;
            }
            else if(diced == 4){
                Player.levelDown();
                Player.levelDown();
                Player.levelDown();
                Player.levelDown();
                return;
            }
            else if(diced == 5){
                Player.levelDown();
                Player.levelDown();
                Player.levelDown();
                Player.levelDown();
                Player.levelDown();
                return;
            }
            else if(diced == 6){
                Player.levelDown();
                Player.levelDown();
                Player.levelDown();
                Player.levelDown();
                Player.levelDown();
                Player.levelDown();
                return;
            }
        }

//        //Laufende Nase: Super Spezialfall: Sie kann dich überall wegputzen. Verlierst du den Kampf, kannst du nicht fliehen. Nichts wird dir helfen. Du verlierst 3 Stufen.
//        if(currentMonster.id == 24){
//
//            return;
//        }

        //Netz-Troll: Er klammert sich an deinem Haupt fest. Verliere deine Kopfbedeckung und zwei Stufen.
        if(currentMonster.id == 25){
            if(Player.hasShoes()){
                Player.discardTableOfPlayerOnlyBonusWearBlocking(4);
                Player.levelDown();

                return;
            }
        }

        //Big Foot
        if(currentMonster.id == 26){
            Player.discardTableOfPlayerOnlyBonusWearBlocking(4);
            return;
        }

        //Zungendämon
        if(currentMonster.id == 27){
            Player.levelDown();
            Player.levelDown();
            if(Player.race == 1){
                Player.levelDown();
            }
            return;
        }

        //vampir
        if(currentMonster.id == 28){
            Player.levelDown();
            Player.levelDown();
            Player.levelDown();
            return;
        }

        //versicherungsvertreter: Du kaufst eine Versicherung. Verliere 3 Gegenstände. Hast du nicht genug, verlierst du alles, was du hast.
        if(currentMonster.id == 29){
            //TODO: Du kaufst eine Versicherung. Verliere 3 Gegenstände. Hast du nicht genug, verlierst du alles, was du hast.
            return;
        }

        //bekiffterGolem
        if(currentMonster.id == 30){
            Player.death();
            return;
        }

        //Schrecken: Unaussprechlich schrecklicher Tod für jeden.
        if(currentMonster.id == 31){
            Player.death();
            return;
        }

        //hippogreif
        if(currentMonster.id == 32){
            Player.setLevel(5);
            Player.loseHand();
            return;
        }

        //König Tut
        if(currentMonster.id == 33){
            Player.loseHand();
            return;
        }

        //Gruftige Gebrüder
        if(currentMonster.id == 34){
            Player.setLevel(1);
            return;
        }

        //Krakzilla
        if(currentMonster.id == 35){
            Player.death();
            return;
        }

        //bullrog
        if(currentMonster.id == 36){
            Player.death();
            return;
        }

        //Plutoniumdrache
        if(currentMonster.id == 37){
            Player.death();
            return;
        }

    }

    /**
     * Repräsentiert den Zustand gleichstand. Bei Gleichstand zischen MonsterStugeGesamt und PlayerStufeGesamt, wird gewürfelt. Der Player gewinnt, falls der Wurf KEINE 1 ist.
     * Also zu einer Wahrscheinlichkeit von 5/6
     * @param Player der kurz vor dem gewinnen/lose ist
     * @param currentMonster gegen das der Player gewinnen musst
     */
    private void draw(Player Player, Monster currentMonster) { // 5/6 dass man gewinnt
        int diced = Dice.getRandom(1, 6);
        if (diced != 1){
            drawTresures(Player, currentMonster.treasure);
        }
        else{
            badThings(Player, currentMonster);
        }
    }

    /**
     * Repräsentiert den Zustand gewinnen. Hier komme ich nur rein, wenn schon alle Stufen ausgewertet wurde und alle Boni (Egal für welche Seite, Monsterdtufe) eingespielt wurden.
     * Also wenn der Kampf eindeutig zu Gunsten des Players entschieden ist.
     * @param Player
     * @param treasuresToDraw Ich bekomme für diese Methode bereits die fertig addierte Zahl der zu drawden Schätze. Muss also vor dem Aufruf fertig berechnet werden.
     */
    private void drawTresures(Player Player, int treasuresToDraw) {

        //richtige Anzahl an Schätzen draw
        for(int i=0; i < treasuresToDraw; i++){
            Player.hand.add(TreasureDeck.draw());
        }
        //TODO: log ausgeben, WIE VIELE karten gezogen wurden

        Player.levelUp();
    }


    /**
     * Repräsentiert das draw einer Card
     * @param Player der verflucht wird
     * @param currentCard Card, Curse, der auf den Player wirkt
     */
    private void fluch(Player Player, Curse currentCard) {
        if (hatWunschring(Player)){
            //Wunschring des Players wird falls er einen hat in der Methode hatWunschring abgelegt
            discardDoor(currentCard);
            fightDeck.remove(currentCard);
        }
        else {
            switch (currentCard.id){
                case 38: Player.searchAndDiscardBiggestBonus(); break;
                case 39: Player.levelDown(); break;
                case 40: Player.levelDown(); break;
                case 41: Player.discardTableOfPlayerOnlyBonusWearBlocking(3); break;
                case 42: Player.discardTableOfPlayerOnlyBonusWearBlocking(5); break;
                case 43: Player.discardTableOfPlayerOnlyBonusWearBlocking(4); break;
                case 44: Player.getRaceCardFormTableAndDiscard(); break;
                case 45: Player.getRaceCardFormTableAndDiscard(); break;
                case 49: Player.levelDown(); Player.levelDown(); break;
                case 50: Player.discardTableOfPlayerOnlyBonusWearSize(1); break;
                case 51: Player.discardTableOfPlayerOnlyBonusWearSize(0); break;
                case 52: Player.discardTableOfPlayerOnlyBonusWearSize(0); break;
                case 53: if(Player.race != 0){
                    Player.getRaceCardFormTableAndDiscard();
                }
                else {
                    Player.levelDown();
                }
                break;
                case 54: if(Player.race != 0){
                    Player.getRaceCardFormTableAndDiscard();
                }
                break;
                default: //TODO: error ausgeben
                break;
            }
        }
    }


    private void handOutCardsForAllPlayers() {
        for (Player currentPlayer: Player.playerList) {
            currentPlayer.handOutCards();
        }
    }


    private boolean hatWunschring(Player Player){
        boolean hasWunschring = false; //nur der Wunschring entfernt den Curse sofort

        //Wunschring in der Hand des Players suchen
        for (Card searchCard: Player.hand) {
            if (0 == searchCard.name.compareTo("Wunschring"))
                Player.hand.remove(searchCard);
                discardTreasure(searchCard);
                hasWunschring = true;
        }
        return hasWunschring;
    }


    /**
     * Ablegen einer Card auf den treasure-discardDeck und entfernen der Card vom fightDeck
     * @param currentCard
     */
    public void discardTreasure(Card currentCard){
        fightDeck.remove(currentCard);
        //TODO: überprügen, dass ich überall wo discardTreasure aufgerufen wurde auch die Card vom Player antfernen, da dies in der Methode nicht geschieht
        TreasureDeck.discardDeck.add(currentCard);
    }


    /**
     * Ablegen einer Card auf den TÜR-discardDeck und entfernen der Card vom fightDeck
     * @param currentCard
     */
    public void discardDoor(Card currentCard){
        if (fightDeck.contains(currentCard)){
            fightDeck.remove(currentCard);
            DoorDeck.discardDeck.add(currentCard);
            return;
        }
        //TODO: Fehlermeldung: karte Sollte abgelegt weden aber die war nicht im fightDeck
    }


    /**
     * Check, ob es bereits einen Sieger gibts
     * @param liste
     * @return false, wenn ein Player level 10 ist; ansonsten true
     */
    private boolean checkWin(ArrayList<Player> liste) {
        for (Player currentPlayer: liste) {
            if(currentPlayer.level == 10){
                return false;
            }
        }
        return true;
    }


    /**
     * Erstellt alle Karten Objekte und fügt sie zu den dazugehörigen Stapel zu
     */
    public void createCards(){

        //Monster
        Monster filzlaeuse = new Monster(1, 0,"Filzläuse","Denen kannst du nicht entkommen!", 1, 1, "Lege deine Rüstung und alle Gegenstände unterhalb der Hüfte ab.");
        Monster sabbernderSchleim = new Monster(2, 0, "Sabbernder Schleim", "Ekliger Schleim! +4 gegen Elfen.", 1, 1, "Lege das Schuhwerk, das du trägst, ab. Verliere eine level, falls du kein Schuhwerk trägst.");
        Monster lahmerGoblin = new Monster(3, 0, "Lahmer Goblin", "Du hast +1 beim runaway.", 1, 1, "Er verhaut dich mit seiner Krücke. Verliere 1 level.");
        Monster hammerRatte = new Monster(4, 0, "Hammer-Ratte", "Höllenkreatur.", 1, 1, "Sie verhaut dich. Verliere 1 level.");
        Monster Topfpflanze = new Monster(5, 0, "Topfpflanze", "Elfen draw 1 zusätzlichen treasure, nachdem sie besiegt wurde.", 1, 1, "Keine. Automatische Flucht.");
        Monster fliegendeFroesche = new Monster(6, 0, "Fliegende Frösche", "Du hast -1 auf runaway.", 2, 1, "Sie beißen! Verliere 2 Stufen.");
        Monster gallertOktaeder = new Monster(7, 0, "Gallert-Oktaeder", "Du hast +1 auf runaway.", 2, 1, "Lass alle deine großen Gegenstände fallen.");
        Monster huhn = new Monster(8, 0, "Großes wütendes Huhn", "Grillhuhn ist lecker.", 2, 1, "Schmerzhaftes Hacken. Verliere 1 level.");
        Monster mrBones = new Monster(9, 0, "Mr. Bones", "Auch bei einer erfolgreichen Flucht verlierst du eine Sutufe.", 2, 1, "Seine knochige Berührung kostet dich 2 Stufen.");
        Monster pitBull = new Monster(10, 0, "Pit Bull", "Kannst du ihn nicht besiegen, darfst du ihn ablenken (automatische Flucht), indem du einen Stab oder Ähnliches fallen lässt.", 2, 1, "Bissspuren im Hintern. Verliere 2 Stufen.");
        Monster harfien = new Monster(11, 0, "Harfien", "Sie widerstehen Magie.", 4, 2, "Ihre Musik ist wirklich schlecht. Verliere 2 Stufen.");
        Monster leprachaun = new Monster(12, 0, "Leprachaun", "Widerlich! +5 gegen Elfen.", 4, 2, "Er nimmt von dir 2 Gegenstände.");
        Monster schnecken = new Monster(13, 0, "Schnecken auf Speed", "Du hast -2 auf runaway.", 4, 2, "Sie stehlen deinen treasure. Würfle und verliere entsprechend viele Gegenstände oder Karten von deiner Hand - deine Wahl.");
        Monster untotessPferd = new Monster(14, 0, "Untotes Pferd", "+5 gegen Zwerge", 4, 2, "Tritt, beißt und riecht furchtbar.");
        Monster anwalt = new Monster(15, 0, "Anwalt", "Würfle. Bei einer 1 kannst du, anstatt ihn zu bekämpfen, 2 Schätze ablegen und verdeckt 2 neue draw.", 6, 2, "Er überzieht dich mit einer Unterlassungsklage. Lege so viele Karten von deiner Hand ab, die der Anzahl der MitPlayer entspricht.");
        Monster entikore = new Monster(16, 0, "Entikore", "Magisches Wesen.", 6, 2, "Lege entweder deine ganze Hand ab oder verliere 2 Stufen.");
        Monster pikotzu = new Monster(17, 0, "Pikotzu","Du erhälst eine Extrastufe, wenn du es ohne Boni besiegst.", 6, 2, "Kostzestrahl-Angriff! Lege deine ganze Hand ab.");
        Monster kreischenderDepp = new Monster(18, 0, "Kreischender Depp","Heller, schriller Schrei!", 6, 2, "Du wirst zu einem normalen, langweiligen Menschen. Lege deine race ab, wenn du eine hast.");
        Monster amazone = new Monster(19, 0, "Amazone","Starke Kämpferin", 8, 2, "Dir wird von einer Frau in den Arsch getreten. Dein Macho Munchkin-Stolz ist dahin. Du verlierst alle racen. Wenn du keine hast, verliere 3 Stufen.");
        Monster gesichtssauger = new Monster(20, 0, "Gesichtssauger","Widerlich! +6 gegen Elfen.", 8, 2, "Wurde dein Gesicht runtergesaugt, verschwindet auch deine Kopfbedeckung. Lege die Kopfbedeckung, die du trägst, ab und verliere 1 level.");
        Monster pavillon = new Monster(21, 0, "Pavillon","Boni können dir nicht helfen. Du musst dich dem Pavillon allein stellen.", 8, 2, "Verliere 3 Stufen.");
        Monster gemeineGhoule = new Monster(22, 0, "Gemeine Ghoule","Gegen sie dürfen keine Gegenstände oder andere Boni eingesetzt werden - kämpfe nur mit deiner Charakterstufe.", 8, 2, "Deine level entspricht der niedrigsten level eines Characters im Spiel.");
        Monster orks = new Monster(23, 0, "3.872 Orks","+6 gegen Zewrge wegen uralter Feindschaft.", 10, 3, "Würfle. Bei einer 1 oder 2 trampeln sie dich zu Tode. Ansonsten verlierst du so viele Stufen, wie gewürfelt wurde.");
        Monster laufendeNase = new Monster(24, 0, "Laufende Nase","Willst du die Laufende Nase nicht bekämpfen, so hast du Pech gehabt. Du musst kämpfen.", 10, 3, "Sie kann dich überall wegputzen. Verlierst du den Kampf, kannst du nicht fliehen. Nichts wird dir helfen. Du verlierst 3 Stufen.");
        Monster netzTroll = new Monster(25, 0, "Netz-Troll","Hat keine besonderen Fähigkeiten und ist deshalb ziemlich sauer.", 10, 3, "Er klammert sich an deinem Haupt fest. Verliere deine Kopfbedeckung und zwei Stufen.");
        Monster bigfoot = new Monster(26, 0, "Bigfoot","+3 gegen Zwerge", 12, 3, "Quetscht dich flach und frisst deinen Hut. Du verlierst deine Kopfbedeckung.");
        Monster zungendämon = new Monster(27, 0, "Zungendämon","Höllenkreatur. Lege einen Gegenstand deiner Wahl vor dem Kapf ab.", 12, 3, "Ein wirklich ekliger Kuss kostet dich 2 Stufen (3 für Elfen).");
        Monster vampir = new Monster(28, 0, "Möchtegern-Vampir","Nicht wirklich gefährlich.", 12, 3, "Versperrt die Tür und erzählt über seinen Charakter. Verliere 3 Stufen.");
        Monster versicherungsvertreter = new Monster(29, 0, "Versicherungsvertreter","Deine level zählt nicht im Kampf. Bekämpfe ihn nur mit deinen Boni!", 14, 4, "Du kaufst eine Versicherung. Verliere 3 Gegenstände. Hast du nicht genug, verlierst du alles, was du hast.");
        Monster bekiffterGolem = new Monster(30, 0, "Bekiffter Golem","Wähle aus: kämpfen oder einfach vorbeigehen lassen.", 14, 4, "Er hat einen Fresskick. Er frisst dich. Du bist tot.");
        Monster schrecken = new Monster(31, 0, "Unglaublicher Unaussprechlicher Schrecken","Unaussprechlich.", 14, 4, "Unaussprechlich schrecklicher Tod für jeden.");
        Monster hippogreif = new Monster(32, 0, "Hippogreif","Verfolgt niemanden der level 3 oder niedriger.", 16, 4, "Du wirst zerstampft und gemampft. Verliere alle Handkarten und starte bei level 5 erneut.");
        Monster koenigTut = new Monster(33, 0, "König Tut","Verfolgen niemanden der level 3 oder niedriger. Charaktere höherer level lose 2 Stufen, sogar wenn sie entkommen.", 16, 4, "Du verlierst alle Karten auf deiner Hand.");
        Monster gruftigeGebrüder = new Monster(34, 0, "Gruftige Gebrüder","Verfolgen niemanden der level 3 oder niedriger. Charaktere höherer level lose 2 Stufen, sogar wenn sie entkommen.", 16, 4, "Du wirst auf level 1 reduziert.");
        Monster krakzilla = new Monster(35, 0, "Krakzilla","Schleimig! Elfen haben -4! Verfolgt niemanden der level 4 oder niedriger. außer Elfen", 18, 4, "Du wirst gegrabscht, angeschleimt, zerquetscht und verschlungen. Du bist tot, tot, tot! Noch Fragen?");
        Monster bullrog = new Monster(36, 0, "Bullrog","Verfolgen niemanden der level 4 oder niedriger.", 18, 5, "Du wirst zu Tode gepeitscht.");
        Monster plutoniumdrache = new Monster(37, 0, "Plutoniumdrache","Verfolgt niemanden der level 5 oder niedriger.", 20, 5, "Du wirst getötet und gefressen. Du bist tot.");


        //Curse: vorsicht: id = 47, 46, 48 gibt es nicht
        Curse wirklichBeschissenderFluch = new Curse(38, 0, "Wirklich Beschissener Curse!", "Verliere den Gegenstand, der dir den größten Bonus verleiht.");
        Curse fluch1 = new Curse(39, 0, "Curse!", "Verliere 1 level");
        Curse fluch2 = new Curse(40, 0, "Curse!", "Verliere 1 level");
        Curse fluch3 = new Curse(41, 0, "Curse!", "Verliere die Rüstung, die du trägst.");
        Curse fluch4 = new Curse(42, 0, "Curse!", "Verliere alles Schuhwerk, das du trägst.");
        Curse fluch5 = new Curse(43, 0, "Curse!", "Verliere die Kopfbedeckung, die du trägst.");
        Curse fluchrace1 = new Curse(44, 0, "Curse! race lose", "Hast du aktuell eine race, verlierst du diese. Ansonsten verschont dich der Curse.");
        Curse fluchrace2 = new Curse(45, 0, "Curse! race lose", "Hast du aktuell eine race, verlierst du diese. Ansonsten verschont dich der Curse.");
        //Curse fluchGeschlecht = new Curse(46, 0, "Curse! Geschlechtsumwandlung", "-5 auf deinen nächsten Kampf, weil du abgelenkt bist. Danach gibt es keinen weiteren Malus. Die Umwandlung ist jedoch permanent.");
        //Curse XXXXXXX = new Curse(47, 0, "Curse!", "");
        //Curse fluchHuhn = new Curse(48, 0, "Curse! Huhn auf deinem Kopf", "-1 auf alle Würfe. Jeder Curse oder alle Schlimmen Dinge, die deine Kopfbedeckung entfernen, nehmen das Huhn mit.");
        Curse fluchEnte = new Curse(49, 0, "Curse! Ente des Schreckens", "Du solltest es besser wissen, als eine Ente in einem Dungeon aufzuheben. Verliere 2 Stufen.");
        Curse fluchGross = new Curse(50, 0, "Curse! Verliere 1 großen Gegenstand", "Dir wird ein großer Gegenstand genommen.");
        Curse fluchKlein1 = new Curse(51, 0, "Curse! Verliere 1 kleinen Gegenstand", "Dir wird ein kleiner Gegenstand genommen. Jeder Gegenstand, der nicht \"groß\" ist, gilt als klein.");
        Curse fluchKlein2 = new Curse(52, 0, "Curse! Verliere 1 kleinen Gegenstand", "Dir wird ein kleiner Gegenstand genommen. Jeder Gegenstand, der nicht \"groß\" ist, gilt als klein.");
        Curse fluchraceStufe = new Curse(53, 0, "Curse! Verliere deine race", "Lege deine racenkarte ab!. Hast du keine, verliere 1 level.");
        Curse fluchraceMensch = new Curse(54, 0, "Curse! Verliere deine race", "Lege deine racenkarte, die du im Spiel hast, ab und werde zu einem Mensch.");


        //race
        Race elf1 = new Race(55, 0, "Elf", "Du hast +1 auf runaway und +2 Bonus für jeden Kampf.");
        Race elf2 = new Race(56, 0, "Elf", "Du hast +1 auf runaway und +2 Bonus für jeden Kampf.");
        Race elf3 = new Race(57, 0, "Elf", "Du hast +1 auf runaway und +2 Bonus für jeden Kampf.");
        Race zwerg1 = new Race(58, 0, "Zwerg", "Du kannst eine beliebige Anzahl großer Gegenstände tragen und du darfst 6 Karten auf deiner Hand haben.");
        Race zwerg2 = new Race(59, 0, "Zwerg", "Du kannst eine beliebige Anzahl großer Gegenstände tragen und du darfst 6 Karten auf deiner Hand haben.");
        Race zwerg3 = new Race(60, 0, "Zwerg", "Du kannst eine beliebige Anzahl großer Gegenstände tragen und du darfst 6 Karten auf deiner Hand haben.");


        //Monsterlevel
        Monsterlevel uralt = new Monsterlevel(70, "Uralt", 10);
        Monsterlevel gigantisch = new Monsterlevel(71, "Gigantisch", 10);
        Monsterlevel baby = new Monsterlevel(72, "Baby", -5);
        Monsterlevel wuetend = new Monsterlevel(73, "Wütend", 5);
        Monsterlevel intelligent = new Monsterlevel(74, "Intelligent", 5);


        //Karten zum DoorDeck hinzufügen
        DoorDeck.deck.add(filzlaeuse);
        DoorDeck.deck.add(sabbernderSchleim);
        DoorDeck.deck.add(lahmerGoblin );
        DoorDeck.deck.add(hammerRatte );
        DoorDeck.deck.add(Topfpflanze );
        DoorDeck.deck.add(fliegendeFroesche );
        DoorDeck.deck.add(gallertOktaeder );
        DoorDeck.deck.add(huhn );
        DoorDeck.deck.add(mrBones );
        DoorDeck.deck.add(pitBull );
        DoorDeck.deck.add(harfien );
        DoorDeck.deck.add(leprachaun );
        DoorDeck.deck.add(schnecken );
        DoorDeck.deck.add(untotessPferd );
        DoorDeck.deck.add(anwalt );
        DoorDeck.deck.add(entikore );
        DoorDeck.deck.add(pikotzu );
        DoorDeck.deck.add(kreischenderDepp );
        DoorDeck.deck.add(amazone );
        DoorDeck.deck.add(gesichtssauger );
        DoorDeck.deck.add(pavillon );
        DoorDeck.deck.add(gemeineGhoule );
        DoorDeck.deck.add(orks  );
        DoorDeck.deck.add(laufendeNase );
        DoorDeck.deck.add(netzTroll );
        DoorDeck.deck.add(bigfoot );
        DoorDeck.deck.add(zungendämon );
        DoorDeck.deck.add(vampir );
        DoorDeck.deck.add(versicherungsvertreter );
        DoorDeck.deck.add(bekiffterGolem );
        DoorDeck.deck.add(schrecken );
        DoorDeck.deck.add(hippogreif );
        DoorDeck.deck.add(koenigTut );
        DoorDeck.deck.add(gruftigeGebrüder );
        DoorDeck.deck.add(krakzilla );
        DoorDeck.deck.add(bullrog );
        DoorDeck.deck.add(plutoniumdrache);


        DoorDeck.deck.add(wirklichBeschissenderFluch );
        DoorDeck.deck.add(fluch1 );
        DoorDeck.deck.add(fluch2);
        DoorDeck.deck.add(fluch3);
        DoorDeck.deck.add(fluch4 );
        DoorDeck.deck.add(fluch5 );
        DoorDeck.deck.add(fluchrace1 );
        DoorDeck.deck.add(fluchrace2 );
        //DoorDeck.deck.add(fluchGeschlecht );
        //DoorDeck.deck.add(fluchHuhn );
        DoorDeck.deck.add(fluchEnte );
        DoorDeck.deck.add(fluchGross );
        DoorDeck.deck.add(fluchKlein1 );
        DoorDeck.deck.add(fluchKlein2 );
        DoorDeck.deck.add(fluchraceStufe );
        DoorDeck.deck.add(fluchraceMensch );


        DoorDeck.deck.add(elf1 );
        DoorDeck.deck.add(elf2 );
        DoorDeck.deck.add(elf3 );
        DoorDeck.deck.add(zwerg1 );
        DoorDeck.deck.add(zwerg2 );
        DoorDeck.deck.add(zwerg3 );

        DoorDeck.deck.add(uralt);
        DoorDeck.deck.add(gigantisch);
        DoorDeck.deck.add(baby);
        DoorDeck.deck.add(wuetend);
        DoorDeck.deck.add(intelligent);



        //Bonus: vorsicht: id = 461 gibt es nicht
        //Bonus riesigerFels = new Bonus(61, "Riesiger Fels", 3, 2, 1);
        Bonus riesigerFels = new Bonus(62, "Riesiger Fels", 3, 2, 1);
        Bonus verdunkelungsumhang = new Bonus(63, "Verdunkelungsumhang", 4, 0, 0);
        Bonus hammer = new Bonus(64, "Kniescheiben zertrümmernder Hammer", 4, 1, 0);
        Bonus scharferStreitkolben = new Bonus(65, "Scharfer Streitkolben", 4, 1, 0);
        Bonus schild = new Bonus(66, "Ganzkörper-Schild", 4, 1, 1);
        Bonus hellebarde = new Bonus(67, "Schweizer Armee-Hellebarde", 4, 2, 1);
        Bonus bogen = new Bonus(68, "Bogen mit bunten Bändern", 4, 2, 0);
        Bonus napalmstab = new Bonus(69, "Napalmstab", 5, 1, 0);



        //BonusWear            blockiert: 0 = nichts, 1 = 1 Hand, 2 = 2 Hände, 3 = Rüstung, 4 = Kopf, 5 = Schuhe
        BonusWear stange = new BonusWear(75, "Stange. 11-Fuß", 1, 2, 0);
        BonusWear helm = new BonusWear(76,"Helm der Tapferkeit", 1, 4, 0);
        BonusWear lederruestung = new BonusWear(77,"Lederrüstung", 1, 3, 0);
        BonusWear schleimigeRuestung = new BonusWear(78,"Schleimige Rüstung", 1, 2, 0);
        BonusWear knie = new BonusWear(79,"Spießige Knie", 1, 0, 0);
        BonusWear geilerHelm = new BonusWear(80,"Geiler Helm", 1, 4, 0);
        BonusWear arschtrittStiefel = new BonusWear(81,"Arschtritt-Stiefel", 2, 5, 0);
        BonusWear buckler = new BonusWear(82,"Flotter Buckler", 2, 1, 0);
        BonusWear flammendeRuestung = new BonusWear(83,"Flammende Rüstung", 2, 3, 0);
        BonusWear schwert = new BonusWear(84,"Singendes & Tanzendes Schwert", 2, 0, 0);
        BonusWear bastardSchwert = new BonusWear(85,"Hinterhältiges Bastard-Schwert", 2, 1, 0);
        BonusWear titel = new BonusWear(86,"Wirklich beeindruckender Titel", 3, 0, 0);
        BonusWear tuch = new BonusWear(87,"Cooles Tuch Für Harte Kerle", 3, 4, 0);
        BonusWear breitschwert = new BonusWear(88,"Braut-Breitschwert", 3, 1, 0);
        BonusWear kaesereibe = new BonusWear(89,"Käsereibe des Friedens", 3, 1, 0);
        BonusWear dolch = new BonusWear(90,"Dolch Des Verrats", 3, 1, 0);
        BonusWear gentlemenKeule = new BonusWear(91,"Gentleman-Keule", 3, 1, 0);
        BonusWear sandwhich = new BonusWear(92,"Limburger Und Sardellen-Sandwich", 3, 0, 0);
        BonusWear hut = new BonusWear(93,"Spitzer Hut Der Macht", 3, 4, 0);
        BonusWear kurzeBreiteRuestung = new BonusWear(94,"Kurze Breite Rüstung", 3, 3, 0);
        BonusWear trittleiter = new BonusWear(95,"Trittleiter", 3, 0, 1);
        BonusWear kettensaege = new BonusWear(96,"Kettensäge Der Blutigen Zerstückelung", 3, 2, 1);
        BonusWear mihrilRuestung = new BonusWear(97,"Mithril-Rüstung", 3, 3, 1);
        BonusWear strumpfhose = new BonusWear(98,"Strumpfhose der Riesenstärke", 3, 0, 0);
        BonusWear rapier = new BonusWear(99,"Unfairer Rapier", 3, 1, 0);


        //BonusSide
        BonusSide lustballons = new BonusSide(100, "Hübsche Luftballons", 5);
        BonusSide magischesGeschoss = new BonusSide(101, "Magisches Geschoss", 5);
        BonusSide ekliger = new BonusSide(102, "Ekliger Sportdrink", 2);
        BonusSide schlaftrunk = new BonusSide(103, "Schlaftrunk", 2);
        BonusSide saeure = new BonusSide(104, "Elektisch Radioaktiver Säuretrank", 5);
        BonusSide heldenmut = new BonusSide(105, "Trank Des Idiotischen Heldenmuts", 2);
        BonusSide mundgeruch = new BonusSide(106, "Trank Des Mundgeruchs", 2);
        BonusSide explosivtrank = new BonusSide(107, "Gefrierender Explosivtrank", 3);
        BonusSide verwirrung = new BonusSide(108, "Vrank Der Terwirrung", 3);
        BonusSide gifttrank = new BonusSide(109, "Flammender Gifttrank", 3);


        //Special
        Special wunschring1 = new Special(110,"Wunschring","Beendet jeden Curse. Jederzeit spielbar. Nur einmal einsetzbar.");
        Special wunschring2 = new Special(111,"Wunschring","Beendet jeden Curse. Jederzeit spielbar. Nur einmal einsetzbar.");
        Special lampe = new Special(112,"Magische Lampe","Nur in der Runde spielbar. Sie beschwört einen Geist, der ein Monster verschwinden lässt, selbst wenn dein Weglaufenwurf verpatzt wurde und es dich fangen würde. War es das einzige Monster, erhälst du seinen treasure, aber keine level. Nur einmal einsetzbar.");
        Special stiefel = new Special(113,"Stiefel zum Echt Schnellen Davonlaufen","Sie geben dir einen +2 Bonus auf runaway.");
        Special tuba = new Special(114,"Tuba Der Verzauberung","Dieses melodiöse Instrument bezaubert deine Gegner und gibt dir +3 auf runaway. Falls du erfolgreich runaway kannst, zieh dir auf dem Weg nach draußen noch verdeckt eine Schatzkarte.");
        Special gezinkterWuerfel = new Special(115,"Gezinkter Würfel","Spiel ihn, nachdem du aus einem beliebigen Grund würfeln musstest. Ändere das Würfelergebnis do wie du willst. Nur einmal einsetzbar.");
        Special fertigmauer = new Special(116,"Fertigmauer","Ermöglicht automatisches Entkommen aus einem Kampf für dich. Nur einmal einsetzbar.");
        Special doppelgaenger = new Special(117,"Doppelgänger","Beschwöre deine exakte Kopie. Verdopple deine Kampfstärke. Nur einmal einsetzbar.");
        Special unsichtbarkeitstrank = new Special(118,"Unsichtbarkeitstrank","Ablegen, wenn der runaway-Wurf misslingt. Du entkommst automatisch. Nur einmal einsetzbar.");
        Special freundschaftstrank = new Special(119,"Freundschaftstrank","Im Kampf spielen. Lege alle Monster des Kampfes ab. Du erhälst keinen treasure, aber du darfst den Raum durchsuchen. Nur einmal einsetzbar.");
        Special polly = new Special(120,"Polly Verwandlungtrank","Im Kampf spielen. Verwandelt ein Monster in einen Papagei, der wegfliegt und seinen treasure zurücklässt. Nur einmal einsetzbar.");
        //Special wuenschelstab = new Special(121,"Wünschelstab","Durchsuche die abgelegten Karten, um eine Card zu finden, die du willst. Nimm die neue Card und lege diese ab.");
        Special schatzhort = new Special(122,"Schatzhort","Ziehe sofort 3 weitere Schatzkarten. Hast du diese Card verdeckt gezogen, ziehst du sie ebenfalls verdeckt, ansonsten offen.");



        //Karten zum TreasureDeck hinzufügen
        TreasureDeck.deck.add(riesigerFels );
        TreasureDeck.deck.add(verdunkelungsumhang );
        TreasureDeck.deck.add(hammer );
        TreasureDeck.deck.add(scharferStreitkolben );
        TreasureDeck.deck.add(schild );
        TreasureDeck.deck.add(hellebarde );
        TreasureDeck.deck.add(bogen );
        TreasureDeck.deck.add(napalmstab );
        TreasureDeck.deck.add(stange );
        TreasureDeck.deck.add(helm );
        TreasureDeck.deck.add(lederruestung );
        TreasureDeck.deck.add(schleimigeRuestung );
        TreasureDeck.deck.add(knie );
        TreasureDeck.deck.add(geilerHelm );
        TreasureDeck.deck.add(arschtrittStiefel  );
        TreasureDeck.deck.add(buckler );
        TreasureDeck.deck.add(flammendeRuestung );
        TreasureDeck.deck.add(schwert );
        TreasureDeck.deck.add(bastardSchwert );
        TreasureDeck.deck.add(titel );
        TreasureDeck.deck.add(tuch );
        TreasureDeck.deck.add(breitschwert );
        TreasureDeck.deck.add(kaesereibe );
        TreasureDeck.deck.add(dolch );
        TreasureDeck.deck.add(gentlemenKeule );
        TreasureDeck.deck.add(sandwhich );
        TreasureDeck.deck.add(hut );
        TreasureDeck.deck.add(kurzeBreiteRuestung );
        TreasureDeck.deck.add(trittleiter );
        TreasureDeck.deck.add(kettensaege );
        TreasureDeck.deck.add(mihrilRuestung );
        TreasureDeck.deck.add(strumpfhose );
        TreasureDeck.deck.add(rapier );

        TreasureDeck.deck.add(lustballons );
        TreasureDeck.deck.add(magischesGeschoss );
        TreasureDeck.deck.add(ekliger );
        TreasureDeck.deck.add(schlaftrunk );
        TreasureDeck.deck.add(saeure );
        TreasureDeck.deck.add(heldenmut );
        TreasureDeck.deck.add(mundgeruch );
        TreasureDeck.deck.add(explosivtrank );
        TreasureDeck.deck.add(verwirrung );
        TreasureDeck.deck.add(gifttrank );

        TreasureDeck.deck.add(wunschring1 );
        TreasureDeck.deck.add(wunschring2 );
        TreasureDeck.deck.add(lampe );
        TreasureDeck.deck.add(stiefel );
        TreasureDeck.deck.add(tuba );
        TreasureDeck.deck.add(gezinkterWuerfel );
        TreasureDeck.deck.add(fertigmauer );
        TreasureDeck.deck.add(doppelgaenger );
        TreasureDeck.deck.add(unsichtbarkeitstrank );
        TreasureDeck.deck.add(freundschaftstrank );
        TreasureDeck.deck.add(polly );
        //TreasureDeck.deck.add(wuenschelstab );
        TreasureDeck.deck.add(schatzhort );


        /*
        Special XXXXX = new Special(123,"","");
        Special XXXXX = new Special(124,"","");
        Special XXXXX = new Special(125,"","");
        Special XXXXX = new Special(126,"","");
        Special XXXXX = new Special(127,"","");
        Special XXXXX = new Special(128,"","");
        Special XXXXX = new Special(129,"","");
        Special XXXXX = new Special(130,"","");
        Special XXXXX = new Special(131,"","");
        Special XXXXX = new Special(132,"","");
        Special XXXXX = new Special(133,"","");
        Special XXXXX = new Special(134,"","");
        Special XXXXX = new Special(135,"","");
        Special XXXXX = new Special(136,"","");
        Special XXXXX = new Special(137,"","");
        Special XXXXX = new Special(138,"","");
        */
    }
}

//package com.example.admin.ezmunchkin;
//
//import java.util.ArrayList;
//import com.example.admin.ezmunchkin.Player.PlayerRace;
//
///**
// * Created by Falco on 26.10.2017.
// */
//
//public class Game {
//
//    ////~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ F I E L D S ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//    public int session;
//    public ArrayList<Card> fightDeck = new ArrayList<Card>();
//    //Alle aktiven an dieser Session spielenden Player: In der Playerklasse die ArrayList: playerList
//
//    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ M E T H O D S ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//    /**
//     * Initialisiere Spiel
//     */
//    public void startGame(){
//        //Player objekte Erzeugen
//        createCards();
//
//        //Karten austeilen
//        //handOutCardsForAllPlayers();
//
//        //Player IDs erstellen
//        //start Player auswählen
//
//        while(checkWin(Player.playerList)){
//            for (Player currentPlayer: Player.playerList) {
//                move(currentPlayer);
//            }
//            return;
//        }
//    }
//
//
//
//    /**
//     * Repräsentiert ein move
//     * @param player aktueller Player
//     */
//    public void move(Player player){
//        phase1(player);
////        phase2(player);
////        phase3(player);
//        phase4(player);
//        fightDeck.clear();
//    }
//
//
//    /**
//     * Repräsentiert die Phase1 aus dem Automaten
//     * @param currentPlayer
//     */
//    public void phase1(Player currentPlayer){
//
//        //TODO: Einfügen, dass der Player Karten von seiner Hand auf den Tisch legen kann
//
//        Card currentCard = DoorDeck.draw();
//        fightDeck.add(currentCard);
//
//        if(currentCard instanceof Monster){ //Entspricht im Automaten dem Zustand 1a
//            if(((Monster)currentCard).checkIfFight(currentPlayer)){
//                ((Monster)currentCard).fight(currentPlayer, this);
//                phase4(currentPlayer);
//                return;
//            }
//        }
//
//        else if(currentCard instanceof Curse){ //Entspricht im Automaten dem Zustand 1b
//            ((Curse)currentCard).execution(currentPlayer, this);
//            //TODO: Player fragen, wie er fortfahren möchte: Phase 2 oder 3
//            if(true){
//                phase2(currentPlayer);
//            }
//            else{
//                phase3(currentPlayer);
//            }
//        }
//
//        else { // TODO: Monsterlevel, race: --> entweder auf die Hand nehmen, auf den Tisch legen oder ablegen //Entspricht im Automaten dem Zustand 1c
//            if(true){ //Hand nehmen
//                currentPlayer.hand.add(currentCard);
//                fightDeck.remove(currentCard);
//            }
//            else if(true){
//                currentPlayer.table.add(currentCard);
//                fightDeck.remove(currentCard);
//            }
//            else{
//                currentPlayer.discardDoor(currentCard, this);
//            }
//
//            //TODO: Player fragen, wie er fortfahren möchte: Phase 2 oder 3
//            if(true){
//                phase2(currentPlayer);
//            }
//            else{
//                phase3(currentPlayer);
//            }
//        }
//    }
//
//
//    /**
//     * Repräsentiert die Phase2 aus dem Automaten
//     * @param currentPlayer
//     */
//    public void phase2(Player currentPlayer){
//        Monster monsterToFight = null; //PlayerWaehltMonsterAus(Player);
//        monsterToFight.fight(currentPlayer, this);
//        phase4(currentPlayer);
//    }
//
//
//
//    /**
//     * Repräsentiert die Phase3 aus dem Automaten
//     * @param Player
//     */
//    public void phase3(Player Player){
//        Player.hand.add(DoorDeck.draw());
//        phase4(Player);
//    }
//
//
//
//    /**
//     * Repräsentiert die Phase4 aus dem Automaten
//     * @param currentPlayer
//     */
//    public void phase4(Player currentPlayer){
//        if(currentPlayer.playerRace == com.example.admin.ezmunchkin.Player.PlayerRace.DWARF && currentPlayer.hand.size() > 6){ //zwerg
//            //TODO: Player fragen, welche Karten er ablegen möchte und dann ablegen
//
//        }
//        else if(currentPlayer.hand.size() > 5){
//            //TODO: Player fragen, welche Karten er ablegen möchte und dann ablegen
//        }
//    }
//
//
//    /**
//     * Check, ob es bereits einen Sieger gibts
//     * @param liste
//     * @return false, wenn ein Player level 10 ist; ansonsten true
//     */
//    private boolean checkWin(ArrayList<Player> liste) {
//        for (Player currentPlayer: liste) {
//            if(currentPlayer.level == 10){
//                return false;
//            }
//        }
//        return true;
//    }
//
//
//    /**
//     * Erstellt alle Karten Objekte und fügt sie zu den dazugehörigen Stapel zu
//     */
//    public void createCards(){
///*
//        //Monster
//        Monster filzlaeuse = new Monster(1,"Filzläuse","Denen kannst du nicht entkommen!", 1, 1, "Lege deine Rüstung und alle Gegenstände unterhalb der Hüfte ab.");
//        Monster sabbernderSchleim = new Monster(2, 0, "Sabbernder Schleim", "Ekliger Schleim! +4 gegen Elfen.", 1, 1, "Lege das Schuhwerk, das du trägst, ab. Verliere eine level, falls du kein Schuhwerk trägst.");
//        Monster lahmerGoblin = new Monster(3, "Lahmer Goblin", "Du hast +1 beim runaway.", 1, 1, "Er verhaut dich mit seiner Krücke. Verliere 1 level.");
//        Monster hammerRatte = new Monster(4, "Hammer-Ratte", "Höllenkreatur.", 1, 1, "Sie verhaut dich. Verliere 1 level.");
//        Monster Topfpflanze = new Monster(5, "Topfpflanze", "Elfen draw 1 zusätzlichen treasure, nachdem sie besiegt wurde.", 1, 1, "Keine. Automatische Flucht.");
//        Monster fliegendeFroesche = new Monster(6, "Fliegende Frösche", "Du hast -1 auf runaway.", 2, 1, "Sie beißen! Verliere 2 Stufen.");
//        Monster gallertOktaeder = new Monster(7, "Gallert-Oktaeder", "Du hast +1 auf runaway.", 2, 1, "Lass alle deine großen Gegenstände fallen.");
//        Monster huhn = new Monster(8, "Großes wütendes Huhn", "Grillhuhn ist lecker.", 2, 1, "Schmerzhaftes Hacken. Verliere 1 level.");
//        Monster mrBones = new Monster(9, "Mr. Bones", "Auch bei einer erfolgreichen Flucht verlierst du eine Sutufe.", 2, 1, "Seine knochige Berührung kostet dich 2 Stufen.");
//        Monster pitBull = new Monster(10, "Pit Bull", "Kannst du ihn nicht besiegen, darfst du ihn ablenken (automatische Flucht), indem du einen Stab oder Ähnliches fallen lässt.", 2, 1, "Bissspuren im Hintern. Verliere 2 Stufen.");
//        Monster harfien = new Monster(11, "Harfien", "Sie widerstehen Magie.", 4, 2, "Ihre Musik ist wirklich schlecht. Verliere 2 Stufen.");
//        Monster leprachaun = new Monster(12, "Leprachaun", "Widerlich! +5 gegen Elfen.", 4, 2, "Er nimmt von dir 2 Gegenstände.");
//        Monster schnecken = new Monster(13, "Schnecken auf Speed", "Du hast -2 auf runaway.", 4, 2, "Sie stehlen deinen treasure. Würfle und verliere entsprechend viele Gegenstände oder Karten von deiner Hand - deine Wahl.");
//        Monster untotessPferd = new Monster(14, "Untotes Pferd", "+5 gegen Zwerge", 4, 2, "Tritt, beißt und riecht furchtbar.");
//        Monster anwalt = new Monster(15, "Anwalt", "Würfle. Bei einer 1 kannst du, anstatt ihn zu bekämpfen, 2 Schätze ablegen und verdeckt 2 neue draw.", 6, 2, "Er überzieht dich mit einer Unterlassungsklage. Lege so viele Karten von deiner Hand ab, die der Anzahl der MitPlayer entspricht.");
//        Monster entikore = new Monster(16, "Entikore", "Magisches Wesen.", 6, 2, "Lege entweder deine ganze Hand ab oder verliere 2 Stufen.");
//        Monster pikotzu = new Monster(17, "Pikotzu","Du erhälst eine Extrastufe, wenn du es ohne Boni besiegst.", 6, 2, "Kostzestrahl-Angriff! Lege deine ganze Hand ab.");
//        Monster kreischenderDepp = new Monster(18, "Kreischender Depp","Heller, schriller Schrei!", 6, 2, "Du wirst zu einem normalen, langweiligen Menschen. Lege deine race ab, wenn du eine hast.");
//        Monster amazone = new Monster(19, "Amazone","Starke Kämpferin", 8, 2, "Dir wird von einer Frau in den Arsch getreten. Dein Macho Munchkin-Stolz ist dahin. Du verlierst alle racen. Wenn du keine hast, verliere 3 Stufen.");
//        Monster gesichtssauger = new Monster(20, "Gesichtssauger","Widerlich! +6 gegen Elfen.", 8, 2, "Wurde dein Gesicht runtergesaugt, verschwindet auch deine Kopfbedeckung. Lege die Kopfbedeckung, die du trägst, ab und verliere 1 level.");
//        Monster pavillon = new Monster(21, "Pavillon","Boni können dir nicht helfen. Du musst dich dem Pavillon allein stellen.", 8, 2, "Verliere 3 Stufen.");
//        Monster gemeineGhoule = new Monster(22, "Gemeine Ghoule","Gegen sie dürfen keine Gegenstände oder andere Boni eingesetzt werden - kämpfe nur mit deiner Charakterstufe.", 8, 2, "Deine level entspricht der niedrigsten level eines Characters im Spiel.");
//        Monster orks = new Monster(23, "3.872 Orks","+6 gegen Zewrge wegen uralter Feindschaft.", 10, 3, "Würfle. Bei einer 1 oder 2 trampeln sie dich zu Tode. Ansonsten verlierst du so viele Stufen, wie gewürfelt wurde.");
//        Monster laufendeNase = new Monster(24, "Laufende Nase","Willst du die Laufende Nase nicht bekämpfen, so hast du Pech gehabt. Du musst kämpfen.", 10, 3, "Sie kann dich überall wegputzen. Verlierst du den Kampf, kannst du nicht fliehen. Nichts wird dir helfen. Du verlierst 3 Stufen.");
//        Monster netzTroll = new Monster(25, "Netz-Troll","Hat keine besonderen Fähigkeiten und ist deshalb ziemlich sauer.", 10, 3, "Er klammert sich an deinem Haupt fest. Verliere deine Kopfbedeckung und zwei Stufen.");
//        Monster bigfoot = new Monster(26, "Bigfoot","+3 gegen Zwerge", 12, 3, "Quetscht dich flach und frisst deinen Hut. Du verlierst deine Kopfbedeckung.");
//        Monster zungendämon = new Monster(27, "Zungendämon","Höllenkreatur. Lege einen Gegenstand deiner Wahl vor dem Kapf ab.", 12, 3, "Ein wirklich ekliger Kuss kostet dich 2 Stufen (3 für Elfen).");
//        Monster vampir = new Monster(28, "Möchtegern-Vampir","Nicht wirklich gefährlich.", 12, 3, "Versperrt die Tür und erzählt über seinen Charakter. Verliere 3 Stufen.");
//        Monster versicherungsvertreter = new Monster(29, "Versicherungsvertreter","Deine level zählt nicht im Kampf. Bekämpfe ihn nur mit deinen Boni!", 14, 4, "Du kaufst eine Versicherung. Verliere 3 Gegenstände. Hast du nicht genug, verlierst du alles, was du hast.");
//        Monster bekiffterGolem = new Monster(30, "Bekiffter Golem","Wähle aus: kämpfen oder einfach vorbeigehen lassen.", 14, 4, "Er hat einen Fresskick. Er frisst dich. Du bist tot.");
//        Monster schrecken = new Monster(31, "Unglaublicher Unaussprechlicher Schrecken","Unaussprechlich.", 14, 4, "Unaussprechlich schrecklicher Tod für jeden.");
//        Monster hippogreif = new Monster(32, "Hippogreif","Verfolgt niemanden der level 3 oder niedriger.", 16, 4, "Du wirst zerstampft und gemampft. Verliere alle Handkarten und starte bei level 5 erneut.");
//        Monster koenigTut = new Monster(33, "König Tut","Verfolgen niemanden der level 3 oder niedriger. Charaktere höherer level lose 2 Stufen, sogar wenn sie entkommen.", 16, 4, "Du verlierst alle Karten auf deiner Hand.");
//        Monster gruftigeGebrüder = new Monster(34, "Gruftige Gebrüder","Verfolgen niemanden der level 3 oder niedriger. Charaktere höherer level lose 2 Stufen, sogar wenn sie entkommen.", 16, 4, "Du wirst auf level 1 reduziert.");
//        Monster krakzilla = new Monster(35, "Krakzilla","Schleimig! Elfen haben -4! Verfolgt niemanden der level 4 oder niedriger. außer Elfen", 18, 4, "Du wirst gegrabscht, angeschleimt, zerquetscht und verschlungen. Du bist tot, tot, tot! Noch Fragen?");
//        Monster bullrog = new Monster(36, "Bullrog","Verfolgen niemanden der level 4 oder niedriger.", 18, 5, "Du wirst zu Tode gepeitscht.");
//        Monster plutoniumdrache = new Monster(37, "Plutoniumdrache","Verfolgt niemanden der level 5 oder niedriger.", 20, 5, "Du wirst getötet und gefressen. Du bist tot.");
//
//
//        //Curse: vorsicht: id = 47, 46, 48 gibt es nicht
//        Curse wirklichBeschissenderFluch = new Curse(38, 0, "Wirklich Beschissener Curse!", "Verliere den Gegenstand, der dir den größten Bonus verleiht.");
//        Curse fluch1 = new Curse(39, 0, "Curse!", "Verliere 1 level");
//        Curse fluch2 = new Curse(40, 0, "Curse!", "Verliere 1 level");
//        Curse fluch3 = new Curse(41, 0, "Curse!", "Verliere die Rüstung, die du trägst.");
//        Curse fluch4 = new Curse(42, 0, "Curse!", "Verliere alles Schuhwerk, das du trägst.");
//        Curse fluch5 = new Curse(43, 0, "Curse!", "Verliere die Kopfbedeckung, die du trägst.");
//        Curse fluchrace1 = new Curse(44, 0, "Curse! race lose", "Hast du aktuell eine race, verlierst du diese. Ansonsten verschont dich der Curse.");
//        Curse fluchrace2 = new Curse(45, 0, "Curse! race lose", "Hast du aktuell eine race, verlierst du diese. Ansonsten verschont dich der Curse.");
//        //Curse fluchGeschlecht = new Curse(46, 0, "Curse! Geschlechtsumwandlung", "-5 auf deinen nächsten Kampf, weil du abgelenkt bist. Danach gibt es keinen weiteren Malus. Die Umwandlung ist jedoch permanent.");
//        //Curse XXXXXXX = new Curse(47, 0, "Curse!", "");
//        //Curse fluchHuhn = new Curse(48, 0, "Curse! Huhn auf deinem Kopf", "-1 auf alle Würfe. Jeder Curse oder alle Schlimmen Dinge, die deine Kopfbedeckung entfernen, nehmen das Huhn mit.");
//        Curse fluchEnte = new Curse(49, 0, "Curse! Ente des Schreckens", "Du solltest es besser wissen, als eine Ente in einem Dungeon aufzuheben. Verliere 2 Stufen.");
//        Curse fluchGross = new Curse(50, 0, "Curse! Verliere 1 großen Gegenstand", "Dir wird ein großer Gegenstand genommen.");
//        Curse fluchKlein1 = new Curse(51, 0, "Curse! Verliere 1 kleinen Gegenstand", "Dir wird ein kleiner Gegenstand genommen. Jeder Gegenstand, der nicht \"groß\" ist, gilt als klein.");
//        Curse fluchKlein2 = new Curse(52, 0, "Curse! Verliere 1 kleinen Gegenstand", "Dir wird ein kleiner Gegenstand genommen. Jeder Gegenstand, der nicht \"groß\" ist, gilt als klein.");
//        Curse fluchraceStufe = new Curse(53, 0, "Curse! Verliere deine race", "Lege deine racenkarte ab!. Hast du keine, verliere 1 level.");
//        Curse fluchraceMensch = new Curse(54, 0, "Curse! Verliere deine race", "Lege deine racenkarte, die du im Spiel hast, ab und werde zu einem Mensch.");
//
//
//        //race
//        Race elf1 = new Race(55, 0, "Elf", "Du hast +1 auf runaway und +2 Bonus für jeden Kampf.");
//        Race elf2 = new Race(56, 0, "Elf", "Du hast +1 auf runaway und +2 Bonus für jeden Kampf.");
//        Race elf3 = new Race(57, 0, "Elf", "Du hast +1 auf runaway und +2 Bonus für jeden Kampf.");
//        Race zwerg1 = new Race(58, 0, "Zwerg", "Du kannst eine beliebige Anzahl großer Gegenstände tragen und du darfst 6 Karten auf deiner Hand haben.");
//        Race zwerg2 = new Race(59, 0, "Zwerg", "Du kannst eine beliebige Anzahl großer Gegenstände tragen und du darfst 6 Karten auf deiner Hand haben.");
//        Race zwerg3 = new Race(60, 0, "Zwerg", "Du kannst eine beliebige Anzahl großer Gegenstände tragen und du darfst 6 Karten auf deiner Hand haben.");
//
//
//        //Monsterlevel
//        Monsterlevel uralt = new Monsterlevel(70, "Uralt", 10);
//        Monsterlevel gigantisch = new Monsterlevel(71, "Gigantisch", 10);
//        Monsterlevel baby = new Monsterlevel(72, "Baby", -5);
//        Monsterlevel wuetend = new Monsterlevel(73, "Wütend", 5);
//        Monsterlevel intelligent = new Monsterlevel(74, "Intelligent", 5);
//
//
//        //Karten zum DoorDeck hinzufügen
//        DoorDeck.deck.add(filzlaeuse);
//        DoorDeck.deck.add(sabbernderSchleim);
//        DoorDeck.deck.add(lahmerGoblin );
//        DoorDeck.deck.add(hammerRatte );
//        DoorDeck.deck.add(Topfpflanze );
//        DoorDeck.deck.add(fliegendeFroesche );
//        DoorDeck.deck.add(gallertOktaeder );
//        DoorDeck.deck.add(huhn );
//        DoorDeck.deck.add(mrBones );
//        DoorDeck.deck.add(pitBull );
//        DoorDeck.deck.add(harfien );
//        DoorDeck.deck.add(leprachaun );
//        DoorDeck.deck.add(schnecken );
//        DoorDeck.deck.add(untotessPferd );
//        DoorDeck.deck.add(anwalt );
//        DoorDeck.deck.add(entikore );
//        DoorDeck.deck.add(pikotzu );
//        DoorDeck.deck.add(kreischenderDepp );
//        DoorDeck.deck.add(amazone );
//        DoorDeck.deck.add(gesichtssauger );
//        DoorDeck.deck.add(pavillon );
//        DoorDeck.deck.add(gemeineGhoule );
//        DoorDeck.deck.add(orks  );
//        DoorDeck.deck.add(laufendeNase );
//        DoorDeck.deck.add(netzTroll );
//        DoorDeck.deck.add(bigfoot );
//        DoorDeck.deck.add(zungendämon );
//        DoorDeck.deck.add(vampir );
//        DoorDeck.deck.add(versicherungsvertreter );
//        DoorDeck.deck.add(bekiffterGolem );
//        DoorDeck.deck.add(schrecken );
//        DoorDeck.deck.add(hippogreif );
//        DoorDeck.deck.add(koenigTut );
//        DoorDeck.deck.add(gruftigeGebrüder );
//        DoorDeck.deck.add(krakzilla );
//        DoorDeck.deck.add(bullrog );
//        DoorDeck.deck.add(plutoniumdrache);
//
//
//        DoorDeck.deck.add(wirklichBeschissenderFluch );
//        DoorDeck.deck.add(fluch1 );
//        DoorDeck.deck.add(fluch2);
//        DoorDeck.deck.add(fluch3);
//        DoorDeck.deck.add(fluch4 );
//        DoorDeck.deck.add(fluch5 );
//        DoorDeck.deck.add(fluchrace1 );
//        DoorDeck.deck.add(fluchrace2 );
//        //DoorDeck.deck.add(fluchGeschlecht );
//        //DoorDeck.deck.add(fluchHuhn );
//        DoorDeck.deck.add(fluchEnte );
//        DoorDeck.deck.add(fluchGross );
//        DoorDeck.deck.add(fluchKlein1 );
//        DoorDeck.deck.add(fluchKlein2 );
//        DoorDeck.deck.add(fluchraceStufe );
//        DoorDeck.deck.add(fluchraceMensch );
//
//
//        DoorDeck.deck.add(elf1 );
//        DoorDeck.deck.add(elf2 );
//        DoorDeck.deck.add(elf3 );
//        DoorDeck.deck.add(zwerg1 );
//        DoorDeck.deck.add(zwerg2 );
//        DoorDeck.deck.add(zwerg3 );
//
//        DoorDeck.deck.add(uralt);
//        DoorDeck.deck.add(gigantisch);
//        DoorDeck.deck.add(baby);
//        DoorDeck.deck.add(wuetend);
//        DoorDeck.deck.add(intelligent);
//
//
//
//        //Bonus: vorsicht: id = 461 gibt es nicht
//        //Bonus riesigerFels = new Bonus(61, "Riesiger Fels", 3, 2, 1);
//        Bonus riesigerFels = new Bonus(62, "Riesiger Fels", 3, 2, 1);
//        Bonus verdunkelungsumhang = new Bonus(63, "Verdunkelungsumhang", 4, 0, 0);
//        Bonus hammer = new Bonus(64, "Kniescheiben zertrümmernder Hammer", 4, 1, 0);
//        Bonus scharferStreitkolben = new Bonus(65, "Scharfer Streitkolben", 4, 1, 0);
//        Bonus schild = new Bonus(66, "Ganzkörper-Schild", 4, 1, 1);
//        Bonus hellebarde = new Bonus(67, "Schweizer Armee-Hellebarde", 4, 2, 1);
//        Bonus bogen = new Bonus(68, "Bogen mit bunten Bändern", 4, 2, 0);
//        Bonus napalmstab = new Bonus(69, "Napalmstab", 5, 1, 0);
//
//
//
//        //BonusWear            blockiert: 0 = nichts, 1 = 1 Hand, 2 = 2 Hände, 3 = Rüstung, 4 = Kopf, 5 = Schuhe
//        BonusWear stange = new BonusWear(75, "Stange. 11-Fuß", 1, 2, 0);
//        BonusWear helm = new BonusWear(76,"Helm der Tapferkeit", 1, 4, 0);
//        BonusWear lederruestung = new BonusWear(77,"Lederrüstung", 1, 3, 0);
//        BonusWear schleimigeRuestung = new BonusWear(78,"Schleimige Rüstung", 1, 2, 0);
//        BonusWear knie = new BonusWear(79,"Spießige Knie", 1, 0, 0);
//        BonusWear geilerHelm = new BonusWear(80,"Geiler Helm", 1, 4, 0);
//        BonusWear arschtrittStiefel = new BonusWear(81,"Arschtritt-Stiefel", 2, 5, 0);
//        BonusWear buckler = new BonusWear(82,"Flotter Buckler", 2, 1, 0);
//        BonusWear flammendeRuestung = new BonusWear(83,"Flammende Rüstung", 2, 3, 0);
//        BonusWear schwert = new BonusWear(84,"Singendes & Tanzendes Schwert", 2, 0, 0);
//        BonusWear bastardSchwert = new BonusWear(85,"Hinterhältiges Bastard-Schwert", 2, 1, 0);
//        BonusWear titel = new BonusWear(86,"Wirklich beeindruckender Titel", 3, 0, 0);
//        BonusWear tuch = new BonusWear(87,"Cooles Tuch Für Harte Kerle", 3, 4, 0);
//        BonusWear breitschwert = new BonusWear(88,"Braut-Breitschwert", 3, 1, 0);
//        BonusWear kaesereibe = new BonusWear(89,"Käsereibe des Friedens", 3, 1, 0);
//        BonusWear dolch = new BonusWear(90,"Dolch Des Verrats", 3, 1, 0);
//        BonusWear gentlemenKeule = new BonusWear(91,"Gentleman-Keule", 3, 1, 0);
//        BonusWear sandwhich = new BonusWear(92,"Limburger Und Sardellen-Sandwich", 3, 0, 0);
//        BonusWear hut = new BonusWear(93,"Spitzer Hut Der Macht", 3, 4, 0);
//        BonusWear kurzeBreiteRuestung = new BonusWear(94,"Kurze Breite Rüstung", 3, 3, 0);
//        BonusWear trittleiter = new BonusWear(95,"Trittleiter", 3, 0, 1);
//        BonusWear kettensaege = new BonusWear(96,"Kettensäge Der Blutigen Zerstückelung", 3, 2, 1);
//        BonusWear mihrilRuestung = new BonusWear(97,"Mithril-Rüstung", 3, 3, 1);
//        BonusWear strumpfhose = new BonusWear(98,"Strumpfhose der Riesenstärke", 3, 0, 0);
//        BonusWear rapier = new BonusWear(99,"Unfairer Rapier", 3, 1, 0);
//
//
//        //BonusSide
//        BonusSide lustballons = new BonusSide(100, "Hübsche Luftballons", 5);
//        BonusSide magischesGeschoss = new BonusSide(101, "Magisches Geschoss", 5);
//        BonusSide ekliger = new BonusSide(102, "Ekliger Sportdrink", 2);
//        BonusSide schlaftrunk = new BonusSide(103, "Schlaftrunk", 2);
//        BonusSide saeure = new BonusSide(104, "Elektisch Radioaktiver Säuretrank", 5);
//        BonusSide heldenmut = new BonusSide(105, "Trank Des Idiotischen Heldenmuts", 2);
//        BonusSide mundgeruch = new BonusSide(106, "Trank Des Mundgeruchs", 2);
//        BonusSide explosivtrank = new BonusSide(107, "Gefrierender Explosivtrank", 3);
//        BonusSide verwirrung = new BonusSide(108, "Vrank Der Terwirrung", 3);
//        BonusSide gifttrank = new BonusSide(109, "Flammender Gifttrank", 3);
//
//
//        //Special
//        Special wunschring1 = new Special(110,"Wunschring","Beendet jeden Curse. Jederzeit spielbar. Nur einmal einsetzbar.");
//        Special wunschring2 = new Special(111,"Wunschring","Beendet jeden Curse. Jederzeit spielbar. Nur einmal einsetzbar.");
//        Special lampe = new Special(112,"Magische Lampe","Nur in der Runde spielbar. Sie beschwört einen Geist, der ein Monster verschwinden lässt, selbst wenn dein Weglaufenwurf verpatzt wurde und es dich fangen würde. War es das einzige Monster, erhälst du seinen treasure, aber keine level. Nur einmal einsetzbar.");
//        Special stiefel = new Special(113,"Stiefel zum Echt Schnellen Davonlaufen","Sie geben dir einen +2 Bonus auf runaway.");
//        Special tuba = new Special(114,"Tuba Der Verzauberung","Dieses melodiöse Instrument bezaubert deine Gegner und gibt dir +3 auf runaway. Falls du erfolgreich runaway kannst, zieh dir auf dem Weg nach draußen noch verdeckt eine Schatzkarte.");
//        Special gezinkterWuerfel = new Special(115,"Gezinkter Würfel","Spiel ihn, nachdem du aus einem beliebigen Grund würfeln musstest. Ändere das Würfelergebnis do wie du willst. Nur einmal einsetzbar.");
//        Special fertigmauer = new Special(116,"Fertigmauer","Ermöglicht automatisches Entkommen aus einem Kampf für dich. Nur einmal einsetzbar.");
//        Special doppelgaenger = new Special(117,"Doppelgänger","Beschwöre deine exakte Kopie. Verdopple deine Kampfstärke. Nur einmal einsetzbar.");
//        Special unsichtbarkeitstrank = new Special(118,"Unsichtbarkeitstrank","Ablegen, wenn der runaway-Wurf misslingt. Du entkommst automatisch. Nur einmal einsetzbar.");
//        Special freundschaftstrank = new Special(119,"Freundschaftstrank","Im Kampf spielen. Lege alle Monster des Kampfes ab. Du erhälst keinen treasure, aber du darfst den Raum durchsuchen. Nur einmal einsetzbar.");
//        Special polly = new Special(120,"Polly Verwandlungtrank","Im Kampf spielen. Verwandelt ein Monster in einen Papagei, der wegfliegt und seinen treasure zurücklässt. Nur einmal einsetzbar.");
//        //Special wuenschelstab = new Special(121,"Wünschelstab","Durchsuche die abgelegten Karten, um eine Card zu finden, die du willst. Nimm die neue Card und lege diese ab.");
//        Special schatzhort = new Special(122,"Schatzhort","Ziehe sofort 3 weitere Schatzkarten. Hast du diese Card verdeckt gezogen, ziehst du sie ebenfalls verdeckt, ansonsten offen.");
//
//
//
//        //Karten zum TreasureDeck hinzufügen
//        TreasureDeck.deck.add(riesigerFels );
//        TreasureDeck.deck.add(verdunkelungsumhang );
//        TreasureDeck.deck.add(hammer );
//        TreasureDeck.deck.add(scharferStreitkolben );
//        TreasureDeck.deck.add(schild );
//        TreasureDeck.deck.add(hellebarde );
//        TreasureDeck.deck.add(bogen );
//        TreasureDeck.deck.add(napalmstab );
//        TreasureDeck.deck.add(stange );
//        TreasureDeck.deck.add(helm );
//        TreasureDeck.deck.add(lederruestung );
//        TreasureDeck.deck.add(schleimigeRuestung );
//        TreasureDeck.deck.add(knie );
//        TreasureDeck.deck.add(geilerHelm );
//        TreasureDeck.deck.add(arschtrittStiefel  );
//        TreasureDeck.deck.add(buckler );
//        TreasureDeck.deck.add(flammendeRuestung );
//        TreasureDeck.deck.add(schwert );
//        TreasureDeck.deck.add(bastardSchwert );
//        TreasureDeck.deck.add(titel );
//        TreasureDeck.deck.add(tuch );
//        TreasureDeck.deck.add(breitschwert );
//        TreasureDeck.deck.add(kaesereibe );
//        TreasureDeck.deck.add(dolch );
//        TreasureDeck.deck.add(gentlemenKeule );
//        TreasureDeck.deck.add(sandwhich );
//        TreasureDeck.deck.add(hut );
//        TreasureDeck.deck.add(kurzeBreiteRuestung );
//        TreasureDeck.deck.add(trittleiter );
//        TreasureDeck.deck.add(kettensaege );
//        TreasureDeck.deck.add(mihrilRuestung );
//        TreasureDeck.deck.add(strumpfhose );
//        TreasureDeck.deck.add(rapier );
//
//        TreasureDeck.deck.add(lustballons );
//        TreasureDeck.deck.add(magischesGeschoss );
//        TreasureDeck.deck.add(ekliger );
//        TreasureDeck.deck.add(schlaftrunk );
//        TreasureDeck.deck.add(saeure );
//        TreasureDeck.deck.add(heldenmut );
//        TreasureDeck.deck.add(mundgeruch );
//        TreasureDeck.deck.add(explosivtrank );
//        TreasureDeck.deck.add(verwirrung );
//        TreasureDeck.deck.add(gifttrank );
//
//        TreasureDeck.deck.add(wunschring1 );
//        TreasureDeck.deck.add(wunschring2 );
//        TreasureDeck.deck.add(lampe );
//        TreasureDeck.deck.add(stiefel );
//        TreasureDeck.deck.add(tuba );
//        TreasureDeck.deck.add(gezinkterWuerfel );
//        TreasureDeck.deck.add(fertigmauer );
//        TreasureDeck.deck.add(doppelgaenger );
//        TreasureDeck.deck.add(unsichtbarkeitstrank );
//        TreasureDeck.deck.add(freundschaftstrank );
//        TreasureDeck.deck.add(polly );
//        //TreasureDeck.deck.add(wuenschelstab );
//        TreasureDeck.deck.add(schatzhort );*/
//
//
//        /*
//        Special XXXXX = new Special(123,"","");
//        Special XXXXX = new Special(124,"","");
//        Special XXXXX = new Special(125,"","");
//        Special XXXXX = new Special(126,"","");
//        Special XXXXX = new Special(127,"","");
//        Special XXXXX = new Special(128,"","");
//        Special XXXXX = new Special(129,"","");
//        Special XXXXX = new Special(130,"","");
//        Special XXXXX = new Special(131,"","");
//        Special XXXXX = new Special(132,"","");
//        Special XXXXX = new Special(133,"","");
//        Special XXXXX = new Special(134,"","");
//        Special XXXXX = new Special(135,"","");
//        Special XXXXX = new Special(136,"","");
//        Special XXXXX = new Special(137,"","");
//        Special XXXXX = new Special(138,"","");
//        */
//    }
//}

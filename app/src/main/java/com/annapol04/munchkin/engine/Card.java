package com.annapol04.munchkin.engine;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.engine.BonusWear.BonusWearName;
import com.annapol04.munchkin.engine.BonusWear.Blocking;
import com.annapol04.munchkin.engine.BonusWear.Size;

import java.lang.reflect.Field;

public class Card {

    public enum Membership{
        DOOR,
        TREASURE;
    }


    /* Monster */
    //public Monster(String name, int imageResourceID, Monster.Monstername monsterName, String description, int level, int treasure, String badThings)
    public static final Card LEPERCHAUN = new Monster("Leperchaun",         R.drawable.leperchaun, Monster.Monstername.LEPRACHAUN,"Beschreibung dummy", 1, 1, "bad Things");
    //public static final Card LEPERCHAUN = new Card("leperchaun", R.drawable.leperchaun, Membership.DOOR);
//
    public static final Card FILZLAEUSE = new Monster("FILZLAEUSE",         R.drawable.crabs, Monster.Monstername.FILZLAEUSE,"Beschreibung dummy", 1, 1, "bad Things");
    public static final Card SABBERNDERSCHLEIM = new Monster("SABBERNDERSCHLEIM", R.drawable.plain, Monster.Monstername.SABBERNDERSCHLEIM,"Beschreibung dummy", 1, 1, "bad Things");
    public static final Card LAHMERGOBLIN = new Monster("LAHMERGOBLIN",     R.drawable.plain, Monster.Monstername.LAHMERGOBLIN,"Beschreibung dummy", 1, 1, "bad Things");
    public static final Card HAMMERRATTE = new Monster("HAMMERRATTE",    R.drawable.plain, Monster.Monstername.HAMMERRATTE,"Beschreibung dummy", 1, 1, "bad Things");
    public static final Card TOPFPFLANZE = new Monster("TOPFPFLANZE",    R.drawable.plain, Monster.Monstername.TOPFPFLANZE,"Beschreibung dummy", 1, 1, "bad Things");
    public static final Card FLIEGENDEFROESCHE = new Monster("FLIEGENDEFROESCHE", R.drawable.plain, Monster.Monstername.FLIEGENDEFROESCHE,"Beschreibung dummy", 1, 1, "bad Things");
    public static final Card GALLERTOKTAEDER = new Monster("GALLERTOKTAEDER", R.drawable.geratinousoctahedron, Monster.Monstername.GALLERTOKTAEDER,"Beschreibung dummy", 1, 1, "bad Things");
    public static final Card HUHN = new Monster("HUHN",                 R.drawable.plain, Monster.Monstername.HUHN,"Beschreibung dummy", 1, 1, "bad Things");


    /* Equipment */
    //public BonusWear (String name, int imageResourceID, BonusWearName bonusWearName, int bonus, Blocking blocking, Size size)
    public static final Card STANGE = new BonusWear("STANGE",               R.drawable.plain, BonusWearName.STANGE, 1, Blocking.TWOHANDS, Size.SMALL);
    public static final Card HELM = new BonusWear("HELM",                   R.drawable.helmofcourage, BonusWearName.HELM, 1, Blocking.HEAD, Size.SMALL);
    public static final Card LEDERRUESTUNG = new BonusWear("LEDERRUESTUNG", R.drawable.tleatherarmor, BonusWearName.LEDERRUESTUNG, 1, Blocking.ARMOR, Size.SMALL);
    public static final Card SCHLEIMIGERUESTUNG = new BonusWear("SCHLEIMIGERUESTUNG", R.drawable.plain, BonusWearName.SCHLEIMIGERUESTUNG, 1, Blocking.ARMOR, Size.SMALL);
    public static final Card KNIE = new BonusWear("KNIE",                   R.drawable.tspikyknies, BonusWearName.KNIE, 1, Blocking.NOTHING, Size.SMALL);
    public static final Card GEILERHELM = new BonusWear("GEILERHELM",       R.drawable.thornyhelmet, BonusWearName.GEILERHELM, 1, Blocking.HEAD, Size.SMALL);
    public static final Card ARSCHTRITTSTIEFEL = new BonusWear("ARSCHTRITTSTIEFEL", R.drawable.plain, BonusWearName.ARSCHTRITTSTIEFEL, 1, Blocking.SHOES, Size.SMALL);


    //old stuff
//    public static final Card TUBA_OF_CHARM = new Card("tubaofcharm", R.drawable.tubaofcharm, Membership.TREASURE);
//    public static final Card STAFF_OF_NAPALM = new Card("staffofnapalm", R.drawable.staffofnapalm, Membership.TREASURE);
//    public static final Card SNEAKY_BASTARDS_WORD = new Card("sneakybastardsword", R.drawable.sneakybastardsword, Membership.TREASURE);
//    public static final Card SANDALS_OF_PROTECTION = new Card("sandalsofprotection", R.drawable.sandalsofprotektion, Membership.TREASURE);
//    public static final Card RAT_ON_A_STICK = new Card("ratonastick", R.drawable.ratonastick, Membership.TREASURE);
//    public static final Card POINTY_HAT_OF_POWER = new Card("pointyhatofpower", R.drawable.pointyhatofpower, Membership.TREASURE);
//    public static final Card PANTYHOSE_OF_GIANT_STRENGTH = new Card("pantyhoseofgiantstrength", R.drawable.pantyhoseofgiantstrength, Membership.TREASURE);
//    public static final Card NASTY_TASTING_SPORT_DRINK = new Card("nastytastingsportdrink", R.drawable.nastytastingsportsdrink, Membership.TREASURE);
//    public static final Card HIRELING = new Card("hireling", R.drawable.hireling, Membership.TREASURE);
//    public static final Card HELM_OF_COURAGE = new Card("helmofcourage", R.drawable.helmofcourage, Membership.TREASURE);
//    public static final Card FLAMING_POISON_PORTION = new Card("flamingpoisonportion", R.drawable.flamingpoisonpotion, Membership.TREASURE);
//    public static final Card BROADSWORD = new Card("broadsword", R.drawable.broadsword, Membership.TREASURE);
//
//    public static final Card BIGFOOT = new Card("bigfoot", R.drawable.bigfoot);
//    public static final Card ORCS = new Card("Orcs",R.drawable.orcs);
//    public static final Card CHICKEN_ON_YOUR_HEAD = new Card("chickenOnYourHead", R.drawable.chickenonyourhead);
//    public static final Card DUCK_OF_DOOM = new Card("duckOfDoom", R.drawable.duckofdoom);
//    public static final Card ELF = new Card("elf", R.drawable.elf);
//    public static final Card GAZEBO = new Card("gazebo", R.drawable.gazebo);
//    public static final Card HALFLING = new Card("halfling", R.drawable.halfling);
//    public static final Card INCOME_TAX = new Card("incomeTax", R.drawable.incometax);
//    public static final Card INSURANCE_SALESMAN = new Card("insuranceSalesman", R.drawable.insurancesalesman);
//    public static final Card LAWYERS = new Card("lawyers", R.drawable.lawyers);
//    public static final Card PLATYCORE = new Card("platycore", R.drawable.platycore);

    private static int idSource = 0;
    private static final Card[] lookup;

    static {
        Field[] fields = Card.class.getFields();

        lookup = new Card[fields.length];

        try {
            for (Field field : fields)
                if (field.getType() == Card.class) {
                    Card card = (Card) field.get(null);
                    lookup[card.getId()] = card;
                }
        } catch (IllegalAccessException e) { /* wont happen... */ }
    }


    private final int id;
    private final String name;
    private final int imageResourceID;
    public Membership member;

    public Card(String name, int imageResourceID, Membership member) {
        this.member = member;
        this.id = idSource++;
        this.name = name;
        this.imageResourceID = imageResourceID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public int getImageResourceID() {
        return imageResourceID;
    }

    public static Card fromId(int id) {
        if (id >= lookup.length)
            throw new IllegalArgumentException("Invalid event card id " + id);

        return lookup[id];
    }
}

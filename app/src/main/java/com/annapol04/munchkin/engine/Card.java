package com.annapol04.munchkin.engine;

import com.annapol04.munchkin.R;

import java.lang.reflect.Field;

public class Card {
    /* Door */
    public static final Card LEPERCHAUN = new Card("leperchaun", R.drawable.leperchaun);
    public static final Card HUMONGOUS_PLUSTOMONSTER =  new Card("humongousplustomonster", R.drawable.humongousplustomonster);
    public static final Card HALFBREEDTOO = new Card("halfbreedtoo", R.drawable.halfbreedtoo);
    public static final Card GELATINOUS_OCTAHEDRON = new Card("geratinousoctahedron", R.drawable.geratinousoctahedron);
    public static final Card ENRAGED = new Card("enraged", R.drawable.enraged);
    public static final Card DWARF = new Card("dwarf", R.drawable.dwarf);
    public static final Card CURSE_CHANGE_SEX = new Card("cursechangesex", R.drawable.cursechangesex);
    public static final Card CURSE_CHANGE_RACE = new Card("cursechangerace", R.drawable.cursechangerace);
    public static final Card CRABS = new Card("crabs", R.drawable.crabs);
    public static final Card CLERIC = new Card("cleric", R.drawable.cleric);
    public static final Card CHEAT = new Card("cheat", R.drawable.cheat);

    /* Treasure */
    public static final Card TUBA_OF_CHARM = new Card("tubaofcharm", R.drawable.tubaofcharm);
    public static final Card STAFF_OF_NAPALM = new Card("staffofnapalm", R.drawable.staffofnapalm);
    public static final Card SNEAKY_BASTARDS_WORD = new Card("sneakybastardsword", R.drawable.sneakybastardsword);
    public static final Card SANDALS_OF_PROTECTION = new Card("sandalsofprotection", R.drawable.sandalsofprotektion);
    public static final Card RAT_ON_A_STICK = new Card("ratonastick", R.drawable.ratonastick);
    public static final Card POINTY_HAT_OF_POWER = new Card("pointyhatofpower", R.drawable.pointyhatofpower);
    public static final Card PANTYHOSE_OF_GIANT_STRENGTH = new Card("pantyhoseofgiantstrength", R.drawable.pantyhoseofgiantstrength);
    public static final Card NASTY_TASTING_SPORT_DRINK = new Card("nastytastingsportdrink", R.drawable.nastytastingsportsdrink);
    public static final Card HIRELING = new Card("hireling", R.drawable.hireling);
    public static final Card HELM_OF_COURAGE = new Card("helmofcourage", R.drawable.helmofcourage);
    public static final Card FLAMING_POISON_PORTION = new Card("flamingpoisonportion", R.drawable.flamingpoisonpotion);
    public static final Card BROADSWORD = new Card("broadsword", R.drawable.broadsword);

    public static final Card BIGFOOT = new Card("bigfoot", R.drawable.bigfoot);
    public static final Card ORCS = new Card("Orcs",R.drawable.orcs);
    public static final Card CHICKEN_ON_YOUR_HEAD = new Card("chickenOnYourHead", R.drawable.chickenonyourhead);
    public static final Card DUCK_OF_DOOM = new Card("duckOfDoom", R.drawable.duckofdoom);
    public static final Card ELF = new Card("elf", R.drawable.elf);
    public static final Card GAZEBO = new Card("gazebo", R.drawable.gazebo);
    public static final Card HALFLING = new Card("halfling", R.drawable.halfling);
    public static final Card INCOME_TAX = new Card("incomeTax", R.drawable.incometax);
    public static final Card INSURANCE_SALESMAN = new Card("insuranceSalesman", R.drawable.insurancesalesman);
    public static final Card LAWYERS = new Card("lawyers", R.drawable.lawyers);
    public static final Card PLATYCORE = new Card("platycore", R.drawable.platycore);

    private static int idSource = 0;
    private static final Card[] lookup;

    static {
        Field[] fields = Action.class.getFields();

        lookup = new Card[fields.length];

        int i = 0;

        try {
            for (Field field : fields)
                if (field.getType() == Card.class)
                    lookup[i++] = (Card) field.get(null);
        } catch (IllegalAccessException e) { /* wont happen... */ }
    }


    private final int id;
    private final String name;
    private final int imageResourceID;

    public Card(String name, int imageResourceID) {
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

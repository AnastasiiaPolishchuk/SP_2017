package com.annapol04.munchkin.engine;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class Monster extends Card {

    public enum Monstername{
        FILZLAEUSE,
        SABBERNDERSCHLEIM,
        LAHMERGOBLIN,
        HAMMERRATTE,
        TOPFPFLANZE,
        FLIEGENDEFROESCHE,
        GALLERTOKTAEDER,
        HUHN,
        MRBONES,
        PITBULL,
        HARFIEN,
        LEPRACHAUN,
        SCHNECKEN,
        UNTOTESPFERD,
        ANWALT,
        ENTIKORE,
        PIKOTZU,
        KREISCHENDERDEPP,
        AMAZONE,
        GESICHTSSAUGER,
        PAVILLON,
        GEMEINEGHOULE,
        ORKS,
        LAUFENDENASE,
        NETZTROLL,
        BIGFOOT,
        ZUNGENDAEMON,
        VAMPIR,
        VERSICHERUNGSVERTRETER,
        BEKIFFTERGOLEM,
        SCHRECKEN,
        HIPPOGREIF,
        KOENIGTUT,
        GRUFTIGEGEBRUEDER,
        KRAKZILLA,
        BULLROG,
        PLUTONIUMDRACHE;
    }

    private int level;
    private int treasure;
    private String badThings;

    public Monster(@StringRes int name, @StringRes int description, @DrawableRes int imageResourceID, int level, int treasure, String badThings) {
        super(name, description, imageResourceID);
        this.level = level;
        this.treasure = treasure;
        this.badThings = badThings;
    }

    public int getLevel() {
        return level;
    }

    public int getTreasure() {
        return treasure;
    }
}

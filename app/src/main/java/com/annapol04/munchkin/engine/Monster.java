package com.annapol04.munchkin.engine;

/**
 * Created by Falco on 30.12.2017.
 */

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

    public int level;
    public int treasure;
    public String badThings;
    public Monstername monsterName;
    public Membership stack;
    public String description;

    public Monster(String name, int imageResourceID, Monstername monsterName, String description, int level, int treasure, String badThings) {
        super(name, imageResourceID, Membership.DOOR);
        this.description = description;
        this.monsterName = monsterName;
        this.level = level;
        this.treasure = treasure;
        this.badThings = badThings;
    }
}

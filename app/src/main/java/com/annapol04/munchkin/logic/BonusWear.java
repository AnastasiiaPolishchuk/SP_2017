//package com.example.admin.ezmunchkin;
//
///**
// * Created by Falco on 26.10.2017.
// */
//
//public class BonusWear extends Card { //Schatzstapel
//
//    public enum Blocking {
//        NOTHING,
//        ONEHAND,
//        TWOHANDS,
//        ARMOR,
//        HEAD,
//        SHOES;
//    }
//
//    public enum Size {
//        SMALL,
//        BIG;
//    }
//
//    public enum BonusWearName{
//        STANGE,
//        HELM,
//        LEDERRUESTUNG,
//        SCHLEIMIGERUESTUNG,
//        KNIE,
//        GEILERHELM,
//        ARSCHTRITTSTIEFEL,
//        BUCKLER,
//        FLAMMENDERUESTUNG,
//        SCHWERT,
//        BASTARDSCHWERT,
//        TITEL,
//        TUCH,
//        BREITSCHWERT,
//        KAESEREIBE,
//        DOLCH,
//        GENTLEMENKEULE,
//        SANDWHICH,
//        HUT,
//        KURZEBREITERUESTUNG,
//        TRITTLEITER,
//        KETTENSAEGE,
//        MIHRILRUESTUNG,
//        STRUMPFHOSE,
//        RAPIER;
//    }
//
//    public int bonus;
//    BonusWearName bonusWearName;
//    Blocking blocking; //0 = nothing, 1 = 1 hand, 2 = 2 hand, 3 = armour, 4 = head, 5 = shoes
//    Size size; //0 = small, 1 = big
//
//    public BonusWear (String name, BonusWearName bonusWearName, int bonus, Blocking blocking, Size size) {
//        super(Membership.TREASURE, name);
//        this.bonusWearName = bonusWearName;
//        this.bonus = bonus;
//        this.blocking = blocking;
//        this.size = size;
//    }
//}

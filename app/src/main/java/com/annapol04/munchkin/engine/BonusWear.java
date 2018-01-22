package com.annapol04.munchkin.engine;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class BonusWear extends Card {

    public enum Blocking {
        NOTHING,
        ONEHAND,
        TWOHANDS,
        ARMOR,
        HEAD,
        SHOES;
    }

    public enum Size {
        SMALL,
        BIG;
    }

    public enum BonusWearName{
        STANGE,
        HELM,
        LEDERRUESTUNG,
        SCHLEIMIGERUESTUNG,
        KNIE,
        GEILERHELM,
        ARSCHTRITTSTIEFEL,
        BUCKLER,
        FLAMMENDERUESTUNG,
        SCHWERT,
        BASTARDSCHWERT,
        TITEL,
        TUCH,
        BREITSCHWERT,
        KAESEREIBE,
        DOLCH,
        GENTLEMENKEULE,
        SANDWHICH,
        HUT,
        KURZEBREITERUESTUNG,
        TRITTLEITER,
        KETTENSAEGE,
        MIHRILRUESTUNG,
        STRUMPFHOSE,
        RAPIER;
    }

    public final int bonus;
    public final Blocking blocking;
    public final Size size;

    public BonusWear (@StringRes int name, @StringRes int description, @DrawableRes int imageResourceID, int bonus, Blocking blocking, Size size) {
        super(name, description, imageResourceID);
        this.bonus = bonus;
        this.blocking = blocking;
        this.size = size;
    }

}

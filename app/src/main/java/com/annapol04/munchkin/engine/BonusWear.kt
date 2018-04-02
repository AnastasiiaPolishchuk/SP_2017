package com.annapol04.munchkin.engine

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

class BonusWear(@StringRes name: Int,
                @StringRes description: Int,
                @DrawableRes imageResourceID: Int,
                val bonus: Int,
                val blocking: Blocking,
                val size: Size)
    : Card(name, description, imageResourceID) {

    enum class Blocking {
        NOTHING,
        ONEHAND,
        TWOHANDS,
        ARMOR,
        HEAD,
        SHOES
    }

    enum class Size {
        SMALL,
        BIG
    }

    enum class BonusWearName {
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
        RAPIER
    }

}

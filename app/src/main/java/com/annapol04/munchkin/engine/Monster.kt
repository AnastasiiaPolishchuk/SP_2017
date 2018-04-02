package com.annapol04.munchkin.engine

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

class Monster(@StringRes name: Int, @StringRes description: Int, @DrawableRes imageResourceID: Int, val level: Int, val treasure: Int, private val badThings: String) : Card(name, description, imageResourceID) {

    enum class Monstername {
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
        PLUTONIUMDRACHE
    }
}

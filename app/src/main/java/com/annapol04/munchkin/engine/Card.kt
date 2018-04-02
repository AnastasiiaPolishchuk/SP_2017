package com.annapol04.munchkin.engine

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

import java.util.ArrayList

open class Card(@param:StringRes @field:StringRes
                val name: Int,
                @param:StringRes @field:StringRes
                private val description: Int,
                @param:DrawableRes @field:DrawableRes
                val imageResourceID: Int) {

    val id: Int

    init {
        this.id = idSource++

        lookup.add(this)
    }

    companion object {
        private val lookup = ArrayList<Card>()
        private var idSource = 0

        fun fromId(id: Int): Card {
            if (id >= lookup.size)
                throw IllegalArgumentException("Invalid event card id $id")

            return lookup[id]
        }
    }

}

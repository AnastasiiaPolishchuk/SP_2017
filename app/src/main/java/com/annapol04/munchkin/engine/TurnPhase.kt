package com.annapol04.munchkin.engine

import android.support.annotation.IdRes
import android.support.annotation.StringRes

import com.annapol04.munchkin.R

enum class TurnPhase private constructor(@param:StringRes @field:StringRes val stringId: Int) {
    IDLE(R.string.tp_idle),
    EQUIPMENT(R.string.tp_equipment),
    KICK_OPEN_THE_DOOR(R.string.tp_kick_open_the_door),
    KICK_OPEN_THE_DOOR_AND_DRAW(R.string.tp_kick_open_the_door_and_draw),
    LOOK_FOR_TROUBLE(R.string.tp_look_for_trouble),
    LOOT_THE_ROOM(R.string.tp_loot_the_room),
    CHARITY(R.string.tp_charity),
    FINISHED(R.string.ev_empty);


    companion object {
        private val lookup = arrayOf(IDLE, EQUIPMENT, KICK_OPEN_THE_DOOR, KICK_OPEN_THE_DOOR_AND_DRAW, LOOK_FOR_TROUBLE, LOOT_THE_ROOM, CHARITY)

        fun fromId(id: Int): TurnPhase {
            if (id >= lookup.size)
                throw IllegalArgumentException("Invalid turn phase id $id")

            return lookup[id]
        }
    }
}

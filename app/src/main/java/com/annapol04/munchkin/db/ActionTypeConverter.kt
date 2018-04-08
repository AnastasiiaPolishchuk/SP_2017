package com.annapol04.munchkin.db

import android.arch.persistence.room.TypeConverter

import com.annapol04.munchkin.engine.Action

class ActionTypeConverter {

    @TypeConverter
    fun toInt(action: Action): Int {
        return action.id
    }

    @TypeConverter
    fun toAction(id: Int): Action {
        return Action.fromId(id)
    }
}

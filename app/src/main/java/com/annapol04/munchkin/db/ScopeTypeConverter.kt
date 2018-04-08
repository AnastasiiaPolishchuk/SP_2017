package com.annapol04.munchkin.db

import android.arch.persistence.room.TypeConverter

import com.annapol04.munchkin.engine.Scope

class ScopeTypeConverter {
    @TypeConverter
    fun toInt(scope: Scope): Int {
        return scope.ordinal
    }

    @TypeConverter
    fun toScope(id: Int): Scope {
        return Scope.fromId(id)
    }
}

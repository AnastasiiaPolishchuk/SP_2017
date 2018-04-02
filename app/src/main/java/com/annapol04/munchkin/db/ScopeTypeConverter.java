package com.annapol04.munchkin.db;

import android.arch.persistence.room.TypeConverter;

import com.annapol04.munchkin.engine.Scope;

public class ScopeTypeConverter {
    @TypeConverter
   public static int toInt(Scope scope) {
        return scope.ordinal();
    }

    @TypeConverter
    public static Scope toScope(int id) {
        return Scope.Companion.fromId(id);
    }
}

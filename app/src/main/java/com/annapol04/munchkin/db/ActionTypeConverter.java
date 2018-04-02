package com.annapol04.munchkin.db;

import android.arch.persistence.room.TypeConverter;

import com.annapol04.munchkin.engine.Action;

public class ActionTypeConverter {

    @TypeConverter
    public static int toInt(Action action) {
        return action.getId();
    }

    @TypeConverter
    public static Action toAction(int id) {
        return Action.Companion.fromId(id);
    }
}

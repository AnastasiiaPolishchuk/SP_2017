package com.annapol04.munchkin.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

import com.annapol04.munchkin.engine.Event

@Database(entities = arrayOf(Event::class), version = 1, exportSchema = false)
@TypeConverters(ActionTypeConverter::class, DataTypeConverter::class, ScopeTypeConverter::class, EventDataTypeConverter::class)
abstract class AppDb : RoomDatabase() {
    abstract fun eventDao(): EventDao
}

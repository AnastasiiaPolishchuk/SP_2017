package com.annapol04.munchkin.db

import android.arch.persistence.room.TypeConverter

import com.annapol04.munchkin.engine.DataType

class DataTypeConverter {
    @TypeConverter
    fun toInt(dataType: DataType): Int {
        return dataType.ordinal
    }

    @TypeConverter
    fun toDataType(id: Int): DataType {
        return DataType.fromId(id)
    }
}

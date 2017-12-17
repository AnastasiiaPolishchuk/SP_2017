package com.annapol04.munchkin.db;

import android.arch.persistence.room.TypeConverter;

import com.annapol04.munchkin.engine.DataType;

public class DataTypeConverter {
    @TypeConverter
    public static int toInt(DataType dataType) {
        return dataType.ordinal();
    }

    @TypeConverter
    public static DataType toDataType(int id) {
        return DataType.fromId(id);
    }
}

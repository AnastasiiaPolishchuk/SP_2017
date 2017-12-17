package com.annapol04.munchkin.db;

import android.arch.persistence.room.TypeConverter;

import com.annapol04.munchkin.engine.Decoder;
import com.annapol04.munchkin.engine.EventData;

public class EventDataTypeConverter {
    private static final Decoder decoder = new Decoder();

    @TypeConverter
    public static byte[] toBytes(EventData data) {
        return data.getBytes();
    }

    @TypeConverter
    public static EventData toEventData(byte[] data) {
        return decoder.decodeEventData(data);
    }
}

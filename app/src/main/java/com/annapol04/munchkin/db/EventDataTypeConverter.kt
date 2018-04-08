package com.annapol04.munchkin.db

import android.arch.persistence.room.TypeConverter

import com.annapol04.munchkin.engine.Decoder
import com.annapol04.munchkin.engine.EventData

class EventDataTypeConverter {
    private val decoder = Decoder()

    @TypeConverter
    fun toBytes(data: EventData): ByteArray {
        return data.getBytes()
    }

    @TypeConverter
    fun toEventData(data: ByteArray): EventData? {
        return decoder.decodeEventData(data)
    }
}

package com.annapol04.munchkin.engine

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class EventData private constructor(val type: DataType, val data: Any?) {

    fun getBytes(): ByteArray {
        val buffer = ByteBuffer.allocate(size())
        buffer.put(type.ordinal.toByte())

        when (type) {
            DataType.STRING -> return buffer.put(getString().toByteArray(StandardCharsets.UTF_8))
                    .put(0.toByte())
                    .array()
            DataType.INTEGER -> return buffer.putInt(getInteger()).array()
            DataType.EMPTY -> return buffer.array()
        }
    }

    fun getString() = data as String
    fun getInteger() = data as Int

    constructor() : this(DataType.EMPTY, null) {}

    constructor(data: Int) : this(DataType.INTEGER, data) {}

    constructor(data: String) : this(DataType.STRING, data) {}

    fun size(): Int {
        when (type) {
            DataType.STRING -> return getString().toByteArray().size + 1 + 1
            DataType.INTEGER -> return 4 + 1
            DataType.EMPTY -> return 1
        }
    }

    override fun toString(): String {
        when (type) {
            DataType.EMPTY -> return "null"
            DataType.INTEGER -> return Integer.toString(getInteger())
            DataType.STRING -> return getString()
        }
    }
}

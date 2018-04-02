package com.annapol04.munchkin.engine

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.Arrays
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Decodes following structure.
 *
 * # Event structure
 * |  previous hash  |   hash   | scope  |  action id |  message id  |  data type |  data   |
 * |-----------------|----------|--------|------------|--------------|------------|---------|
 * |    16 bytes     | 16 bytes | 1 Byte |   4 Byte   |   4 Byte     |   1 Byte   | n Bytes |
 */
@Singleton
class Decoder @Inject
constructor() {
    private var decodedEvents: MutableList<Event>? = null

    fun decode(data: ByteArray, pos: Int, length: Int): List<Event> {
        var pos = pos
        val end = pos + length

        decodedEvents = mutableListOf()

        while (pos < end)
            pos = decodeEvent(data, pos, end)

        return decodedEvents!!
    }

    fun decodeEventData(data: ByteArray): EventData? {
        if (data.size < 4)
            throw IllegalArgumentException("Illegal event data format: " + Arrays.toString(data))

        val buffer = ByteBuffer.wrap(data)

        when (DataType.fromId(buffer.int)) {
            DataType.EMPTY -> return EventData()
            DataType.INTEGER -> {
                if (data.size < 8)
                    throw IllegalArgumentException("Illegal event data format: " + Arrays.toString(data))

                return EventData(buffer.int)
            }
            DataType.STRING -> {
                if (data.size < 5)
                    throw IllegalArgumentException("Illegal event data format: " + Arrays.toString(data))

                val stringBuffer = Arrays.copyOfRange(buffer.array(), buffer.position(), 0)
                val string = String(stringBuffer, StandardCharsets.UTF_8)
                return EventData(String(stringBuffer, StandardCharsets.UTF_8))
            }
        }
    }

    private fun parseInteger(data: ByteArray, pos: Int): Int {
        return (data[pos].toInt() shl 24
                or (data[pos + 1].toInt() and 0xFF shl 16)
                or (data[pos + 2].toInt() and 0xFF shl 8)
                or (data[pos + 3].toInt() and 0xFF))
    }

    private fun parseHash(data: ByteArray, pos: Int): ByteArray {
        val hash = ByteArray(16)

        System.arraycopy(data, pos, hash, 0, 16)

        return hash
    }

    private fun strlen(data: ByteArray, pos: Int, end: Int): Int {
        var length = 0

        while (pos + length < end) {
            if (data[pos + length] == 0.toByte())
                return length

            length++
        }

        return -1
    }

    private fun decodeEvent(data: ByteArray, pos: Int, end: Int): Int {
        var endOfheader = pos + HEADER_LENGTH

        if (endOfheader <= end) {
            try {
                val previousHash = parseHash(data, pos)
                val hash = parseHash(data, pos + 16)
                val scopeId = data[pos + 32].toInt()
                val actionId = parseInteger(data, pos + 33)
                val messageId = parseInteger(data, pos + 37)
                val dataTypeId = data[pos + 41].toInt()

                val scope = Scope.fromId(scopeId)
                val action = Action.fromId(actionId)
                val dataType = DataType.fromId(dataTypeId)

                if (dataType === DataType.EMPTY)
                    decodedEvents!!.add(Event(scope, action, messageId, previousHash, hash))
                else if (dataType === DataType.STRING) {
                    val length = strlen(data, endOfheader, end)

                    if (length < 0)
                        throw IllegalStateException("Unknown string format")
                    else {
                        val stringBuffer = ByteArray(length)
                        System.arraycopy(data, endOfheader, stringBuffer, 0, length)
                        val string = String(stringBuffer, StandardCharsets.UTF_8)
                        decodedEvents!!.add(Event(0, scope, action, messageId, EventData(string), previousHash, hash))
                        endOfheader += length + 1
                    }
                } else if (dataType === DataType.INTEGER) {
                    if (endOfheader + 4 > end)
                        throw IllegalStateException("Unknown integer format")
                    else
                        decodedEvents!!.add(Event(0, scope, action, messageId,
                                EventData(parseInteger(data, endOfheader)), previousHash, hash))
                } else
                    throw IllegalStateException("Unknown event data type $dataTypeId")
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }

            return endOfheader
        }

        return end
    }

    companion object {

        private val HEADER_LENGTH = 42
    }

}

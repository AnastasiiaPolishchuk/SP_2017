package com.annapol04.munchkin.engine

import com.annapol04.munchkin.engine.Action
import com.annapol04.munchkin.engine.DataType
import com.annapol04.munchkin.engine.Decoder
import com.annapol04.munchkin.engine.Event
import com.annapol04.munchkin.engine.Scope

import junit.framework.Assert

import org.junit.Test

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.Arrays

import junit.framework.Assert.assertEquals


class DecoderTest {
    @Test
    fun decodeEventWithEmptyData() {
        val b = byteArrayOf(0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), // previous hash
                0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), 7.toByte(), 6.toByte(), 1.toByte(), 0.toByte(), 4.toByte(), 0.toByte(), 1.toByte(), 0.toByte(), // hash
                0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), 2.toByte(), // player 2 scope
                0.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), // action id nothing
                0.toByte(), 0.toByte(), 0.toByte(), 3.toByte(), // message id empty
                0.toByte())// data type empty

        val decoder = Decoder()

        val events = decoder.decode(b, 0, b.size)

        assertEquals(1, events.size)

        val event = events[0]
        Assert.assertEquals(Scope.PLAYER2, event.scope)
        Assert.assertEquals(Action.JOIN_PLAYER, event.action)
        assertEquals(3, event.messageId)
        Assert.assertEquals(DataType.EMPTY, event.getDataType())
    }

    @Test
    @Throws(IOException::class)
    fun decodeEventWithHash() {
        val b = byteArrayOf(0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), // previous hash
                0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), 7.toByte(), 6.toByte(), 1.toByte(), 0.toByte(), 4.toByte(), 0.toByte(), 1.toByte(), 0.toByte(), // hash
                0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), 2.toByte(), // player 2 scope
                0.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), // action id nothing
                0.toByte(), 0.toByte(), 0.toByte(), 3.toByte(), // message id empty
                1.toByte(), // data type empty
                1.toByte(), 2.toByte(), 3.toByte(), 4.toByte())// ‭16909060‬

        val decoder = Decoder()

        val events = decoder.decode(b, 0, b.size)

        assertEquals(1, events.size)

        val event = events[0]
        Assert.assertTrue(
                Arrays.equals(byteArrayOf(0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), // previous hash
                        0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte()),
                        event.getPreviousHash()))

        Assert.assertTrue(
                Arrays.equals(byteArrayOf(7.toByte(), 6.toByte(), 1.toByte(), 0.toByte(), 4.toByte(), 0.toByte(), 1.toByte(), 0.toByte(), // hash
                        0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte()),
                        event.hash))
    }

    @Test
    fun decodeEventWithIntegerData() {
        val b = byteArrayOf(0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), // previous hash
                0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), 7.toByte(), 6.toByte(), 1.toByte(), 0.toByte(), 4.toByte(), 0.toByte(), 1.toByte(), 0.toByte(), // hash
                0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), 2.toByte(), // player 2 scope
                0.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), // action id nothing
                0.toByte(), 0.toByte(), 0.toByte(), 3.toByte(), // message id empty
                1.toByte(), // data type empty
                1.toByte(), 2.toByte(), 3.toByte(), 4.toByte())// ‭16909060‬

        val decoder = Decoder()

        val events = decoder.decode(b, 0, b.size)

        assertEquals(1, events.size)

        val event = events[0]
        assertEquals(Scope.PLAYER2, event.scope)
        assertEquals(Action.JOIN_PLAYER, event.action)
        assertEquals(3, event.messageId)
        assertEquals(DataType.INTEGER, event.getDataType())
        assertEquals(16909060, event.getInteger())
    }

    @Test
    @Throws(IOException::class)
    fun decodeEventWithStringData() {
        val outputStream = ByteArrayOutputStream()
        outputStream.write(byteArrayOf(0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), // previous hash
                0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), 7.toByte(), 6.toByte(), 1.toByte(), 0.toByte(), 4.toByte(), 0.toByte(), 1.toByte(), 0.toByte(), // hash
                0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), 2.toByte(), // player 2 scope
                0.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), // action id nothing
                0.toByte(), 0.toByte(), 0.toByte(), 3.toByte(), // message id empty
                2.toByte())// data type empty
        )
        outputStream.write("Hello World!".toByteArray(StandardCharsets.UTF_8))
        outputStream.write(byteArrayOf(0.toByte()))
        val b = outputStream.toByteArray()

        val decoder = Decoder()

        val events = decoder.decode(b, 0, b.size)

        assertEquals(1, events.size)

        val event = events[0]
        assertEquals(Scope.PLAYER2, event.scope)
        assertEquals(Action.JOIN_PLAYER, event.action)
        assertEquals(3, event.messageId)
        assertEquals(DataType.STRING, event.getDataType())
        assertEquals("Hello World!", event.getString())
    }

    @Test
    @Throws(IOException::class)
    fun decodeMultipleEvents() {
        val outputStream = ByteArrayOutputStream()
        outputStream.write(byteArrayOf(0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), // previous hash
                0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), 7.toByte(), 6.toByte(), 1.toByte(), 0.toByte(), 4.toByte(), 0.toByte(), 1.toByte(), 0.toByte(), // hash
                0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), 2.toByte(), // player 2 scope
                0.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), // action id nothing
                0.toByte(), 0.toByte(), 0.toByte(), 3.toByte(), // message id empty
                2.toByte())// data type empty
        )
        outputStream.write("Hello World!".toByteArray(StandardCharsets.UTF_8))
        outputStream.write(byteArrayOf(0.toByte()))
        outputStream.write(byteArrayOf(0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), // previous hash
                0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), 7.toByte(), 6.toByte(), 1.toByte(), 0.toByte(), 4.toByte(), 0.toByte(), 1.toByte(), 0.toByte(), // hash
                0.toByte(), 1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte(), 2.toByte(), // player 2 scope
                0.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), // action id nothing
                0.toByte(), 0.toByte(), 0.toByte(), 3.toByte(), // message id empty
                2.toByte())// data type empty
        )
        outputStream.write("Hello World!".toByteArray(StandardCharsets.UTF_8))
        outputStream.write(byteArrayOf(0.toByte()))
        val b = outputStream.toByteArray()

        val decoder = Decoder()

        val events = decoder.decode(b, 0, b.size)

        assertEquals(2, events.size)
    }
}

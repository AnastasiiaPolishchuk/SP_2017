package com.annapol04.munchkin.engine

import org.junit.Test

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.Arrays

import junit.framework.Assert

class EventTest {
    @Test
    fun serializeEventWithEmptyData() {
        val event = Event(Scope.PLAYER2, Action.JOIN_PLAYER, 3)

        val b = byteArrayOf(
                // previous hash
                0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 2.toByte(), // player 2 scope
                0.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), // action id JOIN_PLAYER
                0.toByte(), 0.toByte(), 0.toByte(), 3.toByte(), // message id 3
                0.toByte())// data type empty

        Assert.assertTrue(Arrays.equals(b, event.getBytes()))
    }

    @Test
    @Throws(IOException::class)
    fun serializeEventWithStringData() {
        val event = Event(Scope.PLAYER2, Action.JOIN_PLAYER, 3, "Hello World!")

        val outputStream = ByteArrayOutputStream()
        outputStream.write(byteArrayOf(
                // previous hash
                0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 2.toByte(), // player 2 scope
                0.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), // action id JOING_PLAYER
                0.toByte(), 0.toByte(), 0.toByte(), 3.toByte(), // message id 3
                2.toByte())// data type STRING
        )
        outputStream.write("Hello World!".toByteArray(StandardCharsets.UTF_8))
        outputStream.write(byteArrayOf(0.toByte()))
        val b = outputStream.toByteArray()

        Assert.assertTrue(Arrays.equals(b, event.getBytes()))
    }

    @Test
    fun serializeEventWithIntegerData() {
        val event = Event(Scope.PLAYER2, Action.JOIN_PLAYER, 3, 16909060)

        val b = byteArrayOf(
                // previous hash
                0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 0.toByte(), 2.toByte(), // player 2 scope
                0.toByte(), 0.toByte(), 0.toByte(), 1.toByte(), // action id JOIN_PLAYER
                0.toByte(), 0.toByte(), 0.toByte(), 3.toByte(), // message id 3
                1.toByte(), // data type INTEGER
                1.toByte(), 2.toByte(), 3.toByte(), 4.toByte())// ‭16909060‬

        Assert.assertTrue(Arrays.equals(b, event.getBytes()))
    }
}

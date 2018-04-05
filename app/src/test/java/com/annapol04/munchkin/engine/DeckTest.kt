package com.annapol04.munchkin.engine

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeckTest {
    lateinit var door: Deck
    lateinit var treasure: Deck

    @Before
    fun setUp() {
        door = build(DoorCards::class.java, listOf(Monster::class.java.simpleName))
        treasure = build(TreasureCards::class.java, listOf(BonusWear::class.java.simpleName))
    }

    @Test
    fun decksHaveCards() {
        assertTrue(door.stack.size > 0)
        assertTrue(treasure.stack.size > 0)
    }

    @Test
    fun canDrawCards() {
        val ds = door.stack.size
        door.draw(DoorCards.FLYING_FROGS)

        assertEquals(ds - 1, door.stack.size)

        val ts = treasure.stack.size
        treasure.draw(TreasureCards.BOOTS_OF_BUTT_KICKING)

        assertEquals(ts - 1, treasure.stack.size)
    }
}
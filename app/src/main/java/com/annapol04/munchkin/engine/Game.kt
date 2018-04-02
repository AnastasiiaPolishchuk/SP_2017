package com.annapol04.munchkin.engine

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

import com.annapol04.munchkin.data.EventRepository

import java.lang.reflect.Field
import java.util.ArrayList
import java.util.Collections
import java.util.Random

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Game @Inject
constructor(eventRepository: EventRepository) {

    val deskCards = MutableLiveData<List<Card>>()
    private val isGameFinished = MutableLiveData<Boolean>()

    private val doorDeck = mutableListOf<Card>()
    private val treasureDeck = mutableListOf<Card>()
    private val state = State.HAND_OUT_CARDS
    private val randomDoor = Random()
    private val randomTreasure = Random()

    val gameFinished: LiveData<Boolean>
        get() = isGameFinished

    val monster: Monster?
        get() = if (deskCards.value!!.size == 0) null else deskCards.value!![0] as Monster

    private enum class State {
        HAND_OUT_CARDS
    }

    init {
        deskCards.value = ArrayList()
        isGameFinished.value = false
    }

    fun reset() {
        deskCards.value = ArrayList()
        isGameFinished.setValue(false)

        doorDeck.clear()
        treasureDeck.clear()

        val doorCardFields = DoorCards::class.java.fields
        val treasureCardFields = TreasureCards::class.java.fields

        try {
            for (field in doorCardFields)
                if (field.type == Card::class.java && field.get(null) is Monster)
                    doorDeck.add(field.get(null) as Card)

            for (field in treasureCardFields)
                if (field.type == Card::class.java && field.get(null) is BonusWear)
                    treasureDeck.add(field.get(null) as Card)

        } catch (e: IllegalAccessException) { /* wont happen... */
        }

    }

    fun getDeskCards(): LiveData<List<Card>> {
        return deskCards
    }

    fun drawDoorCard(card: Card) {
        deskCards.value = deskCards.value!! + listOf(card)
        doorDeck.remove(card)
    }

    fun drawTreasureCard(card: Card) {
        treasureDeck.remove(card)
    }

    private fun <T : Comparable<T>> hasElement(list: List<T>, element: T): Boolean {
        return list.stream().anyMatch { it.compareTo(element) == 0 }
    }

    fun getRandomDoorCards(amount: Int): List<Card> {
        if (amount > doorDeck.size)
            throw IllegalArgumentException("Can not get " + amount + " door cards with deck size: " + doorDeck.size)

        val cards = ArrayList<Card>(amount)
        val indices = ArrayList<Int>(amount)

        for (i in 0 until amount) {
            var index: Int?
            do {
                index = randomDoor.nextInt(doorDeck.size)
            } while (hasElement(indices, index!!))

            indices.add(index)
            cards.add(doorDeck[index])
        }

        return cards
    }

    fun getRandomTreasureCards(amount: Int): List<Card> {
        if (amount > treasureDeck.size)
            throw IllegalArgumentException("Can not get " + amount + " treasure cards with deck size: " + treasureDeck.size)

        val cards = ArrayList<Card>(amount)
        val indices = ArrayList<Int>(amount)

        for (i in 0 until amount) {
            var index: Int?
            do {
                index = randomTreasure.nextInt(treasureDeck.size)
            } while (hasElement(indices, index!!))

            indices.add(index)
            cards.add(treasureDeck[index])
        }

        return cards
    }

    fun runAwayFromMonster() {
        deskCards.value = deskCards.value!!.drop(1)
    }

    override fun toString(): String {
        return StringBuilder()
                .append("door cards: ")
                .append(doorDeck.size)
                .append(", treasure cards: ")
                .append(treasureDeck.size)
                .append(", finished: ")
                .append(isGameFinished.value)
                .toString()
    }

    @Throws(IllegalEngineStateException::class)
    fun pushAwayMonsterCard() {
        if (deskCards.value!!.size == 0)
            throw IllegalEngineStateException("There is no monster card to be pushed away")

        deskCards.value = deskCards.value!!.drop(1)
    }

    companion object {
        private val TAG = Game::class.java.simpleName
    }

}

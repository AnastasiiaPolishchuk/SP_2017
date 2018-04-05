package com.annapol04.munchkin.engine

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

import com.annapol04.munchkin.data.EventRepository
import com.annapol04.munchkin.util.NonNullLiveData
import com.annapol04.munchkin.util.NonNullMutableLiveData

import java.lang.reflect.Field
import java.util.ArrayList
import java.util.Collections
import java.util.Random

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class Game @Inject
constructor(@Named("treasure") private val treasureDeck: Deck,
            @Named("door") private val doorDeck: Deck) {

    private var _deskCards = NonNullMutableLiveData<List<Card>>(emptyList())
    val deskCards: NonNullLiveData<List<Card>> get() = _deskCards

    private val isGameFinished = MutableLiveData<Boolean>()

    private val state = State.HAND_OUT_CARDS
    private val randomDoor = Random()
    private val randomTreasure = Random()

    val gameFinished: LiveData<Boolean>
        get() = isGameFinished

    val monster: Monster?
        get() = if (_deskCards.value.size == 0) null else _deskCards.value[0] as Monster

    private enum class State {
        HAND_OUT_CARDS
    }

    init {
        _deskCards.value = emptyList()
        isGameFinished.value = false
    }

    fun reset() {
        _deskCards.value = emptyList()
        isGameFinished.setValue(false)

        doorDeck.reset()
        treasureDeck.reset()
    }

    fun drawDoorCard(card: Card) {
        doorDeck.draw(card)

        _deskCards.value += listOf(card)
    }

    fun drawTreasureCard(card: Card) {
        treasureDeck.draw(card)
    }

    private fun <T : Comparable<T>> hasElement(list: List<T>, element: T): Boolean {
        return list.stream().anyMatch { it.compareTo(element) == 0 }
    }

    fun getRandomDoorCards(amount: Int): List<Card> {
        return doorDeck.getRandomStackCards(amount)
    }

    fun getRandomTreasureCards(amount: Int): List<Card> {
        return treasureDeck.getRandomStackCards(amount)
    }

    fun runAwayFromMonster() {
        _deskCards.value = _deskCards.value.drop(1)
    }

    override fun toString(): String {
        return StringBuilder()
                .append("door cards: ")
                .append(doorDeck)
                .append(", treasure cards: ")
                .append(treasureDeck)
                .append(", finished: ")
                .append(isGameFinished.value)
                .toString()
    }

    @Throws(IllegalEngineStateException::class)
    fun pushAwayMonsterCard() {
        if (_deskCards.value.size == 0)
            throw IllegalEngineStateException("There is no monster card to be pushed away")

        _deskCards.value = _deskCards.value.drop(1)
    }

    companion object {
        private val TAG = Game::class.java.simpleName
    }

}

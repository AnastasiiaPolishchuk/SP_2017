package com.annapol04.munchkin.engine

import com.annapol04.munchkin.util.NonNullLiveData
import com.annapol04.munchkin.util.NonNullMutableLiveData

import java.util.Random

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class Desk @Inject
constructor(@Named("treasure") private val treasureDeck: Deck,
            @Named("door") private val doorDeck: Deck) {

    private var _deskCards = NonNullMutableLiveData<List<Card>>(emptyList())
    val deskCards: NonNullLiveData<List<Card>> get() = _deskCards

    private val state = State.HAND_OUT_CARDS
    private val randomDoor = Random()
    private val randomTreasure = Random()

    val monster: Monster?
        get() = if (_deskCards.value.size == 0) null else _deskCards.value[0] as Monster

    private enum class State {
        HAND_OUT_CARDS
    }

    init {
        _deskCards.value = emptyList()
    }

    fun reset() {
        _deskCards.value = emptyList()

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
                .toString()
    }

    @Throws(IllegalEngineStateException::class)
    fun pushAwayMonsterCard() {
        if (_deskCards.value.size == 0)
            throw IllegalEngineStateException("There is no monster card to be pushed away")

        val dropped = _deskCards.value[0]

        _deskCards.value = _deskCards.value.drop(1)

        doorDeck.putBack(dropped)
    }

    fun pushAwayTreasureCard(card: Card) {
        treasureDeck.putBack(card)
    }

    companion object {
        private val TAG = Desk::class.java.simpleName
    }

}

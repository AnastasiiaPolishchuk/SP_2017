package com.annapol04.munchkin.engine

import java.lang.RuntimeException
import java.util.*

fun <T> build(clazz: Class<T>, types: List<String>): Deck {
    try {
        return Deck(clazz.fields.filter { it.type == Card::class.java && it.name in types }
                .map{ it.get(null) as Card })
    } catch (e: IllegalAccessException) {
        throw RuntimeException(e)
    }
}

open class Deck(private val allCards: List<Card>) {
    var stack = listOf<Card>()
    var inGame = listOf<Card>()
    var played = listOf<Card>()
    val random = Random()

    fun reset() {
        stack = allCards
        played = listOf()
    }

    open fun getRandomStackCards(amount: Int): List<Card> {
        if (amount > stack.size)
            throw IllegalArgumentException("Can not get " + amount + " cards with stack size: " + stack.size)

        val randomCards = mutableListOf<Card>()

        for (i in 1..amount) {
            var card: Card

            do {
                card = stack[random.nextInt(stack.size)]
            } while (card in randomCards)

            randomCards.add(card)
        }

        return randomCards
    }

    open fun draw(card: Card) {
        stack -= listOf(card)
        inGame += listOf(card)
    }
}
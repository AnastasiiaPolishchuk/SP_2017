package com.annapol04.munchkin.engine

import java.util.*

fun <T> build(clazz: Class<T>, types: List<String>): Deck {
    val race = Race::class.simpleName in types
    val curse = Curse::class.simpleName in types
    val level = Monsterlevel::class.simpleName in types
    val wear = BonusWear::class.simpleName in types
    val special = Special::class.simpleName in types
    val side = BonusSide::class.simpleName in types
    val monster = Monster::class.simpleName in types

    return Deck(clazz.fields
            .filter { it.type.simpleName == Card::class.java.simpleName }
            .map { it.get(null) as Card }
            .filter {
                when(it) {
                    is Monster -> monster
                    is Race -> race
                    is Curse -> curse
                    is Monsterlevel -> level
                    is BonusWear -> wear
                    is Special -> special
                    is BonusSide -> side
                    else -> throw NotImplementedError("Card type is not implemented")
                }
            }.toSet())
}

open class Deck(private val allCards: Set<Card>) {
    var stack = allCards
    var backStack = setOf<Card>()
    open val random = Random()

    fun reset() {
        stack = allCards
        backStack = setOf()
    }

    open fun getRandomStackCards(amount: Int): List<Card> {
        if (amount > stack.size) {
            if (amount > stack.size + backStack.size)
                throw IllegalArgumentException("Can not get " + amount + " cards with stack size: " + stack.size)
            else {

            }
        }

        val randomCards = mutableListOf<Card>()

        for (i in 1..amount) {
            var card: Card

            do {
                card = stack.elementAt(random.nextInt(stack.size))
            } while (card in randomCards)

            randomCards.add(card)
        }

        return randomCards
    }

    open fun draw(card: Card) {
        if (card in backStack) {
            stack += backStack

            backStack = setOf()
        }

        stack -= card
    }

    open fun putBack(card: Card) {
        backStack += card
    }
}
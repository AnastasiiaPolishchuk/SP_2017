package com.annapol04.munchkin

import com.annapol04.munchkin.engine.Card
import com.annapol04.munchkin.engine.Deck

class DeterministicDeck(private val allCards: List<Card>): Deck(allCards.toSet()) {
    private var s = allCards.toMutableList()
    private var bs = mutableListOf<Card>()

    override fun getRandomStackCards(amount: Int): List<Card> {
        if (amount > s.size) {
            if (amount > s.size + bs.size)
                throw IllegalStateException("Not enough cards in test deck")
            else {
                s.addAll(index = s.size, elements = bs)
                bs = mutableListOf()
            }
        }
        val randomCards = s.subList(0, amount)
        s = s.subList(amount, s.size)
        return randomCards
    }

    override fun draw(card: Card) {
        s.remove(card)
    }

    override fun putBack(card: Card) {
        bs.add(card)
    }
}
package com.annapol04.munchkin.engine

import com.annapol04.munchkin.R
import com.annapol04.munchkin.data.EventRepository

import java.util.ArrayList
import java.util.Arrays

import javax.inject.Inject
import javax.inject.Named

class FakeMatch @Inject
constructor(desk: Desk,
            @Named("myself") myself: Player,
            eventRepository: EventRepository)
    : Match(desk, myself, eventRepository) {

    override fun start(amountOfPlayers: Int) {
        super.start(amountOfPlayers)

        for (i in 0 until amountOfPlayers - 1)
            eventRepository.push(Event(Scope.GAME, Action.JOIN_PLAYER, 0, i))
    }

    override fun namingRound() {
        super.namingRound()

        val names = ArrayList(Arrays.asList("Helga", "Cannabiene"))

        val events = ArrayList<Event>((amountOfPlayers - 1) * 2)

        for (i in 1..amountOfPlayers-1) {
            events.add(Event(players.value[i].scope, Action.NAME_PLAYER, R.string.ev_join_player, names[i - 1]))
            events.add(Event(players.value[i].scope, Action.HAND_OVER_TOKEN, 0, players_.value[(i + 1) % players_.value.size].scope.ordinal))
        }

        eventRepository.push(events)
    }

    @Throws(IllegalEngineStateException::class)
    override fun handOverToken(scope: Scope, playerNr: Int) {
        super.handOverToken(scope, playerNr)

        if (current != myself && areAllPlayersNamed()) {
            if (state == Match.State.STARTED)
                drawInitialHandcards()
        }
    }
}

package com.annapol04.munchkin.engine

import android.util.Log

import com.annapol04.munchkin.data.EventRepository

import java.util.Arrays

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Executor @Inject
constructor(private val match: Match, private val desk: Desk, private val repository: EventRepository, private val messageBook: MessageBook) : EventRepository.OnNewEventListener {
    var errorCount = 0
        private set

    init {
        repository.newEventListener = this
    }

    override fun onNewEvent(event: Event) {
        val player = if (event.scope == Scope.GAME || event.action == Action.ASSIGN_PLAYER_NUMBER) null else match.getPlayer(event.scope)
        val anonymized = isAnonymized(player, event.action)

        if (Arrays.equals(repository.topHash, event.getPreviousHash())) {
            Log.d(TAG, "executing: " + event.toString(messageBook, player, anonymized))

            repository.topHash = event.hash!!

            logMessage(event, player, anonymized)

            try {
                event.execute(match, desk)
            } catch (exception: IllegalEngineStateException) {
                match.undoLog()

                errorCount++

                Log.e(TAG, "ERROR: " + exception.message)
            }

        } else {
            val msg = "failed to execute: " + event.toString(messageBook, player, anonymized) + " because of wrong hash value"

            Log.e(TAG, msg)
            throw IllegalStateException(msg)
        }
    }

    private fun isAnonymized(player: Player?, action: Action): Boolean {
        return !match.isMyself(player) && (
                   action === Action.DRAW_TREASURECARD
                || action === Action.DROP_CARD
                )
    }

    private fun logMessage(event: Event, player: Player?, anonymized: Boolean) {
        val message = event.getMessage(messageBook, player, anonymized)

        if (message.length > 0)
            match.log(message)
    }

    companion object {
        private val TAG = Executor::class.java.simpleName
    }
}
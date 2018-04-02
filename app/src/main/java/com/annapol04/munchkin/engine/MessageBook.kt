package com.annapol04.munchkin.engine

import android.app.Application
import android.support.annotation.StringRes

import com.annapol04.munchkin.R

import javax.inject.Inject

class MessageBook @Inject
constructor(private val application: Application) {

    fun find(id: Int): String {
        return application.getString(id)
    }

    @JvmOverloads
    fun build(event: Event, player: Player? = null, anonymized: Boolean = false): String {
        val id = event.messageId

        return if (id == 0) "" else replace(find(id), event, player, anonymized)

    }

    private fun replace(message: String, event: Event, player: Player?, anonymized: Boolean): String {
        var message = message
        if (message.contains("%scope%"))
            message = message.replace("%scope%", event.scope.toString())

        if (player != null && message.contains("%player%"))
            message = message.replace("%player%", player.getName())

        if (message.contains("%card%")) {
            if (anonymized)
                message = message.replace("%card%", find(R.string.anonymized_card))
            else
                message = message.replace("%card%", find(Card.fromId(event.getInteger()).name))
        }

        if (message.contains("%integer%"))
            message = message.replace("%integer%", Integer.toString(event.getInteger()))

        if (message.contains("%string%"))
            message = message.replace("%string%", event.getString())

        if (message.contains("%turn-phase%"))
            message = message.replace("%turn-phase%", find(TurnPhase.fromId(event.getInteger()).stringId))

        return message
    }
}

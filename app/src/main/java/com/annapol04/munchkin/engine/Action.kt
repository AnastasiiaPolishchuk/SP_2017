package com.annapol04.munchkin.engine

private var idSource = 0

class Action(private val modifier: (Match, Game, Event) -> Unit) {

    val id: Int
    private var name: String? = null

    init {
        this.id = idSource++
    }

    @Throws(IllegalEngineStateException::class)
    operator fun invoke(match: Match, game: Game, data: Event) {
        modifier(match, game, data)
    }

    private fun initialize(name: String): Action {
        this.name = name
        return this
    }

    override fun toString(): String {
        return name ?: ""
    }

    companion object {
        @JvmField val MESSAGE = Action({ match, game, event -> })
        @JvmField val JOIN_PLAYER = Action({ match, game, event -> match.joinPlayer(event.getInteger()) })
        @JvmField val ASSIGN_PLAYER_NUMBER = Action({ match, game, event -> match.assignPlayerNumber(event.scope.ordinal, event.getInteger()) })
        @JvmField val NAME_PLAYER = Action({ match, game, event -> match.namePlayer(event.scope.ordinal, event.getString()) })
        @JvmField val HAND_OVER_TOKEN = Action({ match, game, event -> match.handOverToken(event.scope, event.getInteger()) })
        @JvmField val LEAVE_PLAYER = Action({ match, game, event -> throw UnsupportedOperationException() })
        @JvmField val DRAW_DOORCARD = Action({ match, game, event -> match.drawDoorCard(event.scope, Card.fromId(event.getInteger())) })
        @JvmField val DRAW_TREASURECARD = Action({ match, game, event -> match.drawTreasureCard(event.scope, Card.fromId(event.getInteger())) })
        @JvmField val PICKUP_CARD = Action({ match, game, event -> match.getPlayer(event.scope).pickupCard(Card.fromId(event.getInteger())) })
        @JvmField val PLAY_CARD = Action({ match, game, event -> match.playCard(event.scope, Card.fromId(event.getInteger())) })
        @JvmField val FIGHT_MONSTER = Action({ match, game, event -> match.fightMonster(event.scope) })
        @JvmField val RUN_AWAY = Action({ match, game, event -> match.getPlayer(event.scope).runAway() })
        @JvmField val ENTER_TURN_PHASE = Action({ match, game, event -> match.enterTurnPhase(event.scope, TurnPhase.fromId(event.getInteger())) })

        private val lookup = Action::class.java.fields
                    .filter { it.type == Action::class.java }
                    .map { (it.get(null) as Action).initialize(it.name) }
                    .sortedBy { it.id }

        fun fromId(id: Int): Action {
            if (id >= lookup.size)
                throw IllegalArgumentException("Invalid event action id $id")

            return lookup[id]
        }
    }
}

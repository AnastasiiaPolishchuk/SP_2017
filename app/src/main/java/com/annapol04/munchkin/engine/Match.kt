package com.annapol04.munchkin.engine

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.StringRes

import com.annapol04.munchkin.R
import com.annapol04.munchkin.data.EventRepository

import java.util.stream.Collectors

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
open class Match @Inject
constructor(protected var game: Game,
            @param:Named("myself") protected val myself: Player,
            protected var eventRepository: EventRepository) {

    protected var log = MutableLiveData<String>()
    protected var observablePlayers = MutableLiveData<List<Player>>()
    protected var currentPlayer = MutableLiveData<Player>()

    protected var players: List<Player> = emptyList()

    protected var current: Player? = null
    protected var first: Player? = null
    protected var turnPhase: TurnPhase? = null

    protected var state = State.JOINING
    var amountOfPlayers = 0
        protected set
    protected var playersRenamed = 0

    protected var named = false
    protected var handCardsDrawn = false

    protected enum class State {
        JOINING,
        NAMING,
        HAND_CARDS,
        STARTED
    }

    init {

        observablePlayers.value = emptyList()

        reset()
    }

    protected fun reset() {
        players = emptyList()
        currentPlayer.setValue(null)
        playersRenamed = 0
        observablePlayers.value = emptyList()
        log.setValue("")

        game.reset()
        myself.reset()

        turnPhase = TurnPhase.IDLE
        state = State.JOINING

        named = false
        handCardsDrawn = false
    }

    fun isMyself(player: Player?): Boolean {
        return player === myself
    }

    fun log(message: String) {
        log.setValue(StringBuilder()
                .append(log.value)
                .append(message)
                .append("\n")
                .toString())
    }

    fun undoLog() {
        log.setValue(log.value!!.replace("\n[^\n]+\n+$".toRegex(), "\n"))
    }

    fun getLog(): LiveData<String> {
        return log
    }

    fun getPlayers(): LiveData<List<Player>> {
        return observablePlayers
    }

    fun getPlayer(playerNr: Int): Player {
        return players[playerNr - 1]
    }

    fun getPlayer(scope: Scope): Player {
        return getPlayer(scope.ordinal)
    }

    fun getCurrentPlayer(): LiveData<Player> {
        return currentPlayer
    }

    open fun start(amountOfPlayers: Int) {
        reset()

        this.amountOfPlayers = amountOfPlayers

        eventRepository.push(Event(Scope.GAME, Action.JOIN_PLAYER, 0, myself.id))
    }

    protected fun evaluateHost(): Player? {
        var highestId: Player? = null

        for (player in players) {
            if (highestId == null)
                highestId = player
            else if (player.id > highestId.id)
                highestId = player
        }

        return highestId
    }

    protected fun specifyPlayerNumbers() {
        myself.scope = Scope.PLAYER1

        eventRepository.push(
                Event(myself.scope!!, Action.ASSIGN_PLAYER_NUMBER, 0, myself.id))

        var nr = 2

        for (player in players)
            if (player != myself)
                eventRepository.push(
                        Event(Scope.fromId(nr++), Action.ASSIGN_PLAYER_NUMBER, 0, player.id))
    }

    protected fun <T> update(liveData: MutableLiveData<T>) {
        liveData.setValue(liveData.value)
    }

    fun joinPlayer(randomNumber: Int) {
        val player = if (randomNumber == myself.id)
            myself
        else
            Player(randomNumber, game, eventRepository)

        player.allowToDrawTreasureCards(AMOUNT_OF_HAND_CARDS)

        players += player

        if (players.size == amountOfPlayers) {
            state = State.NAMING

            current = evaluateHost()
            currentPlayer.setValue(current)

            first = current

            if (current == myself) {
                specifyPlayerNumbers()

                named = true

                namingRound()
            }
        }
    }

    fun canPlayerFightMonster(player: Player): Boolean {
        return game.getDeskCards().value!!.size > 0
                && player.getFightLevel().value!! > (game.getDeskCards().value!![0] as Monster).level

    }

    protected open fun namingRound() {
        eventRepository.push(
                Event(current!!.scope!!, Action.NAME_PLAYER, R.string.ev_join_player, myself.getName()),
                Event(current!!.scope!!, Action.HAND_OVER_TOKEN, 0, nextPlayer().scope!!.ordinal)
        )
    }

    protected fun nextPlayer(): Player {
        return players[(players.indexOf(current) + 1) % players.size]
    }

    protected fun drawInitialHandcards() {
        eventRepository.push(game.getRandomTreasureCards(AMOUNT_OF_HAND_CARDS)
                .map{ Event(current!!.scope!!, Action.DRAW_TREASURECARD, 0, it.id) })

        emitHandOverToken(current!!.scope, nextPlayer())
    }

    private fun areAllPlayersNamed(): Boolean {
        return playersRenamed == amountOfPlayers
    }

    private fun areAllInitialCardsDrawn(): Boolean {
        return !players.stream().anyMatch{ it.isAllowedToDrawTreasureCard }
    }

    @Throws(IllegalEngineStateException::class)
    open fun handOverToken(scope: Scope, playerNr: Int) {
        current = getPlayer(playerNr)
        currentPlayer.setValue(current)

        when (state) {
            Match.State.NAMING -> {
                if (!named && current == myself) {
                    named = true

                    namingRound()
                }
                if (areAllPlayersNamed()) {
                    state = State.HAND_CARDS

                    if (!handCardsDrawn && current == myself) {
                        handCardsDrawn = true

                        drawInitialHandcards()
                    }
                    if (areAllInitialCardsDrawn()) {
                        state = State.STARTED

                        if (current == myself) {
                            turnPhase = TurnPhase.IDLE

                            playRound()
                        }
                    }
                }
            }
            Match.State.HAND_CARDS -> {
                if (!handCardsDrawn && current == myself) {
                    handCardsDrawn = true
                    drawInitialHandcards()
                }
                if (areAllInitialCardsDrawn()) {
                    state = State.STARTED

                    if (current == myself) {
                        turnPhase = TurnPhase.IDLE
                        playRound()
                    }
                }
            }
            Match.State.STARTED -> if (current == myself) {
                turnPhase = TurnPhase.IDLE
                playRound()
            }
            else -> throw IllegalEngineStateException("Illegal state")
        }
    }

    protected fun emitHandOverToken(scope: Scope?, player: Player) {
        eventRepository.push(
                Event(current!!.scope!!, Action.HAND_OVER_TOKEN, 0, player.scope!!.ordinal)
        )
    }

    @Throws(IllegalEngineStateException::class)
    protected fun playRound() {
        if (turnPhase == TurnPhase.IDLE)
            emitEnterTurnPhase(current!!.scope, TurnPhase.EQUIPMENT)
        else
            throw IllegalEngineStateException("We can start the turn only from \"idle\" turn phase")
    }

    fun assignPlayerNumber(playerNr: Int, randomNumber: Int) {
        var allScopesAssigned = true

        for (player in players) {
            if (player.id == randomNumber)
                player.scope = Scope.fromId(playerNr)
            if (player.scope == null)
                allScopesAssigned = false
        }

        if (allScopesAssigned)
            players = players.sortedBy{ it.scope!!.ordinal }
    }

    fun namePlayer(playerNr: Int, name: String) {
        players[playerNr - 1].rename(name)

        if (++playersRenamed == amountOfPlayers)
            observablePlayers.setValue(players)
    }

    @Throws(IllegalEngineStateException::class)
    protected fun testHost(player: Player) {
        if (player != current)
            throw IllegalEngineStateException("can not play a card in another player's round")
    }


    /****************************************************************************************
     * ==== Action implementations ====
     */

    fun emitMessage(scope: Scope, @StringRes message: Int, integer: Int) {
        eventRepository.push(
                Event(scope, Action.MESSAGE, message, integer)
        )
    }

    fun emitDrawDoorCard(scope: Scope) {
        val card = game.getRandomDoorCards(1)[0]

        emitEnterTurnPhase(scope, TurnPhase.KICK_OPEN_THE_DOOR)

        eventRepository.push(
                Event(scope, Action.DRAW_DOORCARD, R.string.ev_draw_card, card.id)
        )
    }

    @Throws(IllegalEngineStateException::class)
    fun drawDoorCard(scope: Scope, card: Card) {
        testHost(getPlayer(scope))

        test(turnPhase == TurnPhase.KICK_OPEN_THE_DOOR,
                "it's not allowed to draw door cards in \"$turnPhase\" phase!")
        test(game.getDeskCards().value!!.size == 0,
                "you can not draw a door card from an empty deck!")

        getPlayer(scope).drawDoorCard(card)
    }


    fun emitDrawTreasureCard(scope: Scope) {
        val card = game.getRandomTreasureCards(1)[0]

        eventRepository.push(
                Event(scope, Action.DRAW_TREASURECARD, R.string.ev_draw_card, card.id)
        )
    }

    @Throws(IllegalEngineStateException::class)
    fun drawTreasureCard(scope: Scope, card: Card) {
        testHost(getPlayer(scope))

        test(turnPhase == TurnPhase.KICK_OPEN_THE_DOOR_AND_DRAW || turnPhase == TurnPhase.IDLE,
                "it's not allowed to draw treasure cards in \"$turnPhase\" phase!")
        test(game.getDeskCards().value!!.size == 0,
                "you can not draw a treasure card from an empty deck!")

        getPlayer(scope).drawTreasureCard(card)

        if (turnPhase == TurnPhase.KICK_OPEN_THE_DOOR_AND_DRAW
                && scope == myself.scope
                && !myself.isAllowedToDrawTreasureCard)
            emitEnterTurnPhase(scope, TurnPhase.CHARITY)
    }


    fun emitFightMonster(scope: Scope) {
        emitEnterTurnPhase(scope, TurnPhase.KICK_OPEN_THE_DOOR_AND_FIGHT)

        eventRepository.push(
                Event(scope, Action.FIGHT_MONSTER, R.string.ev_fight_monster)
        )
    }

    @Throws(IllegalEngineStateException::class)
    fun fightMonster(scope: Scope) {
        testHost(getPlayer(scope))

        test(turnPhase == TurnPhase.KICK_OPEN_THE_DOOR_AND_FIGHT,
                "monsters can not fought in \"$turnPhase\" phase!")

        val result = getPlayer(scope).fightMonster()

        val cardsToDraw = 1

        if (scope == myself.scope) {
            emitMessage(scope, R.string.player_killed_monster, result.first.id)
            emitMessage(scope, R.string.player_gets_level, result.second)

            emitEnterTurnPhase(scope, TurnPhase.KICK_OPEN_THE_DOOR_AND_DRAW)
            emitMessage(scope, R.string.player_draws_treasure_cards, cardsToDraw)
        }

        getPlayer(scope).allowToDrawTreasureCards(cardsToDraw)
    }


    fun emitRunAway(scope: Scope) {
        eventRepository.push(
                Event(scope, Action.RUN_AWAY, R.string.ev_run_away)
        )
    }

    @Throws(IllegalEngineStateException::class)
    fun runAway(scope: Scope) {
        testHost(getPlayer(scope))

        test(turnPhase == TurnPhase.KICK_OPEN_THE_DOOR_AND_FIGHT,
                "can not run away from monster in \"$turnPhase\" phase!")

        getPlayer(scope).runAway()
    }


    fun emitPlayCard(scope: Scope, card: Card) {
        eventRepository.push(
                Event(scope, Action.PLAY_CARD, R.string.ev_play_card, card.id)
        )
    }


    @Throws(IllegalEngineStateException::class)
    fun playCard(scope: Scope, card: Card) {
        testHost(getPlayer(scope))

        test(turnPhase == TurnPhase.EQUIPMENT,
                "can not equip items in \"$turnPhase\" phase!")

        getPlayer(scope).playCard(card)
    }


    protected fun emitEnterTurnPhase(scope: Scope?, phase: TurnPhase) {
        eventRepository.push(
                Event(scope!!, Action.ENTER_TURN_PHASE, R.string.ev_enter_turn_phase, phase.ordinal)
        )
    }

    @Throws(IllegalEngineStateException::class)
    fun enterTurnPhase(scope: Scope, phase: TurnPhase) {
        when (turnPhase) {
            TurnPhase.IDLE -> {
                test(phase == TurnPhase.EQUIPMENT,
                        "it is not allowed to enter phase \"$phase\" from \"$turnPhase\"")

                getPlayer(scope).allowToDrawDoorCards(1)
            }
            TurnPhase.EQUIPMENT -> test(phase == TurnPhase.KICK_OPEN_THE_DOOR,
                    "it is not allowed to enter phase \"$phase\" from \"$turnPhase\"")
            TurnPhase.KICK_OPEN_THE_DOOR -> test(phase == TurnPhase.KICK_OPEN_THE_DOOR_AND_FIGHT,
                    "it is not allowed to enter phase \"$phase\" from \"$turnPhase\"")
            TurnPhase.KICK_OPEN_THE_DOOR_AND_FIGHT -> test(phase == TurnPhase.KICK_OPEN_THE_DOOR_AND_DRAW,
                    "it is not allowed to enter phase \"$phase\" from \"$turnPhase\"")
            TurnPhase.KICK_OPEN_THE_DOOR_AND_DRAW, TurnPhase.LOOK_FOR_TROUBLE, TurnPhase.LOOT_THE_ROOM -> {
                test(phase == TurnPhase.CHARITY,
                        "it is not allowed to enter phase \"$phase\" from \"$turnPhase\"")

                if (scope == myself.scope) {
                    emitEnterTurnPhase(scope, TurnPhase.IDLE)
                    emitHandOverToken(scope, nextPlayer())
                }
            }
            TurnPhase.CHARITY -> {
            }
        }

        turnPhase = phase
    }

    companion object {
        protected val AMOUNT_OF_HAND_CARDS = 2
        protected val MAX_AMOUNT_OF_HAND_CARDS = 4

        protected val TAG = Match::class.java.simpleName
    }
}
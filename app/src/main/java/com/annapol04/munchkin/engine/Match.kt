package com.annapol04.munchkin.engine

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.StringRes

import com.annapol04.munchkin.R
import com.annapol04.munchkin.data.EventRepository

import com.annapol04.munchkin.util.*

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.math.E

@Singleton
open class Match @Inject
constructor(protected var game: Game,
            @param:Named("myself") protected val myself: Player,
            protected var eventRepository: EventRepository) {

    protected val log_ by lazy { NonNullMutableLiveData<String>("") }
    val log: NonNullLiveData<String> get() = log_

    protected val players_ by lazy { NonNullMutableLiveData<List<Player>>(emptyList()) }
    val players: NonNullLiveData<List<Player>> get() = players_

    protected val currentPlayer_ by lazy { NonNullMutableLiveData(myself) }
    val currentPlayer: NonNullLiveData<Player> get() = currentPlayer_

    protected val canFinishRound_ by lazy { NonNullMutableLiveData(false) }
    val canFinishRound: NonNullLiveData<Boolean> get() = canFinishRound_

    protected val canStartCombat_ by lazy { NonNullMutableLiveData(false) }
    val canStartCombat: NonNullLiveData<Boolean> get() = canStartCombat_

    protected var current: Player = myself
        set(value) {
            field = value
            currentPlayer_.value = field
        }

    protected var first = myself
    protected var turnPhase = TurnPhase.IDLE
        set(value) {
            field = value

            if (value == TurnPhase.FINISHED)
                canFinishRound_.value = true
            else if (canFinishRound_.value)
                canFinishRound_.value = false

            if (value == TurnPhase.KICK_OPEN_THE_DOOR)
                canStartCombat_.value = true
            else if (canStartCombat_.value)
                canStartCombat_.value = false
        }

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
        reset()
    }

    protected fun reset() {
        players_.value = emptyList()
        playersRenamed = 0
        log_.value = ""

        game.reset()
        myself.reset()

        turnPhase = TurnPhase.IDLE
        state = State.JOINING

        named = false
        handCardsDrawn = false
        current = myself
    }

    fun isMyself(player: Player?): Boolean {
        return player === myself
    }

    fun log(message: String) {
        log_.value = StringBuilder()
                .append(log_.value)
                .append(message)
                .append("\n")
                .toString()
    }

    fun undoLog() {
        log_.value = log_.value.replace("\n[^\n]+\n+$".toRegex(), "\n")
    }

    fun getPlayer(playerNr: Int): Player {
        return players_.value[playerNr - 1]
    }

    fun getPlayer(scope: Scope): Player {
        return getPlayer(scope.ordinal)
    }

    open fun start(amountOfPlayers: Int) {
        reset()

        this.amountOfPlayers = amountOfPlayers

        eventRepository.push(Event(Scope.GAME, Action.JOIN_PLAYER, 0, myself.id))
    }

    protected fun evaluateHost(): Player {
        return players_.value.maxBy { it.id } ?: myself
    }

    protected fun specifyPlayerNumbers() {
        myself.scope = Scope.PLAYER1

        eventRepository.push(
                Event(myself.scope, Action.ASSIGN_PLAYER_NUMBER, 0, myself.id))

        var nr = 2

        for (player in players_.value)
            if (player != myself)
                eventRepository.push(
                        Event(Scope.fromId(nr++), Action.ASSIGN_PLAYER_NUMBER, 0, player.id))
    }

    fun joinPlayer(randomNumber: Int) {
        val player = if (randomNumber == myself.id)
            myself
        else
            Player(randomNumber, game, eventRepository)

        player.allowToDrawTreasureCards(AMOUNT_OF_HAND_CARDS)

        players_.value += player

        if (players_.value.size == amountOfPlayers) {
            state = State.NAMING

            current = evaluateHost()
            first = current

            if (current == myself) {
                specifyPlayerNumbers()

                named = true

                namingRound()
            }
        }
    }

    fun canPlayerFightMonster(player: Player): Boolean {
        return game.deskCards.value.size > 0
                && player.getFightLevel().value!! > (game.deskCards.value[0] as Monster).level

    }

    protected open fun namingRound() {
        eventRepository.push(
                Event(current.scope, Action.NAME_PLAYER, R.string.ev_join_player, myself.getName()),
                Event(current.scope, Action.HAND_OVER_TOKEN, 0, nextPlayer().scope.ordinal)
        )
    }

    protected fun nextPlayer(): Player {
        return players_.value[(players_.value.indexOf(current) + 1) % players_.value.size]
    }

    protected fun drawInitialHandcards() {
        eventRepository.push(game.getRandomTreasureCards(AMOUNT_OF_HAND_CARDS)
                .map{ Event(current.scope, Action.DRAW_TREASURECARD, 0, it.id) })

        emitHandOverToken(current.scope, nextPlayer())
    }

    private fun areAllPlayersNamed(): Boolean {
        return playersRenamed == amountOfPlayers
    }

    private fun areAllInitialCardsDrawn(): Boolean {
        return players_.value.all{ !it.isAllowedToDrawTreasureCard }
    }

    protected fun emitHandOverToken(scope: Scope, player: Player) {
        eventRepository.push(
                Event(current.scope, Action.HAND_OVER_TOKEN, 0, player.scope.ordinal)
        )
    }

    @Throws(IllegalEngineStateException::class)
    open fun handOverToken(scope: Scope, playerNr: Int) {
        if (state === Match.State.STARTED)
            test(canFinishRound_.value,"You can hand over the control after doing the required steps")

        current = getPlayer(playerNr)

        turnPhase = TurnPhase.IDLE

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

                        playRound()
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

                    playRound()
                }
            }
            Match.State.STARTED -> {
                playRound()
            }
            else -> throw IllegalEngineStateException("Illegal state")
        }
    }

    protected fun playRound() {
        turnPhase = TurnPhase.EQUIPMENT
        current.allowToDrawDoorCards(1)
    }

    fun assignPlayerNumber(playerNr: Int, randomNumber: Int) {
        var allScopesAssigned = true

        for (player in players_.value) {
            if (player.id == randomNumber)
                player.scope = Scope.fromId(playerNr)
            if (player.scope === Scope.GAME)
                allScopesAssigned = false
        }

        if (allScopesAssigned)
            players_.value = players_.value.sortedBy{ it.scope.ordinal }
    }

    fun namePlayer(playerNr: Int, name: String) {
        players_.value[playerNr - 1].rename(name)
        ++playersRenamed
    }

    @Throws(IllegalEngineStateException::class)
    protected fun testHost(player: Player) {
        if (player !== current)
            throw IllegalEngineStateException("can not play a card in another player's round")
    }

    fun startCombat() {
        emitMessage(current.scope, R.string.tp_kick_open_the_door_and_fight);
    }


    /****************************************************************************************
     * ==== Action implementations ====
     */

    fun emitMessage(scope: Scope, @StringRes message: Int) {
        eventRepository.push(
                Event(scope, Action.MESSAGE, message)
        )
    }

    fun emitMessage(scope: Scope, @StringRes message: Int, integer: Int) {
        eventRepository.push(
                Event(scope, Action.MESSAGE, message, integer)
        )
    }

    fun emitDrawDoorCard(scope: Scope) {
        val card = game.getRandomDoorCards(1)[0]

        emitMessage(scope, R.string.tp_kick_open_the_door)

        eventRepository.push(
                Event(scope, Action.DRAW_DOORCARD, R.string.ev_draw_card, card.id)
        )
    }

    @Throws(IllegalEngineStateException::class)
    fun drawDoorCard(scope: Scope, card: Card) {
        testHost(getPlayer(scope))

        if (turnPhase == TurnPhase.EQUIPMENT)
            turnPhase = TurnPhase.KICK_OPEN_THE_DOOR

        test(turnPhase == TurnPhase.KICK_OPEN_THE_DOOR,
                "it's not allowed to draw door cards in \"$turnPhase\" phase!")
        test(game.deskCards.value.size == 0,
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
        test(game.deskCards.value.size == 0,
                "you can not draw a treasure card from an empty deck!")

        getPlayer(scope).drawTreasureCard(card)

        if (turnPhase == TurnPhase.KICK_OPEN_THE_DOOR_AND_DRAW
        && !current.isAllowedToDrawTreasureCard) {
            current.limitHandCards(max = MAX_AMOUNT_OF_HAND_CARDS)

            startCharitingOrFinish();
        }
    }

    private fun startCharitingOrFinish() {
        if (current.isAllowedToDropCard) {
            if (current == myself) {
                if (myself.handCards.value.size > MAX_AMOUNT_OF_HAND_CARDS)
                    emitMessage(current.scope, R.string.tp_charity)
            }

            turnPhase = TurnPhase.CHARITY
        } else
            turnPhase = TurnPhase.FINISHED
    }


    fun emitFightMonster(scope: Scope) {
        eventRepository.push(
                Event(scope, Action.FIGHT_MONSTER, R.string.ev_fight_monster)
        )
    }

    @Throws(IllegalEngineStateException::class)
    fun fightMonster(scope: Scope) {
        testHost(getPlayer(scope))

        test(turnPhase == TurnPhase.KICK_OPEN_THE_DOOR,
                "monsters can not fought in \"$turnPhase\" phase!")

        val result = getPlayer(scope).fightMonster()

        val cardsToDraw = 1

        if (scope == myself.scope) {
            emitMessage(scope, R.string.player_killed_monster, result.first.id)
            emitMessage(scope, R.string.player_gets_level, result.second)
            emitMessage(scope, R.string.tp_kick_open_the_door_and_draw)
            emitMessage(scope, R.string.player_draws_treasure_cards, cardsToDraw)
        }

        getPlayer(scope).allowToDrawTreasureCards(cardsToDraw)

        turnPhase = TurnPhase.KICK_OPEN_THE_DOOR_AND_DRAW
    }


    fun emitRunAway(scope: Scope) {
        eventRepository.push(
                Event(scope, Action.RUN_AWAY, R.string.ev_run_away)
        )
    }

    @Throws(IllegalEngineStateException::class)
    fun runAway(scope: Scope) {
        testHost(getPlayer(scope))

        test(turnPhase == TurnPhase.KICK_OPEN_THE_DOOR,
                "can not run away from monster in \"$turnPhase\" phase!")

        getPlayer(scope).runAway()

        startCharitingOrFinish()
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


    fun emitDropCard(scope: Scope, card: Card) {
        eventRepository.push(
                Event(scope, Action.DROP_CARD, R.string.ev_drop_card, card.id)
        )
    }

    fun dropCard(scope: Scope, card: Card) {
        testHost(getPlayer(scope))

        test(turnPhase == TurnPhase.CHARITY,
                "can not drop cards in \"$turnPhase\" phase!")

        getPlayer(scope).dropCard(card)

        if (turnPhase == TurnPhase.CHARITY && !current.isAllowedToDropCard)
            turnPhase = TurnPhase.FINISHED
    }


   /* private fun emitEnterTurnPhase(scope: Scope, phase: TurnPhase) {
        eventRepository.push(
                Event(scope, Action.ENTER_TURN_PHASE, R.string.ev_enter_turn_phase, phase.ordinal)
        )
    }*/

 /*   @Throws(IllegalEngineStateException::class)
    fun enterTurnPhase(scope: Scope, phase: TurnPhase) {
        when (turnPhase) {
            TurnPhase.IDLE -> {
                test(phase == TurnPhase.EQUIPMENT,
                        "it is not allowed to enter phase \"$phase\" from \"$turnPhase\"")


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
       //             emitEnterTurnPhase(scope, TurnPhase.IDLE)
                    emitHandOverToken(scope, nextPlayer())
                }
            }
            TurnPhase.CHARITY -> {
            }
        }

        turnPhase = phase

        if (turnPhase == TurnPhase.CHARITY)
            canFinishRound_.value = true
        else if (canFinishRound_.value)
            canFinishRound_.value = false

        if (turnPhase == TurnPhase.KICK_OPEN_THE_DOOR)
            canStartCombat_.value = true
        else if (canStartCombat_.value)
            canStartCombat_.value = false
    }
*/
    fun finishRound() {
        emitHandOverToken(current.scope, nextPlayer())
    }

    companion object {
        protected val AMOUNT_OF_HAND_CARDS = 2
        protected val MAX_AMOUNT_OF_HAND_CARDS = 4

        protected val TAG = Match::class.java.simpleName
    }
}
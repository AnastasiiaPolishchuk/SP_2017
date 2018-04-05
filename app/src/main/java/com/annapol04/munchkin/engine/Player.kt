package com.annapol04.munchkin.engine

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Pair

import com.annapol04.munchkin.R
import com.annapol04.munchkin.data.EventRepository
import com.annapol04.munchkin.util.NonNullMutableLiveData

import java.util.ArrayList

class Player(val id: Int, private val game: Game, private val eventRepository: EventRepository) : LiveData<Player>() {

    private var numberOfAllowedTreasureCardsToDraw = 0
    private var numberOfAllowedDoorCardsToDraw = 0
    private var numberOfAllowedCardsToDrop = 0
    private val name = MutableLiveData<String>()
    private val level = MutableLiveData<Int>()
    private val fightLevel = MutableLiveData<Int>()
    private val race = MutableLiveData<PlayerRace>()
    private val runAway = MutableLiveData<Int>()
    private val bonus = MutableLiveData<Int>()

    private var headGeer: BonusWear? = null
    private var armor: BonusWear? = null
    private var shoes: BonusWear? = null
    private var firstOneHander: BonusWear? = null
    private var secondOneHander: BonusWear? = null
    private var twoHander: BonusWear? = null

    private val isHeadgearEquiped = MutableLiveData<Boolean>()
    private val isArmorEquiped = MutableLiveData<Boolean>()
    private val areShoesquiped = MutableLiveData<Boolean>()
    private val isRightHandEquiped = MutableLiveData<Boolean>()
    private val isLeftHandEquiped = MutableLiveData<Boolean>()

    private val canPlayBigEquipment = MutableLiveData<Boolean>()
    private val canPlayHeadgeer = MutableLiveData<Boolean>()
    private val canPlayArmor = MutableLiveData<Boolean>()
    private val canPlayShoes = MutableLiveData<Boolean>()
    private val canPlayOneHander = MutableLiveData<Boolean>()
    private val canPlayTwoHander = MutableLiveData<Boolean>()

    val handCards = NonNullMutableLiveData<List<Card>>(emptyList())
    val playedCards = NonNullMutableLiveData<List<Card>>(emptyList())
    var scope = Scope.GAME

    val isAllowedToDrawTreasureCard: Boolean
        get() = numberOfAllowedTreasureCardsToDraw > 0

    val isAllowedToDrawDoorCard: Boolean
        get() = numberOfAllowedDoorCardsToDraw > 0

    val isAllowedToDropCard: Boolean
        get() = numberOfAllowedCardsToDrop > 0

    val areShoesEquiped: LiveData<Boolean>
        get() = areShoesquiped

    enum class PlayerRace {
        HUMAN,
        ELF,
        DWARF
        //Zwerg
    }

    init {
        name.value = ""

        reset()
    }

    fun reset() {
        numberOfAllowedTreasureCardsToDraw = 0
        numberOfAllowedDoorCardsToDraw = 0
        numberOfAllowedCardsToDrop = 0

        level.value = 1
        fightLevel.value = 1
        race.value = PlayerRace.HUMAN
        runAway.value = 0
        handCards.value = ArrayList()
        playedCards.value = ArrayList()
        bonus.value = 0

        canPlayBigEquipment.value = true
        canPlayHeadgeer.value = true
        canPlayArmor.value = true
        canPlayShoes.value = true
        canPlayOneHander.value = true
        canPlayTwoHander.value = true

        isHeadgearEquiped.value = false
        isArmorEquiped.value = false
        areShoesquiped.value = false
        isLeftHandEquiped.value = false
        isRightHandEquiped.value = false
    }

    fun allowToDrawTreasureCards(amount: Int) {
        numberOfAllowedTreasureCardsToDraw = amount
    }

    fun allowToDrawDoorCards(amount: Int) {
        numberOfAllowedDoorCardsToDraw = amount
    }

    fun limitHandCards(max: Int) {
        if (max < 0)
            throw IllegalArgumentException("There can only be a positive amount o")

        if (handCards.value.size > max) {
            numberOfAllowedCardsToDrop = handCards.value.size - max
        }
    }

    fun rename(name: String) {
        this.name.value = name
    }

    fun takePlayedCard(card: Card) {
        playedCards.value = playedCards.value.filter { it === card }
    }

    fun levelUp() {
        level.value = level.value!! + 1
    }

    private fun <T> update(liveData: MutableLiveData<T>) {
        liveData.value = liveData.value
    }

    fun getName(): String {
        return name.value!!
    }

    fun getLevel(): LiveData<Int> {
        return level
    }

    fun getFightLevel(): LiveData<Int> {
        return fightLevel
    }

    fun getCanPlayBigEquipment(): LiveData<Boolean> {
        return canPlayBigEquipment
    }

    fun getCanPlayHeadgeer(): LiveData<Boolean> {
        return canPlayHeadgeer
    }

    fun getCanPlayArmor(): LiveData<Boolean> {
        return canPlayArmor
    }

    fun getCanPlayShoes(): LiveData<Boolean> {
        return canPlayShoes
    }

    fun getCanPlayOneHander(): LiveData<Boolean> {
        return canPlayOneHander
    }

    fun getCanPlayTwoHander(): LiveData<Boolean> {
        return canPlayTwoHander
    }

    fun getIsHeadgearEquiped(): LiveData<Boolean> {
        return isHeadgearEquiped
    }

    fun getIsArmorEquiped(): LiveData<Boolean> {
        return isArmorEquiped
    }

    fun getIsRightHandEquiped(): LiveData<Boolean> {
        return isRightHandEquiped
    }

    fun getIsLeftHandEquiped(): LiveData<Boolean> {
        return isLeftHandEquiped
    }

    fun getHandCards(): LiveData<List<Card>> {
        return handCards
    }

    fun getPlayedCards(): LiveData<List<Card>> {
        return playedCards
    }

    @Throws(IllegalEngineStateException::class)
    fun drawTreasureCard(card: Card) {
        if (numberOfAllowedTreasureCardsToDraw == 0)
            throw IllegalEngineStateException("not allowed to draw a treasure card!")

        numberOfAllowedTreasureCardsToDraw--

        game.drawTreasureCard(card)

        handCards.value += listOf(card)
    }

    @Throws(IllegalEngineStateException::class)
    fun drawDoorCard(card: Card) {
        if (numberOfAllowedDoorCardsToDraw == 0)
            throw IllegalEngineStateException("not allowed to draw a door card!")

        numberOfAllowedDoorCardsToDraw--

        game.drawDoorCard(card)
    }

    @Throws(IllegalEngineStateException::class)
    fun playCard(card: Card) {
        if (card is BonusWear) {

            if (card.size === BonusWear.Size.BIG) {
                if (!canPlayBigEquipment.value!!)
                    throw IllegalEngineStateException("Can not equip " + card + " for player " + this)
                else
                    canPlayBigEquipment.setValue(false)
            }

            when (card.blocking) {
                BonusWear.Blocking.NOTHING -> {
                }
                BonusWear.Blocking.ONEHAND -> if (twoHander == null && firstOneHander == null) {
                    firstOneHander = card

                    isLeftHandEquiped.setValue(true)
                    canPlayOneHander.setValue(secondOneHander == null)
                } else if (twoHander == null && secondOneHander == null) {
                    secondOneHander = card

                    isRightHandEquiped.setValue(true)
                    canPlayOneHander.setValue(false)
                } else
                    throw IllegalEngineStateException("Can not equip " + card + " for player " + this)
                BonusWear.Blocking.TWOHANDS -> if (firstOneHander == null && secondOneHander == null && twoHander == null) {
                    twoHander = card

                    isLeftHandEquiped.setValue(true)
                    isRightHandEquiped.setValue(true)
                    canPlayTwoHander.setValue(false)
                } else
                    throw IllegalEngineStateException("Can not equip " + card + " for player " + this)
                BonusWear.Blocking.ARMOR -> if (armor == null) {
                    armor = card

                    isArmorEquiped.setValue(true)
                    canPlayArmor.setValue(false)
                } else
                    throw IllegalEngineStateException("Can not equip " + card + " for player " + this)
                BonusWear.Blocking.HEAD -> if (headGeer == null) {
                    headGeer = card

                    isHeadgearEquiped.setValue(true)
                    canPlayHeadgeer.setValue(false)
                } else
                    throw IllegalEngineStateException("Can not equip " + card + " for player " + this)
                BonusWear.Blocking.SHOES -> if (shoes == null) {
                    shoes = card

                    areShoesquiped.setValue(true)
                    canPlayShoes.setValue(false)
                } else
                    throw IllegalEngineStateException("Can not equip " + card + " for player " + this)
            }

            fightLevel.setValue(fightLevel.value!! + card.bonus)
        } else
            throw IllegalArgumentException("Unknown card type $card")

        handCards.value = handCards.value - listOf(card)
        playedCards.value = playedCards.value + listOf(card)
    }

    fun dropCard(card: Card) {
        test(card in handCards.value, "Can not drop a card not on the hand")
        test(numberOfAllowedCardsToDrop > 0, "Not allowed to drop a card")

        numberOfAllowedCardsToDrop--

        handCards.value -= listOf(card)
    }

    fun emitPickupCard(card: Card) {
        eventRepository.push(
                Event(scope, Action.PICKUP_CARD, R.string.ev_pickup_card, card.id)
        )
    }

    @Throws(IllegalEngineStateException::class)
    fun pickupCard(card: Card) {
        if (card is BonusWear) {

            if (card.size === BonusWear.Size.BIG)
                canPlayBigEquipment.setValue(true)

            when (card.blocking) {
                BonusWear.Blocking.NOTHING -> {
                }
                BonusWear.Blocking.ONEHAND -> {
                    if (secondOneHander == card) {
                        secondOneHander = null

                        isRightHandEquiped.setValue(false)
                    } else if (firstOneHander == card) {
                        firstOneHander = null

                        isLeftHandEquiped.setValue(false)
                    } else
                        throw IllegalEngineStateException("Can not unequip " + card + " for player " + this)

                    canPlayOneHander.setValue(true)
                }
                BonusWear.Blocking.TWOHANDS -> if (twoHander == card) {
                    twoHander = null

                    isLeftHandEquiped.setValue(false)
                    isRightHandEquiped.setValue(false)
                    canPlayTwoHander.setValue(true)
                } else
                    throw IllegalEngineStateException("Can not unequip " + card + " for player " + this)
                BonusWear.Blocking.ARMOR -> if (armor == card) {
                    armor = null

                    isArmorEquiped.setValue(false)
                    canPlayArmor.setValue(true)
                } else
                    throw IllegalEngineStateException("Can not unequip " + card + " for player " + this)
                BonusWear.Blocking.HEAD -> if (headGeer == card) {
                    headGeer = null

                    isHeadgearEquiped.setValue(false)
                    canPlayHeadgeer.setValue(true)
                } else
                    throw IllegalEngineStateException("Can not unequip " + card + " for player " + this)
                BonusWear.Blocking.SHOES -> if (shoes == card) {
                    shoes = null

                    areShoesquiped.setValue(false)
                    canPlayShoes.setValue(true)
                } else
                    throw IllegalEngineStateException("Can not unequip " + card + " for player " + this)
            }

            fightLevel.setValue(fightLevel.value!! - card.bonus)
        } else
            throw IllegalArgumentException("Unknown card type $card")

        playedCards.value = playedCards.value - listOf(card)
        handCards.value = handCards.value + listOf(card)
    }

    @Throws(IllegalEngineStateException::class)
    fun runAway() {
        game.pushAwayMonsterCard()
    }

    @Throws(IllegalEngineStateException::class)
    fun fightMonster(): Pair<Monster, Int> {
        val cards = game.deskCards.value

        test(cards.size > 0,
                "a monster has to be on the table to fight against it!")

        val card = cards.get(0)

        test(card is Monster,
                "the card on the playdesk has to be a monster to fight against it!")

        val monster = card as Monster

        test(fightLevel.value!! > monster.level,
                "can not fight against a monster with greater or equal fight level!")

        level.setValue(level.value!! + 1)
        game.pushAwayMonsterCard()

        return Pair(monster, 1)
    }

    override fun toString(): String {
        return name.value!!
    }
}

package com.annapol04.munchkin

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.InstrumentationRegistry
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.annapol04.munchkin.data.EventRepository
import com.annapol04.munchkin.db.AppDb
import com.annapol04.munchkin.db.EventDao
import com.annapol04.munchkin.engine.*
import com.annapol04.munchkin.gui.PlayDeskViewModel
import com.annapol04.munchkin.network.PlayClientDummy
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class GameRuleTest {
    @get:Rule
    public val instantExecutor = InstantTaskExecutorRule()

    lateinit var app: Application
    lateinit var db: AppDb
    lateinit var eventDao: EventDao
    lateinit var executors: AppExecutors
    lateinit var client: PlayClient
    lateinit var decoder: Decoder
    lateinit var repository: EventRepository
    lateinit var treasureCards: Deck
    lateinit var doorCards: Deck
    lateinit var game: Game
    lateinit var myself: Player
    lateinit var match: Match
    lateinit var executor: Executor
    lateinit var vm: PlayDeskViewModel

    @Before
    fun setup() {
        app = InstrumentationRegistry.getTargetContext().applicationContext as Application

        eventDao = FakeEventDao()

        client = PlayClientDummy()
        executors = AppExecutors()
        decoder = Decoder()

        repository = EventRepository(executors, eventDao, client, decoder)

        treasureCards = DeterministicDeck(listOf(
                TreasureCards.ELEVEN_FOOT_POLE,
                TreasureCards.HELM_OF_COURAGE,
                TreasureCards.LEATHER_ARMOR,
                TreasureCards.SLIMY_ARMOR,
                TreasureCards.SPIKY_KNEES,
                TreasureCards.HORNY_HELMET,
                TreasureCards.BOOTS_OF_BUTT_KICKING,
                TreasureCards.DAGGER_OF_TREACHERY,
                TreasureCards.RAPIER_OF_UNFAIRNESS,
                TreasureCards.REALLY_IMPRESSIVE_TITLE,
                TreasureCards.BAD_ASS_BANDANA,
                TreasureCards.SWISS_ARMY_POLEARM,
                TreasureCards.ELEVEN_FOOT_POLE,
                TreasureCards.HELM_OF_COURAGE,
                TreasureCards.LEATHER_ARMOR,
                TreasureCards.SLIMY_ARMOR,
                TreasureCards.SPIKY_KNEES,
                TreasureCards.HORNY_HELMET,
                TreasureCards.BOOTS_OF_BUTT_KICKING,
                TreasureCards.DAGGER_OF_TREACHERY,
                TreasureCards.RAPIER_OF_UNFAIRNESS,
                TreasureCards.REALLY_IMPRESSIVE_TITLE,
                TreasureCards.BAD_ASS_BANDANA,
                TreasureCards.SWISS_ARMY_POLEARM
        ))

        doorCards = DeterministicDeck(listOf(
                DoorCards.LAME_GOBLIN,
                DoorCards.UNDEAD_HORSE,
                DoorCards.HARPIES,
                DoorCards.POTTED_PLANT,
                DoorCards.AMAZON,
                DoorCards.BIGFOOT,
                DoorCards.CRABS,
                DoorCards.UNDEAD_HORSE,
                DoorCards.HARPIES,
                DoorCards.POTTED_PLANT,
                DoorCards.AMAZON,
                DoorCards.BIGFOOT,
                DoorCards.LAME_GOBLIN,
                DoorCards.UNDEAD_HORSE,
                DoorCards.HARPIES,
                DoorCards.POTTED_PLANT,
                DoorCards.UNDEAD_HORSE,
                DoorCards.HARPIES,
                DoorCards.POTTED_PLANT,
                DoorCards.UNDEAD_HORSE,
                DoorCards.HARPIES,
                DoorCards.POTTED_PLANT,
                DoorCards.UNDEAD_HORSE,
                DoorCards.HARPIES,
                DoorCards.POTTED_PLANT,
                DoorCards.UNDEAD_HORSE,
                DoorCards.HARPIES,
                DoorCards.POTTED_PLANT,
                DoorCards.AMAZON,
                DoorCards.BIGFOOT,
                DoorCards.CRABS,
                DoorCards.UNDEAD_HORSE,
                DoorCards.HARPIES,
                DoorCards.POTTED_PLANT,
                DoorCards.AMAZON,
                DoorCards.BIGFOOT,
                DoorCards.LAME_GOBLIN,
                DoorCards.UNDEAD_HORSE,
                DoorCards.HARPIES,
                DoorCards.POTTED_PLANT,
                DoorCards.UNDEAD_HORSE,
                DoorCards.HARPIES,
                DoorCards.POTTED_PLANT,
                DoorCards.UNDEAD_HORSE,
                DoorCards.HARPIES,
                DoorCards.POTTED_PLANT,
                DoorCards.UNDEAD_HORSE,
                DoorCards.HARPIES,
                DoorCards.POTTED_PLANT
        ))

        game = Game(treasureCards, doorCards)
        myself = Player(Int.MAX_VALUE, game, repository)
        myself.rename("Marvin")

        match = FakeMatch(game, myself, repository)
        executor = Executor(match, game, repository, MessageBook(app, match))

        vm = PlayDeskViewModel(app, myself, client, match, game, repository, executor)

        client.startQuickGame()
    }

    @After
    fun tearDown() {
        assertEquals(0, executor.errorCount)
    }

    @Test
    fun playersJoinTheMatch() {
        assertEquals(3, match.players.value.size)
    }

    @Test
    fun playersHaveDrawnInitialCards() {
        assertFalse(match.players.value[0].isAllowedToDrawTreasureCard)
        assertFalse(match.players.value[1].isAllowedToDrawTreasureCard)
        assertFalse(match.players.value[2].isAllowedToDrawTreasureCard)

        assertEquals(2, match.players.value[0].handCards.value.size)
        assertEquals(2, match.players.value[1].handCards.value.size)
        assertEquals(2, match.players.value[2].handCards.value.size)
    }

    @Test
    fun playerDrawsTreasureCardInRound() {
        vm.drawDoorCard()

        assertEquals(1, game.deskCards.value.size)
    }

    @Test
    fun playerEquipsInRound() {
        vm.playCard(myself.handCards.value[0])

        assertEquals(1, myself.playedCards.value.size)
    }

    @Test
    fun playerFinishesFirstRound() {
        vm.playCard(myself.handCards.value[0])
        vm.drawDoorCard()
        vm.startCombat()
        vm.fightMonster()
        vm.drawTreasureCard()
        vm.finishRound()

        assertEquals(match.players.value[1], match.currentPlayer.value)
    }

    @Test
    fun playerCanFinishRound() {
        assertFalse(match.canFinishRound.value)

        vm.playCard(myself.handCards.value[0])
        assertFalse(match.canFinishRound.value)

        vm.drawDoorCard()
        assertFalse(match.canFinishRound.value)

        vm.startCombat()
        assertFalse(match.canFinishRound.value)

        vm.fightMonster()
        assertFalse(match.canFinishRound.value)

        vm.drawTreasureCard()
        assertTrue(match.canFinishRound.value)

        vm.finishRound()
    }

    @Test
    fun playerCanStartCombat() {
        assertFalse(match.canStartCombat.value)

        vm.playCard(myself.handCards.value[0])
        assertFalse(match.canStartCombat.value)

        vm.drawDoorCard()
        assertTrue(match.canStartCombat.value)

        vm.startCombat()
        assertTrue(match.canStartCombat.value)

        vm.fightMonster()
        assertFalse(match.canStartCombat.value)

        vm.drawTreasureCard()
        assertFalse(match.canStartCombat.value)

        vm.finishRound()

        assertEquals(match.players.value[1], match.currentPlayer.value)
    }

    @Test
    fun playerGetsLevelWhenBeatingMonster() {
        assertEquals(1, match.players.value[0].getLevel().value)

        vm.playCard(myself.handCards.value[0])
        vm.drawDoorCard()
        vm.startCombat()
        vm.fightMonster()

        assertEquals(2, match.players.value[0].getLevel().value)
    }

    @Test
    fun playerGetsTreasuresWhenBeatingMonster() {
        assertFalse(match.players.value[0].isAllowedToDrawTreasureCard)

        vm.playCard(myself.handCards.value[0])
        vm.drawDoorCard()
        vm.startCombat()
        vm.fightMonster()

        assertTrue(match.players.value[0].isAllowedToDrawTreasureCard)
    }

    @Test
    fun playerHasToDropCards() {
        assertFalse(match.players.value[0].isAllowedToDropCard)

        vm.playCard(myself.handCards.value[0])

        for (j in 1..3) {
            vm.displayPlayer(1)
            vm.drawDoorCard()
            vm.startCombat()
            vm.fightMonster()
            vm.drawTreasureCard()
            vm.finishRound()

            assertFalse(match.players.value[0].isAllowedToDropCard)

            for (i in 2..3) {
                vm.displayPlayer(i)
                vm.drawDoorCard()
                vm.startCombat()
                vm.runAwayFromMonster()
                vm.finishRound()
            }
        }

        vm.displayPlayer(1)
        vm.drawDoorCard()
        vm.startCombat()
        vm.fightMonster()
        vm.drawTreasureCard()

        assertTrue(match.players.value[0].isAllowedToDropCard)
        assertFalse(match.canFinishRound.value)

        vm.dropCard(match.players.value[0].handCards.value[0])

        assertFalse(match.players.value[0].isAllowedToDropCard)
        assertTrue(match.canFinishRound.value)
    }

    @Test
    fun playerWinsWithSpecificLevel() {
        vm.displayPlayer(1)
        vm.playCard(myself.handCards.value[0])
        vm.playCard(myself.handCards.value[0])

        for (j in 1..5) {
            vm.displayPlayer(1)
            vm.drawDoorCard()
            vm.startCombat()
            vm.fightMonster()

            if (match.result.value != null)
                break

            vm.drawTreasureCard()

            if (match.players.value[0].isAllowedToDropCard)
                vm.dropCard(match.players.value[0].handCards.value[0])

            vm.finishRound()

            for (i in 2..3) {
                vm.displayPlayer(i)
                vm.drawDoorCard()
                vm.startCombat()
                vm.runAwayFromMonster()
                vm.finishRound()
            }
        }

        assertEquals(10, match.players.value[0].getLevel().value)
        assertNotNull(match.result)
    }
}
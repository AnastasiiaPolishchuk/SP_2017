package com.annapol04.munchkin

import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4

import com.annapol04.munchkin.data.EventRepository
import com.annapol04.munchkin.db.AppDb
import com.annapol04.munchkin.db.EventDao
import com.annapol04.munchkin.engine.Decoder
import com.annapol04.munchkin.engine.Event
import com.annapol04.munchkin.engine.Executor
import com.annapol04.munchkin.engine.FakeMatch
import com.annapol04.munchkin.engine.Game
import com.annapol04.munchkin.engine.Match
import com.annapol04.munchkin.engine.MessageBook
import com.annapol04.munchkin.engine.PlayClient
import com.annapol04.munchkin.engine.Player
import com.annapol04.munchkin.gui.PlayDeskViewModel
import com.annapol04.munchkin.network.PlayClientDummy

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

import java.util.ArrayList

import org.hamcrest.Matchers.`is`
import org.junit.Assert.*

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
    lateinit var game: Game
    lateinit var myself: Player
    lateinit var match: Match
    lateinit var executor: Executor
    lateinit var vm: PlayDeskViewModel

    internal inner class Events : EventDao {
        val events: MutableList<Event> = ArrayList()
        val d = MutableLiveData<List<Event>>()

        override fun loadEntries(): LiveData<List<Event>> {
            return d
        }

        override fun insert(event: Event) {
            events.add(event)
        }
    }

    @Before
    fun setup() {
        app = InstrumentationRegistry.getTargetContext().applicationContext as Application

        eventDao = Events()

        client = PlayClientDummy()
        executors = AppExecutors()
        decoder = Decoder()

        repository = EventRepository(executors, eventDao, client, decoder)
        game = Game(repository)
        myself = Player(Int.MAX_VALUE, game, repository)
        myself.rename("Marvin")

        match = FakeMatch(game, myself, repository)
        executor = Executor(match, game, repository, MessageBook(app))

        vm = PlayDeskViewModel(app, myself, client, match, game, repository, executor)

        client.startQuickGame()
    }

    @After
    fun tearDown() {
        assertEquals(0, executor.errorCount)
    }

    @Test
    fun playersJoinTheMatch() {
        assertEquals(3, match.players.value!!.size)
    }

    @Test
    fun playersHaveDrawnInitialCards() {
        assertFalse(match.players.value!![0].isAllowedToDrawTreasureCard)
        assertFalse(match.players.value!![1].isAllowedToDrawTreasureCard)
        assertFalse(match.players.value!![2].isAllowedToDrawTreasureCard)

        assertEquals(2, match.players.value!![0].handCards.value!!.size)
        assertEquals(2, match.players.value!![1].handCards.value!!.size)
        assertEquals(2, match.players.value!![2].handCards.value!!.size)
    }

    @Test
    fun playerDrawsTreasureCardInRound() {
        vm.displayPlayer(1)
        vm.drawDoorCard()

        assertEquals(1, game.deskCards.value!!.size)
    }

    @Test
    fun playerEquipsInRound() {
        vm.displayPlayer(1)
        vm.playCard(myself.handCards.value!![0])

        assertEquals(1, myself.playedCards.value!!.size)
    }

 /*   @Test
    fun playerFinishesFirstRound() {
        vm.displayPlayer(1)
        vm.playCard(myself.handCards.value!![0])
        vm.drawDoorCard()

        if (vm.canFightMonster())
            vm.fightMonster()
        else
            vm.runAwayFromMonster()

        assertEquals(match.players.value!![1], match.currentPlayer.value!!)
    }*/
}
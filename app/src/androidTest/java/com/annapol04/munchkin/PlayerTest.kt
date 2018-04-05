package com.annapol04.munchkin

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.annapol04.munchkin.AppExecutors
import com.annapol04.munchkin.data.EventRepository
import com.annapol04.munchkin.db.EventDao
import com.annapol04.munchkin.engine.*
import com.annapol04.munchkin.network.PlayClientDummy
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.ArrayList

@RunWith(AndroidJUnit4::class)
@LargeTest
class PlayerTest {
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    lateinit var game: Game
    lateinit var repository: EventRepository
    lateinit var player: Player

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
    fun setUp() {
        val eventDao = Events()

        val client = PlayClientDummy()
        val executors = AppExecutors()
        val decoder = Decoder()

        repository = EventRepository(executors, eventDao, client, decoder)

        val treasureCards = DeterministicDeck(listOf(
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

        val doorCards = DeterministicDeck(listOf(
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
                DoorCards.POTTED_PLANT
        ))

        game = Game(treasureCards, doorCards)
        player = Player(0, game, repository)
    }

    @Test
    fun hasFightLevel() {
        player.playCard(TreasureCards.BOOTS_OF_BUTT_KICKING)

        Assert.assertEquals(3, player.getFightLevel().value)
    }
}
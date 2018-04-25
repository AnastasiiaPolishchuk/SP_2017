package com.annapol04.munchkin.data

import android.util.Log
import com.annapol04.munchkin.AppExecutors
import com.annapol04.munchkin.db.EventDao
import com.annapol04.munchkin.engine.Decoder
import com.annapol04.munchkin.engine.Event
import com.annapol04.munchkin.engine.Executor
import com.annapol04.munchkin.engine.PlayClient
import java.util.*

import javax.inject.Singleton
import javax.inject.Inject

@Singleton
class EventRepository @Inject
constructor(private val executors: AppExecutors,
            private val dao: EventDao,
            private val client: PlayClient,
            private val decoder: Decoder)
    : PlayClient.OnMessageReceivedListener {
    private var topHash = (1..16).map { it.toByte() }.toByteArray()
        set(value) {
            if (Arrays.equals(field, pushTopHash))
                pushTopHash = value

            field = value
        }

    private var pushTopHash = topHash

    var newEventListener: OnNewEventListener? = null

    interface OnNewEventListener {
        fun onNewEvent(event: Event)
    }

    init {
        client.messageReceivedListener = this

        reset()
    }

    fun reset() {
        executors.diskIO.execute { dao.deleteAll() }

        topHash = (1..16).map { it.toByte() }.toByteArray()
        pushTopHash = topHash
    }

    fun push(events: Collection<Event>, ignoreHash: Boolean = false) {
        for (event in events) {
            event.setPreviousHash(pushTopHash)

            if (ignoreHash)
                event.hash = pushTopHash
            else
                pushTopHash = event.hash

            client.sendToAll(event.getBytes())
        }
    }

    fun push(vararg events: Event, ignoreHash: Boolean = false) {
        push(events.asList(), ignoreHash)
    }

    override fun onMessageReceived(data: ByteArray) {
        decoder.decode(data, 0, data.size)
            .forEach {
                it.id = null

                if (Arrays.equals(topHash, it.getPreviousHash())) {
                    topHash = it.hash

                    executors.diskIO.execute { dao.insert(it) }

                    newEventListener?.onNewEvent(it)
                } else {
                    val msg = "Received event in wrong order: " + it.toString() + ""

                    Log.e(TAG, msg)
                    Log.e(TAG, Arrays.toString(topHash) + " != " + Arrays.toString(it.getPreviousHash()))
                    throw IllegalStateException(msg)
                }
            }
    }

    companion object {
        private val TAG = EventRepository::class.java.simpleName
    }
}

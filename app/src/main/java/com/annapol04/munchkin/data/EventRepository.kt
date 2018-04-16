package com.annapol04.munchkin.data

import com.annapol04.munchkin.AppExecutors
import com.annapol04.munchkin.db.EventDao
import com.annapol04.munchkin.engine.Decoder
import com.annapol04.munchkin.engine.Event
import com.annapol04.munchkin.engine.PlayClient

import javax.inject.Singleton
import javax.inject.Inject

@Singleton
class EventRepository @Inject
constructor(private val executors: AppExecutors,
            private val dao: EventDao,
            private val client: PlayClient,
            private val decoder: Decoder)
    : PlayClient.OnMessageReceivedListener {
    var topHash = (1..16).map { it.toByte() }.toByteArray()
    private var pushTopHash = topHash

    var newEventListener: OnNewEventListener? = null

    interface OnNewEventListener {
        fun onNewEvent(event: Event)
    }

    init {
        client.messageReceivedListener = this

        executors.diskIO.execute { dao.deleteAll() }
    }

    fun push(events: Collection<Event>) {
        for (event in events) {
            event.setPreviousHash(pushTopHash)
            pushTopHash = event.hash

            client.sendToAll(event.getBytes())
        }
    }

    fun push(vararg events: Event) {
        for (event in events) {
            event.setPreviousHash(pushTopHash)
            pushTopHash = event.hash

            client.sendToAll(event.getBytes())
        }
    }

    override fun onMessageReceived(data: ByteArray) {
        decoder.decode(data, 0, data.size)
            .forEach {
                it.id = null
                executors.diskIO.execute { dao.insert(it) }

                newEventListener?.onNewEvent(it)
            }
    }

    companion object {
        private val TAG = EventRepository::class.java.simpleName
    }
}

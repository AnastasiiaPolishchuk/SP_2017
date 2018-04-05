package com.annapol04.munchkin.data

import android.util.Log

import com.annapol04.munchkin.AppExecutors
import com.annapol04.munchkin.db.EventDao
import com.annapol04.munchkin.engine.Decoder
import com.annapol04.munchkin.engine.Event
import com.annapol04.munchkin.engine.HashUtil
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
    var topHash = byteArrayOf(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(),
            6.toByte(), 7.toByte(), 8.toByte(), 9.toByte(), 10.toByte(), 11.toByte(), 12.toByte(),
            13.toByte(), 14.toByte(), 15.toByte(), 16.toByte())

    private var pushTopHash = topHash

    private var listener: OnNewEventListener? = null

    interface OnNewEventListener {
        fun onNewEvent(event: Event)
    }

    init {
        this.client.messageReceivedListener = this
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

    fun setNewEventListener(listener: OnNewEventListener) {
        this.listener = listener
    }

    fun newEvent(event: Event) {
        if (listener != null)
            listener!!.onNewEvent(event)
    }

    override fun onMessageReceived(data: ByteArray) {
        val received = decoder.decode(data, 0, data.size)

        for (event in received) {
            executors.diskIO().execute { dao.insert(event) }

            newEvent(event)
        }
    }

    companion object {
        private val TAG = EventRepository::class.java.simpleName
    }
}

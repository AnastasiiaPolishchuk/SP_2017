package com.annapol04.munchkin

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.annapol04.munchkin.db.EventDao
import com.annapol04.munchkin.engine.Event
import java.util.ArrayList

class FakeEventDao: EventDao {
    override fun deleteAll() {
        events.clear()
    }

    val events: MutableList<Event> = ArrayList()
    val d = MutableLiveData<List<Event>>()

    override fun loadEntries(): LiveData<List<Event>> {
        return d
    }

    override fun insert(event: Event) {
        events.add(event)
    }
}
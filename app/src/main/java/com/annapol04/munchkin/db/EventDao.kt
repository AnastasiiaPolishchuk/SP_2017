package com.annapol04.munchkin.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

import com.annapol04.munchkin.engine.Event

@Dao
interface EventDao {
    @Query("SELECT * from events")
    fun loadEntries(): LiveData<List<Event>>

    @Insert
    fun insert(event: Event)
}

package com.huseyincan.eventdriven.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.association.EventWithTickets
import java.util.UUID

@Dao
interface EventDao {
    @Query("SELECT * FROM Event")
    fun getAllEvents(): List<Event>

    @Query("SELECT * FROM Event WHERE eid IN (:eids)")
    fun getByEID(eids: Array<String>): List<Event>

    @Query("SELECT * FROM Event WHERE event_name LIKE :eventName")
    fun getByEventName(eventName: String): List<Event>

    @Transaction
    @Query("SELECT * FROM Event")
    fun getEventsWithTickets(): List<EventWithTickets>

    @Insert
    fun insertAll(vararg events: Event)

    @Delete
    fun delete(event: Event)

    @Update
    fun updateEvent(event: Event)

}
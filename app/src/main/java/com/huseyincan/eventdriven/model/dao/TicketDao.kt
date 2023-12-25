package com.huseyincan.eventdriven.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.huseyincan.eventdriven.model.data.Ticket
import java.util.UUID

@Dao
interface TicketDao {
    @Query("SELECT * FROM Ticket")
    fun getAllTickets(): List<Ticket>

    @Query("SELECT * FROM Ticket WHERE profile_id = :pid")
    fun getUserTickets(pid: String): List<Ticket>

    @Query("SELECT * FROM Ticket WHERE event_id = :eid")
    fun getEventTickets(eid: String): List<Ticket>

    @Insert
    fun insertAll(vararg tickets: Ticket)

    @Delete
    fun delete(ticket: Ticket)
}
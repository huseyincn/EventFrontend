package com.huseyincan.eventdriven.model.data.base

import androidx.room.Database
import androidx.room.RoomDatabase
import com.huseyincan.eventdriven.model.dao.EventDao
import com.huseyincan.eventdriven.model.dao.ProfileDao
import com.huseyincan.eventdriven.model.dao.TicketDao
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.Profile
import com.huseyincan.eventdriven.model.data.Ticket

@Database(entities = [Event::class, Profile::class, Ticket::class], version = 1)
abstract class Saver : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun tickettDao(): TicketDao
    abstract fun profileDao(): ProfileDao
}
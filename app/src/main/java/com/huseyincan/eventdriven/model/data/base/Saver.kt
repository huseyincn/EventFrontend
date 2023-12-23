package com.huseyincan.eventdriven.model.data.base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.huseyincan.eventdriven.model.dao.EventDao
import com.huseyincan.eventdriven.model.dao.ProfileDao
import com.huseyincan.eventdriven.model.dao.TicketDao
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.Profile
import com.huseyincan.eventdriven.model.data.Ticket

@Database(entities = [Event::class, Profile::class, Ticket::class], version = 5)
abstract class Saver : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun ticketDao(): TicketDao
    abstract fun profileDao(): ProfileDao

    companion object {
        private const val Database_NAME = "eventmanager.db"

        /**
         * As we need only one instance of db in our app will use to store
         * This is to avoid memory leaks in android when there exist multiple instances of db
         */
        @Volatile
        private var INSTANCE: Saver? = null

        fun getInstance(context: Context): Saver {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        Saver::class.java,
                        Database_NAME
                    ).fallbackToDestructiveMigration()
                        .build() // .fallbackToDestructiveMigration() -> FOR DELETE MIGRATION

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
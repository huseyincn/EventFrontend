package com.huseyincan.eventdriven.model.data.base

import android.content.Context
import android.graphics.Bitmap
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huseyincan.eventdriven.model.dao.EventDao
import com.huseyincan.eventdriven.model.dao.ProfileDao
import com.huseyincan.eventdriven.model.dao.RatingDao
import com.huseyincan.eventdriven.model.dao.TicketDao
import com.huseyincan.eventdriven.model.data.Event
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SaverTest {
    private lateinit var db: Saver
    private lateinit var eventDao: EventDao
    private lateinit var ticketDao: TicketDao
    private lateinit var profileDao: ProfileDao
    private lateinit var ratingDao: RatingDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, Saver::class.java).build()
        eventDao = db.eventDao()
        ticketDao = db.ticketDao()
        profileDao = db.profileDao()
        ratingDao = db.ratingDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeEventAndReadInList() {
        val event: Event = Event(
            "name",
            "detail",
            "location",
            "time",
            "date",
            "price",
            "row",
            Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888),
            "id"
        )
        db.eventDao().insertAll(event)
        val byId = db.eventDao().getByEID(arrayOf(event.eid))
        assertEquals(byId.get(0).eid, event.eid)
    }
}
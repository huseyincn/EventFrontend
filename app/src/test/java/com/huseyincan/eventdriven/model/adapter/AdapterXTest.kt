package com.huseyincan.eventdriven.model.adapter

import android.graphics.Bitmap
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.TicketForAdapter
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class AdapterXTest {
    private lateinit var adapter: AdapterX
    private val items = listOf<Event>(Event("name","date","location","time","date","row","seat",
        mock(Bitmap::class.java),"ticketID"))
    private val mockListener = Mockito.mock(AdapterX.onItemClickListener::class.java)

    @Before
    fun setup() {
        adapter = AdapterX(items)
        adapter.setOnItemClickListener(mockListener)
    }

    @Test
    fun getItemCount_returnsCorrectSize() {
        assertEquals(items.size, adapter.itemCount)
    }
}
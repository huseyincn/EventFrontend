package com.huseyincan.eventdriven.model.adapter

import android.view.LayoutInflater
import androidx.test.core.app.ApplicationProvider
import com.huseyincan.eventdriven.model.data.TicketForAdapter
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class AdapterTicketTest {
    private lateinit var adapter: AdapterTicket
    private val items = listOf<TicketForAdapter>(TicketForAdapter("name","date","location","time",null,"row","seat","eventId","ticketID"))
    private val mockListener = mock(AdapterTicket.onItemClickListener::class.java)

    @Before
    fun setup() {
        adapter = AdapterTicket(items)
        adapter.setOnItemClickListener(mockListener)
    }

    @Test
    fun getItemCount_returnsCorrectSize() {
        assertEquals(items.size, adapter.itemCount)
    }
}
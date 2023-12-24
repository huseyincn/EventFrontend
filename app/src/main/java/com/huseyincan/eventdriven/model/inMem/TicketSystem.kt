package com.huseyincan.eventdriven.model.inMem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.Ticket

class TicketSystem:ViewModel() {
    private val _tickets = MutableLiveData<MutableList<Ticket>>();
    val tickets: LiveData<MutableList<Ticket>> = _tickets

    fun addItem(ticket: Ticket) {
        val updatedList = _tickets.value?.toMutableList() ?: mutableListOf()
        updatedList.add(ticket)
        _tickets.value = updatedList
    }

    fun addAll(ticketlist: List<Ticket>) {
        ticketlist.onEach { addItem(it) }
    }

    fun createModel(ticketlist: List<Ticket>) {
        _tickets.value = ticketlist.toMutableList()
    }
}
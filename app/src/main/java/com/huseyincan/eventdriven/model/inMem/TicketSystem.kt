package com.huseyincan.eventdriven.model.inMem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huseyincan.eventdriven.model.data.TicketForAdapter

class TicketSystem:ViewModel() {
    private val _tickets = MutableLiveData<MutableList<TicketForAdapter>>();
    val tickets: LiveData<MutableList<TicketForAdapter>> = _tickets

    fun addItem(ticket: TicketForAdapter) {
        val updatedList = _tickets.value?.toMutableList() ?: mutableListOf()
        updatedList.add(ticket)
        _tickets.value = updatedList
    }

    fun addAll(ticketlist: List<TicketForAdapter>) {
        ticketlist.onEach { addItem(it) }
    }

    fun createModel(ticketlist: List<TicketForAdapter>) {
        _tickets.value = ticketlist.toMutableList()
    }
}
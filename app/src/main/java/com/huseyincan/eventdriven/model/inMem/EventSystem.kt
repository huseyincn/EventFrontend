package com.huseyincan.eventdriven.model.inMem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huseyincan.eventdriven.model.data.Event

class EventSystem : ViewModel() {

    private val _events = MutableLiveData<MutableList<Event>>();
    val events: LiveData<MutableList<Event>> = _events

    fun addItem(event: Event) {
        val updatedList = _events.value?.toMutableList() ?: mutableListOf()
        updatedList.add(event)
        _events.value = updatedList
    }

    fun addAll(eventlist: List<Event>) {
        eventlist.onEach { addItem(it) }
    }

    fun createModel(eventlist: List<Event>) {
        _events.value = eventlist.toMutableList()
    }

}

/*
retrofit moshi
 */
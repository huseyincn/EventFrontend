package com.huseyincan.eventdriven.model.inMem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huseyincan.eventdriven.model.data.Event

class EventSystem : ViewModel() {

    private val _events = MutableLiveData<MutableList<Event>>();
    val events: LiveData<MutableList<Event>> = _events

    init {
        addItem(Event("Redkeygang", "Khont ve ekibi sahne alıcak", "İzmir", "22:22"))
        addItem(
            Event(
                "A1 Capital Baskını",
                "Spekülasyon sonrası a1 capitali basmaya gidiyoruz",
                "Bursa",
                "10:00"
            )
        )
    }

    fun addItem(event: Event) {
        val updatedList = _events.value?.toMutableList() ?: mutableListOf()
        updatedList.add(event)
        _events.value = updatedList
    }

}
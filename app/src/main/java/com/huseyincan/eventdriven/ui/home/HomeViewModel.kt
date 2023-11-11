package com.huseyincan.eventdriven.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<List<Event>>().apply {
        value = mutableListOf()
    }

    val text: LiveData<List<Event>> = _text

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
        val updatedList = _text.value?.toMutableList() ?: mutableListOf()
        updatedList.add(event)
        _text.value = updatedList
    }
}
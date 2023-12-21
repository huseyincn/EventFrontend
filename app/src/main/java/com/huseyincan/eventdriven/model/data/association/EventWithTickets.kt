package com.huseyincan.eventdriven.model.data.association

import androidx.room.Embedded
import androidx.room.Relation
import com.huseyincan.eventdriven.model.data.Event
import com.huseyincan.eventdriven.model.data.Ticket

data class EventWithTickets(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eid",
        entityColumn = "tid"
    ) val ticketList: List<Ticket>
)
package com.huseyincan.eventdriven.model.data.association

import androidx.room.Embedded
import androidx.room.Relation
import com.huseyincan.eventdriven.model.data.Profile
import com.huseyincan.eventdriven.model.data.Ticket

data class ProfileWithTickets(
    @Embedded val profile: Profile,
    @Relation(
        parentColumn = "pid",
        entityColumn = "tid"
    ) val ticketList: List<Ticket>
)

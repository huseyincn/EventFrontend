package com.huseyincan.eventdriven.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Rating(
    @PrimaryKey val rid: String,
    @ColumnInfo(name = "event_id") val eventId: String?,
    @ColumnInfo(name = "profile_id") val profileId: String?,
    @ColumnInfo(name = "ticket_id") val ticketId: String?,
    @ColumnInfo(name = "rate") val rate: String?
) {
    constructor(
        eventId: String,
        profileId: String,
        ticketId: String,
        rate: String
    ) : this(UUID.randomUUID().toString(), eventId, profileId, ticketId, rate)
}

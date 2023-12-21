package com.huseyincan.eventdriven.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Ticket(
    @PrimaryKey val tid: UUID,
    @ColumnInfo(name = "event_id") val eventId: UUID,
    @ColumnInfo(name = "profile_id") val profileId: UUID,
    @ColumnInfo(name = "row") val row: String?,
    @ColumnInfo(name = "seat") val seat: String?,
) {
    constructor(
        eventId: UUID,
        profileId: UUID,
        row: String,
        seat: String
    ) : this(UUID.randomUUID(), eventId, profileId, row, seat)
}

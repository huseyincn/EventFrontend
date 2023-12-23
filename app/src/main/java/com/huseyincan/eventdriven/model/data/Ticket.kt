package com.huseyincan.eventdriven.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Ticket(
    @PrimaryKey val tid: String,
    @ColumnInfo(name = "event_id") val eventId: String,
    @ColumnInfo(name = "profile_id") val profileId: String,
    @ColumnInfo(name = "row") val row: String?,
    @ColumnInfo(name = "seat") val seat: String?,
) {
    constructor(
        eventId: String,
        profileId: String,
        row: String,
        seat: String
    ) : this(UUID.randomUUID().toString(), eventId, profileId, row, seat)
}

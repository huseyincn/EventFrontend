package com.huseyincan.eventdriven.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Event(
    @PrimaryKey val eid: String,
    @ColumnInfo(name = "event_name") val eventName: String?,
    @ColumnInfo(name = "event_detail") val eventDetail: String?,
    @ColumnInfo(name = "event_location") val eventLocation: String?,
    @ColumnInfo(name = "event_time") val eventTime: String?,
    @ColumnInfo(name = "event_date") val eventDate: String?,
) {
    constructor(
        eventName: String,
        eventDetail: String,
        eventLocation: String,
        eventTime: String,
        eventDate: String
    ) : this(UUID.randomUUID().toString(), eventName, eventDetail, eventLocation, eventTime, eventDate)
}

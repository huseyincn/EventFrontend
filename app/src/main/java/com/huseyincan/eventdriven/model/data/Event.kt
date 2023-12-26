package com.huseyincan.eventdriven.model.data

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity
@Parcelize
data class Event(
    @PrimaryKey val eid: String,
    @ColumnInfo(name = "event_name") var eventName: String?,
    @ColumnInfo(name = "event_detail") var eventDetail: String?,
    @ColumnInfo(name = "event_location") var eventLocation: String?,
    @ColumnInfo(name = "event_time") var eventTime: String?,
    @ColumnInfo(name = "event_date") var eventDate: String?,
    @ColumnInfo(name = "event_price") var eventPrice: String?,
    @ColumnInfo(name = "row_column") var rowColumn: String?,
    @ColumnInfo(name = "event_image") var image: Bitmap?,
    @ColumnInfo(name = "organizer_id") var organizerId: String?
) : Parcelable {
    constructor(
        eventName: String,
        eventDetail: String,
        eventLocation: String,
        eventTime: String,
        eventDate: String,
        eventPrice: String,
        rowColumn: String,
        image: Bitmap,
        organizerId: String
    ) : this(
        UUID.randomUUID().toString(),
        eventName,
        eventDetail,
        eventLocation,
        eventTime,
        eventDate,
        eventPrice,
        rowColumn,
        image,
        organizerId
    )
}

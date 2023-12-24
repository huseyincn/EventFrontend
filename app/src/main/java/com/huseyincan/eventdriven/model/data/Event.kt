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
    @ColumnInfo(name = "event_name") val eventName: String?,
    @ColumnInfo(name = "event_detail") val eventDetail: String?,
    @ColumnInfo(name = "event_location") val eventLocation: String?,
    @ColumnInfo(name = "event_time") val eventTime: String?,
    @ColumnInfo(name = "event_date") val eventDate: String?,
    @ColumnInfo(name = "event_price") val eventPrice: String?,
    @ColumnInfo(name = "row_column") val rowColumn: String?,
    @ColumnInfo(name = "event_image") val image: Bitmap?
) : Parcelable {
    constructor(
        eventName: String,
        eventDetail: String,
        eventLocation: String,
        eventTime: String,
        eventDate: String,
        eventPrice: String,
        rowColumn: String,
        image: Bitmap
    ) : this(
        UUID.randomUUID().toString(),
        eventName,
        eventDetail,
        eventLocation,
        eventTime,
        eventDate,
        eventPrice,
        rowColumn,
        image
    )
}

package com.huseyincan.eventdriven.model.data

import android.graphics.Bitmap

data class TicketForAdapter(
    val ticketName: String?,
    val ticketDate: String?,
    val ticketLocation: String?,
    val ticketTime: String?,
    val ticketImage: Bitmap?,
    val row: String?,
    val seat: String?,
    val eventId: String?,
    val ticketId: String?
)

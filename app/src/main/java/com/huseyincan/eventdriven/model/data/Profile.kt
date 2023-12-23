package com.huseyincan.eventdriven.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Profile(
    @PrimaryKey val pid: String,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
) {
    constructor(
        firstName: String,
        lastName: String
    ) : this(UUID.randomUUID().toString(), firstName, lastName)
}

package com.huseyincan.eventdriven.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.huseyincan.eventdriven.model.data.Profile

@Dao
interface ProfileDao {
    @Insert
    fun insertAll(vararg profiles: Profile)

    @Delete
    fun delete(profile: Profile)
}
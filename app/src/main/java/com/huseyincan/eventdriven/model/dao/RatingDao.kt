package com.huseyincan.eventdriven.model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.huseyincan.eventdriven.model.data.Rating

@Dao
interface RatingDao {
    @Query("SELECT * FROM Rating WHERE event_id =:eventids AND profile_id=:pid AND ticket_id=:tid")
    fun getByEventId(eventids: String, pid: String, tid: String): List<Rating>

    @Query("SELECT * FROM Rating WHERE event_id =:eventids")
    fun getRatingForReport(eventids: String): List<Rating>

    @Insert
    fun insertAll(vararg rating: Rating)

    @Delete
    fun delete(rating: Rating)
}
package com.example.submissionawalandroidfundamental.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.submissionawalandroidfundamental.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM listEvents")
    fun getEvents(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM listEvents where bookmarked = 1")
    fun getBookmarkedEvents(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvents(events: List<EventEntity>)

    @Update
    fun updateEvent(event: EventEntity)

    @Query("DELETE FROM listEvents WHERE bookmarked = 0")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM listEvents WHERE name = :name AND bookmarked = 1)")
    fun isEventBookmarked(name: String): Boolean
}
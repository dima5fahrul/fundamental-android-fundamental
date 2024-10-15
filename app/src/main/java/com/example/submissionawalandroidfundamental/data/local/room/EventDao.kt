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

    @Query("SELECT * FROM listEvents where beginTime >= :currentDate")
    fun getUpcomingEvents(currentDate: String): LiveData<List<EventEntity>>

    @Query("SELECT * FROM listEvents where beginTime < :currentDate")
    fun getFinishedEvents(currentDate: String): LiveData<List<EventEntity>>

    @Query("SELECT * FROM listEvents where bookmarked = 1")
    fun getBookmarkedEvents(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Query("DELETE FROM listEvents WHERE bookmarked = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM listEvents WHERE name = :name AND bookmarked = 1)")
    suspend fun isEventBookmarked(name: String): Boolean
}
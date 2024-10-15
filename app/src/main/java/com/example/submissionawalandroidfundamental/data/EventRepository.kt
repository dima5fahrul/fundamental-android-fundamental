package com.example.submissionawalandroidfundamental.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.submissionawalandroidfundamental.data.local.entity.EventEntity
import com.example.submissionawalandroidfundamental.data.local.room.EventDao
import com.example.submissionawalandroidfundamental.data.remote.retrofit.ApiService
import com.example.submissionawalandroidfundamental.utils.AppExecutors
import com.example.submissionawalandroidfundamental.utils.DataHelper

class EventRepository private constructor(
    private var apiService: ApiService,
    private val eventDao: EventDao,
) {

    fun getBookmarkedEvents(): LiveData<List<EventEntity>> = eventDao.getBookmarkedEvents()

    suspend fun setBookmarkedEvent(event: EventEntity, state: Boolean) {
        event.isBookmarked = state
        eventDao.updateEvent(event)
    }

    fun getUpcomingEvents(query: String?): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getUpcomingEvents(query = query)
            val events = response.listEvents
            val eventList = events.map { event ->
                val isBookmarked = event.name.let { eventDao.isEventBookmarked(it) }
                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.description,
                    event.imageLogo,
                    event.mediaCover,
                    event.category,
                    event.ownerName,
                    event.cityName,
                    event.quota,
                    event.registrants,
                    DataHelper.convertDate(event.beginTime),
                    DataHelper.convertDate(event.endTime),
                    event.link,
                    isBookmarked
                )
            }
            eventDao.deleteAll()
            eventDao.insertEvents(eventList)
        } catch (e: Exception) {
            Log.e("EventRepository", e.message.toString())
            throw Exception(e.message)
        }

        val localData: LiveData<Result<List<EventEntity>>> =
            eventDao.getUpcomingEvents(DataHelper.getCurrentDate()).map { Result.Success(it) }
        emitSource(localData)
    }

    fun getFinishedEvents(query: String?): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getFinishedEvents(query = query)
            val events = response.listEvents
            val eventList = events.map { event ->
                val isBookmarked = event.name.let { eventDao.isEventBookmarked(it) }
                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.description,
                    event.imageLogo,
                    event.mediaCover,
                    event.category,
                    event.ownerName,
                    event.cityName,
                    event.quota,
                    event.registrants,
                    DataHelper.convertDate(event.beginTime),
                    DataHelper.convertDate(event.endTime),
                    event.link,
                    isBookmarked
                )
            }
            eventDao.deleteAll()
            eventDao.insertEvents(eventList)
        } catch (e: Exception) {
            Log.e("EventRepository", e.message.toString())
            throw Exception(e.message)
        }

        val localData: LiveData<Result<List<EventEntity>>> =
            eventDao.getFinishedEvents(DataHelper.getCurrentDate()).map { Result.Success(it) }
        emitSource(localData)
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}
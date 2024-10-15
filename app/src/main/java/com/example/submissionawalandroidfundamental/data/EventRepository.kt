package com.example.submissionawalandroidfundamental.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.submissionawalandroidfundamental.data.local.entity.EventEntity
import com.example.submissionawalandroidfundamental.data.local.room.EventDao
import com.example.submissionawalandroidfundamental.data.remote.response.EventResponse
import com.example.submissionawalandroidfundamental.data.remote.retrofit.ApiConfig
import com.example.submissionawalandroidfundamental.data.remote.retrofit.ApiService
import com.example.submissionawalandroidfundamental.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(
    private var apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Result<List<EventEntity>>>()

    fun getBookmarkedEvents(): LiveData<List<EventEntity>> = eventDao.getBookmarkedEvents()

    fun setBookmarkedEvent(event: EventEntity, state: Boolean) {
        appExecutors.diskIO.execute {
            event.isBookmarked = state
            eventDao.updateEvent(event)
        }
    }

    fun getUpcomingEvents(query: String?): LiveData<Result<List<EventEntity>>> {
        result.value = Result.Loading
        apiService = ApiConfig.getApiService()
        val client = apiService.getUpcomingEvents(query = query)
        try {
            client.enqueue(object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {
                    if (response.isSuccessful) {
                        val events = response.body()?.listEvents
                        val eventList = ArrayList<EventEntity>()

                        appExecutors.diskIO.execute {
                            events?.forEach { event ->
                                val isBookmarked = event.name.let { eventDao.isEventBookmarked(it) }

                                val eventEntity = EventEntity(
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
                                    event.beginTime,
                                    event.endTime,
                                    event.link,
                                    isBookmarked
                                )
                                eventList.add(eventEntity)
                            }
                            eventDao.deleteAll()
                            eventDao.insertEvents(eventList)
                        }
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    result.value = Result.Error(t.message.toString())
                }
            })

            val localData = eventDao.getEvents()
            result.addSource(localData) { newData: List<EventEntity> ->
                result.value = Result.Success(newData)
            }
            return result
        } catch (e: Exception) {
            Log.e("EventRepository", e.message.toString())
            throw Exception(e.message)
        }
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
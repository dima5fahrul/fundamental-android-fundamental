package com.example.submissionawalandroidfundamental.data.remote.retrofit

import com.example.submissionawalandroidfundamental.data.remote.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("/events")
    suspend fun getFinishedEvents(
        @Query("active") active: Int = 0,
        @Query("q") query: String? = null
    ): EventResponse

    @GET("/events")
    suspend fun getUpcomingEvents(
        @Query("active") active: Int = 1,
        @Query("q") query: String? = null
    ): EventResponse

}
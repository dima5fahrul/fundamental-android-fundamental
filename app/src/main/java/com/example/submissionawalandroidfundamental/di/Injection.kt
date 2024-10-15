package com.example.submissionawalandroidfundamental.di

import android.content.Context
import com.example.submissionawalandroidfundamental.data.EventRepository
import com.example.submissionawalandroidfundamental.data.local.room.EventDatabase
import com.example.submissionawalandroidfundamental.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        return EventRepository.getInstance(apiService, dao)
    }
}
package com.example.submissionawalandroidfundamental.di

import android.content.Context
import com.example.submissionawalandroidfundamental.data.EventRepository
import com.example.submissionawalandroidfundamental.data.local.room.EventDatabase
import com.example.submissionawalandroidfundamental.data.remote.retrofit.ApiConfig
import com.example.submissionawalandroidfundamental.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, dao, appExecutors)
    }
}
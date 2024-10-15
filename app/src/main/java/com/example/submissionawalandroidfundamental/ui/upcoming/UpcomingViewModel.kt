package com.example.submissionawalandroidfundamental.ui.upcoming

import androidx.lifecycle.ViewModel
import com.example.submissionawalandroidfundamental.data.EventRepository
import com.example.submissionawalandroidfundamental.data.local.entity.EventEntity

class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getUpcomingEvents(query: String?) = eventRepository.getUpcomingEvents(query)
    fun saveEvent(event: EventEntity) = eventRepository.setBookmarkedEvent(event, true)
    fun deleteEvent(event: EventEntity) = eventRepository.setBookmarkedEvent(event, false)
}
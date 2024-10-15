package com.example.submissionawalandroidfundamental.ui.finished

import androidx.lifecycle.ViewModel
import com.example.submissionawalandroidfundamental.data.EventRepository
import com.example.submissionawalandroidfundamental.data.local.entity.EventEntity

class FinishedViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getFinishedEvents(query: String?) = eventRepository.getFinishedEvents(query)
    fun saveEvent(event: EventEntity) = eventRepository.setBookmarkedEvent(event, true)
    fun deleteEvent(event: EventEntity) = eventRepository.setBookmarkedEvent(event, false)
}
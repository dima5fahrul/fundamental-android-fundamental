package com.example.submissionawalandroidfundamental.ui.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionawalandroidfundamental.data.EventRepository
import com.example.submissionawalandroidfundamental.data.local.entity.EventEntity
import kotlinx.coroutines.launch

class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getUpcomingEvents(query: String?) = eventRepository.getUpcomingEvents(query)

    fun saveEvent(eventEntity: EventEntity, state: Boolean) {
        viewModelScope.launch { eventRepository.setBookmarkedEvent(eventEntity, state) }
    }

    fun deleteEvent(eventEntity: EventEntity) {
        viewModelScope.launch { eventRepository.setBookmarkedEvent(eventEntity, false) }
    }
}
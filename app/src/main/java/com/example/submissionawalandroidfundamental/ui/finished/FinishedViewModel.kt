package com.example.submissionawalandroidfundamental.ui.finished

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionawalandroidfundamental.data.EventRepository
import com.example.submissionawalandroidfundamental.data.local.entity.EventEntity
import kotlinx.coroutines.launch

class FinishedViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getFinishedEvents(query: String?) = eventRepository.getFinishedEvents(query)

    fun saveEvent(eventEntity: EventEntity, state: Boolean) {
        viewModelScope.launch { eventRepository.setBookmarkedEvent(eventEntity, state) }
    }

    fun deleteEvent(eventEntity: EventEntity) {
        viewModelScope.launch { eventRepository.setBookmarkedEvent(eventEntity, false) }
    }
}
package com.example.submissionawalandroidfundamental.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionawalandroidfundamental.data.EventRepository
import com.example.submissionawalandroidfundamental.data.local.entity.EventEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getBookmarkedEvents() = eventRepository.getBookmarkedEvents()
    fun deleteEvent(eventEntity: EventEntity) {
        viewModelScope.launch { eventRepository.setBookmarkedEvent(eventEntity, false) }
    }
}
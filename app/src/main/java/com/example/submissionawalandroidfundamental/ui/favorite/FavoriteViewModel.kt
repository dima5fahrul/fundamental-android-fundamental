package com.example.submissionawalandroidfundamental.ui.favorite

import androidx.lifecycle.ViewModel
import com.example.submissionawalandroidfundamental.data.EventRepository
import com.example.submissionawalandroidfundamental.data.local.entity.EventEntity

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getBookmarkedEvents() = eventRepository.getBookmarkedEvents()
    fun deleteEvent(event: EventEntity) = eventRepository.setBookmarkedEvent(event, false)
}
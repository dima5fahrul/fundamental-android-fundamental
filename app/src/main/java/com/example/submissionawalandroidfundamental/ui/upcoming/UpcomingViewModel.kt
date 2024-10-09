package com.example.submissionawalandroidfundamental.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UpcomingViewModel {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Upcoming Event"
    }
    val text: LiveData<String> = _text
}
package com.example.submissionawalandroidfundamental.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionawalandroidfundamental.models.EventModel
import com.example.submissionawalandroidfundamental.utils.Constants
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class HomeViewModel : ViewModel() {
    private val _upcomingEvents = MutableLiveData<List<EventModel>>()
    val upcomingEvents: LiveData<List<EventModel>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<EventModel>>()
    val finishedEvents: LiveData<List<EventModel>> = _finishedEvents

    private val _isLoadingUpcoming = MutableLiveData<Boolean>()
    val isLoadingUpcoming: LiveData<Boolean> = _isLoadingUpcoming

    private val _isLoadingFinished = MutableLiveData<Boolean>()
    val isLoadingFinished: LiveData<Boolean> = _isLoadingFinished

    private val _errorUpcoming = MutableLiveData<String>()
    val errorUpcoming: LiveData<String> = _errorUpcoming

    private val _errorFinished = MutableLiveData<String>()
    val errorFinished: LiveData<String> = _errorFinished

    init {
        loadUpcomingEvents()
        loadFinishedEvents()
    }

    private fun loadUpcomingEvents() {
        _isLoadingUpcoming.value = true

        val client = AsyncHttpClient()
        client.get("${Constants.BASE_URL}?active=1", object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                _isLoadingUpcoming.value = false
                val listItems = ArrayList<EventModel>()
                val result = String(responseBody!!)

                try {
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("listEvents")

                    for (i in 0 until list.length()) {
                        val event = list.getJSONObject(i)
                        val eventModel = EventModel(
                            id = event.getLong("id"),
                            name = event.getString("name"),
                            summary = event.getString("summary"),
                            description = event.getString("description"),
                            imageLogo = event.getString("imageLogo"),
                            mediaCover = event.getString("mediaCover"),
                            category = event.getString("category"),
                            ownerName = event.getString("ownerName"),
                            cityName = event.getString("cityName"),
                            quota = event.getLong("quota"),
                            registrants = event.getLong("registrants"),
                            beginTime = event.getString("beginTime"),
                            endTime = event.getString("endTime"),
                            link = event.getString("link")
                        )
                        listItems.add(eventModel)
                    }
                    _upcomingEvents.postValue(listItems)
                } catch (e: Exception) {
                    _errorUpcoming.postValue(e.message)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                _isLoadingUpcoming.value = false
                _errorUpcoming.postValue(error?.message)
            }
        })
    }

    private fun loadFinishedEvents() {
        _isLoadingFinished.value = true

        val client = AsyncHttpClient()
        client.get("${Constants.BASE_URL}?active=0", object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                _isLoadingFinished.value = false
                val listItems = ArrayList<EventModel>()
                val result = String(responseBody!!)

                try {
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("listEvents")

                    for (i in 0 until list.length()) {
                        val event = list.getJSONObject(i)
                        val eventModel = EventModel(
                            id = event.getLong("id"),
                            name = event.getString("name"),
                            summary = event.getString("summary"),
                            description = event.getString("description"),
                            imageLogo = event.getString("imageLogo"),
                            mediaCover = event.getString("mediaCover"),
                            category = event.getString("category"),
                            ownerName = event.getString("ownerName"),
                            cityName = event.getString("cityName"),
                            quota = event.getLong("quota"),
                            registrants = event.getLong("registrants"),
                            beginTime = event.getString("beginTime"),
                            endTime = event.getString("endTime"),
                            link = event.getString("link")
                        )
                        listItems.add(eventModel)
                    }
                    _finishedEvents.postValue(listItems)
                } catch (e: Exception) {
                    _errorFinished.postValue(e.message)
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                _isLoadingFinished.value = false
                _errorFinished.postValue(error?.message)
            }
        })
    }
}
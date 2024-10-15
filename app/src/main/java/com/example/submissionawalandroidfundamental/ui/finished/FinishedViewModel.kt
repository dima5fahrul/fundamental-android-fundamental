package com.example.submissionawalandroidfundamental.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionawalandroidfundamental.models.EventModel
import com.example.submissionawalandroidfundamental.utils.Constants
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class FinishedViewModel : ViewModel() {
    private val _finishedEvents = MutableLiveData<MutableList<EventModel>>()
    val finishedEvents: LiveData<MutableList<EventModel>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    init {
        loadFinishedEvents(null)
    }

    fun loadFinishedEvents(query: String?) {
        _isLoading.value = true

        val client = AsyncHttpClient()
        val url = "${Constants.BASE_URL}?active=0" + (query?.let { "&q=$it" } ?: "")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                _isLoading.value = false
                val listFinishedEvent = ArrayList<EventModel>()
                val result = String(responseBody!!)

                try {
                    val response = JSONObject(result)
                    val jsonArray = response.getJSONArray("listEvents")

                    for (i in 0 until jsonArray.length()) {
                        val event = jsonArray.getJSONObject(i)

                        val finishedEvent = EventModel(
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
                        listFinishedEvent.add(finishedEvent)
                    }
                    if (listFinishedEvent.isEmpty()) _error.value =
                        "No data found" else _error.value = null
                    _finishedEvents.value = listFinishedEvent
                } catch (e: Exception) {
                    _isLoading.value = false
                    _error.value = e.message
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                _isLoading.value = false
                _error.value = error?.message
            }
        })
    }
}
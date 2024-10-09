package com.example.submissionawalandroidfundamental.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionawalandroidfundamental.models.FinishedEventModel
import com.example.submissionawalandroidfundamental.utils.Constants
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class FinishedViewModel : ViewModel() {
    private val _finishedEvents = MutableLiveData<MutableList<FinishedEventModel>>()
    val finishedEvents: LiveData<MutableList<FinishedEventModel>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadFinishedEvents() {
        _isLoading.value = true

        val client = AsyncHttpClient()
        client.get("${Constants.BASE_URL}?active=0", object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                _isLoading.value = false
                val listFinishedEvent = ArrayList<FinishedEventModel>()
                val result = String(responseBody!!)

                try {
                    val response = JSONObject(result)
                    val jsonArray = response.getJSONArray("listEvents")

                    for (i in 0 until jsonArray.length()) {
                        val event = jsonArray.getJSONObject(i)

                        val finishedEvent = FinishedEventModel(
                            event.getString("mediaCover"),
                            event.getString("name"),
                            convertedDate(event.getString("endTime")),
                            event.getString("cityName")
                        )

                        listFinishedEvent.add(finishedEvent)
                    }

                    _finishedEvents.value = listFinishedEvent
                } catch (e: Exception) {
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

    private fun convertedDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date!!)
    }
}
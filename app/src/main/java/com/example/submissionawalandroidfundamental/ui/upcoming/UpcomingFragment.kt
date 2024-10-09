package com.example.submissionawalandroidfundamental.ui.upcoming

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawalandroidfundamental.databinding.FragmentUpcomingBinding
import com.example.submissionawalandroidfundamental.models.UpcomingEventModel
import com.example.submissionawalandroidfundamental.utils.Constants
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private lateinit var binding: FragmentUpcomingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvUpcoming.layoutManager = LinearLayoutManager(context)
        getListUpcomingEvent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getListUpcomingEvent() {
        binding.progressBar.visibility = View.VISIBLE

        val client = AsyncHttpClient()

        client.get("${Constants.BASE_URL}?active=1", object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                binding.progressBar.visibility = View.INVISIBLE

                val listUpcomingEvent = ArrayList<UpcomingEventModel>()
                val result = String(responseBody!!)
                Log.d(TAG, result)

                try {
                    val response = JSONObject(result)
                    val jsonArray = response.getJSONArray("listEvents")

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val photo = jsonObject.getString("mediaCover")
                        val name = jsonObject.getString("name")
                        val beginTime = jsonObject.getString("beginTime")
                        val cityName = jsonObject.getString("cityName")

                        listUpcomingEvent.add(
                            UpcomingEventModel(
                                photo,
                                name,
                                convertDate(beginTime),
                                cityName
                            )
                        )
                    }

                    val adapter = UpcomingAdapter(listUpcomingEvent)
                    binding.rvUpcoming.adapter = adapter
                } catch (e: Exception) {
                    Log.d(TAG, "onSuccess: ${e.message}")
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                Log.e(TAG, "onFailure: ${error?.message}")
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }

                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun convertDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date!!)
    }
}
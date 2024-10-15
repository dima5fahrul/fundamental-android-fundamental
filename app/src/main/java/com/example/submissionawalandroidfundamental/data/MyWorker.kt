package com.example.submissionawalandroidfundamental.data

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.submissionawalandroidfundamental.BuildConfig
import com.example.submissionawalandroidfundamental.R
import com.example.submissionawalandroidfundamental.utils.DataHelper
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    companion object {
        private val TAG = MyWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"
    }

    private var resultStatus: Result? = null

    override fun doWork(): Result = getCurrentEvent()

    private fun getCurrentEvent(): Result {
        Log.d(TAG, "getCurrentEvent: Mulai.....")
        Looper.prepare()
        val client = SyncHttpClient()
        val url = BuildConfig.BASE_URL + "/events?active=1&limit=1"
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>?,
                response: JSONObject?
            ) {
                Log.d(TAG, "onSuccess: Berhasil.....")
                val result = response?.getJSONArray("listEvents")
                if (result != null) {
                    for (i in 0 until result.length()) {
                        val event = result.getJSONObject(i)
                        val eventName = event.getString("name")
                        val beginTime = DataHelper.convertDate(event.getString("beginTime"))
                        showNotification(eventName, beginTime)
                    }
                    resultStatus = Result.success()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>?,
                throwable: Throwable?,
                errorResponse: JSONObject?
            ) {
                Log.d(TAG, "onFailure: Gagal.....")
                resultStatus = Result.failure()
            }
        })

        return resultStatus as Result
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun showNotification(eventName: String, beginTime: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(eventName)
            .setContentText(beginTime)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}
// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json       = Json { allowStructuredMapKeys = true }
// val eventModel = json.parse(EventModel.serializer(), jsonString)

package com.example.submissionawalandroidfundamental.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventModel(
    val id: Long? = null,
    val name: String? = null,
    val summary: String? = null,
    val description: String? = null,
    val imageLogo: String? = null,
    val mediaCover: String? = null,
    val category: String? = null,
    val ownerName: String? = null,
    val cityName: String? = null,
    val quota: Long? = null,
    val registrants: Long? = null,
    val beginTime: String? = null,
    val endTime: String? = null,
    val link: String? = null,
    val isBookmarked: Boolean? = null
) : Parcelable
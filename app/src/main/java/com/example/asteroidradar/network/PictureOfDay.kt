package com.example.asteroidradar.network

import com.example.asteroidradar.database.PicturesByDay
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PictureOfTheDay(
    val date: String,
    val explanation: String,
    @Json(name = "hdurl") val hdUrl: String,
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "service_version") val serviceVersion: String,
    val title: String,
    val url: String
)

fun PictureOfTheDay.asDatabaseModel(): PicturesByDay {
    return PicturesByDay(
        date = this.date,
        explanation = this.explanation,
        hdUrl = this.hdUrl,
        mediaType = this.mediaType,
        serviceVersion = this.serviceVersion,
        title = this.title,
        url = this.url
    )
}
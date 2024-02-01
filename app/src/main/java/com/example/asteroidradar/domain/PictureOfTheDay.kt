package com.example.asteroidradar.domain

data class PictureOfTheDay(
    val id: Long,
    val url: String,
    val title: String?,
    val description: String?
)

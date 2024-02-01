package com.example.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.asteroidradar.domain.Asteroid
import com.example.asteroidradar.domain.PictureOfTheDay

@Entity(tableName = "asteroid_table")
data class AsteroidEntity(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

@Entity(tableName = "pictures_table")
data class PicturesByDay(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val explanation: String,
    val hdUrl: String,
    val mediaType: String,
    val serviceVersion: String,
    val title: String,
    val url: String
)


fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            absoluteMagnitude = it.absoluteMagnitude,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun PicturesByDay.asDomainModel(): PictureOfTheDay {
    return PictureOfTheDay(
        id = this.id,
        url = this.url,
        title = this.title,
        description = this.explanation)
}
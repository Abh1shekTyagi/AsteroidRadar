package com.example.asteroidradar.network

import com.example.asteroidradar.database.AsteroidEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NeoFeedResponse(
    val links: Links,
    @Json(name = "element_count") val elementCount: Int,
    @Json(name = "near_earth_objects") val nearEarthObjects: Map<String, List<NeoObject>>
)

@JsonClass(generateAdapter = true)
data class Links(
    val next: String,
    val previous: String,
    val self: String
)

@JsonClass(generateAdapter = true)
data class NeoObject(
    val links: SelfLink,
    val id: String,
    @Json(name = "neo_reference_id") val neoReferenceId: String,
    val name: String,
    @Json(name = "nasa_jpl_url") val nasaJplUrl: String,
    @Json(name = "absolute_magnitude_h") val absoluteMagnitudeH: Double,
    @Json(name = "estimated_diameter") val estimatedDiameter: EstimatedDiameter,
    @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardousAsteroid: Boolean,
    @Json(name = "close_approach_data") val closeApproachData: List<CloseApproachData>,
    @Json(name = "is_sentry_object") val isSentryObject: Boolean
)

@JsonClass(generateAdapter = true)
data class SelfLink(
    val self: String
)

@JsonClass(generateAdapter = true)
data class EstimatedDiameter(
    val kilometers: Diameter,
    val meters: Diameter,
    val miles: Diameter,
    val feet: Diameter
)

@JsonClass(generateAdapter = true)
data class Diameter(
    @Json(name = "estimated_diameter_min") val estimatedDiameterMin: Double,
    @Json(name = "estimated_diameter_max") val estimatedDiameterMax: Double
)

@JsonClass(generateAdapter = true)
data class CloseApproachData(
    @Json(name = "close_approach_date") val closeApproachDate: String,
    @Json(name = "close_approach_date_full") val closeApproachDateFull: String,
    @Json(name = "epoch_date_close_approach") val epochDateCloseApproach: Long,
    @Json(name = "relative_velocity") val relativeVelocity: Velocity,
    @Json(name = "miss_distance") val missDistance: MissDistance,
    @Json(name = "orbiting_body") val orbitingBody: String
)

@JsonClass(generateAdapter = true)
data class Velocity(
    @Json(name = "kilometers_per_second") val kilometersPerSecond: String,
    @Json(name = "kilometers_per_hour") val kilometersPerHour: String,
    @Json(name = "miles_per_hour") val milesPerHour: String
)

@JsonClass(generateAdapter = true)
data class MissDistance(
    val astronomical: String,
    val lunar: String,
    val kilometers: String,
    val miles: String
)

fun NeoFeedResponse.asDatabaseModel(): Array<AsteroidEntity> {
    val asteroidEntity = mutableListOf<AsteroidEntity>()
    nearEarthObjects.values.map { list ->
        list.map {
            asteroidEntity.add(
                AsteroidEntity(
                    id = it.id.toLong(),
                    codename = it.name,
                    absoluteMagnitude = it.absoluteMagnitudeH,
                    estimatedDiameter = it.estimatedDiameter.kilometers.estimatedDiameterMin,
                    closeApproachDate = it.closeApproachData[0].closeApproachDate,
                    relativeVelocity = it.closeApproachData[0].relativeVelocity.kilometersPerSecond.toDouble(),
                    distanceFromEarth = it.closeApproachData[0].missDistance.astronomical.toDouble(),
                    isPotentiallyHazardous = it.isPotentiallyHazardousAsteroid
                )
            )
        }
    }
    return asteroidEntity.toTypedArray()
}


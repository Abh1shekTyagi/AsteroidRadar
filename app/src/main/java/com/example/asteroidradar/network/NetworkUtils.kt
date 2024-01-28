package com.example.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(Constants.BASE_URL)
        .build()

    val asteroid: AsteroidApiService = retrofit.create(AsteroidApiService::class.java)
}

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidList(
        @Query("api_key")
        apiKey:String = API_KEY
    ): NeoFeedResponse
    @GET("planetary/apod")
    suspend fun getPicture(
        @Query("api_key")
        apiKey: String = API_KEY
    ): PictureOfTheDay
}

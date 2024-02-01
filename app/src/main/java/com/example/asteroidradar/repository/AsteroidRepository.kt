package com.example.asteroidradar.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.map
import com.example.asteroidradar.database.AsteroidDatabase
import com.example.asteroidradar.database.asDomainModel
import com.example.asteroidradar.domain.Asteroid
import com.example.asteroidradar.network.Network
import com.example.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.DEFAULT_END_DATE_DAYS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AsteroidRepository(private val database: AsteroidDatabase, private val context: Context) {
    private val calendar = Calendar.getInstance()
    private val currentTime = calendar.time

    @SuppressLint("WeekBasedYear")
    private val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    val asteroids = database.asteroidDao.getAsteroidsLiveData().map {
        it.asDomainModel()
    }

    val pictureOfTheDay = database.asteroidDao.getRecentPicture().map {
        it?.asDomainModel()
    }

    @SuppressLint("WeekBasedYear")
    suspend fun getAsteroids() {
        withContext(Dispatchers.IO) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetwork

            if (networkInfo != null) {
                val asteroidList = Network.asteroid.getAsteroidList()
                database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
                database.asteroidDao.deletePassedAsteroids(dateFormat.format(currentTime))
            }
        }
    }

    suspend fun getPictureOfDay() {
        withContext(Dispatchers.IO) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetwork

            if (networkInfo != null) {
                database.asteroidDao.insertPicture(Network.asteroid.getPicture().asDatabaseModel())
            }
        }
    }

    suspend fun getTodayAsteroids(): List<Asteroid> {
        return database.asteroidDao.getTodayAsteroids(dateFormat.format(currentTime))
            .asDomainModel()
    }

    suspend fun getHazardousAsteroids(): List<Asteroid> {
        return database.asteroidDao.getHazardousAsteroids().asDomainModel()
    }

    suspend fun getNotHazardousAsteroids(): List<Asteroid> {
        return database.asteroidDao.getNotHazardousAsteroids().asDomainModel()
    }

    suspend fun getAllAsteroids(): List<Asteroid> {
        return database.asteroidDao.getAllAsteroids().asDomainModel()
    }

    suspend fun getWeekAsteroids(): List<Asteroid> {
        val startDate = dateFormat.format(currentTime)
        val calendarInstance = calendar
        calendarInstance.add(Calendar.DAY_OF_MONTH, DEFAULT_END_DATE_DAYS)
        val endDate = dateFormat.format(calendarInstance.time)
        return database.asteroidDao.getWeekAsteroids(startDate, endDate).asDomainModel()
    }
}
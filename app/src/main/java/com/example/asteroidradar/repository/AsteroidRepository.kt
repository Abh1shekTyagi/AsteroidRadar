package com.example.asteroidradar.repository

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.map
import com.example.asteroidradar.database.AsteroidDatabase
import com.example.asteroidradar.database.asDomainModel
import com.example.asteroidradar.network.Network
import com.example.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AsteroidRepository(private val database: AsteroidDatabase, private val context: Context) {
    val asteroids = database.asteroidDao.getAll().map {
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
                val calendar = Calendar.getInstance()
                val currentTime = calendar.time
                val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
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
}
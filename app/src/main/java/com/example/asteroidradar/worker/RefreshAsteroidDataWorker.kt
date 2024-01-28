package com.example.asteroidradar.worker

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.asteroidradar.database.getDatabase
import com.example.asteroidradar.repository.AsteroidRepository

class RefreshAsteroidDataWorker(applicationContext: Context, params: WorkerParameters): CoroutineWorker(applicationContext,params) {
    companion object {
        const val WORK_NAME = "RefreshAsteroidWorker"
    }
    @SuppressLint("WeekBasedYear")
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database,applicationContext)
        return try {
            repository.getAsteroids()
            repository.getPictureOfDay()
            Result.success()
        }catch (exception: Exception){
            Result.retry()
        }
    }
}
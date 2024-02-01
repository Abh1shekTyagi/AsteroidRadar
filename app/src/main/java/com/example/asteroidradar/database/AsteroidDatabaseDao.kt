package com.example.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao {
    @Insert(onConflict = IGNORE)
    suspend fun insertAll(vararg asteroids: AsteroidEntity)

    @Query("Select * from asteroid_table order by closeApproachDate")
    fun getAsteroidsLiveData(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = IGNORE)
    suspend fun insertPicture(picturesOfDay: PicturesByDay)

    @Query("select * from pictures_table order by id desc limit 1")
    fun getRecentPicture(): LiveData<PicturesByDay?>

    @Query("delete from asteroid_table where closeApproachDate < :date")
    suspend fun deletePassedAsteroids(date: String)

    @Query("Select * from asteroid_table where isPotentiallyHazardous = 1")
    suspend fun getHazardousAsteroids(): List<AsteroidEntity>

    @Query("Select * from asteroid_table where isPotentiallyHazardous = 0")
    suspend fun getNotHazardousAsteroids(): List<AsteroidEntity>

    @Query("Select * from asteroid_table where closeApproachDate = :date")
    suspend fun getTodayAsteroids(date: String): List<AsteroidEntity>

    @Query("Select * from asteroid_table order by closeApproachDate")
    suspend fun getAllAsteroids(): List<AsteroidEntity>

}


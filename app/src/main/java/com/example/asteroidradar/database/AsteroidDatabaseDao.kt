package com.example.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertAll(vararg asteroids: AsteroidEntity)

    @Query("Select * from asteroid_table order by closeApproachDate")
    fun getAll():LiveData<List<AsteroidEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insertPicture(picturesOfDay: PicturesByDay)

    @Query("select * from pictures_table order by id desc limit 1")
    fun getRecentPicture(): LiveData<PicturesByDay?>

    @Query("delete from asteroid_table where closeApproachDate < :date")
    suspend fun deletePassedAsteroids( date: String)
}


package com.example.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AsteroidEntity::class, PicturesByDay::class], version = 1
)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDatabaseDao

//    companion object{
//        val migration_1_2 = object : Migration(1, 2) {
//            override fun migrate(db: SupportSQLiteDatabase) {
//                // Create the new table if it doesn't exist
//                db.execSQL("CREATE TABLE IF NOT EXISTS `pictures_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `date` TEXT NOT NULL, `explanation` TEXT NOT NULL, `hdUrl` TEXT NOT NULL, `mediaType` TEXT NOT NULL, `serviceVersion` TEXT NOT NULL, `title` TEXT NOT NULL, `url` TEXT NOT NULL)")
////
////                // Copy data from the old table to the new table
////                db.execSQL("INSERT INTO `pictures_table` (`id`, `date`, `explanation`, `hdUrl`, `mediaType`, `serviceVersion`, `title`, `url`) SELECT `id`, `date`, `explanation`, `hdUrl`, `mediaType`, `serviceVersion`, `title`, `url` FROM `OldTable`")
////
////                // Drop the old table
////                db.execSQL("DROP TABLE IF EXISTS `OldTable`")
//            }
//        }
//
//
//    }
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroid"
            ).build()
        }
    }
    return INSTANCE
}

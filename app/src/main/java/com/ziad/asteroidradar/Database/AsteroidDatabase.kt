package com.ziad.asteroidradar.Database

import android.content.Context
import androidx.room.*
import com.ziad.asteroidradar.Asteroid
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized


@Database(entities = [Asteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase(){
    abstract val astroDao: AstroDao
}

private lateinit var INSTANCE :AsteroidDatabase
@OptIn(InternalCoroutinesApi::class)
fun database(context: Context): AsteroidDatabase{
    synchronized(AsteroidDatabase::class.java){
        if (!::INSTANCE.isInitialized){
            INSTANCE=Room.databaseBuilder(context.applicationContext,AsteroidDatabase::class.java,
            "Database").build()
        }
    }
    return INSTANCE
}
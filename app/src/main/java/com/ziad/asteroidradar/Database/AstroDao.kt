package com.ziad.asteroidradar.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ziad.asteroidradar.Asteroid


@Dao
interface AstroDao {


    @androidx.room.Query("select * from asteroid ORDER BY closeApproachDate ASC")
    fun getAstro(): LiveData<List<Asteroid>>
    @androidx.room.Query("select * from asteroid WHERE closeApproachDate=:startDate ")
    fun getTodayAstro(startDate : String): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAstro(asteroids: ArrayList<Asteroid>)

    @androidx.room.Query("DELETE FROM asteroid")
    fun delete()
}

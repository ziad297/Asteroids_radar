package com.ziad.asteroidradar

import android.util.Log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ziad.asteroidradar.Constants.API_KEY
import com.ziad.asteroidradar.Database.AsteroidDatabase
import com.ziad.asteroidradar.api.NetworkRetrofit
import com.ziad.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DataRepo(private val database: AsteroidDatabase) {
    private val currentTime: LocalDateTime = LocalDateTime.now()
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT)
    private val startDate: String =currentTime.format(dateTimeFormatter)

    val astros : LiveData<List<Asteroid>> = database.astroDao.getAstro()
    val astrosToday : LiveData<List<Asteroid>> = database.astroDao.getTodayAstro(startDate)

    val POD= MutableLiveData<POD>()


    suspend fun GetAstro(startDate:String){

        withContext(Dispatchers.IO)
        {
           val Astro = parseAsteroidsJsonResult(
                JSONObject(
                    NetworkRetrofit
                        .asteroids
                        .getData(startDate,API_KEY)
                )
            )
            Log.i("Called arr size ",Astro.size.toString())

         database.astroDao.delete()
        database.astroDao.insertAstro(Astro)
        }

    }

    suspend fun GetPOD(){
        val POD= withContext(Dispatchers.IO){
            val POD= NetworkRetrofit.asteroids.getPictureOfDay(API_KEY)
            return@withContext POD
        }
        POD.let {
            this.POD.value=it
        }
    }
    fun CachePOD(url:String, title:String){
        POD.value= POD("image",title,url)
    }
}
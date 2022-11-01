package com.ziad.asteroidradar.main

import android.app.Application
import android.content.Context
import android.content.SharedPreferences


import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ziad.asteroidradar.Asteroid
import com.ziad.asteroidradar.Constants.API_QUERY_DATE_FORMAT

import com.ziad.asteroidradar.DataRepo
import com.ziad.asteroidradar.Database.database
import com.ziad.asteroidradar.POD
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import kotlin.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var filterMutableLiveData= MutableLiveData(Filter.ALL)

    private val asteroidDatabase= database(application)
    private val dataRepo= DataRepo(asteroidDatabase)
    private var preferences: SharedPreferences =application.getSharedPreferences("PodCache", Context.MODE_PRIVATE)
    private var PODCache: SharedPreferences.Editor =preferences.edit()

    private val currentTime: LocalDateTime =LocalDateTime.now()
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(API_QUERY_DATE_FORMAT)
    private val startDate: String =currentTime.format(dateTimeFormatter)


     val navigateToSelectedAsteroid =MutableLiveData<Asteroid?>()



    init {
        GetData()
    }

    fun astroDetails(asteroid: Asteroid) {
        navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAstroDetailsCompleted(){
        navigateToSelectedAsteroid.value=null
    }

    private fun GetData(){

        viewModelScope.launch {
            try {
                dataRepo.GetAstro(startDate)

            }
            catch (e: Exception){

                e.printStackTrace()
            }

            try {
                dataRepo.GetPOD()
                dataRepo.POD.value?.let { cachePOD(it) }
            }
            catch (e:Exception){
                preferences.getString("URL","")?.let {
                    dataRepo.CachePOD(it,
                        preferences.getString("Description","")!!
                    ) }
            }
        }
    }

    val Astro=dataRepo.astros
    val AstroToday=dataRepo.astrosToday
    val POD=dataRepo.POD

    private fun cachePOD(POD: POD){
        PODCache.putString("URL",POD.url)
        PODCache.putString("Description",POD.title)
        PODCache.apply()
    }


}
package com.ziad.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ziad.asteroidradar.Asteroid

class DetialsVM(asteroid: Asteroid, application: Application): AndroidViewModel(application) {

    val selectedAstro = MutableLiveData<Asteroid>()




    init {
        selectedAstro.value=asteroid
    }


}
package com.ziad.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ziad.asteroidradar.Asteroid
import java.lang.IllegalArgumentException

class DetailsVMfactory (
    private val asteroid: Asteroid,
    private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetialsVM::class.java)){
            return DetialsVM(asteroid,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
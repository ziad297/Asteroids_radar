package com.ziad.asteroidradar

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.DataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AstroApp: Application() {
    private val applicationScope= CoroutineScope(Dispatchers.Default)
    private fun delayInit()=applicationScope.launch {
        setupRecurringWork()
    }

    override fun onCreate() {
        super.onCreate()
        delayInit()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()
    val repeatingRequest= PeriodicWorkRequestBuilder<DataWorker>(1,TimeUnit.DAYS)
        .setConstraints(constraints)
        .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            DataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }

}
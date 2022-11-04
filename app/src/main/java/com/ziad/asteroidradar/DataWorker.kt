package com.ziad.asteroidradar

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ziad.asteroidradar.Constants
import com.ziad.asteroidradar.DataRepo
import com.ziad.asteroidradar.Database.database
import retrofit2.HttpException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DataWorker(appContext: Context, params: WorkerParameters):CoroutineWorker(appContext,params) {
    companion object{
        const val WORK_NAME="RefreshDataWorker"
    }

    private val current: LocalDateTime = LocalDateTime.now()
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT)
    private val startDate: String =current.format(formatter)
    override suspend fun doWork(): Result {
        val database= database(applicationContext)
        val repository= DataRepo(database)
        return try {
            repository.GetAstro(startDate)
            Result.success()
        }catch (e: HttpException){
            Result.retry()
        }
    }
}
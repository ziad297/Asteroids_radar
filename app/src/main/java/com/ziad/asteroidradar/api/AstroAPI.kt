package com.ziad.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ziad.asteroidradar.BuildConfig
import com.ziad.asteroidradar.Constants.BASE_URL
import com.ziad.asteroidradar.POD
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getData(
        @Query("start_date") startDate: String,
        @Query("api_key") apiKey: String
    ): String
    @GET("planetary/pod")
    suspend fun getPictureOfDay(
        @Query("api_key") apiKey: String
    ): POD
}


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object NetworkRetrofit {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(getClient())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private fun getClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }
        return httpClient.build()
    }

    val asteroids: ApiService = retrofit.create(ApiService::class.java)
}
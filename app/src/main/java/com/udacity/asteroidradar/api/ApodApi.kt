package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.domain.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Connect with the APOD web service of the Nasa API to retrieve the image of the day
 */

object ApodApi {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val moshiRetrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(Constants.BASE_URL)
        .build()

    interface ApodService {
        @GET("planetary/apod")
        suspend fun getImageOfTheDay(@Query("api_key") apiKey: String) : PictureOfDay
    }

    val apodService: ApodService by lazy {
        moshiRetrofit.create(ApodService::class.java)
    }
}
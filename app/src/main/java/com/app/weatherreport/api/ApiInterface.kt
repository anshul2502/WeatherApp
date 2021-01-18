package com.app.weatherreport.api

import androidx.viewbinding.BuildConfig
import com.app.weatherreport.weatherModelData.WeatherModelData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiInterface {

    //Api to get Data from service
    @GET("data/2.5/weather")
    fun getWeatherReport(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") appid:String ): Call<WeatherModelData>


    //Initializing Retrofit
    companion object{

        var BASE_URL = "http://api.openweathermap.org/"

        operator fun invoke(
        ) :  ApiInterface{

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor()
                    .apply {
                        level = if (BuildConfig.DEBUG)
                            HttpLoggingInterceptor.Level.BODY
                        else
                            HttpLoggingInterceptor.Level.NONE
                    })
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
        }
    }
}
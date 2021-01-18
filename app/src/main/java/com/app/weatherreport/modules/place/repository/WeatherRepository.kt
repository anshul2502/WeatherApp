package com.app.weatherreport.modules.place.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.weatherreport.api.ApiInterface
import com.app.weatherreport.api.Resource
import com.app.weatherreport.weatherModelData.WeatherModelData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository {

    //Api call to get data from the server
    fun getWeatherData(lat: Double, lon: Double, appid: String):LiveData<Resource<WeatherModelData>>{

        val  weatherResponse = MutableLiveData<Resource<WeatherModelData>> ()

        ApiInterface().getWeatherReport(lat,lon,appid).enqueue(object :Callback<WeatherModelData>{
            override fun onFailure(call: Call<WeatherModelData>, t: Throwable) {
                weatherResponse.value = Resource.error(null,t.message.toString())
            }

            override fun onResponse(call: Call<WeatherModelData>,response: Response<WeatherModelData>) {
                if(response.isSuccessful){
                    val res:WeatherModelData = response.body()!!
                    weatherResponse.value = Resource.success(res)
                }
            }
        })
        return weatherResponse
    }


}
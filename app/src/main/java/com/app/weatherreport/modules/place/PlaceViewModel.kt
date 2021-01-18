package com.app.weatherreport.modules.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.weatherreport.api.Resource
import com.app.weatherreport.modules.place.repository.WeatherRepository
import com.app.weatherreport.weatherModelData.WeatherModelData

class PlaceViewModel:ViewModel() {

    fun getWeatherReport(lat: Double, lon: Double,  appid: String) :LiveData<Resource<WeatherModelData>>{
        return WeatherRepository().getWeatherData(lat,lon,appid)
    }


}
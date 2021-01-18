package com.app.weatherreport.modules.place.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.weatherreport.api.Resource
import com.app.weatherreport.modules.place.repository.WeatherRepository
import com.app.weatherreport.weatherModelData.WeatherModelData

class PlaceViewModel(val applications: Application):AndroidViewModel(applications) {

    var data:LiveData<Resource<WeatherModelData>>?=null

    //Fetching Weather Report
    fun getWeatherReport(lat: Double, lon: Double){
        data = WeatherRepository(applications ).getWeatherData(lat,lon)
    }

    fun saveBookMark(lat: Double, lon: Double,city:String){
        WeatherRepository(applications).insertBookmark(lat,lon,city)
    }

}
package com.app.weatherreport.modules.place.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.weatherreport.api.ApiInterface
import com.app.weatherreport.api.Resource
import com.app.weatherreport.db.BookMarkedDataModel
import com.app.weatherreport.db.BookmarkDao
import com.app.weatherreport.db.BookmarkDataBase
import com.app.weatherreport.weatherModelData.WeatherModelData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository(application: Application) {

    private val bookmarkDao: BookmarkDao

    init {
        val bookmarkDataBase = BookmarkDataBase.invoke(application)
        bookmarkDao = bookmarkDataBase?.getBookmarkDao()!!
    }

    //inserting the data to database when user marked the place as book mark
    fun insertBookmark(lat:Double,lon:Double,city:String){
        CoroutineScope(IO).launch {
            val bookMarkedDataModel = BookMarkedDataModel(lat,lon,city)
            bookmarkDao.addPlace(bookMarkedDataModel)
        }

    }

    //Api call to get data from the server
    fun getWeatherData(lat: Double, lon: Double):LiveData<Resource<WeatherModelData>>{

        val  weatherResponse = MutableLiveData<Resource<WeatherModelData>> ()

        ApiInterface().getWeatherReport(lat,lon,"fae7190d7e6433ec3a45285ffcf55c86").enqueue(object :Callback<WeatherModelData>{
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
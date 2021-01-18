package com.app.weatherreport.modules.bookmark.respository

import android.app.Application
import androidx.lifecycle.LiveData
import com.app.weatherreport.db.BookMarkedDataModel
import com.app.weatherreport.db.BookmarkDao
import com.app.weatherreport.db.BookmarkDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class BookmarkRepository(application: Application) {

    private val bookmarkDao: BookmarkDao
    private val listLiveData: LiveData<List<BookMarkedDataModel>>

    init {
        val bookmarkDataBase = BookmarkDataBase.invoke(application)
        bookmarkDao = bookmarkDataBase?.getBookmarkDao()!!
        listLiveData = bookmarkDao.getBookmarkPlaces()

    }

    //Getting list of database
    fun getBookmarkDataList(): LiveData<List<BookMarkedDataModel>> {
        return listLiveData
    }

    //Adding default data to database
    fun setDefaultData(){
        CoroutineScope(IO).launch { bookmarkDao.addMultiplePlaces(*getBookmarkList().toTypedArray()) }
    }

    // Removing the data from database
    fun removeBookmark(bookMarkedDataModel: BookMarkedDataModel){
        CoroutineScope(IO).launch {
            bookmarkDao.deleteBookmark(bookMarkedDataModel)
        }
    }

    //Creating the default value to database
    private fun getBookmarkList():List<BookMarkedDataModel>{
        val bookmarkList: MutableList<BookMarkedDataModel> = mutableListOf()
        bookmarkList.add(getBookmarkData(28.6600,77.2300,"Delhi"))
        bookmarkList.add(getBookmarkData(18.9667,72.8333,"Mumbai"))
        bookmarkList.add(getBookmarkData(22.5411,88.3378,"Kolkata"))
        bookmarkList.add(getBookmarkData(12.9699,77.5980,"Bangalore"))
        bookmarkList.add(getBookmarkData(13.0825,80.2750,"Chennai"))
        bookmarkList.add(getBookmarkData(17.3667,78.4667,"Hyderabad"))
        bookmarkList.add(getBookmarkData(18.5196,73.8553,"Pune"))
        bookmarkList.add(getBookmarkData(23.0300,72.5800,"Ahmadabad"))
        bookmarkList.add(getBookmarkData(26.8470,80.9470,"Lucknow"))
        bookmarkList.add(getBookmarkData(26.9167,75.8667,"Jaipur"))

        return bookmarkList
    }

    private fun getBookmarkData(lat:Double,lon:Double,place:String):BookMarkedDataModel{
        return BookMarkedDataModel(lat,lon,place)
    }

}
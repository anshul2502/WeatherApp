package com.app.weatherreport.modules.bookmark.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.app.weatherreport.modules.bookmark.respository.BookmarkRepository
import com.app.weatherreport.db.BookMarkedDataModel

class BookmarkViewModel(application: Application): AndroidViewModel(application) {

    private val bookmarkRepository: BookmarkRepository = BookmarkRepository(application)
    internal val getBookmarkList: LiveData<List<BookMarkedDataModel>>

    init {
        //Loading default List
        getBookmarkList = bookmarkRepository.getBookmarkDataList()
    }

    //Loading default data
    fun loadDefaultData(){
        bookmarkRepository.setDefaultData()
    }

    //Deleting value from database
    fun deleteBookmark(bookMarkedDataModel: BookMarkedDataModel){
        bookmarkRepository.removeBookmark(bookMarkedDataModel)
    }


}
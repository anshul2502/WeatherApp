package com.app.weatherreport.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookmarkDao {

    @Insert
    suspend fun addPlace(bookMarkedDataModel: BookMarkedDataModel)

    @Query("Select * from bookmarks")
    fun getBookmarkPlaces(): LiveData<List<BookMarkedDataModel>>

    @Insert
    suspend fun addMultiplePlaces(vararg bookMarkedLocations: BookMarkedDataModel)

    @Delete
    suspend fun deleteBookmark(bookMarkedDataModel: BookMarkedDataModel)

}
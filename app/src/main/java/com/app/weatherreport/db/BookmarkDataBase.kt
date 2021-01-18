package com.app.weatherreport.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
//Initializing the data
@Database(entities = [BookMarkedDataModel::class],version = 1)
abstract class BookmarkDataBase:RoomDatabase() {

    abstract fun getBookmarkDao():BookmarkDao

    companion object {
        @Volatile private var INSTANCE: BookmarkDataBase? = null

        operator fun invoke(context: Context): BookmarkDataBase? {
            if (INSTANCE == null) {
                synchronized(BookmarkDataBase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            BookmarkDataBase::class.java, "weather.db"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }

}
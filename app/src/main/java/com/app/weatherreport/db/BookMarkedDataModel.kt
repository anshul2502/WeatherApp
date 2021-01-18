package com.app.weatherreport.db

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "bookmarks")
data class BookMarkedDataModel(


    @ColumnInfo(name = "lat")
    val latitude:Double,

    @ColumnInfo(name = "lon")
    val longitude:Double,

    @ColumnInfo(name = "city")
    val place:String
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    var id:Int = 0
}
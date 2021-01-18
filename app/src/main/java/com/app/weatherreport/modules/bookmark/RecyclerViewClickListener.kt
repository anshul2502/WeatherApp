package net.simplifiedcoding.ui.movies

import android.view.View
import com.app.weatherreport.db.BookMarkedDataModel

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClick(view: View, dataModel: BookMarkedDataModel)
}
package com.app.weatherreport.modules.bookmark.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.weatherreport.R
import com.app.weatherreport.databinding.BookmarkViewBinding
import com.app.weatherreport.db.BookMarkedDataModel
import net.simplifiedcoding.ui.movies.RecyclerViewClickListener
import java.util.*
import kotlin.collections.ArrayList

class BookmarksAdapter( private val listener: RecyclerViewClickListener): RecyclerView.Adapter<BookmarksAdapter.BookmarksViewHolder>() {

    private var dataModelList: List<BookMarkedDataModel>?= null

    //Creating viewholder for recyclerview
    inner class BookmarksViewHolder(
        val bookmarkViewBinding: BookmarkViewBinding
    ) : RecyclerView.ViewHolder(bookmarkViewBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BookmarksViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.bookmark_view,
                parent,
                false
            )
        )

    override fun getItemCount(): Int {
        if(dataModelList.isNullOrEmpty()){
            return 0
        }
        return dataModelList!!.size
    }

    override fun onBindViewHolder(holder: BookmarksViewHolder, position: Int) {
        dataModelList?.let {
            holder.bookmarkViewBinding.bookmarkViewBinding = it.get(index = position)

            //On click of button passing the view and data to main class
            holder.bookmarkViewBinding.btReport.setOnClickListener {
                listener.onRecyclerViewItemClick(holder.bookmarkViewBinding.btReport,dataModelList!!.get(index = position))
            }

            //Same event functionality as above
            holder.bookmarkViewBinding.btRemove.setOnClickListener {
                listener.onRecyclerViewItemClick(holder.bookmarkViewBinding.btRemove, dataModelList!!.get(index = position))
            }
        }


    }

    //updating the view by calling notifyDataSetChanged
    internal fun setBookmark(bookmarkList: List<BookMarkedDataModel>) {
        this.dataModelList = bookmarkList
        notifyDataSetChanged()
    }



}
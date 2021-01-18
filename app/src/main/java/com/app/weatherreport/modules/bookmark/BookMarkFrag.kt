package com.app.weatherreport.modules.bookmark

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.weatherreport.BaseFragment
import com.app.weatherreport.R
import com.app.weatherreport.databinding.BookmarkBinding
import com.app.weatherreport.db.BookMarkedDataModel
import com.app.weatherreport.modules.bookmark.adapter.BookmarksAdapter
import com.app.weatherreport.modules.bookmark.viewmodel.BookmarkViewModel
import net.simplifiedcoding.ui.movies.RecyclerViewClickListener

class BookMarkFrag:BaseFragment(),RecyclerViewClickListener {

    private lateinit var bookmarkBinding:BookmarkBinding
    private lateinit var mContext: Context
    private var views: View? = null
    private lateinit var bookmarkViewModel: BookmarkViewModel
    private lateinit var adapter:BookmarksAdapter
    private var navController: NavController?= null

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(views==null){
           // views = inflater.inflate(R.layout.bookmark, container, false)
            bookmarkBinding= DataBindingUtil.inflate(layoutInflater, R.layout.bookmark, container, false)
            views = bookmarkBinding.root
            setHasOptionsMenu(false)
        }
        return views
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        // Setting up the recyclerview & call data from database
        bookmarkBinding.rvBookmarks.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        bookmarkBinding.rvBookmarks.setHasFixedSize(true)
         adapter = BookmarksAdapter(this@BookMarkFrag)
        bookmarkBinding.rvBookmarks.adapter = adapter

        bookmarkViewModel = ViewModelProvider(this).get(BookmarkViewModel::class.java)
        bookmarkViewModel.getBookmarkList.observe(viewLifecycleOwner, Observer { dataList->

            //When list becomes empty, default data will be loaded automatically
            if(dataList.isNullOrEmpty()){
                bookmarkViewModel.loadDefaultData()
            }else{
                adapter.setBookmark(dataList)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    //Receiving the value of click event from the list
    override fun onRecyclerViewItemClick(view: View, dataModel: BookMarkedDataModel) {
        when(view.id){
            R.id.btReport -> {
                val bundle = bundleOf("city_data" to dataModel)
                navController!!.navigate(R.id.viewPlacesFrag,bundle)
            }
            R.id.btRemove ->{
                deleteBookmark(dataModel)
            }
        }
    }


    private fun deleteBookmark(dataModel: BookMarkedDataModel){
        AlertDialog.Builder(mContext).apply {
            setTitle("Warning")
            setMessage("Are you sure you want to remove this bookmark?")
            setPositiveButton(" Yes "){_,_->
                bookmarkViewModel.deleteBookmark(dataModel)
            }
            setNegativeButton(" No "){ dialog,_->
                dialog.dismiss()
            }
        }.create().show()
    }



}
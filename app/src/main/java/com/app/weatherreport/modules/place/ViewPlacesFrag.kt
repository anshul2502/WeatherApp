package com.app.weatherreport.modules.place

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.weatherreport.R
import com.app.weatherreport.api.Status
import com.app.weatherreport.databinding.PlacesBinding
import com.app.weatherreport.db.BookMarkedDataModel
import com.app.weatherreport.modules.place.viewmodel.PlaceViewModel
import com.app.weatherreport.weatherModelData.WeatherModelData
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.places.*


class ViewPlacesFrag:Fragment(), OnMapReadyCallback {

    private lateinit var mGoogleMap:GoogleMap
    private lateinit var mContext: Context
    private var views: View? = null
    private var cityData: BookMarkedDataModel?=null
    private lateinit var placeViewModel: PlaceViewModel
    private lateinit var plaveViewBinding:PlacesBinding
    private lateinit var mWeatherModelData:WeatherModelData


    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cityData = requireArguments().getParcelable("city_data")!!
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mvPlaces.onCreate(savedInstanceState)
        mvPlaces.onResume()
        mvPlaces.getMapAsync(this)

        //Initializing the view model
        placeViewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)

        //Click Event for adding the bookmark
        plaveViewBinding.btAddBookmark.setOnClickListener {
            val lat = getWeatherModelData().coord.lat
            val lng = getWeatherModelData().coord.lon
            val city = getWeatherModelData().name
            addBookmark(lat,lng,city)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.info -> {
                infoMessage()
                true
            }
            /*R.id.action_clear_log -> {
                Toast.makeText(activity, "Clear call log", Toast.LENGTH_SHORT).show()
                true
            }*/
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(views==null){
            plaveViewBinding= DataBindingUtil.inflate(layoutInflater, R.layout.places, container, false)
            views = plaveViewBinding.root
            setHasOptionsMenu(true)
        }
        return views
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.let {
            //Initiazing
            mGoogleMap = it

            //Default Value
            var lat = -34.0
            var lon = 151.0
            var cityName = "Sydney"

            if(cityData!=null){
                lat = cityData!!.latitude
                lon = cityData!!.longitude
                cityName = cityData!!.place
            }

            val city = LatLng(lat, lon)
            mGoogleMap.addMarker(
                MarkerOptions()
                .position(city)
                .title("Marker in $cityName"))
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(city))
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(city, 10F), 2000, null)

            //Click event for google map
            mGoogleMap.setOnMapClickListener {
                //Clearing the marking
                mGoogleMap.clear()
                val marker =  MarkerOptions().position(LatLng(it.latitude, it.longitude)) .title("New Place")
                mGoogleMap.addMarker(marker)

                //Creating the network request for the update lat lng value
                loadData(it.latitude,it.longitude,isUserTapped = true)

            }

            //Creating the network request
            loadData(lat,lon, isUserTapped = false)

        }
    }

    private fun loadData(lat:Double,lon:Double,isUserTapped:Boolean){
        placeViewModel.getWeatherReport(lat,lon)
        placeViewModel.data?.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS-> {

                    plaveViewBinding.llForecastDetail.visibility = View.VISIBLE
                    plaveViewBinding.tvStatus.visibility = View.GONE

                    //Showing the button if user has tapped on googlemap
                    if(isUserTapped){
                        plaveViewBinding.btAddBookmark.visibility = View.VISIBLE
                    }
                    //Setting the required data to view
                    it.data?.let { it1 -> setData(it1) }
                }
                Status.LOADING-> {
                    plaveViewBinding.llForecastDetail.visibility = View.GONE
                    plaveViewBinding.tvStatus.visibility = View.VISIBLE
                    plaveViewBinding.tvStatus.text = getString(R.string.str_wait)
                }
                Status.ERROR->{
                    plaveViewBinding.llForecastDetail.visibility = View.GONE
                    plaveViewBinding.tvStatus.visibility = View.VISIBLE
                    val strMessage = "Error: ${it.message}"
                    plaveViewBinding.tvStatus.text = strMessage
                }
            }
        })

    }


    private fun getWeatherModelData():WeatherModelData{
        return mWeatherModelData
    }

    private fun setWeatherModelData(weatherModelData:WeatherModelData){
        mWeatherModelData = weatherModelData
    }

    //Setting the data to view
    private fun setData(weatherModelData:WeatherModelData){

        plaveViewBinding.tvCountryName.text = getString(R.string.country_name,weatherModelData.sys.country)
        plaveViewBinding.tvName.text = getString(R.string.place_name,weatherModelData.name)
        plaveViewBinding.tvWeatherDesc.text = getString(R.string.description,weatherModelData.weather[0].description)
        plaveViewBinding.tvTempLow.text = getString(R.string.min_temperature,weatherModelData.main.temp_min.toString())
        plaveViewBinding.tvTempMax.text = getString(R.string.max_temperature,weatherModelData.main.temp_max.toString())
        plaveViewBinding.tvHumidity.text = getString(R.string.humidity,weatherModelData.main.humidity.toString())
        plaveViewBinding.tvSpeed.text = getString(R.string.wind_speed,weatherModelData.wind.speed.toString())

        //Setting the current data to use it globally
        setWeatherModelData(weatherModelData)
    }

    //Adding bookmark when user tap on screen
    private fun addBookmark(lat:Double,lng:Double,city:String){
        AlertDialog.Builder(mContext).apply {
            setMessage("Are you sure you want to add this to your bookmark?")
            setPositiveButton(" Yes "){_,_->
                //Saving the data to the database
                placeViewModel.saveBookMark(lat,lng,city)
                Toast.makeText(mContext, "Bookmark Added Successfully", Toast.LENGTH_SHORT).show()
            }
            setNegativeButton(" No "){ dialog,_->
                dialog.dismiss()
            }
        }.create().show()
    }

    //Info Message Dialog
    private fun infoMessage(){
        AlertDialog.Builder(mContext).apply {
            setMessage("If you want to add bookmark, please tap on google map screen. Also data will be loaded accordingly when user tap on map.")

            setNegativeButton(" OK "){ dialog,_->
                dialog.dismiss()
            }
        }.create().show()
    }


}
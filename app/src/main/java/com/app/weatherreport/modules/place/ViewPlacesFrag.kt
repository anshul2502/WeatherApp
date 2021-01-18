package com.app.weatherreport.modules.place

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.weatherreport.R
import com.app.weatherreport.api.Status
import com.app.weatherreport.databinding.PlacesBinding
import com.app.weatherreport.db.BookMarkedDataModel
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

        placeViewModel = ViewModelProvider(this).get(PlaceViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(views==null){
            plaveViewBinding= DataBindingUtil.inflate(layoutInflater, R.layout.places, container, false)
            views = plaveViewBinding.root
        }
        return views
    }

    override fun onMapReady(p0: GoogleMap?) {
        p0?.let {
            mGoogleMap = it

            var lat:Double = -34.0
            var lon:Double = 151.0
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
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(city, 10F), 2000, null);

            //Creating the network request
            loadData(lat,lon,cityName)

        }
    }

    private fun loadData(lat:Double,lon:Double,city:String){
        placeViewModel.getWeatherReport(lat,lon,"fae7190d7e6433ec3a45285ffcf55c86").observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS-> {
                    plaveViewBinding.llForecastDetail.visibility = View.VISIBLE
                    plaveViewBinding.tvStatus.visibility = View.GONE
                    it.data?.let { it1 -> setData(it1) }
                }
                Status.LOADING-> {
                    plaveViewBinding.llForecastDetail.visibility = View.GONE
                    plaveViewBinding.tvStatus.visibility = View.VISIBLE
                    plaveViewBinding.tvStatus.text = getString(R.string.str_wait)
                }
                Status.ERROR->{
                    plaveViewBinding.llForecastDetail.visibility = View.GONE
                    plaveViewBinding.tvStatus.visibility = View.GONE
                }
            }
        })

    }

    private fun setData(weatherModelData:WeatherModelData){

        plaveViewBinding.tvCountryName.text = getString(R.string.country_name,weatherModelData.sys.country)
        plaveViewBinding.tvName.text = getString(R.string.place_name,weatherModelData.name)
        plaveViewBinding.tvWeatherDesc.text = getString(R.string.description,weatherModelData.weather.get(0).description)
        plaveViewBinding.tvTempLow.text = getString(R.string.min_temperature,weatherModelData.main.temp_min.toString())
        plaveViewBinding.tvTempMax.text = getString(R.string.max_temperature,weatherModelData.main.temp_max.toString())
        plaveViewBinding.tvHumidity.text = getString(R.string.humidity,weatherModelData.main.humidity.toString())
        plaveViewBinding.tvSpeed.text = getString(R.string.wind_speed,weatherModelData.wind.speed.toString())

    }



}
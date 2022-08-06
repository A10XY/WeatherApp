package com.ahmed.weather_app.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmed.weather_app.ui.adapters.DaysAdapter
import com.ahmed.weather_app.ui.adapters.MoreInfoAdapter
import com.ahmed.weather_app.R
import com.ahmed.weather_app.data.DaysInfo
import com.ahmed.weather_app.data.MoreInfo
import com.ahmed.weather_app.data.db.InfoTableData
import com.ahmed.weather_app.data.repository.Repository
import com.ahmed.weather_app.databinding.ActivityMainBinding
import com.ahmed.weather_app.util.*
import okhttp3.*

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var client: OkHttpClient
    private var accessCoarseLocationGranted = false
    private var currentLocation: String? = null

    private val locationPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                accessCoarseLocationGranted = true
                shortToast(this, "Location permission granted!")
                getCurrentLocation()
            }
            else -> {
                shortToast(this, "Location permission not granted!")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val dataList = Repository(this).getAllDataFromInfoTable()
        binding.pbAll.isVisible = true
        if (dataList.isNotEmpty()) {
            setUpRecyclerView(dataList)
            setUpCurrentLocation(dataList)
        } else {
            getCurrentLocation()
        }
        setContentView(binding.root)
    }

    private fun makeGetRequest() {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host("api.tomorrow.io")
            .addPathSegment("v4")
            .addPathSegment("timelines")
            .addQueryParameter("location", currentLocation!!)
            .addQueryParameter("fields", "temperature,windSpeed,humidity,visibility,sunriseTime,sunsetTime,weatherCode")
            .addQueryParameter("timesteps", "1d")
            .addQueryParameter("units", "metric")
            .addQueryParameter("apikey", "TJEtMJS9JkZJp3YBy4qtEAhLZdYC0jIr")
            .addQueryParameter("startTime", getCurrentTime("yyyy-MM-dd'T'HH:mm:ss"))
            .addQueryParameter("endTime", getTimeAfterOneWeekFromStartTime("yyyy-MM-dd'T'HH:mm:ss"))
            .addQueryParameter("timeZone", getTimeZone())
            .build()

        val request = Request.Builder().url(url).build()
        client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                Log.d(TAG, "Failed: ${e.message}")
                shortToast(this@MainActivity, "Failed, check your internet connection!")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "Success!")
                response.body?.string()?.let { jsonString ->
                    val infoResponses = JsonParser().parseJsonResponseToInfoResponse(jsonString)
                    val address = Geocoder(this@MainActivity).getFromLocation(currentLocation!!.split(',')[0].toDouble(), currentLocation!!.split(',')[1].toDouble(), 1)[0]
                    val dataList = Repository(this@MainActivity).getAllDataFromInfoTable()
                    if (dataList.isEmpty()) {
                        Log.d(TAG, "Inserting into DB")
                        infoResponses.forEach {
                            Repository(this@MainActivity).insertDataIntoInfoTable(
                                dayName = getDayName(it.day),
                                time = getCurrentTime("dd/MM/yyyy HH:mm"),
                                location = "${address.countryName}, ${address.adminArea}",
                                temperature = it.temperature,
                                humidity = it.humidity,
                                windSpeed = it.windSpeed,
                                visibility = it.visibility,
                                sunriseTime = it.sunriseTime,
                                sunsetTime = it.sunsetTime,
                                weatherCode = it.weatherCode
                            )
                        }
                        runOnUiThread {
                            setUpRecyclerView(dataList)
                            setUpCurrentLocation(dataList)
                        }
                    } else {
                        Log.d(TAG, "Updating DB")
                        var id = 0
                        infoResponses.forEach {
                            Repository(this@MainActivity).updateDataInInfoTableById(
                                id = id++.toLong(),
                                dayName = getDayName(it.day),
                                time = getCurrentTime("dd/MM/yyyy HH:mm"),
                                location = "${address.countryName}, ${address.adminArea}",
                                temperature = it.temperature,
                                humidity = it.humidity,
                                windSpeed = it.windSpeed,
                                visibility = it.visibility,
                                sunriseTime = it.sunriseTime,
                                sunsetTime = it.sunsetTime,
                                weatherCode = it.weatherCode
                            )
                        }
                        val updatedDataList = Repository(this@MainActivity).getAllDataFromInfoTable()
                        runOnUiThread {
                            setUpRecyclerView(updatedDataList)
                            setUpCurrentLocation(updatedDataList)
                        }
                    }
                }
            }
        })
    }

    private fun setUpCurrentLocation(dataList: List<InfoTableData>) {
        binding.run {
            tvTemperature.text = "${dataList[0].temperature} C°"
            tvLocation.text = dataList[0].location
            tvDatetime.text = dataList[0].time
            tvWeatherStatus.text = showWeatherStatusDependingOnWeatherCode(dataList[0].weatherCode)
            ivDayOrNight.setImageResource(getImageIconDependOnWeatherCode(dataList[0].weatherCode))
            tvUpdate.setOnClickListener {
                pbAll.isVisible = true
                cvCurrentLocation.visibility = View.INVISIBLE
                cvTimes.visibility = View.INVISIBLE
                cvMoreInfo.visibility = View.INVISIBLE
                getCurrentLocation()
            }
            pbAll.isVisible = false
            cvCurrentLocation.visibility = View.VISIBLE
            cvTimes.visibility = View.VISIBLE
            cvMoreInfo.visibility = View.VISIBLE
        }
    }

    private fun setUpRecyclerView(dataList: List<InfoTableData>) {
        val list = listOf(
            MoreInfo("Sunrise", getTimeFromDate(dataList[0].sunriseTime), R.drawable.ic_sunrise),
            MoreInfo("Sunset", getTimeFromDate(dataList[0].sunsetTime), R.drawable.ic_sunset),
            MoreInfo("Wind Speed", "${dataList[0].windSpeed} m/s", R.drawable.ic_wind_speed),
            MoreInfo("Humidity", "${dataList[0].humidity} %", R.drawable.ic_humidity),
            MoreInfo("Visibility", "${dataList[0].visibility} km", R.drawable.ic_visibility),
        )
        binding.rvMoreInfo.adapter = MoreInfoAdapter(list)
        val timesList = listOf(
            DaysInfo(getImageIconDependOnWeatherCode(dataList[1].weatherCode), dataList[1].dayName, "${dataList[1].temperature} C°"),
            DaysInfo(getImageIconDependOnWeatherCode(dataList[2].weatherCode), dataList[2].dayName, "${dataList[2].temperature} C°"),
            DaysInfo(getImageIconDependOnWeatherCode(dataList[3].weatherCode), dataList[3].dayName, "${dataList[3].temperature} C°"),
            DaysInfo(getImageIconDependOnWeatherCode(dataList[4].weatherCode), dataList[4].dayName, "${dataList[4].temperature} C°"),
            DaysInfo(getImageIconDependOnWeatherCode(dataList[5].weatherCode), dataList[5].dayName, "${dataList[5].temperature} C°"),
            DaysInfo(getImageIconDependOnWeatherCode(dataList[6].weatherCode), dataList[6].dayName, "${dataList[6].temperature} C°"),
        )
        binding.rvTimes.run {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = DaysAdapter(timesList)
        }
    }

    private fun getCurrentLocation() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                requestLocationPermission()
                return
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f
            ) {
                if (currentLocation == null) {
                    currentLocation = "${it.latitude},${it.longitude}"
                    makeGetRequest()
                }
            }
        } else {
            shortToast(this, "You must turn location on!")
        }
    }

    private fun requestLocationPermission() = locationPermissionRequest.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "MAIN_ACTIVITY_TAG"
    }
}
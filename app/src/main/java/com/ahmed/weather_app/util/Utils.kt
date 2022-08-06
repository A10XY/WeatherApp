package com.ahmed.weather_app.util

import android.content.Context
import android.widget.Toast
import com.ahmed.weather_app.R
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

fun shortToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun getTimeZone(): String {
    return TimeZone.getDefault().id
}

fun getDayName(date: String): String {
    return SimpleDateFormat("EEEE", Locale.ENGLISH).format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).parse(date)!!)
}

fun getCurrentTime(pattern: String): String {
    return SimpleDateFormat(pattern, Locale.ENGLISH).format(Calendar.getInstance().time)
}

fun getTimeAfterOneWeekFromStartTime(pattern: String): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_MONTH, 7)
    return SimpleDateFormat(pattern, Locale.ENGLISH).format(calendar.time)
}

fun showWeatherStatusDependingOnWeatherCode(weatherCode: String): String {
    return when (weatherCode) {
        "1000", "1100" -> "Clear"
        "1001", "1101", "1102" -> "Cloudy"
        "2000", "2100" -> "Fog"
        "3000", "3001", "3002" -> "Windy"
        "4000", "4001", "4200", "4201" -> "Rainy"
        "5000", "5001", "5100", "5101" -> "Snow"
        "6000", "6001", "6200", "6201" -> "Freezing"
        "7000", "7101", "7102" -> "Ice"
        "8000" -> "Thunder Storm"
        else -> "Unknown"
    }
}

fun getTimeFromDate(date:String): String {
    return SimpleDateFormat("HH:mm", Locale.ENGLISH).format(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH).parse(date)!!)
}

fun getImageIconDependOnWeatherCode(weatherCode: String): Int {
    return when (showWeatherStatusDependingOnWeatherCode(weatherCode)) {
        "Clear" -> R.drawable.ic_sun
        "Cloudy" -> R.drawable.ic_cloudy
        "Fog" -> R.drawable.ic_fog
        "Windy" -> R.drawable.ic_wind_speed
        "Rainy" -> R.drawable.ic_rainy
        "Snow", "Freezing", "Ice", "Thunder Storm" -> R.drawable.ic_snow
        else -> 0
    }
}
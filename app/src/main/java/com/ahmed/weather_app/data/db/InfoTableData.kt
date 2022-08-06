package com.ahmed.weather_app.data.db

data class InfoTableData(
    val id: Int,
    val dayName: String,
    val time: String,
    val location: String,
    val temperature: String,
    val humidity: String,
    val visibility: String,
    val windSpeed: String,
    val sunriseTime: String,
    val sunsetTime: String,
    val weatherCode: String,
)

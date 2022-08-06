package com.ahmed.weather_app.data

data class InfoResponse(
    val day: String,
    val temperature: String,
    val humidity: String,
    val visibility: String,
    val windSpeed: String,
    val sunriseTime: String,
    val sunsetTime: String,
    val weatherCode: String
)

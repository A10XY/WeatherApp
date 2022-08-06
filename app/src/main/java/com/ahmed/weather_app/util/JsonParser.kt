package com.ahmed.weather_app.util

import com.ahmed.weather_app.data.InfoResponse
import org.json.JSONObject

class JsonParser {
    fun parseJsonResponseToInfoResponse(jsonString: String): List<InfoResponse> {
        val list = mutableListOf<InfoResponse>()
        val intervalsArray = JSONObject(jsonString).getJSONObject("data").getJSONArray("timelines").getJSONObject(0).getJSONArray("intervals")
        for (i in 0 until intervalsArray.length()) {
            list.add(
                InfoResponse(
                    day = intervalsArray.getJSONObject(i).getString("startTime"),
                    temperature = intervalsArray.getJSONObject(i).getJSONObject("values").getString("temperature"),
                    humidity = intervalsArray.getJSONObject(i).getJSONObject("values").getString("humidity"),
                    visibility = intervalsArray.getJSONObject(i).getJSONObject("values").getString("visibility"),
                    windSpeed = intervalsArray.getJSONObject(i).getJSONObject("values").getString("windSpeed"),
                    sunriseTime = intervalsArray.getJSONObject(i).getJSONObject("values").getString("sunriseTime"),
                    sunsetTime = intervalsArray.getJSONObject(i).getJSONObject("values").getString("sunsetTime"),
                    weatherCode = intervalsArray.getJSONObject(i).getJSONObject("values").getString("weatherCode"),
                )
            )
        }
        return list
    }
}
package com.ahmed.weather_app.data.repository

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.ahmed.weather_app.data.InfoResponse
import com.ahmed.weather_app.data.db.DbHelper
import com.ahmed.weather_app.data.db.InfoTable
import com.ahmed.weather_app.data.db.InfoTableData

class Repository(private val context: Context) {
    fun insertDataIntoInfoTable(
        dayName: String,
        time: String,
        location: String,
        temperature: String,
        humidity: String,
        windSpeed: String,
        visibility: String,
        sunriseTime: String,
        sunsetTime: String,
        weatherCode: String
    ) {
        val dbHelper = DbHelper(context = context)
        val newEntry = ContentValues().apply {
            put(InfoTable.DAY_NAME, dayName)
            put(InfoTable.TIME, time)
            put(InfoTable.LOCATION, location)
            put(InfoTable.TEMPERATURE, temperature)
            put(InfoTable.HUMIDITY, humidity)
            put(InfoTable.WIND_SPEED, windSpeed)
            put(InfoTable.VISIBILITY, visibility)
            put(InfoTable.SUNRISE_TIME, sunriseTime)
            put(InfoTable.SUNSET_TIME, sunsetTime)
            put(InfoTable.WEATHER_CODE, weatherCode)
        }
        dbHelper.writableDatabase.insert(InfoTable.TABLE_NAME, null, newEntry)
    }

    fun updateDataInInfoTableById(
        id: Long,
        dayName: String,
        time: String,
        location: String,
        temperature: String,
        humidity: String,
        windSpeed: String,
        visibility: String,
        sunriseTime: String,
        sunsetTime: String,
        weatherCode: String,
    ) {
        val dbHelper = DbHelper(context = context)
        val whereClause = "${InfoTable.ID}=?"
        val whereArgs = arrayOf(id.toString())
        val values = ContentValues().apply {
            put(InfoTable.DAY_NAME, dayName)
            put(InfoTable.TIME, time)
            put(InfoTable.LOCATION, location)
            put(InfoTable.TEMPERATURE, temperature)
            put(InfoTable.HUMIDITY, humidity)
            put(InfoTable.WIND_SPEED, windSpeed)
            put(InfoTable.VISIBILITY, visibility)
            put(InfoTable.SUNRISE_TIME, sunriseTime)
            put(InfoTable.SUNSET_TIME, sunsetTime)
            put(InfoTable.WEATHER_CODE, weatherCode)
        }
        dbHelper.writableDatabase.update(
            InfoTable.TABLE_NAME,
            values,
            whereClause,
            whereArgs
        )
    }

    fun getAllDataFromInfoTable(): List<InfoTableData> {
        val list = mutableListOf<InfoTableData>()
        val projection = arrayOf(
            InfoTable.ID,
            InfoTable.DAY_NAME,
            InfoTable.TIME,
            InfoTable.LOCATION,
            InfoTable.TEMPERATURE,
            InfoTable.HUMIDITY,
            InfoTable.WIND_SPEED,
            InfoTable.VISIBILITY,
            InfoTable.SUNRISE_TIME,
            InfoTable.SUNSET_TIME,
            InfoTable.WEATHER_CODE
        )
        val cursor = DbHelper(context).readableDatabase.query(
            InfoTable.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            list.add(
                InfoTableData(
                    id = cursor.getInt(0),
                    dayName = cursor.getString(1),
                    time = cursor.getString(2),
                    location = cursor.getString(3),
                    temperature = cursor.getString(4),
                    humidity = cursor.getString(5),
                    windSpeed = cursor.getString(6),
                    visibility = cursor.getString(7),
                    sunriseTime = cursor.getString(8),
                    sunsetTime = cursor.getString(9),
                    weatherCode = cursor.getString(10)
                )
            )
        }
        cursor.close()
        return list
    }
}
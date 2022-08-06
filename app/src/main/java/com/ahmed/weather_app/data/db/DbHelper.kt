package com.ahmed.weather_app.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE ${InfoTable.TABLE_NAME} (" +
                "${InfoTable.ID} INTEGER PRIMARY KEY," +
                "${InfoTable.DAY_NAME} TEXT," +
                "${InfoTable.TIME} TEXT," +
                "${InfoTable.LOCATION} TEXT," +
                "${InfoTable.TEMPERATURE} TEXT," +
                "${InfoTable.HUMIDITY} TEXT," +
                "${InfoTable.WIND_SPEED} TEXT," +
                "${InfoTable.VISIBILITY} TEXT," +
                "${InfoTable.SUNRISE_TIME} TEXT," +
                "${InfoTable.SUNSET_TIME} TEXT," +
                "${InfoTable.WEATHER_CODE} TEXT )"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        private const val DB_NAME = "WeatherAppInfoDatabase"
        private const val DB_VERSION = 1
    }
}
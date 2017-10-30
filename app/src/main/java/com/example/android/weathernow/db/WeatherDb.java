package com.example.android.weathernow.db;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.android.weathernow.models.ConsolidatedWeather;
import com.example.android.weathernow.models.Location;

/**
 * Main database class.
 */
@Database(entities = {ConsolidatedWeather.class, Location.class}, version = 1)
public abstract class WeatherDb extends RoomDatabase {
    abstract public WeatherDao weatherDao();

    abstract public LocationDao locationDao();
}

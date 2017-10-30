package com.example.android.weathernow.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.weathernow.models.ConsolidatedWeather;

import java.util.List;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

@Dao
public interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ConsolidatedWeather> weatherList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ConsolidatedWeather consolidatedWeather);

    @Query("SELECT * " +
            "FROM weather " +
            "JOIN location " +
            "WHERE weather.location_woeid = location.woeid " +
            "AND location.woeid = :woeid")
    LiveData<List<ConsolidatedWeather>> findWeatherByWoeid(int woeid);
}
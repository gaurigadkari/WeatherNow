package com.example.android.weathernow.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.example.android.weathernow.models.ConsolidatedWeather;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

@Dao
public interface WeatherDao {
    @Insert
    void insertAll(ConsolidatedWeather... consolidatedWeathers);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ConsolidatedWeather consolidatedWeather);
}
package com.example.android.weathernow.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.weathernow.AppExecutors;
import com.example.android.weathernow.db.LocationDao;
import com.example.android.weathernow.db.WeatherDao;
import com.example.android.weathernow.db.WeatherDb;
import com.example.android.weathernow.models.ConsolidatedWeather;
import com.example.android.weathernow.models.Location;
import com.example.android.weathernow.models.Resource;
import com.example.android.weathernow.models.Response;
import com.example.android.weathernow.network.ApiResponse;
import com.example.android.weathernow.network.WeatherApi;
import com.example.android.weathernow.util.AbsentLiveData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

@Singleton
public class SearchLocationRepository {
    private WeatherApi weatherApi;
    private final AppExecutors appExecutors;
    private final LocationDao locationDao;
    private final WeatherDao weatherDao;
    private final WeatherDb db;

    @Inject
    SearchLocationRepository(WeatherApi weatherApi, AppExecutors appExecutors, LocationDao locationDao, WeatherDao weatherDao, WeatherDb db) {
        this.weatherApi = weatherApi;
        this.appExecutors = appExecutors;
        this.locationDao = locationDao;
        this.weatherDao = weatherDao;
        this.db = db;
    }

    public LiveData<Resource<Location>> loadLocationData(String place) {
        return new NetworkBoundResource<Location, List<Location>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<Location> item) {
                db.beginTransaction();
                try {
                    Location location = null;
                    if (item != null && !item.isEmpty()) {
                        location = item.get(0);
                        location.setSearchTime(new Date().getTime());
                        locationDao.insert(location);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable Location data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<Location> loadFromDb() {
                return Transformations.switchMap(locationDao.findByLocationName(place), searchData -> {
                    if (searchData == null) {
                        return AbsentLiveData.create();
                    } else {
                        MutableLiveData<Location> location = new MutableLiveData<>();
                        location.setValue(searchData);
                        return location;
                    }
                });
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Location>>> createCall() {
                return weatherApi.getWoeid(place);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<ConsolidatedWeather>>> loadWeatherData(int woeid) {
        return new NetworkBoundResource<List<ConsolidatedWeather>, Response>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Response item) {
                db.beginTransaction();
                List<ConsolidatedWeather> weathers = new ArrayList<>();
                try {
                    for (ConsolidatedWeather weather : item.getConsolidatedWeather()) {
                        weather.setLocationWoeid(woeid);
                        weathers.add(weather);
                    }
                    weatherDao.insertAll(weathers);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ConsolidatedWeather> data) {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<ConsolidatedWeather>> loadFromDb() {
                return weatherDao.findWeatherByWoeid(woeid);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Response>> createCall() {
                return weatherApi.getSearchResults(woeid);
            }
        }.asLiveData();
    }

    public LiveData<List<Location>> getMostRecentSearches() {
        return locationDao.findRecentFive();
    }

}


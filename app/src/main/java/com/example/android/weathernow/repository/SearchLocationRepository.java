package com.example.android.weathernow.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.android.weathernow.models.ConsolidatedWeather;
import com.example.android.weathernow.models.Location;
import com.example.android.weathernow.network.WeatherApi;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

@Singleton
public class SearchLocationRepository {
    Retrofit retrofit;
    @Inject
    SearchLocationRepository(Retrofit retrofit){
        this.retrofit = retrofit;
    }
    public LiveData<List<Location>> loadLocationData(MutableLiveData<String> place) {
        Call<List<Location>> callWoeid = retrofit.create(WeatherApi.class).getWoeid(place.getValue());
        final MutableLiveData<List<Location>> locationList = new MutableLiveData<>();
        callWoeid.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                locationList.setValue(response.body());
            }
            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
            }
        });
        return locationList;
    }
    public LiveData<List<ConsolidatedWeather>> loadWeatherData(int woeid) {
        return null;
    }
}


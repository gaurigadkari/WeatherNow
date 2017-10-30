package com.example.android.weathernow.view.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.android.weathernow.models.ConsolidatedWeather;
import com.example.android.weathernow.models.Location;
import com.example.android.weathernow.models.Resource;
import com.example.android.weathernow.repository.SearchLocationRepository;
import com.example.android.weathernow.util.AbsentLiveData;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

public class SearchLocationViewModel extends ViewModel {
    private MutableLiveData<String> place = new MutableLiveData<>();
    private LiveData<Resource<Location>> locationList = new MutableLiveData<>();
    private LiveData<Resource<List<ConsolidatedWeather>>> weatherList;

    @Inject
    public SearchLocationViewModel(SearchLocationRepository searchLocationRepository) {
        locationList = Transformations.switchMap(place, place -> {
            if (place == null || place.trim().length() == 0) {
                return AbsentLiveData.create();
            } else {
                return searchLocationRepository.loadLocationData(place);
            }
        });
        weatherList = Transformations.switchMap(locationList, location -> {
            if (location == null || location.data == null) {
                return AbsentLiveData.create();
            } else {
                return searchLocationRepository.loadWeatherData(location.data.getWoeid());
            }
        });
    }

    public void setPlace(String place) {
        if (Objects.equals(this.place.getValue(), place)) {
            return;
        }
        this.place.setValue(place);
    }

    public LiveData<Resource<List<ConsolidatedWeather>>> getConsolidatedWeather() {
        return weatherList;
    }

    public LiveData<Resource<Location>> getLocationList() {
        return locationList;
    }
}
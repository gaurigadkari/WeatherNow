package com.example.android.weathernow.view.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.example.android.weathernow.models.ConsolidatedWeather;
import com.example.android.weathernow.models.Location;
import com.example.android.weathernow.repository.SearchLocationRepository;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

public class SearchLocationViewModel extends ViewModel {
    private MutableLiveData<String> place = new MutableLiveData<>();
    private LiveData<List<Location>> locationList;
    private LiveData<List<ConsolidatedWeather>> weatherList;
    @Inject
    public SearchLocationViewModel(SearchLocationRepository searchLocationRepository) {
        if (locationList == null) {
            locationList = new MutableLiveData<>();
        } else {
            locationList = searchLocationRepository.loadLocationData(place);
        }
        weatherList = Transformations.switchMap(locationList, locationList -> {
            Location location = locationList.get(0);
            LiveData<List<ConsolidatedWeather>> weatherData = null;
            if (weatherData == null) {
                weatherData = new MutableLiveData<>();
            } else {
                weatherData = searchLocationRepository.loadWeatherData(location.getWoeid());
            }
            return weatherData;
        });
    }
    public void setPlace(String place) {
        if (Objects.equals(this.place.getValue(), place)) {
            return;
        }
        this.place.setValue(place);
    }
    public LiveData<List<ConsolidatedWeather>> getConsolidatedWeather() {
        return weatherList;
    }
    public LiveData<List<Location>> getLocationList() {
        return locationList;
    }
}

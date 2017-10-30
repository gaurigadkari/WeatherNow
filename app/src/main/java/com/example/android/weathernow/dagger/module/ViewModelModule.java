package com.example.android.weathernow.dagger.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.android.weathernow.dagger.ViewModelKey;
import com.example.android.weathernow.view.WeatherViewModelFactory;
import com.example.android.weathernow.view.search.SearchLocationViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by Gauri Gadkari on 10/29/17.
 */

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchLocationViewModel.class)
    abstract ViewModel bindSearchViewModel(SearchLocationViewModel searchViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(WeatherViewModelFactory factory);
}
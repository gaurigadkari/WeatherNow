package com.example.android.weathernow.dagger.module;

import com.example.android.weathernow.view.detail.DetailWeatherFragment;
import com.example.android.weathernow.view.search.SearchLocationFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract SearchLocationFragment contributeSearchLocationFragment();
    @ContributesAndroidInjector
    abstract DetailWeatherFragment contributeDetailWeatherFragment();
}


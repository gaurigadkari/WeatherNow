package com.example.android.weathernow.dagger.component;

/**
 * Created by Gauri Gadkari on 10/29/17.
 */

import com.example.android.weathernow.view.search.SearchLocationViewModel;

import dagger.Subcomponent;

/**
 * A sub component to create ViewModels. It is called by the
 * {@link com.example.android.weathernow.view.WeatherViewModelFactory}. Using this component allows
 * ViewModels to define {@link javax.inject.Inject} constructors.
 */
@Subcomponent
public interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        ViewModelSubComponent build();
    }

    SearchLocationViewModel searchLocationViewModel();
}

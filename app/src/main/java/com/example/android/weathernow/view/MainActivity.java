package com.example.android.weathernow.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.weathernow.R;
import com.example.android.weathernow.adapters.WeatherListAdapter;
import com.example.android.weathernow.models.ConsolidatedWeather;
import com.example.android.weathernow.view.detail.DetailWeatherFragment;
import com.example.android.weathernow.view.search.SearchLocationFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector, WeatherListAdapter.DetailHelper {
    private static final String TAG = "Main activity";
    FragmentManager fragmentManager;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    SearchLocationFragment searchLocationFragment;
    DetailWeatherFragment detailWeatherFragment;
    private static final String SEARCH_LOCATION_FRAGMENT = "search_location_fragment";
    private static final String DEATIAL_WEATHER_FRAGMENT = "detail_weathe_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        searchLocationFragment = (SearchLocationFragment) fragmentManager.findFragmentByTag(SEARCH_LOCATION_FRAGMENT);

        if (searchLocationFragment == null) {
            searchLocationFragment = new SearchLocationFragment();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainer, searchLocationFragment, SEARCH_LOCATION_FRAGMENT)
                    .addToBackStack(SEARCH_LOCATION_FRAGMENT)
                    .commit();
        }

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onWeatherClickHandler(ConsolidatedWeather weather) {
        detailWeatherFragment = DetailWeatherFragment.newInstance(weather);
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, detailWeatherFragment)
                .addToBackStack(DEATIAL_WEATHER_FRAGMENT)
                .commit();
    }
}

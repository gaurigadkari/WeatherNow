package com.example.android.weathernow.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.weathernow.R;
import com.example.android.weathernow.view.search.SearchLocationFragment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    private static final String TAG = "Main activity";
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    SearchLocationFragment searchLocationFragment;
    private static final String SEARCH_LOCATION_FRAGMENT = "search_location_fragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        searchLocationFragment = (SearchLocationFragment) fragmentManager.findFragmentByTag(SEARCH_LOCATION_FRAGMENT);

        if(searchLocationFragment == null){
            searchLocationFragment = new SearchLocationFragment();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainer, searchLocationFragment, SEARCH_LOCATION_FRAGMENT)
                    .commit();
        }

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}

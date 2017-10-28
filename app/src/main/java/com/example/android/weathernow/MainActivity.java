package com.example.android.weathernow;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.weathernow.view.search.SearchLocationFragment;

public class MainActivity extends AppCompatActivity {
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
}

package com.example.android.weathernow.view.search;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.weathernow.R;
import com.example.android.weathernow.adapters.WeatherListAdapter;
import com.example.android.weathernow.dagger.utility.Injectable;
import com.example.android.weathernow.databinding.FragmentSearchLocationBinding;
import com.example.android.weathernow.models.ConsolidatedWeather;
import com.example.android.weathernow.util.Utilities;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class SearchLocationFragment extends Fragment implements Injectable {
    private static final String TAG = "SearchLocationFragment";
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int REQUEST_LOCATION = 1002;
    FragmentSearchLocationBinding binding;
    RecyclerView rvWeatherList;
    List<ConsolidatedWeather> weatherList;
    WeatherListAdapter adapter;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    AppCompatImageButton searchButton;
    ImageView hamburgerIcon;
    PlaceDetectionClient placeDetectionClient;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private SearchLocationViewModel searchLocationViewModel;


    public SearchLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_search_location, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchLocationViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchLocationViewModel.class);
        weatherList = new ArrayList<>();
        rvWeatherList = binding.weatherList;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvWeatherList.setLayoutManager(linearLayoutManager);
        adapter = new WeatherListAdapter(getContext(), weatherList);
        rvWeatherList.setAdapter(adapter);
        // setting the visibility of default search icon for place autocomplete search to Gone
        searchButton = getView().findViewById(R.id.place_autocomplete_search_button);
        searchButton.setVisibility(View.GONE);
        hamburgerIcon = binding.menu;
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.openDrawer(GravityCompat.START);
            }
        });

        getLocation();

        mDrawer = getView().findViewById(R.id.drawer_layout);


        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();

        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                search(place.getName().toString());
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        searchLocationViewModel.getLocationList().observe(this, result -> {
            Log.d(TAG, "Observer" + result);
        });

        searchLocationViewModel.getConsolidatedWeather().observe(this, result -> {
            Log.d(TAG, "Observer" + result);
            if (result != null
                    && result.status == com.example.android.weathernow.models.Status.SUCCESS
                    && result.data != null) {
                weatherList.clear();
                weatherList.addAll(result.data);
                adapter.notifyDataSetChanged();
            }
        });

    }

    public void getLocation() {
        Log.i(TAG, "Show contacts button pressed. Checking permissions.");

        // Verify that all required contact permissions have been granted.
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Contacts permissions have not been granted.
            Log.i(TAG, "Contact permissions has NOT been granted. Requesting permissions.");
            requestLocationPermissions();

        } else {

            // Contact permissions have been granted. Show the contacts fragment.
            Log.i(TAG, "Contact permissions have already been granted. Displaying contact details.");
            //TODO get current location and make a network call or fetch data from db


        }
    }


    //trying out getting device location
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Place place = (Place) placeDetectionClient.getCurrentPlace(new PlaceFilter());
            place.getName();
        }

    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_LOCATION, REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            Log.i(TAG, "Received response for Location permissions request.");

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // All required permissions have been granted, display contacts fragment.
                Snackbar.make(rvWeatherList, R.string.permision_available_location,
                        Snackbar.LENGTH_SHORT)
                        .show();
                getDeviceLocation();

            } else {
                Log.i(TAG, "Location permissions were NOT granted.");
                Snackbar.make(rvWeatherList, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void search(String query) {
        if (query.length() == 0) {
            Snackbar.make(rvWeatherList, R.string.empty_search, Toast.LENGTH_LONG).show();
        }

        if (Utilities.isNetworkAvailable(getContext()) && Utilities.isOnline()) {
            searchLocationViewModel.setPlace(query);
        } else {
            Snackbar.make(rvWeatherList, R.string.device_offline, Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

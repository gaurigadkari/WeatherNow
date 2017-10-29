package com.example.android.weathernow.view.search;

import android.Manifest;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.android.weathernow.R;
import com.example.android.weathernow.Utilities;
import com.example.android.weathernow.adapters.WeatherListAdapter;
import com.example.android.weathernow.dagger.utility.Injectable;
import com.example.android.weathernow.databinding.FragmentSearchLocationBinding;
import com.example.android.weathernow.models.ConsolidatedWeather;
import com.example.android.weathernow.models.Location;
import com.example.android.weathernow.models.Response;
import com.example.android.weathernow.network.WeatherApi;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


public class SearchLocationFragment extends Fragment implements Injectable {
    private static final String TAG = "SearchLocationFragment";
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int REQUEST_LOCATION = 1002;
    FragmentSearchLocationBinding binding;
    RecyclerView rvWeatherList;
    List<ConsolidatedWeather> weatherList;
    WeatherListAdapter adapter;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    AppCompatImageButton searchButton;
    ImageView hamburgerIcon;
    @Inject
    Retrofit retrofit;
    PlaceDetectionClient placeDetectionClient;

    public SearchLocationFragment() {
        // Required empty public constructor
    }

    public static SearchLocationFragment newInstance(String param1, String param2) {
        SearchLocationFragment fragment = new SearchLocationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//        inflater.inflate(R.menu.search_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchQuery = query;
//                search(query);
//                searchView.clearFocus();
//                searchView.setQuery("", false);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//        super.onCreateOptionsMenu(menu, inflater);
//    }



    private void search(String query) {
        if (query.length() == 0) {
            Snackbar.make(rvWeatherList, R.string.empty_search, Toast.LENGTH_LONG).show();
        }

        if (Utilities.isNetworkAvailable(getContext()) && Utilities.isOnline()) {
            retroNetworkCall(query);
        } else {
            Snackbar.make(rvWeatherList, R.string.device_offline, Snackbar.LENGTH_LONG).show();
        }
    }

    private void retroNetworkCall(String location) {
        //final int woeid;
        Call<List<Location>> callWoeid = retrofit.create(WeatherApi.class).getWoeid(location);

        callWoeid.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, retrofit2.Response<List<Location>> response) {
                Log.d("TAG", response.body() + "");
                int woeid = response.body().get(0).getWoeid();
                Call<Response> weatherCall = retrofit.create(WeatherApi.class).getSearchResults(woeid);
                weatherCall.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        weatherList.clear();
                        List<ConsolidatedWeather> weatherArrayList = response.body().getConsolidatedWeather();
                        weatherList.addAll(weatherArrayList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                Log.d("TAG", t.getMessage() + "");
            }
        });
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

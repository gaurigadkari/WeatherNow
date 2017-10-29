package com.example.android.weathernow.view.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.android.weathernow.R;
import com.example.android.weathernow.Utilities;
import com.example.android.weathernow.adapters.WeatherListAdapter;
import com.example.android.weathernow.dagger.utility.Injectable;
import com.example.android.weathernow.databinding.FragmentSearchLocationBinding;
import com.example.android.weathernow.models.ConsolidatedWeather;
import com.example.android.weathernow.models.Location;
import com.example.android.weathernow.models.Response;
import com.example.android.weathernow.network.RetrofitClient;
import com.example.android.weathernow.network.WeatherApi;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


public class SearchLocationFragment extends Fragment implements Injectable{
    private static final String TAG = "SearchLocationFragment";

    FragmentSearchLocationBinding binding;
    RecyclerView rvWeatherList;
    List<ConsolidatedWeather> weatherList;
    WeatherListAdapter adapter;
    Toolbar toolbar;
    String searchQuery;
    @Inject
    Retrofit retrofit;
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
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();

        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
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

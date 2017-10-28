package com.example.android.weathernow.view.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.weathernow.R;
import com.example.android.weathernow.adapters.WeatherListAdapter;
import com.example.android.weathernow.databinding.FragmentSearchLocationBinding;
import com.example.android.weathernow.models.ConsolidatedWeather;
import com.example.android.weathernow.models.Location;
import com.example.android.weathernow.models.Response;
import com.example.android.weathernow.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class SearchLocationFragment extends Fragment {
    FragmentSearchLocationBinding binding;
    RecyclerView rvWeatherList;
    EditText etLocation;
    Button btnSearch;
    List<ConsolidatedWeather> weatherList;
    WeatherListAdapter adapter;
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
        etLocation = binding.etLocation;
        btnSearch = binding.btnSearch;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvWeatherList.setLayoutManager(linearLayoutManager);
        adapter = new WeatherListAdapter(getContext(), weatherList);
        rvWeatherList.setAdapter(adapter);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retroNetworkCall(etLocation.getText().toString());
                rvWeatherList.setVisibility(View.VISIBLE);
            }
        });
    }

    private void retroNetworkCall(String location) {
        //final int woeid;
        Call<List<Location>> callWoeid = RetrofitClient.getInstance().getApiInterface().getWoeid(location);

        callWoeid.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, retrofit2.Response<List<Location>> response) {
                Log.d("TAG", response.body() + "");
                int woeid = response.body().get(0).getWoeid();
                Call<Response> weatherCall = RetrofitClient.getInstance().getApiInterface().getSearchResults(woeid);
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

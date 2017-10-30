package com.example.android.weathernow.view.detail;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.weathernow.R;
import com.example.android.weathernow.dagger.utility.Injectable;
import com.example.android.weathernow.databinding.FragmentDetailWeatherBinding;
import com.example.android.weathernow.models.ConsolidatedWeather;
import com.example.android.weathernow.util.Utilities;
import com.example.android.weathernow.view.search.SharedSearchDetailViewModel;

import org.parceler.Parcels;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailWeatherFragment extends Fragment implements Injectable {
    FragmentDetailWeatherBinding binding;
    android.support.v7.widget.Toolbar toolbar;
    TextView weatherStateName, temperature, maxTemp, minTemp, humidity, today, date;
    ImageView weatherStateIcon;
    private static final String WEATHER = "weather";
    private static final String LOCATION_TITLE = "location_title";
    ConsolidatedWeather weather;
    String locationTitle;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private SharedSearchDetailViewModel sharedSearchDetailViewModel;
    public DetailWeatherFragment() {
    }

    public static DetailWeatherFragment newInstance(ConsolidatedWeather weather, String locationTitle) {
        Bundle args = new Bundle();
        DetailWeatherFragment detailWeatherFragment = new DetailWeatherFragment();
        args.putParcelable(WEATHER, Parcels.wrap(weather));
        args.putCharSequence(LOCATION_TITLE, locationTitle);
        detailWeatherFragment.setArguments(args);
        return detailWeatherFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weather = Parcels.unwrap(getArguments().getParcelable(WEATHER));
        this.locationTitle = getArguments().getString(LOCATION_TITLE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_detail_weather, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sharedSearchDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(SharedSearchDetailViewModel.class);
        weatherStateName = binding.weatherStateName;
        temperature = binding.temp;
        humidity = binding.humidity;
        maxTemp = binding.maxTemp;
        minTemp = binding.minTemp;
        toolbar = binding.toolbar;
        weatherStateIcon = binding.weatherStateIcon;
        weatherStateName.setText(weather.getWeatherStateName());
        temperature.setText(Utilities.getDisplayableTemp(weather.getTheTemp()) + "°C");
        minTemp.setText(Utilities.getDisplayableTemp(weather.getMinTemp()) + "°C");
        maxTemp.setText(Utilities.getDisplayableTemp(weather.getMaxTemp()) + "°C");
        toolbar.setTitle(locationTitle + " " + weather.getApplicableDate());
        Glide.with(getContext()).load(weather.getWeatherIconPath()).into(weatherStateIcon);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

    }
}

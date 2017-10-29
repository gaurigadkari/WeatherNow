package com.example.android.weathernow.view.detail;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.android.weathernow.R;
import com.example.android.weathernow.databinding.FragmentDetailWeatherBinding;
import com.example.android.weathernow.models.ConsolidatedWeather;

import org.parceler.Parcels;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailWeatherFragment extends Fragment {
    FragmentDetailWeatherBinding binding;
    android.support.v7.widget.Toolbar toolbar;
    TextView weatherStateName, temperature, maxTemp, minTemp, humidity, today, date;
    ImageView weatherStateIcon;
    private static final String WEATHER = "weather";
    ConsolidatedWeather weather;
    public DetailWeatherFragment() {
    }

    public static DetailWeatherFragment newInstance(ConsolidatedWeather weather) {
        Bundle args = new Bundle();
        DetailWeatherFragment detailWeatherFragment = new DetailWeatherFragment();
        args.putParcelable(WEATHER, Parcels.wrap(weather));
        detailWeatherFragment.setArguments(args);
        return detailWeatherFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weather = Parcels.unwrap(getArguments().getParcelable(WEATHER));

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weatherStateName = binding.weatherStateName;
        temperature = binding.temp;
        humidity = binding.humidity;
        maxTemp = binding.maxTemp;
        minTemp = binding.minTemp;
        toolbar = binding.toolbar;
        weatherStateIcon = binding.weatherStateIcon;
        weatherStateName.setText(weather.getWeatherStateName());
        temperature.setText(weather.getTheTemp() + "°C");
        minTemp.setText(weather.getMinTemp() + "°C");
        maxTemp.setText(weather.getMaxTemp() + "°C");
        toolbar.setTitle(weather.getApplicableDate());
        Glide.with(getContext()).load(weather.getWeatherIconPath()).into(weatherStateIcon);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

    }
}

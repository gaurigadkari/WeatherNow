package com.example.android.weathernow.view.detail;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
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

import com.bumptech.glide.Glide;
import com.example.android.weathernow.R;
import com.example.android.weathernow.dagger.utility.Injectable;
import com.example.android.weathernow.databinding.FragmentDetailWeatherBinding;
import com.example.android.weathernow.models.ConsolidatedWeather;
import com.example.android.weathernow.util.Utilities;
import com.example.android.weathernow.view.search.SharedSearchDetailViewModel;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailWeatherFragment extends Fragment implements Injectable {
    private FragmentDetailWeatherBinding binding;
    private android.support.v7.widget.Toolbar toolbar;
    private TextView weatherStateName;
    private TextView temperature;
    private TextView maxTemp;
    private TextView minTemp;
    private TextView windSpeed;
    private TextView windDirection;
    TextView today;
    TextView date;
    private ImageView weatherStateIcon;
    private static final String WEATHER = "weather";
    private static final String LOCATION_TITLE = "location_title";
    private ConsolidatedWeather weather;
    private String locationTitle;
    @Inject
    private
    ViewModelProvider.Factory viewModelFactory;

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
        SharedSearchDetailViewModel sharedSearchDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(SharedSearchDetailViewModel.class);
        weatherStateName = binding.weatherStateName;
        temperature = binding.temp;
        windSpeed = binding.windSpeed;
        windDirection = binding.windDirection;
        maxTemp = binding.maxTemp;
        minTemp = binding.minTemp;
        toolbar = binding.toolbar;
        weatherStateIcon = binding.weatherStateIcon;
        weatherStateName.setText(weather.getWeatherStateName());
        temperature.setText(String.format("%d°C", Utilities.getDisplayableTemp(weather.getTheTemp())));
        minTemp.setText(String.format("%d°C", Utilities.getDisplayableTemp(weather.getMinTemp())));
        maxTemp.setText(String.format("%d°C", Utilities.getDisplayableTemp(weather.getMaxTemp())));
        windSpeed.setText(String.format("%s", weather.getWindSpeed()));
        windDirection.setText(String.format("%s", weather.getWindDirectionCompass()));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(weather.getApplicableDate());
            String newDate = new SimpleDateFormat("dd-MMM").format(date);
            toolbar.setTitle(locationTitle + " " + newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Glide.with(getContext()).load(weather.getWeatherIconPath()).into(weatherStateIcon);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

    }
}

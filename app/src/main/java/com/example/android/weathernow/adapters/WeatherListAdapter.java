package com.example.android.weathernow.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.weathernow.R;
import com.example.android.weathernow.databinding.WeatherItemBinding;
import com.example.android.weathernow.models.ConsolidatedWeather;

import java.util.List;

/**
 * Created by Gauri Gadkari on 10/27/17.
 */

public class WeatherListAdapter extends RecyclerView.Adapter {
    List<ConsolidatedWeather> weatherList;
    Context context;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(v);
        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConsolidatedWeather weather = weatherList.get(position);
        ((WeatherViewHolder) holder).weatherStateName.setText(weather.getWeatherStateName());
        ((WeatherViewHolder) holder).temperature.setText(weather.getTheTemp() + "");
        ((WeatherViewHolder) holder).humidity.setText(weather.getHumidity() + "%");
        weather.setWeatherIconPath(weather.getWeatherStateAbbr());
        String path = weather.getWeatherIconPath();
        Glide.with(context).load(path).into(((WeatherViewHolder) holder).weatherStateIcon);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public WeatherListAdapter(Context context, List<ConsolidatedWeather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView weatherStateName, temperature, humidity;
        ImageView weatherStateIcon;
        public WeatherViewHolder(View itemView) {
            super(itemView);
            weatherStateName = itemView.findViewById(R.id.weather_state_name);
            temperature = itemView.findViewById(R.id.temp);
            humidity = itemView.findViewById(R.id.humidity);
            weatherStateIcon = itemView.findViewById(R.id.weather_state_icon);
        }
    }
}

package com.example.android.weathernow.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.weathernow.R;
import com.example.android.weathernow.models.ConsolidatedWeather;
import com.example.android.weathernow.util.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Gauri Gadkari on 10/27/17.
 */

public class WeatherListAdapter extends RecyclerView.Adapter {
    private final List<ConsolidatedWeather> weatherList;
    private final Context context;
    private final DetailHelper listener;
    private String locationTitle;


    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public WeatherListAdapter(Context context, List<ConsolidatedWeather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
        listener = (DetailHelper) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, parent, false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view);
        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConsolidatedWeather weather = weatherList.get(position);
        ((WeatherViewHolder) holder).weatherStateName.setText(weather.getWeatherStateName());
        ((WeatherViewHolder) holder).temperature.setText(String.format("%d°C", Utilities.getDisplayableTemp(weather.getTheTemp())));
        ((WeatherViewHolder) holder).humidity.setText(String.format(context.getString(R.string.humidity_adapter), weather.getHumidity()));
        weather.setWeatherIconPath(weather.getWeatherStateAbbr());
        String path = weather.getWeatherIconPath();
        Glide.with(context).load(path).into(((WeatherViewHolder) holder).weatherStateIcon);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date today = calendar.getTime();
            Date date = formatter.parse(weather.getApplicableDate());
            if (date.toString().equals(today.toString())) {
                ((WeatherViewHolder) holder).today.setVisibility(View.VISIBLE);
                ((WeatherViewHolder) holder).date.setVisibility(View.GONE);
            } else {
                String newDate = new SimpleDateFormat("dd-MMM").format(date);
                ((WeatherViewHolder) holder).date.setText(newDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> listener.onWeatherClickHandler(weather, locationTitle));
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        final TextView weatherStateName;
        final TextView temperature;
        final TextView humidity;
        final TextView today;
        final TextView date;
        final ImageView weatherStateIcon;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            weatherStateName = itemView.findViewById(R.id.weather_state_name);
            temperature = itemView.findViewById(R.id.temp);
            humidity = itemView.findViewById(R.id.humidity);
            weatherStateIcon = itemView.findViewById(R.id.weather_state_icon);
            today = itemView.findViewById(R.id.today);
            date = itemView.findViewById(R.id.date);
        }
    }

    public interface DetailHelper {
        void onWeatherClickHandler(ConsolidatedWeather weather, String locationTitle);
    }
}

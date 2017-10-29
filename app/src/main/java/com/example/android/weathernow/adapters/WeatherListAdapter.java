package com.example.android.weathernow.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.weathernow.R;
import com.example.android.weathernow.databinding.WeatherItemBinding;
import com.example.android.weathernow.models.ConsolidatedWeather;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Gauri Gadkari on 10/27/17.
 */

public class WeatherListAdapter extends RecyclerView.Adapter {
    List<ConsolidatedWeather> weatherList;
    Context context;
    DetailHelper listener;
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
        ((WeatherViewHolder) holder).temperature.setText(weather.getTheTemp() + "°C");
        ((WeatherViewHolder) holder).humidity.setText("HUMIDITY " + weather.getHumidity() + "%");
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
            if(date.toString().equals(today.toString())) {
                ((WeatherViewHolder) holder).today.setVisibility(View.VISIBLE);
                ((WeatherViewHolder) holder).date.setVisibility(View.GONE);
            } else {
                String newDate = new SimpleDateFormat("dd-MMM").format(date);
                ((WeatherViewHolder) holder).date.setText(newDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onWeatherClickHandler(weather);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public WeatherListAdapter(Context context, List<ConsolidatedWeather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
        listener = (DetailHelper) context;
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView weatherStateName, temperature, humidity, today, date;
        ImageView weatherStateIcon;
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
        public void onWeatherClickHandler(ConsolidatedWeather weather);
    }
}

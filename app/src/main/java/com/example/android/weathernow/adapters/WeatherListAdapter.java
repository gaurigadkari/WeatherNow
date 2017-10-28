package com.example.android.weathernow.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public WeatherListAdapter(Context context, List<ConsolidatedWeather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {

        public WeatherViewHolder(View itemView) {
            super(itemView);
        }
    }
}

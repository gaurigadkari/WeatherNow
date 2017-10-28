package com.example.android.weathernow.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gauri Gadkari on 10/27/17.
 */

public class RetrofitClient {
    private static RetrofitClient instance = null;
    public static final String BASE_URL = "https://www.metaweather.com/api/";
    public static final String LOCATION = "location";
    public static final String PAGINATION_PARAMETER = "page";
    public static final String OK_STATUS = "OK";
    public static final int ERROR_CODE_TOO_MANY_REQUESTS = 429;

    // Keep your services here, build them in buildRetrofit method later
    private WeatherApi apiInterface;
    private Retrofit retrofit;

    public WeatherApi getApiInterface() {
        return apiInterface;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    // Build retrofit once when creating a single instance
    private RetrofitClient() {
        // Implement a method to build your retrofit
        buildRetrofit();
    }

    private void buildRetrofit() {
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Build your services once
        this.apiInterface = retrofit.create(WeatherApi.class);
    }
}

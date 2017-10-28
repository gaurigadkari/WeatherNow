package com.example.android.weathernow.network;

import com.example.android.weathernow.models.Location;
import com.example.android.weathernow.models.Response;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Gauri Gadkari on 10/26/17.
 */

public interface WeatherApi {
    @GET("location/{woeid}")
    Call<Response> getSearchResults(@Path("woeid") int woeid);

    @GET("location/search/")
    Call<List<Location>> getWoeid(@Query("query") String location);

}

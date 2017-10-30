package com.example.android.weathernow.dagger.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.android.weathernow.db.LocationDao;
import com.example.android.weathernow.db.WeatherDao;
import com.example.android.weathernow.db.WeatherDb;
import com.example.android.weathernow.network.WeatherApi;
import com.example.android.weathernow.repository.LiveDataCallAdapterFactory;
import com.example.android.weathernow.util.Constant;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

@Module(includes = ViewModelModule.class)
public class AppModule {
    @Provides
    @Singleton
    Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(Cache cache) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.cache(cache);
        return client.build();
    }

    @Provides
    @Singleton
    WeatherApi provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Constant.REST_API_URL)
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(WeatherApi.class);
    }

    @Singleton
    @Provides
    WeatherDb provideDb(Application app) {
        return Room.databaseBuilder(app, WeatherDb.class, Constant.DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    WeatherDao provideWeatherDao(WeatherDb db) {
        return db.weatherDao();
    }

    @Singleton
    @Provides
    LocationDao provideLocationDao(WeatherDb db) {
        return db.locationDao();
    }
}
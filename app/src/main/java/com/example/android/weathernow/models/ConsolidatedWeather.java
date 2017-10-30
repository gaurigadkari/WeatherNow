package com.example.android.weathernow.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.android.weathernow.util.Constant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Gauri Gadkari on 10/26/17.
 */
@Entity(tableName = "weather")
@Parcel
public class ConsolidatedWeather {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("weather_state_name")
    @Expose
    @ColumnInfo(name = "weather_state_name")
    private String weatherStateName;
    @SerializedName("weather_state_abbr")
    @Expose
    @ColumnInfo(name = "weather_state_abbr")
    private String weatherStateAbbr;
    @SerializedName("wind_direction_compass")
    @Expose
    @ColumnInfo(name = "wind_direction_compass")
    private String windDirectionCompass;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("applicable_date")
    @Expose
    @ColumnInfo(name = "applicable_date")
    private String applicableDate;
    @SerializedName("min_temp")
    @Expose
    @ColumnInfo(name = "min_temp")
    private float minTemp;
    @SerializedName("max_temp")
    @Expose
    @ColumnInfo(name = "max_temp")
    private float maxTemp;
    @SerializedName("the_temp")
    @Expose
    @ColumnInfo(name = "the_temp")
    private float theTemp;
    @SerializedName("wind_speed")
    @Expose
    @ColumnInfo(name = "wind_speed")
    private float windSpeed;
    @SerializedName("wind_direction")
    @Expose
    @ColumnInfo(name = "wind_direction")
    private float windDirection;
    @SerializedName("air_pressure")
    @Expose
    @ColumnInfo(name = "air_pressure")
    private float airPressure;
    @SerializedName("humidity")
    @Expose
    private int humidity;
    @SerializedName("predictability")
    @Expose
    private int predictability;
    @ColumnInfo(name = "location_woeid")
    private int locationWoeid;
    @ColumnInfo(name = "weather_icon_path")
    private String weatherIconPath;

    public String getWeatherIconPath() {
        return weatherIconPath;
    }

    public void setWeatherIconPath(String weatherStateAbbr) {
        this.weatherIconPath = Constant.IMAGE_BASER_URL + weatherStateAbbr + ".png";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeatherStateName() {
        return weatherStateName;
    }

    public void setWeatherStateName(String weatherStateName) {
        this.weatherStateName = weatherStateName;
    }

    public String getWeatherStateAbbr() {
        return weatherStateAbbr;
    }

    public void setWeatherStateAbbr(String weatherStateAbbr) {
        this.weatherStateAbbr = weatherStateAbbr;
    }

    public String getWindDirectionCompass() {
        return windDirectionCompass;
    }

    public void setWindDirectionCompass(String windDirectionCompass) {
        this.windDirectionCompass = windDirectionCompass;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getApplicableDate() {
        return applicableDate;
    }

    public void setApplicableDate(String applicableDate) {
        this.applicableDate = applicableDate;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public float getTheTemp() {
        return theTemp;
    }

    public void setTheTemp(float theTemp) {
        this.theTemp = theTemp;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public float getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(float windDirection) {
        this.windDirection = windDirection;
    }

    public float getAirPressure() {
        return airPressure;
    }

    public void setAirPressure(float airPressure) {
        this.airPressure = airPressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPredictability() {
        return predictability;
    }

    public void setPredictability(int predictability) {
        this.predictability = predictability;
    }

    public int getLocationWoeid() {
        return locationWoeid;
    }

    public void setLocationWoeid(int locationWoeid) {
        this.locationWoeid = locationWoeid;
    }

}

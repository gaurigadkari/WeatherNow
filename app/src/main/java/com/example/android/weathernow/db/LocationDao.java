package com.example.android.weathernow.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.weathernow.models.Location;

import java.util.List;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

@Dao
public interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Location location);

    @Query("SELECT * FROM location WHERE title = :location")
    LiveData<Location> findByLocationName(String location);

    @Query("select * from location order by search_time desc limit 5")
    LiveData<List<Location>> findRecentFive();

}

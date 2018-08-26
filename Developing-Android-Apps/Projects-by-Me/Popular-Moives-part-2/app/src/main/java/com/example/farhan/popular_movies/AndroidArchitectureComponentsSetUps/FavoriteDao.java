package com.example.farhan.popular_movies.AndroidArchitectureComponentsSetUps;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.farhan.popular_movies.model.Favorites;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorites")
    LiveData<List<Favorites>> loadAllFavorites();

    @Insert
    void insertTask(Favorites favorites);

    @Delete
    void deleteTask(Favorites favorites);

    @Query("SELECT * FROM favorites WHERE id = :id")
    LiveData<Favorites> loadFavoriteById(int id);

    @Query("SELECT * FROM favorites WHERE id = :id")
    Favorites loadFavoriteByIdWithNoLiveData(int id);
}

package com.example.farhan.popular_movies.AndroidArchitectureComponentsSetUps;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.farhan.popular_movies.model.Favorites;

public class AddFavoriteViewModel extends ViewModel {

    private LiveData<Favorites> favorite;

    // Note: The constructor should receive the database and the taskId
    public AddFavoriteViewModel(AppDatabase database, int taskId) {
        favorite = database.favoriteDao().loadFavoriteById(taskId);
    }

    public LiveData<Favorites> getTask() {
        return favorite;
    }
}

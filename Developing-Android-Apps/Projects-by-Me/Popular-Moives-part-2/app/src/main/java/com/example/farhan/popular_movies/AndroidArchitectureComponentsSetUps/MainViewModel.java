package com.example.farhan.popular_movies.AndroidArchitectureComponentsSetUps;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.farhan.popular_movies.model.Favorites;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Favorites>> favorites;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.e(TAG, "Actively retrieving the tasks from the DataBase");
        favorites = database.favoriteDao().loadAllFavorites();
    }

    public LiveData<List<Favorites>> getTasks() {
        return favorites;
    }
}

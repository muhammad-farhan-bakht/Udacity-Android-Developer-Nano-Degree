package com.example.farhan.popular_movies.AndroidArchitectureComponentsSetUps;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

public class AddTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    private final AppDatabase mDb;
    private final int mFavoriteId;

    public AddTaskViewModelFactory(AppDatabase database, int taskId) {
        mDb = database;
        mFavoriteId = taskId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddFavoriteViewModel(mDb, mFavoriteId);
    }
}
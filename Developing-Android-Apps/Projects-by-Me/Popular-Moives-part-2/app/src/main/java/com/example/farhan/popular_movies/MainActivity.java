package com.example.farhan.popular_movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farhan.popular_movies.AndroidArchitectureComponentsSetUps.MainViewModel;
import com.example.farhan.popular_movies.SetUpRetrofit.RetrofitClient;
import com.example.farhan.popular_movies.adapter.MoviesAdapter;
import com.example.farhan.popular_movies.model.BaseModel;
import com.example.farhan.popular_movies.model.Favorites;
import com.example.farhan.popular_movies.model.Movies;
import com.example.farhan.popular_movies.utils.Constants;
import com.example.farhan.popular_movies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    MoviesAdapter moviesAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String FAVORITE = "favorite";
    private static final String SORT_BY = "sort_by";
    private String sortBy = POPULAR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.containsKey(SORT_BY)) {
            sortBy = savedInstanceState.getString(SORT_BY);
            Log.e(TAG, "onCreate: sortBy" + sortBy);
        }

        mRecyclerView = findViewById(R.id.recyclerView_movies);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns(MainActivity.this)));
        mRecyclerView.setHasFixedSize(true);

        moviesAdapter = new MoviesAdapter();
        mRecyclerView.setAdapter(moviesAdapter);
        moviesAdapter.setMoviesAdapterOnClickHandler(MainActivity.this);

        loadMoviesData();

    }

    private void loadMoviesData() {

        if (sortBy.equals(FAVORITE)) {
            sortBy = FAVORITE;
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            loadFavoriteData();

        } else {
            if (NetworkUtils.isNetworkAvailable(this)) {
                showMoviesDataView();
                callSortByApi(sortBy);

            } else {
                if (!sortBy.equals(FAVORITE)) {
                    sortBy = FAVORITE;
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    loadFavoriteData();
                }
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_BY, sortBy);
    }

    private void callSortByApi(String sortBy) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getService().getMoviesSortBy(sortBy, Constants.API_KEY).enqueue(new Callback<BaseModel<Movies>>() {
            @Override
            public void onResponse(Call<BaseModel<Movies>> call, Response<BaseModel<Movies>> response) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                Log.e(TAG, "onResponse: Successful callSortByApi");
                if (response.body() != null) {
                    moviesAdapter.setMovieData(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<Movies>> call, Throwable t) {
                Log.e(TAG, "onFailure: callSortByApi" + t.getMessage());
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "No Network is found Sort Changed to Favorite", Toast.LENGTH_SHORT).show();
                networkDisconnects();
            }
        });
    }

    private void networkDisconnects() {
        if (!sortBy.equals(FAVORITE)) {
            sortBy = FAVORITE;
            loadFavoriteData();
        }
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    private void showMoviesDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movies moviesObj) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("moviesObj", moviesObj);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_most_popular: {
                if (!sortBy.equals(POPULAR)) {
                    sortBy = POPULAR;
                    loadMoviesData();
                    return true;
                } else {
                    return false;
                }
            }

            case R.id.action_top_rated: {
                if (!sortBy.equals(TOP_RATED)) {
                    sortBy = TOP_RATED;
                    loadMoviesData();
                    return true;
                } else {
                    return false;
                }
            }

            case R.id.action_top_favorite: {
                if (!sortBy.equals(FAVORITE)) {
                    sortBy = FAVORITE;
                    loadFavoriteData();
                    return true;
                } else {
                    return false;
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }
    // I searched it from StackOverFlow that how to convert byte Array into Base64
    private void loadFavoriteData() {

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getTasks().observe(MainActivity.this, new Observer<List<Favorites>>() {
            @Override
            public void onChanged(@Nullable List<Favorites> favorites) {
                if (favorites.size() != 0) {
                    final List<Movies> movies = new ArrayList<>();
                    for (Favorites favObj : favorites) {
                        Movies moviesObj = new Movies(favObj.getTitle(), favObj.getRelease_date(), Base64.encodeToString(favObj.getImage(), Base64.NO_WRAP), favObj.getVote_average(), favObj.getOverview(), favObj.getId());
                        movies.add(moviesObj);
                    }

                    if (movies != null) {
                        moviesAdapter.setMovieData(movies);
                    } else {
                        showErrorMessage();
                    }

                } else {
                    showErrorMessage();
                }
            }
        });


    }

}

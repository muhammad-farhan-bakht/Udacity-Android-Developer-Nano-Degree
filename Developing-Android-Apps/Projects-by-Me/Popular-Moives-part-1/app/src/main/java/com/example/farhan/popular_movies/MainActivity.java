package com.example.farhan.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farhan.popular_movies.adapter.MoviesAdapter;
import com.example.farhan.popular_movies.model.Movies;
import com.example.farhan.popular_movies.utils.JsonUtils;
import com.example.farhan.popular_movies.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    MoviesAdapter moviesAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private ArrayList<Movies> moviesArrayList;
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private String sortBy = POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerView_movies);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        moviesArrayList = new ArrayList<>();

        int numberOfColumns = 2;
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mRecyclerView.setHasFixedSize(true);

        moviesAdapter = new MoviesAdapter();
        mRecyclerView.setAdapter(moviesAdapter);
        moviesAdapter.setMoviesAdapterOnClickHandler(MainActivity.this);
        loadMoviesData();
    }

    private void loadMoviesData() {
        if (isNetworkAvailable()) {
            showMoviesDataView();
            new FetchMoviesTask().execute(sortBy);
        } else {
            showErrorMessage();
        }
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

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movies>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movies> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String sortByParams = params[0];
            URL moviesRequestUrl = NetworkUtils.buildUrl(sortByParams);

            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

                return JsonUtils.getObjectFromJson(jsonMoviesResponse);

            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> movies) {
            super.onPostExecute(movies);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showMoviesDataView();
                moviesAdapter.setMovieData(movies);
            } else {
                Toast.makeText(MainActivity.this, "Please Re-Try", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
                    moviesArrayList.clear();
                    loadMoviesData();
                    return true;
                } else {
                    return false;
                }
            }

            case R.id.action_top_rated: {
                if (!sortBy.equals(TOP_RATED)) {
                    sortBy = TOP_RATED;
                    moviesArrayList.clear();
                    loadMoviesData();
                    return true;
                } else {
                    return false;
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.farhan.popular_movies.utils;

import android.util.Log;

import com.example.farhan.popular_movies.model.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();
    private static final String RESULTS = "results";
    private static final String TITLE = "title";
    private static final String RELEASE_DATE = "release_date";
    private static final String POSTER_PATH = "poster_path";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String OVERVIEW = "overview";
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185";

    public static ArrayList<Movies> getObjectFromJson(String moviesJsonStr) {

        ArrayList<Movies> moviesArrayList = new ArrayList<>();

        try {
            JSONObject  moviesJson = new JSONObject(moviesJsonStr);

            JSONArray results = moviesJson.getJSONArray(RESULTS);

            for (int i = 0; i < results.length(); i++) {

                JSONObject resultsObj = results.getJSONObject(i);
                String title = resultsObj.getString(TITLE);
                String releaseDate = resultsObj.getString(RELEASE_DATE);
                String posterPath = IMAGE_URL + resultsObj.getString(POSTER_PATH);
                String voteAverage = resultsObj.getString(VOTE_AVERAGE);
                String overView = resultsObj.getString(OVERVIEW);

                Log.e(TAG, "Movie Poster URL " + posterPath);
                moviesArrayList.add(new Movies(title, releaseDate, posterPath, voteAverage, overView));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Failed Json Parsing");
        }
        return moviesArrayList;
    }
}

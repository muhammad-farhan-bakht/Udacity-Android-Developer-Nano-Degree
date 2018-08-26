package com.example.farhan.popular_movies.utils;

import com.example.farhan.popular_movies.BuildConfig;

public class Constants {
    private Constants(){
    }

    public static final String STATIC_MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;
    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

}

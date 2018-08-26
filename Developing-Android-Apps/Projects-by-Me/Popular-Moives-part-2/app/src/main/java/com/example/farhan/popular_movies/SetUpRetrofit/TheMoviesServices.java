package com.example.farhan.popular_movies.SetUpRetrofit;

import com.example.farhan.popular_movies.model.BaseModel;
import com.example.farhan.popular_movies.model.Movies;
import com.example.farhan.popular_movies.model.Reviews;
import com.example.farhan.popular_movies.model.Trailers;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// I searched it from Retrofit docs
public interface TheMoviesServices {

    @GET("{sort_by}")
    Call<BaseModel<Movies>> getMoviesSortBy(@Path("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("{movie_id}/reviews")
    Call<BaseModel<Reviews>> getMovieReviews(@Path("movie_id") long movieId, @Query("api_key") String apiKey);

    @GET("{movie_id}/videos")
    Call<BaseModel<Trailers>> getMovieTrailer(@Path("movie_id") long movieId, @Query("api_key") String apiKey);
}
